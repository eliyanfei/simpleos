package net.itsite.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.itsite.ItSiteUtil;
import net.itsite.i.ICommonBeanAware;
import net.itsite.i.IItSiteApplicationModule;
import net.itsite.i.ISendMail;
import net.itsite.utils.Dom4jUtils;
import net.itsite.utils.StringsUtils;
import net.simpleframework.ado.DataObjectManagerUtils;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.ado.db.event.ITableEntityListener;
import net.simpleframework.ado.lucene.AbstractLuceneManager;
import net.simpleframework.applets.attention.AttentionBean;
import net.simpleframework.applets.attention.AttentionUtils;
import net.simpleframework.applets.attention.ISentCallback;
import net.simpleframework.applets.notification.MailMessageNotification;
import net.simpleframework.applets.notification.NotificationUtils;
import net.simpleframework.content.AbstractContentApplicationModule;
import net.simpleframework.content.IAttentionsBeanAware;
import net.simpleframework.content.component.remark.RemarkItem;
import net.simpleframework.core.ExecutorRunnable;
import net.simpleframework.core.IInitializer;
import net.simpleframework.core.ITaskExecutorAware;
import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.id.ID;
import net.simpleframework.core.id.LongID;
import net.simpleframework.organization.IUser;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.util.IoUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.script.ScriptEvalUtils;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.WebApplicationConfig;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.HandleException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

/**
 * 
 * @Description：
 * @author: 李岩飞
 * @Time: 2011-7-28 下午1:42:05 
 */
public abstract class AItSiteAppclicationModule extends AbstractContentApplicationModule implements IItSiteApplicationModule {

	protected Map<String, PrjColumns> prjColumnMap = new HashMap<String, PrjColumns>();

	public PrjColumns getPrjColumns(final String type) {
		return prjColumnMap.get(type);
	}

	@Override
	public void doUpdate(Object[] columns, Object bean) {
		final ITableEntityManager tMgr = getDataObjectManager(bean.getClass());
		if (tMgr != null) {
			tMgr.update(columns, bean);
		}
	}

	@Override
	public void doUpdate(Object[] columns, Object bean, ITableEntityListener tel) {
		final ITableEntityManager tMgr = getDataObjectManager(bean.getClass());
		if (tMgr != null) {
			tMgr.updateTransaction(columns, bean, tel);
		}
	}

	@Override
	public ITableEntityManager getDataObjectManager() {
		return DataObjectManagerUtils.getTableEntityManager(this);
	}

	@Override
	public ITableEntityManager getDataObjectManager(Class<?> t) {
		return DataObjectManagerUtils.getTableEntityManager(this, t);
	}

	@Override
	public void doUpdate(String sql) {
		final ITableEntityManager entityManager = getDataObjectManager();
		entityManager.execute(new SQLValue(sql));
	}

	@Override
	public void doUpdate(List<? extends AbstractIdDataObjectBean> beanList) {
		for (AbstractIdDataObjectBean bean : beanList) {
			doUpdate(bean);
		}
	}

	@Override
	public String getViewUrl(Object id) {
		return null;
	}

	@Override
	public String getViewUrl1(PageRequestResponse requestResponse, final Object... params) {
		return null;
	}

	@Override
	public String getViewUrl1(final String url, Object... params) {
		final StringBuilder sb = new StringBuilder();
		sb.append("/").append(url).append("/");
		for (int i = 0; i < params.length; i++) {
			sb.append(params[i]);
			if (i != params.length - 1) {
				sb.append("-");
			}
		}
		sb.append(".html");
		return sb.toString();
	}

	@Override
	public void doUpdate(AbstractIdDataObjectBean bean, ITableEntityListener tel) {
		final ITableEntityManager entityManager = getDataObjectManager(bean.getClass());
		if (bean.getId() == null) {
			bean.setId(ID.Utils.newID(entityManager.nextIncValue("id")));
			entityManager.insertTransaction(bean, tel);
		} else {
			entityManager.updateTransaction(bean, tel);
		}
	}

	@Override
	public void doDelete(AbstractIdDataObjectBean bean, ITableEntityListener tel) {
		final ITableEntityManager entityManager = getDataObjectManager(bean.getClass());
		if (entityManager != null)
			entityManager.deleteTransaction(new ExpressionValue("id=?", new Object[] { bean.getId() }), tel);
	}

	@Override
	public void init(IInitializer initializer) {
		super.init(initializer);
		LocaleI18n.addBasename(this.getClass());
		if (getDeployPath() != null)
			IInitializer.Utils.deploySqlScript(getClass(), initializer.getApplication(), getDeployPath());
		loadColumns();

		doInit(getClass(), getDeployPath());
	}

