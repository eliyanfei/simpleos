package net.simpleframework.content.component.remark;

import java.util.Map;

import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.content.AbstractContentHandle;
import net.simpleframework.core.ado.DataObjectException;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.core.bean.IIdBeanAware;
import net.simpleframework.core.id.ID;
import net.simpleframework.core.id.LongID;
import net.simpleframework.my.message.MessageUtils;
import net.simpleframework.util.HTTPUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.ComponentException;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleos.SimpleosUtil;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultRemarkHandle extends AbstractContentHandle implements IRemarkHandle {
	@Override
	public void handleCreated(final PageRequestResponse requestResponse, final AbstractComponentBean componentBean) {
		if (RemarkItem.class.equals(getEntityBeanClass())) {
			PageUtils.doDatabase(RemarkItem.class, componentBean);
		}
	}

	@Override
	public Class<? extends IIdBeanAware> getEntityBeanClass() {
		return RemarkItem.class;
	}

	@Override
	public void putTables(final Map<Class<?>, Table> tables) {
		tables.put(RemarkItem.class, RemarkUtils.table_remark);
	}

	@Override
	public <T extends IDataObjectBean> void doBeforeEdit(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		super.doBeforeEdit(compParameter, temgr, t, data, beanClazz);
		final RemarkItem remark = (RemarkItem) t;
		remark.setContent((String) data.get("content"));
	}

	@Override
	public <T extends IDataObjectBean> void doBeforeAdd(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		super.doBeforeAdd(compParameter, temgr, t, data, beanClazz);
		final RemarkItem remark = (RemarkItem) t;
		remark.initThis(compParameter);
		remark.setParentId(temgr.getTable().newID(data.get("parentId")));
		final Object documentId = getDocumentId(compParameter);
		if (documentId != null) {
			remark.setDocumentId(ID.Utils.newID(documentId));
		}
		remark.setContent((String) data.get("content"));
		remark.setIp(HTTPUtils.getRemoteAddr(compParameter.request));
		final String topic = getTopic(compParameter, remark.getDocumentId());
		if (topic != null) {
			//发布消息
			ID userId = remark.getUserId();
			if (userId == null) {
				if ("true".equals(SimpleosUtil.attrMap.get("sys.sys_remark"))) {
					throw new DataObjectException("你还没有登入?");
				}
				userId = LongID.zero;
			}
			if (remark.getParentId() == null) {
				try {
					if (!userId.equals2(compParameter.getRequestAttribute("userId"))) {
						final StringBuffer textBody = new StringBuffer();
						textBody.append(topic).append("<br/>");
						textBody.append(remark.getContent());
						final String subject = remark.getUserText() + "评论你";
						MessageUtils.createNotifation(compParameter, subject, textBody.toString(), userId,
								(ID) compParameter.getRequestAttribute("userId"));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					final RemarkItem remark1 = (RemarkItem) getTableEntityManager(compParameter).queryForObjectById(remark.getParentId(),
							getEntityBeanClass());
					if (!userId.equals2(remark1.getUserId())) {
						final StringBuffer textBody = new StringBuffer();
						textBody.append(topic).append("<br/>");
						textBody.append("<blockquote>原评论<br/>").append(remark1.getContent()).append("</blockquote>");
						textBody.append("<blockquote>现评论<br/>").append(remark.getContent()).append("</blockquote>");
						final String subject = remark.getUserText() + "评论你的评论";
						MessageUtils.createNotifation(compParameter, subject, textBody.toString(), userId, remark1.getUserId());
					}
				} catch (Exception e) {
				}
			}
		} else {
		}
	}

	protected String getTopic(final ComponentParameter compParameter, final ID documentId) {
		return null;
	}

	@Override
	public long getPostInterval() {
		return 1000 * 60;
	}

	@Override
	public long getCount(final ComponentParameter compParameter) {
		final ExpressionValue ev = new ExpressionValue("documentid=?", new Object[] { getDocumentId(compParameter) });
		return getTableEntityManager(compParameter).getCount(ev);
	}

	@Override
	public void doDelete(final ComponentParameter compParameter, final IDataObjectValue ev) {
		final Object id = ev.getValues()[0];
		if (getRemarkItems(compParameter, id).getCount() > 0) {
			throw new ComponentException(LocaleI18n.getMessage("DefaultRemarkHandle.0"));
		} else {
			final RemarkItem remark = (RemarkItem) getTableEntityManager(compParameter).queryForObject(ev, getEntityBeanClass());
			if (remark != null) {
				SimpleosUtil.update(compParameter, remark.getUserId(), remark.getId(), true);
			}
			super.doDelete(compParameter, ev);
		}
	}

	@Override
	public IDataObjectQuery<RemarkItem> getRemarkItems(final ComponentParameter compParameter) {
		return getRemarkItems(compParameter, null);
	}

	@Override
	public IDataObjectQuery<RemarkItem> getRemarkItems(final ComponentParameter compParameter, final Object parentId) {
		return beans(compParameter, parentId, " order by createdate desc");
	}

	@Override
	public RemarkItem doSupportOpposition(final ComponentParameter compParameter, final Object itemId, final boolean support) {
		final RemarkItem item = getEntityBeanById(compParameter, itemId);
		if (item != null) {
			if (support) {
				item.setSupport(item.getSupport() + 1);
			} else {
				item.setOpposition(item.getOpposition() + 1);
			}
			getTableEntityManager(compParameter).update(item);
		}
		return item;
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, RemarkUtils.BEAN_ID);
		putParameter(compParameter, parameters, getDocumentIdParameterName(compParameter), getDocumentId(compParameter));
		return parameters;
	}
}
