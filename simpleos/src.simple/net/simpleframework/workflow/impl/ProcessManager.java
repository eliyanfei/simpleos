package net.simpleframework.workflow.impl;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.IUser;
import net.simpleframework.workflow.IProcessManager;
import net.simpleframework.workflow.IScriptAware;
import net.simpleframework.workflow.InitiateItem;
import net.simpleframework.workflow.ProcessBean;
import net.simpleframework.workflow.ProcessLobBean;
import net.simpleframework.workflow.ProcessModelBean;
import net.simpleframework.workflow.WorkflowUtils;
import net.simpleframework.workflow.schema.ProcessDocument;
import net.simpleframework.workflow.schema.ProcessNode;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ProcessManager extends AbstractWorkflowManager<ProcessBean> implements IProcessManager {

	@Override
	public ProcessBean createProcess(final InitiateItem item, final IUser user, final String topic) {
		return createProcess(item, user, topic, null);
	}

	@Override
	public ProcessBean createProcess(final InitiateItem item, final IUser user, final String topic,
			final Map<String, Object> variables) {
		final IJob job = item.getSelected();
		if (job == null) {
		}
		final ProcessModelBean model = item.model();
		if (model == null) {
		}

		final ProcessBean process = new ProcessBean();
		process.setModelId(model.getId());
		process.setJobId(job.getId());
		process.setUserId(user.getId());
		process.setCreateDate(new Date());
		process.setTitle(topic);
		getEntityManager().insertTransaction(process, new TableEntityAdapter() {
			@Override
			public void afterInsert(final ITableEntityManager manager, final Object[] objects) {
				final ProcessDocument doc = model.document();
				final ProcessNode pnode = doc.getProcessNode();
				if (!pnode.isInstanceShared()) {
					final ProcessLobBean lob = new ProcessLobBean();
					lob.setId(process.getId());
					lob.setProcessModel(new StringReader(doc.toString()));
					WorkflowUtils.getTableEntityManager(ProcessLobBean.class).insert(lob);
				}
				// 创建开始任务
				WorkflowUtils
						.am()
						.getActivityCallback(
								ActivityUtils.createActivity(process, pnode.startNode(), null, null))
						.next();
			}
		});
		return process;
	}

	@Override
	public Map<String, Object> createVariables(final ProcessBean process) {
		final Map<String, Object> variables = ((IScriptAware<ProcessModelBean>) WorkflowUtils.pmm())
				.createVariables(process.model());
		variables.put("process", process);
		for (final String variable : getVariableNames()) {
			variables.put(variable, getVariable(process, variable));
		}
		return variables;
	}

	@Override
	public Object getVariable(final ProcessBean bean, final String name) {
		return null;
	}

	@Override
	public void setVariable(final ProcessBean bean, final String[] names, final Object[] values) {
	}

	@Override
	public void setVariable(final ProcessBean bean, final String name, final Object value) {
		setVariable(bean, new String[] { name }, new Object[] { value });
	}

	@Override
	public Collection<String> getVariableNames() {
		return new ArrayList<String>();
	}
}
