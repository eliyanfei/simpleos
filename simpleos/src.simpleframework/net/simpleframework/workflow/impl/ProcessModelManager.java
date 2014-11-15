package net.simpleframework.workflow.impl;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.IUser;
import net.simpleframework.util.StringUtils;
import net.simpleframework.workflow.EProcessModelStatus;
import net.simpleframework.workflow.IProcessModelManager;
import net.simpleframework.workflow.InitiateItem;
import net.simpleframework.workflow.ProcessModelBean;
import net.simpleframework.workflow.ProcessModelLobBean;
import net.simpleframework.workflow.WorkflowUtils;
import net.simpleframework.workflow.schema.AbstractProcessParticipantType;
import net.simpleframework.workflow.schema.AbstractProcessStartupType;
import net.simpleframework.workflow.schema.AbstractProcessStartupType.Manual;
import net.simpleframework.workflow.schema.ProcessDocument;
import net.simpleframework.workflow.schema.ProcessNode;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ProcessModelManager extends AbstractWorkflowManager<ProcessModelBean> implements
		IProcessModelManager {
	@Override
	public ProcessModelBean add(final IUser login, final Reader reader) {
		final ProcessModelBean bean = new ProcessModelBean();
		final ProcessDocument document;
		if (reader == null) {
			document = new ProcessDocument();
		} else {
			document = new ProcessDocument(reader);
		}
		bean.setUserId(login.getId());
		bean.setCreateDate(new Date());
		final ProcessNode processNode = document.getProcessNode();
		bean.setTitle(processNode.getText());
		if (!StringUtils.hasText(processNode.getAuthor())) {
			processNode.setAuthor(login.toString());
		}
		getEntityManager().insertTransaction(bean, new TableEntityAdapter() {
			@Override
			public void afterInsert(final ITableEntityManager manager, final Object[] objects) {
				final ProcessModelLobBean lob = new ProcessModelLobBean();
				lob.setId(bean.getId());
				lob.setProcessSchema(new StringReader(document.toString()));
				WorkflowUtils.getTableEntityManager(ProcessModelLobBean.class).insert(lob);
			}
		});
		return bean;
	}

	@Override
	public IDataObjectQuery<ProcessModelBean> models(final EProcessModelStatus status) {
		return query(status == null ? null : new ExpressionValue("status=?", new Object[] { status }));
	}

	private final Map<ID, Map<ID, InitiateItem>> itemsCache = new ConcurrentHashMap<ID, Map<ID, InitiateItem>>();

	@Override
	public Map<ID, InitiateItem> items(final IUser user) {
		if (user == null) {
			return new HashMap<ID, InitiateItem>();
		}
		final ID userkey = user.getId();
		Map<ID, InitiateItem> items = itemsCache.get(userkey);
		if (items != null) {
			for (final Map.Entry<ID, InitiateItem> entry : new ArrayList<Map.Entry<ID, InitiateItem>>(
					items.entrySet())) {
				final InitiateItem item = entry.getValue();
				if (item.model() == null || item.getInitiators().size() == 0) {
					items.remove(entry.getKey());
				}
			}
			return items;
		}
		items = new LinkedHashMap<ID, InitiateItem>();
		final IDataObjectQuery<ProcessModelBean> query = models(EProcessModelStatus.deploy);
		ProcessModelBean model;
		while ((model = query.next()) != null) {
			final AbstractProcessStartupType startupType = model.document().getProcessNode()
					.getStartupType();
			if (startupType instanceof Manual) {
				final Collection<IJob> jobs = initiators(user,
						((Manual) startupType).getParticipantType());
				if (jobs != null && jobs.size() > 0) {
					final InitiateItem item = new InitiateItem(model.getId());
					for (final IJob job : jobs) {
						item.getInitiators().add(job.getId());
					}
					items.put(item.getModelId(), item);
				}
			}
		}
		itemsCache.put(userkey, items);
		return items;
	}

	private Collection<IJob> initiators(final IUser user, final AbstractProcessParticipantType pt) {
		if (user == null) {
			return null;
		}
		final Collection<IJob> jobs = new LinkedHashSet<IJob>();
		final String participant = pt.getParticipant();
		if (pt instanceof AbstractProcessParticipantType.User) {
			final IUser user2 = WorkflowUtils.org().user(participant);
			if (user2 != null && user2.equals(user)) {
				jobs.add(user2.primary());
			}
		} else if (pt instanceof AbstractProcessParticipantType.Job) {
			final IJob job = WorkflowUtils.org().job(participant);
			if (job != null && job.member(user)) {
				jobs.add(job);
			}
		}
		return jobs;
	}

	@Override
	public Map<String, Object> createVariables(final ProcessModelBean model) {
		final Map<String, Object> variables = super.createVariables(model);
		variables.put("model", model);
		return variables;
	}
}
