package net.simpleos.module.docu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.ado.lucene.AbstractLuceneManager;
import net.simpleframework.content.EContentStatus;
import net.simpleframework.core.IInitializer;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.core.bean.IIdBeanAware;
import net.simpleframework.core.bean.ITreeBeanAware;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.organization.component.login.LoginUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.util.IoUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.page.IPageConstants;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.tabs.EMatchMethod;
import net.simpleframework.web.page.component.ui.tabs.TabHref;
import net.simpleframework.web.page.component.ui.tabs.TabsUtils;
import net.simpleos.SimpleosUtil;
import net.simpleos.i.ICommonBeanAware;
import net.simpleos.impl.ASimpleosAppclicationModule;
import net.simpleos.mvc.myfavorite.MyFavoriteUtils;

/**
 * 
 * @email eliyanfei@126.com
 * @date 2014年11月21日 下午10:34:29 
 *
 */
public class DocuAppModule extends ASimpleosAppclicationModule implements IDocuAppModule {
	private String deployName = "docu";

	public String getDeployName() {
		return deployName;
	}

	@Override
	public void init(IInitializer initializer) {
		try {
			super.init(initializer);
			doInit(DocuUtils.class, deployName);
			final ITableEntityManager tMgr = getDataObjectManager();
			DocuUtils.setDocuCounter(tMgr.getCount(new SQLValue("select count(id) from " + tMgr.getTablename() + " where status=?",
					new Object[] { EContentStatus.publish })));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getViewUrl(Object id) {
		final StringBuilder sb = new StringBuilder();
		sb.append("/docu").append("/v/").append(id);
		sb.append(".html");
		return sb.toString();
	}

	@Override
	public Class<? extends IIdBeanAware> getEntityBeanClass() {
		return DocuBean.class;
	}

	@Override
	protected Class<?> getRemarkEntityBeanClass() {
		return DocuRemark.class;
	}

	public static final Table docu_documentshare = new Table("simpleos_document");
	public static final Table docu_catalog = new Table("simpleos_document_catalog");
	public static final Table docu_remark = new Table("simpleos_document_remark");
	public static final Table docu_log = new Table("simpleos_document_log");

	@Override
	protected void putTables(Map<Class<?>, Table> tables) {
		super.putTables(tables);
		tables.put(DocuBean.class, docu_documentshare);
		tables.put(DocuCatalog.class, docu_catalog);
		tables.put(DocuRemark.class, docu_remark);
		tables.put(DocuLogBean.class, docu_log);
	}

	@Override
	public AbstractComponentBean getComponentBean(PageRequestResponse requestResponse) {
		return null;
	}

	@Override
	protected String getParameters(PageRequestResponse requestResponse) {
		final String s = StringUtils.text(requestResponse.getRequestParameter("s"), "docu");
		return "s=" + s;
	}

	@Override
	public String getViewUrl1(PageRequestResponse requestResponse, Object... params) {
		final StringBuilder sb = new StringBuilder();
		sb.append("/mydocu.html");
		return sb.toString();
	}

	@Override
	protected Map<Integer, String> getOrderData() {
		Map<Integer, String> orderData = super.getOrderData();
		orderData.put(4, LocaleI18n.getMessage("Docu.3"));
		orderData.put(5, LocaleI18n.getMessage("Docu.4"));
		return orderData;
	}

	public String tabs13(PageRequestResponse requestResponse) {
		final StringBuffer buf = new StringBuffer();
		int tempOd = 0;
		final int temp = ConvertUtils.toInt(requestResponse.getRequestParameter("od"), tempOd);
		int i = 0;
		Map<Integer, String> orderData = getOrderData();
		for (final Integer od : orderData.keySet()) {
			buf.append("<a onclick=\"$IT.active(this,'#docu_bar_tab a','nav_arrow');$IT.A('documentPaperAct','od=" + od + "');\"");
			buf.append(" href=\"javascript:void;\"");
			if (i == temp) {
				buf.append(" class=\"nav_arrow\"");
			}
			buf.append(">").append(orderData.get(od)).append("</a>");
			if (i++ != orderData.size() - 1) {
				buf.append("<span style=\"margin: 0px 4px;\">|</span>");
			}
		}
		return buf.toString();
	}

	@Override
	public String tabs(PageRequestResponse requestResponse) {
		final List<TabHref> tabHrefs = new ArrayList<TabHref>();
		TabHref href = new TabHref(LocaleI18n.getMessage("Docu.2"), "/docu.html");
		href.setMatchMethod(EMatchMethod.startsWith);
		tabHrefs.add(href);
		final IAccount login = AccountSession.getLogin(requestResponse.getSession());
		href = new TabHref(LocaleI18n.getMessage("Docu.1"), login == null ? LoginUtils.getLocationPath() : "/mydocu.html");
		href.setMatchMethod(EMatchMethod.startsWith);
		tabHrefs.add(href);
		return TabsUtils.tabs(requestResponse, tabHrefs.toArray(new TabHref[tabHrefs.size()]));
	}

	@Override
	public String tabs2(PageRequestResponse requestResponse) {
		final StringBuffer buf = new StringBuffer();
		return buf.toString();
	}

	@Override
	public String getApplicationUrl(PageRequestResponse requestResponse) {
		return "/docu.html";
	}

	@Override
	public String getDeployPath() {
		return deployName;
	}

	@Override
	public ITableEntityManager getDataObjectManager() {
		return super.getDataObjectManager(DocuBean.class);
	}

	@Override
	public DocuBean getViewDocuBean(PageRequestResponse requestResponse) {
		final DocuBean docuBean = getBean(DocuBean.class, requestResponse.getRequestParameter(DocuUtils.docuId));
		try {
			long views = docuBean.getViews();
			ICommonBeanAware.Utils.updateViews(requestResponse, getDataObjectManager(), docuBean);
			if (views != docuBean.getViews()) {
				// 添加全文索引
				DocuUtils.applicationModule.createLuceneManager(new ComponentParameter(requestResponse.request, requestResponse.response, null))
						.objects2DocumentsBackground(docuBean);
			}
		} catch (final Exception e) {
		}
		return docuBean;
	}

	@Override
	public IQueryEntitySet<DocuCatalog> queryCatalogs(PageRequestResponse requestResponse, ITreeBeanAware parent) {
		final ITableEntityManager catalog_mgr = getDataObjectManager(DocuCatalog.class);
		final StringBuilder sql = new StringBuilder();
		final ArrayList<Object> al = new ArrayList<Object>();
		if (parent == null) {
			sql.append(Table.nullExpr(catalog_mgr.getTable(), "parentid"));
		} else {
			sql.append("parentid=?");
			al.add(parent.getId());
		}
		sql.append(" order by oorder desc");
		return catalog_mgr.query(new ExpressionValue(sql.toString(), al.toArray()), DocuCatalog.class);
	}

	@Override
	public IQueryEntitySet<DocuCatalog> queryCatalogs(Object catalogId) {
		final ITableEntityManager catalog_mgr = getDataObjectManager(DocuCatalog.class);
		final StringBuilder sql = new StringBuilder();
		final ArrayList<Object> al = new ArrayList<Object>();
		if (catalogId == null) {
			sql.append(Table.nullExpr(catalog_mgr.getTable(), "parentid"));
		} else {
			sql.append("parentid=?");
			al.add(catalogId);
		}
		sql.append(" order by oorder desc");
		return catalog_mgr.query(new ExpressionValue(sql.toString(), al.toArray()), DocuCatalog.class);
	}

	@Override
	public AbstractLuceneManager createLuceneManager(final ComponentParameter compParameter) {
		final File iPath = new File(compParameter.getServletContext().getRealPath(IPageConstants.DATA_HOME + "/" + "docu_index"));
		IoUtils.createDirectoryRecursively(iPath);
		return new DocuLuceneManager(compParameter, iPath);
	}

	@Override
	public EFunctionModule getEFunctionModule() {
		return EFunctionModule.docu;
	}

	@Override
	public String getActionHTML(final PageRequestResponse requestResponse, final ICommonBeanAware commonBean) {
		final IAccount account = SimpleosUtil.getLoginAccount(requestResponse);
		final StringBuffer buf = new StringBuffer();
		if (account != null) {
			buf.append("<a class=\"a2\" id='favorite_act' href='javascript:void;' dl='false' onclick=\"");
			DocuBean docuBean = (DocuBean) commonBean;
			long c = MyFavoriteUtils.getFavorites(commonBean.getId(), account.getId());
			if (c > 0) {
				buf.append("$Actions['cancelFavorite']('refId=" + commonBean.getId() + "');\">");
				buf.append(LocaleI18n.getMessage("AbstractContentPagerHandle.2"));
			} else {
				buf.append("$Actions['addFavorite']('refId=" + commonBean.getId() + "&title=" + docuBean.getTitle() + "&type="
						+ EFunctionModule.docu.name() + "');\">");
				buf.append(LocaleI18n.getMessage("AbstractContentPagerHandle.1"));
			}
			buf.append("</a>");
			buf.append("&nbsp;(<a id='favorite_num' dl='false' onclick=\"$Actions['attentionUsersWindow']('attentionId=").append(docuBean.getId())
					.append("');\">").append(c).append("</a>) ");

			buf.append(HTMLBuilder.SEP).append(" ");
		}
		return buf.toString();
	}

	@Override
	protected String getTypeName() {
		return LocaleI18n.getMessage("Docu.0");
	}

}
