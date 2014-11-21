package net.simpleframework.content.component.vote;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import net.simpleframework.ado.DataObjectManagerUtils;
import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IEntityManager;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.content.AbstractContentHandle;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.core.bean.IIdBeanAware;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.util.HTTPUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.HandleException;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultVoteHandle extends AbstractContentHandle implements IVoteHandle {
	@Override
	public void handleCreated(final PageRequestResponse requestResponse,
			final AbstractComponentBean componentBean) {
		if (Vote.class.equals(getEntityBeanClass())) {
			PageUtils.doDatabase(Vote.class, componentBean);
		}
	}

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("jobSubmit".equals(beanProperty)) {
			return IJob.sj_account_normal;
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public Class<? extends IIdBeanAware> getEntityBeanClass() {
		return Vote.class;
	}

	@Override
	public void putTables(final Map<Class<?>, Table> tables) {
		VoteUtils.putTables(tables);
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		parameters.put(VoteUtils.BEAN_ID, compParameter.componentBean.hashId());
		final IVoteHandle vHandle = (IVoteHandle) compParameter.getComponentHandle();
		final Vote vote = vHandle.getVoteByDocumentId(compParameter);
		if (vote != null) {
			parameters.put(vHandle.getIdParameterName(compParameter), vote.getId());
		}
		return parameters;
	}

	@Override
	public Vote getVoteByDocumentId(final ComponentParameter compParameter) {
		return getVoteByDocumentId(compParameter, getDocumentId(compParameter));
	}

	@Override
	public Vote getVoteByDocumentId(final ComponentParameter compParameter, final Object documentId) {
		getTableEntityManager(compParameter).reset();
		return getTableEntityManager(compParameter).queryForObject(
				new ExpressionValue("documentId=?", new Object[] { documentId }), Vote.class);
	}

	@Override
	public <T extends IDataObjectBean> void doBeforeEdit(final ComponentParameter compParameter,
			final ITableEntityManager temgr, final T t, final Map<String, Object> data,
			final Class<T> beanClazz) {
		super.doBeforeEdit(compParameter, temgr, t, data, beanClazz);
		if (Vote.class.isAssignableFrom(beanClazz)) {
			setObjectFromMap(t, data);
		} else if (VoteItemGroup.class.isAssignableFrom(beanClazz)) {
			final VoteItemGroup itemGroup = (VoteItemGroup) t;
			itemGroup.setText((String) data.get("text"));
			itemGroup.setMultiple(ConvertUtils.toShort(data.get("multiple"), (short) 0));
		} else if (VoteItem.class.isAssignableFrom(beanClazz)) {
			final VoteItem voteItem = (VoteItem) t;
			voteItem.setText((String) data.get("text"));
		}
	}

	@Override
	public <T extends IDataObjectBean> void doBeforeAdd(final ComponentParameter compParameter,
			final ITableEntityManager temgr, final T t, final Map<String, Object> data,
			final Class<T> beanClazz) {
		if (Vote.class.isAssignableFrom(beanClazz)) {
			final Vote vote = (Vote) t;
			vote.setDocumentId(ID.Utils.newID(getDocumentId(compParameter)));
			vote.initThis(compParameter);
		} else if (VoteItemGroup.class.isAssignableFrom(beanClazz)) {
			final VoteItemGroup itemGroup = (VoteItemGroup) t;
			final Vote vote = getEntityBeanById(compParameter, data.get("voteId"));
			itemGroup.setVoteId(vote.getId());
			itemGroup.setText((String) data.get("text"));
			itemGroup.setMultiple(ConvertUtils.toShort(data.get("multiple"), (short) 0));
		} else if (VoteItem.class.isAssignableFrom(beanClazz)) {
			final VoteItem voteItem = (VoteItem) t;
			final VoteItemGroup itemGroup = getEntityBeanById(compParameter,
					data.get(VoteUtils.VOTE_GROUP_ID), VoteItemGroup.class);
			voteItem.setGroupId(itemGroup.getId());
			voteItem.setText((String) data.get("text"));
		}
	}

	@Override
	public IDataObjectQuery<VoteItemGroup> getItemGroups(final ComponentParameter compParameter,
			final Vote vote) {
		return getTableEntityManager(compParameter, VoteItemGroup.class).query(
				new ExpressionValue("voteid=?", new Object[] { vote.getId() }), VoteItemGroup.class);
	}

	@Override
	public <T extends IDataObjectBean> void doDelete(final ComponentParameter compParameter,
			final IDataObjectValue ev, final Class<T> beanClazz) {
		if (VoteResult.class.isAssignableFrom(beanClazz)) {
			super.doDelete(compParameter, ev, beanClazz);
		} else {
			final Object[] ids = ev.getValues();
			final SQLValue[] sqls = VoteUtils.getDeleteSQLs(compParameter, beanClazz, ids);
			if (sqls != null && sqls.length > 0) {
				DataObjectManagerUtils.getQueryEntityManager(PageUtils.pageContext.getApplication())
						.executeTransaction(sqls, new TableEntityAdapter() {
							@Override
							public void afterExecute(final IEntityManager manager,
									final SQLValue[] sqlValues) {
								doDeleteCallback(compParameter, null, beanClazz);
							}
						});
			}
		}
	}

	@Override
	public IDataObjectQuery<VoteItem> getVoteItems(final ComponentParameter compParameter,
			final VoteItemGroup itemGroup) {
		return getTableEntityManager(compParameter, VoteItem.class).query(
				new ExpressionValue("groupid=?", new Object[] { itemGroup.getId() }), VoteItem.class);
	}

	@Override
	public VoteResult[] submitVote(final ComponentParameter compParameter, final Object[] itemIds) {
		if (itemIds == null || itemIds.length == 0) {
			return null;
		}
		final VoteResult[] results = new VoteResult[itemIds.length];
		int i = 0;
		final IAccount account = AccountSession.getLogin(compParameter.getSession());
		final ITableEntityManager voteresult_mgr = getTableEntityManager(compParameter,
				VoteResult.class);
		for (final Object id : itemIds) {
			results[i] = new VoteResult();
			final VoteItem item = getEntityBeanById(compParameter, id, VoteItem.class);
			if (item == null) {
				continue;
			}
			final ID itemId = item.getId();
			if (account != null) {
				final ID userId = account.getId();
				if (voteresult_mgr.getCount(new ExpressionValue("itemid=? and userId=?", new Object[] {
						itemId, userId })) > 0) {
					throw HandleException.wrapException(LocaleI18n.getMessage("DefaultVoteHandle.0",
							item.getText()));
				}
				results[i].setUserId(userId);
			}
			results[i].setItemId(itemId);
			results[i].setCreateDate(new Date());
			results[i].setIp(HTTPUtils.getRemoteAddr(compParameter.request));
			i++;
		}
		voteresult_mgr.insert(results);
		return results;
	}

	@Override
	public IDataObjectQuery<VoteResult> getResults(final ComponentParameter compParameter,
			final Object itemId) {
		return getTableEntityManager(compParameter, VoteResult.class).query(
				new ExpressionValue("itemid=?", new Object[] { itemId }), VoteResult.class);
	}

	final static String VOTE_ID = "__vote_Id";

	@Override
	public String getIdParameterName(final ComponentParameter compParameter) {
		return VOTE_ID;
	}

	protected final String TB_VOTE_DELETE = "<a onclick=\"__vote_action(this)._delete();\">"
			+ LocaleI18n.getMessage("Delete") + "</a>";

	protected final String TB_VOTE_REFRESH = "<a onclick=\"__vote_action(this).refresh();\">"
			+ LocaleI18n.getMessage("Refresh") + "</a>";

	protected final String TB_VOTE_EDIT = "<a onclick=\"__vote_action(this)._edit();\">"
			+ LocaleI18n.getMessage("Edit") + "</a>";

	@Override
	public String getManagerToolbar(final ComponentParameter compParameter) {
		final ArrayList<String> al = new ArrayList<String>();
		final IAccount account = AccountSession.getLogin(compParameter.getSession());
		if (account != null) {
			final IUser user = account.user();
			if (OrgUtils.isMember((String) compParameter.getBeanProperty("jobEdit"), user)) {
				al.add(TB_VOTE_REFRESH);
				al.add(TB_VOTE_EDIT);
			}
			if (OrgUtils.isMember((String) compParameter.getBeanProperty("jobDelete"), user)) {
				al.add(TB_VOTE_DELETE);
			}
		}
		return StringUtils.join(al, HTMLBuilder.SEP);
	}

	@Override
	public String getJavascriptCallback(final ComponentParameter compParameter,
			final String jsAction, final Object bean) {
		final String name = (String) compParameter.getBeanProperty("name");
		if ("submit".equals(jsAction)) {
			return "$Actions['" + name + "']._view();";
		} else if ("delete".equals(jsAction) || "add".equals(jsAction)) {
			return "$Actions['" + name + "'].refresh();";
		}
		return super.getJavascriptCallback(compParameter, jsAction, bean);
	}
}