	public void loadColumns() {
		try {
			Document document = Dom4jUtils.createDocument(this.getClass().getResourceAsStream("columns.xml"));
			if (document.getRootElement() == null)
				return;
			final List<Element> list = document.getRootElement().elements();
			for (Element subEle : list) {
				PrjColumns column = new PrjColumns(subEle);
				prjColumnMap.put(column.getName(), column);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doUpdate(AbstractIdDataObjectBean bean) {
		final ITableEntityManager entityManager = getDataObjectManager(bean.getClass());
		if (bean.getId() == null) {
			bean.setId(ID.Utils.newID(entityManager.nextIncValue("id")));
			entityManager.insert(bean);
		} else {
			entityManager.update(bean);
		}
	}

	@Override
	public void doDelete(final AbstractIdDataObjectBean bean) {
		final ITableEntityManager entityManager = getDataObjectManager(bean.getClass());
		if (entityManager != null)
			entityManager.delete(new ExpressionValue("id=?", new Object[] { bean.getId() }));
	}

	@Override
	public void doDelete(Class<?> bean, Object id) {
		final ITableEntityManager entityManager = getDataObjectManager(bean);
		if (entityManager != null)
			entityManager.delete(new ExpressionValue("id=?", new Object[] { id }));
	}

	@Override
	public void doDelete(List<? extends AbstractIdDataObjectBean> beanList) {
		for (final AbstractIdDataObjectBean bean : beanList) {
			doDelete(bean);
		}
	}

	@Override
	public void doDelete(List<? extends Object> idList, Class<? extends Object> beanClass) {
		for (final Object id : idList) {
			doDelete(beanClass, id);
		}
	}

	@Override
	public void doDelete(String field, List<? extends Object> idList, Class<? extends Object> beanClass) {
		for (final Object id : idList) {
			final ITableEntityManager entityManager = getDataObjectManager(beanClass);
			if (entityManager != null)
				entityManager.delete(new ExpressionValue(field + "=?", new Object[] { id }));
		}
	}

	@Override
	public void doDelete(Class<?> bean, Object id, ITableEntityListener tel) {
		final ITableEntityManager entityManager = getDataObjectManager(bean);
		if (entityManager != null)
			entityManager.deleteTransaction(new ExpressionValue("id=?", new Object[] { id }), tel);
	}

	@Override
	public void doDelete(final PageRequestResponse requestResponse) {
	}

	@Override
	public IQueryEntitySet<Map<String, Object>> queryBean(String sql, final Object[] values) {
		final ITableEntityManager tMgr = getDataObjectManager();
		return tMgr.query(new SQLValue(sql, values));
	}

	public Map<String, Object> queryBean(SQLValue dataObjectValue) {
		ITableEntityManager tMgr = getDataObjectManager();
		return tMgr.queryForMap(dataObjectValue);
	}

	@Override
	public <T> IQueryEntitySet<T> queryBean(ExpressionValue exp, Class<T> t) {
		final ITableEntityManager tMgr = getDataObjectManager(t);
		return tMgr.query(exp, t);
	}

	@Override
	public <T> IQueryEntitySet<T> queryBean(Class<T> t) {
		final ITableEntityManager tMgr = getDataObjectManager(t);
		return tMgr.query(new ExpressionValue("1=1"), t);
	}

	@Override
	public <T> IQueryEntitySet<T> queryBean(String exp, Class<T> t) {
		final ITableEntityManager tMgr = getDataObjectManager(t);
		return tMgr.query(new ExpressionValue(exp == null ? "1=1" : exp), t);
	}

	@Override
	public <T> IQueryEntitySet<T> queryBean(String exp, Object[] values, Class<T> t) {
		final ITableEntityManager tMgr = getDataObjectManager(t);
		return tMgr.query(new ExpressionValue(exp == null ? "1=1" : exp, values), t);
	}

	@Override
	public <T> List<T> doQuery(String sql, String col, Class<T> t) {
		final List<T> list = new ArrayList<T>();
		final ITableEntityManager tMgr = getDataObjectManager(t);
		final IQueryEntitySet<Map<String, Object>> qs = tMgr.query(new SQLValue(sql));
		if (qs != null) {
			Map<String, Object> data;
			while ((data = qs.next()) != null) {
				try {
					list.add(t.cast(data.get(col)));
				} catch (Exception e) {
				}
			}
		}
		return list;
	}

	@Override
	public void doDelete(final String where, Class<?> bean) {
		final ITableEntityManager entityManager = getDataObjectManager(bean);
		if (entityManager != null)
			entityManager.deleteTransaction(new SQLValue("delete from " + entityManager.getTablename() + " where " + where));
	}

	@Override
	public <T> T getBean(Class<T> t, Object id) {
		final ITableEntityManager tMgr = getDataObjectManager(t);
		return tMgr.queryForObjectById(id, t);
	}

	@Override
	public <T> T getBeanByExp(Class<T> t, String exp, final Object[] params) {
		final ITableEntityManager tMgr = getDataObjectManager(t);
		return tMgr.queryForObject(new ExpressionValue(exp, params), t);
	}

	@Override
	public AbstractComponentBean getComponentBean(PageRequestResponse requestResponse) {
		return null;
	}

	@Override
	public String tabs(PageRequestResponse requestResponse) {
		return null;
	}

	public AbstractLuceneManager createLuceneManager(final ComponentParameter compParameter) {
		return null;
	}

	protected void doAttentionMail(final ComponentParameter compParameter, final ISendMail sendMail) {
		((ITaskExecutorAware) getApplication()).getTaskExecutor().execute(new ExecutorRunnable() {
			@Override
			public void task() {
				final MailMessageNotification mailMessage = new MailMessageNotification();
				mailMessage.setHtmlContent(true);
				final IUser user = sendMail.getUser();
				if (sendMail.isSent(user)) {
					mailMessage.getTo().add(user);
					mailMessage.setSubject(sendMail.getSubject(user));
					mailMessage.setTextBody(sendMail.getBody(user));
					NotificationUtils.sendMessage(mailMessage);
				}
			}
		});
	}

	final static Map<Integer, String> orderData = new LinkedHashMap<Integer, String>();
	static {
		orderData.put(0, "最新");
		orderData.put(1, "最多阅读");
		//		orderData.put(2, "最高评价");
		//		orderData.put(3, "最新评论");
		//		orderData.put(4, "最多关注");
	}

	/**
	 * 获得参数
	 * @param requestResponse
	 * @return
	 */
	protected String getParameters(PageRequestResponse requestResponse) {
		return null;
	}

	protected Map<Integer, String> getOrderData() {
		return new LinkedHashMap<Integer, String>(orderData);
	}

	public String tabs13(PageRequestResponse requestResponse) {
		final StringBuffer buf = new StringBuffer();
		final int t = ConvertUtils.toInt(requestResponse.getRequestParameter("t"), 0);
		final String c = requestResponse.getRequestParameter("c");
		int tempOd = 0;
		if (StringsUtils.isNotBlank(c)) {
			tempOd = 1;
		}
		final int temp = ConvertUtils.toInt(requestResponse.getRequestParameter("od"), tempOd);
		int i = 0;
		Map<Integer, String> orderData = getOrderData();
		for (final Integer od : orderData.keySet()) {
			buf.append("<a hidefocus=\"hidefocus\"");
			buf.append(" href=\"" + getViewUrl1(requestResponse, t, i) + "\"");
			if (i == temp) {
				buf.append(" class=\"a2 nav_arrow\"");
			}
			buf.append(">").append(orderData.get(od)).append("</a>");
			if (i++ != orderData.size() - 1) {
				buf.append("<span style=\"margin: 0px 4px;\">|</span>");
			}
		}
		return buf.toString();
	}

	@Override
	public void deleteAttentions(final PageRequestResponse requestResponse, Object attentionId) {
		AttentionUtils.delete(requestResponse, getEFunctionModule(), new LongID(attentionId));
	}

	@Override
	public void deleteRemarks(Object documentId) {
		final Class<?> cla = getRemarkEntityBeanClass();
		if (cla != null) {
			try {
				getDataObjectManager(cla).delete(new ExpressionValue("documentId=" + documentId));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected Class<?> getRemarkEntityBeanClass() {
		return null;
	}

	@Override
	public long nextId() {
		return getDataObjectManager().nextIncValue("id");
	}

	@Override
	public long getValue(String sql, Object[] values) {
		final ITableEntityManager tMgr = getDataObjectManager();
		final IQueryEntitySet<Map<String, Object>> qs = tMgr.query(new SQLValue(sql, values));
		if (qs == null)
			return 0;
		Map<String, Object> data;
		while ((data = qs.next()) != null) {
			return ConvertUtils.toLong(data.get(data.keySet().iterator().next()), 0);
		}
		return 0;
	}

	public long count(String sql, Object[] values) {
		final ITableEntityManager tMgr = getDataObjectManager();
		final IQueryEntitySet<Map<String, Object>> qs = tMgr.query(new SQLValue(sql, values));
		return qs.getCount();
	}

	@Override
	public <T> long count(String sql, Object[] values, Class<T> t) {
		final ITableEntityManager tMgr = getDataObjectManager(t);
		IQueryEntitySet<T> qs = tMgr.query(new ExpressionValue(sql == null ? "1=1" : sql, values), t);
		return qs.getCount();
	}

	public EFunctionModule getEFunctionModule() {
		return null;
	}

	@Override
	public AttentionBean getAttention(final PageRequestResponse requestResponse, Object id) {
		return AttentionUtils.get(requestResponse, getEFunctionModule(), id);
	}

	protected String buildAttentionHTML(final PageRequestResponse requestResponse, final ICommonBeanAware commonBean, final String text,
			final String pId, final String editId) {
		final StringBuffer buf = new StringBuffer();
		if (ItSiteUtil.isLogin(requestResponse)) {
			buf.append("<a class='a2' param='" + pId + "=" + commonBean.getId() + "'  id='__doAttention'>");
			buf.append(ItSiteUtil.isAttention(requestResponse, getEFunctionModule(), commonBean.getId(), "取消" + text, text));
			buf.append("</a>(<a class='a2' id='__attentionWindow' param='vtype=").append(getEFunctionModule().name()).append("&attentionId=");
			buf.append(commonBean.getId()).append("'>").append(commonBean.getAttentions()).append("</a>) ");
			if (editId != null && ItSiteUtil.isManageOrSelf(requestResponse, this, commonBean.getUserId())) {
				buf.append("<span style='margin: 0px 4px;'> | </span><a class='a2'");
				buf.append("id='" + editId + "' param='" + pId + "=" + commonBean.getId() + "'>修改</a> ");
			}
			buf.append(HTMLBuilder.SEP).append(" ");
		}
		return buf.toString();
	}

	@Override
	public String getActionHTML(final PageRequestResponse requestResponse, final ICommonBeanAware commonBean) {
		return null;
	}

	protected void doAttentionMail(final ComponentParameter compParameter, final ISentCallback attentionMail) {
		((ITaskExecutorAware) PageUtils.pageContext.getApplication()).getTaskExecutor().execute(new ExecutorRunnable() {
			@Override
			public void task() {
				AttentionUtils.sentMessage(compParameter, getEFunctionModule(), attentionMail);
			}
		});
	}

	protected abstract class AttentionMail implements ISentCallback {
		private final ComponentParameter compParameter;

		private final IAttentionsBeanAware attentionsBean;

		protected AttentionMail(final ComponentParameter compParameter, final IAttentionsBeanAware attentionsBean) {
			this.compParameter = compParameter;
			this.attentionsBean = attentionsBean;
		}

		@Override
		public Object getId() {
			return attentionsBean.getId();
		}

		@Override
		public boolean isSent(final IUser toUser) {
			return true;
		}

		protected String linkUrl(final String url) {
			final StringBuilder sb = new StringBuilder();
			final String href = ((WebApplicationConfig) PageUtils.pageContext.getApplication().getApplicationConfig()).getServerUrl()
					+ compParameter.wrapContextPath(url);
			sb.append("<a target=\"_blank\" href=\"").append(href).append("\">");
			sb.append(href).append("</a>");
			return sb.toString();
		}

		protected String getFromTemplate(final Map<String, Object> variable, final Class<?> templateClazz) {
			try {
				final String readerString = IoUtils.getStringFromInputStream(templateClazz.getClassLoader().getResourceAsStream(
						BeanUtils.getResourceClasspath(templateClazz, "attention.html")));
				return ScriptEvalUtils.replaceExpr(variable, readerString);
			} catch (final Exception e) {
				throw HandleException.wrapException(e);
			}
		}
	}

	@Override
	public void doAttentionSent(final ComponentParameter compParameter, final RemarkItem remark, final Class<?> classBean) {
		final AbstractCommonBeanAware commonBean = (AbstractCommonBeanAware) getBean(classBean, remark.getDocumentId());

		doAttentionMail(compParameter, new AttentionMail(compParameter, commonBean) {
			@Override
			public boolean isSent(final IUser toUser) {
				return !toUser.getId().equals2(remark.getUserId());
			}

			@Override
			public String getSubject(final IUser toUser) {
				final StringBuilder sb = new StringBuilder();
				sb.append("\"").append(remark.getUserText()).append("\"").append(LocaleI18n.getMessage("DefaultNewsPagerHandle.1"));
				return sb.toString();
			}

			@Override
			public String getBody(final IUser toUser) {
				final Map<String, Object> variable = new HashMap<String, Object>();
				variable.put("topicHref", linkUrl(getViewUrl(commonBean.getId())));
				variable.put("toUser", toUser);
				variable.put("createUser", commonBean.getUserText());
				variable.put("createDate", ConvertUtils.toDateString(commonBean.getCreateDate()));
				variable.put("fromUser", remark.getUserText());
				variable.put("topic", commonBean.getTopicName());
				variable.put("content", remark.getContent());
				variable.put("type", getTypeName());
				return getFromTemplate(variable, AItSiteAppclicationModule.class);
			}
		});
	}

	protected String getTypeName() {
		return null;
	}

	@Override
	public String getCatalogName(Object id, Class<?> cla) {
		AbstractCatalog catalog = (AbstractCatalog) getBean(cla, id);
		if (catalog == null) {
			return "";
		}
		return catalog.getText();
	}
}
