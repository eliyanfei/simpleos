package net.simpleframework.content.blog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.content.EContentStatus;
import net.simpleframework.content.EContentType;
import net.simpleframework.content.component.newspager.DefaultNewsPagerHandle;
import net.simpleframework.content.component.newspager.ENewsTemplate;
import net.simpleframework.content.component.newspager.NewsBean;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.core.bean.ITreeBeanAware;
import net.simpleframework.core.id.ID;
import net.simpleframework.my.space.MySpaceUtils;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountContext;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.TablePagerColumn;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractBlogPagerHandle extends DefaultNewsPagerHandle {
	@Override
	public IBlogApplicationModule getApplicationModule() {
		return BlogUtils.applicationModule;
	}

	@Override
	public Class<? extends IDataObjectBean> getEntityBeanClass() {
		return Blog.class;
	}

	@Override
	public String getRemarkHandleClass(final ComponentParameter compParameter) {
		return BlogRemarkHandle.class.getName();
	}

	@Override
	public BlogCatalog getCatalogById(final ComponentParameter compParameter, final Object id) {
		return getTableEntityManager(compParameter, BlogCatalog.class).queryForObjectById(id, BlogCatalog.class);
	}

	@Override
	public String getCatalogIdName(final PageRequestResponse requestResponse) {
		return getApplicationModule().getCatalogIdName(requestResponse);
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, "c");
		return parameters;
	}

	@Override
	public Collection<? extends ITreeBeanAware> getSelectedCatalogs(final ComponentParameter compParameter, final ITreeBeanAware parent) {
		final IAccount login = AccountSession.getLogin(compParameter.getSession());
		if (login == null) {
			return null;
		}
		final ITableEntityManager catalog_mgr = BlogUtils.getTableEntityManager(BlogCatalog.class);
		final StringBuilder sql = new StringBuilder();
		final ArrayList<Object> al = new ArrayList<Object>();
		sql.append("userid=?");
		al.add(login.getId());
		sql.append(" and ");
		if (parent == null) {
			sql.append(Table.nullExpr(catalog_mgr.getTable(), "parentid"));
		} else {
			sql.append("parentid=?");
			al.add(parent.getId());
		}
		return IDataObjectQuery.Utils.toList(catalog_mgr.query(new ExpressionValue(sql.toString(), al.toArray()), BlogCatalog.class));
	}

	@Override
	public String getNavigateHTML(final ComponentParameter compParameter) {
		final StringBuilder sb = new StringBuilder();
		final IBlogApplicationModule aModule = getApplicationModule();
		sb.append("<a href=\"").append(aModule.getApplicationUrl(compParameter)).append("\">").append(aModule.getApplicationText()).append("</a>");
		return sb.toString();
	}

	@Override
	public <T extends IDataObjectBean> void doBeforeAdd(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		super.doBeforeAdd(compParameter, temgr, t, data, beanClazz);
		final Blog blog = (Blog) t;
		blog.setViewTemplate(ENewsTemplate.t_blog);
		blog.setStatus(EContentStatus.publish);

		final ITableEntityManager catalogmgr = getTableEntityManager(compParameter, BlogCatalog.class);
		final BlogCatalog catalog = catalogmgr.queryForObjectById(blog.getCatalogId(), BlogCatalog.class);
		if (catalog != null) {
			catalog.setBlogs(catalog.getBlogs() + 1);
			catalogmgr.update(new Object[] { "blogs" }, catalog);
		}
	}

	@Override
	public <T extends IDataObjectBean> void doEditCallback(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		super.doEditCallback(compParameter, temgr, t, data, beanClazz);

		final Blog blog = (Blog) t;
		if (data.get("ttype") == EContentType.recommended) {
			AccountContext.update(OrgUtils.am().queryForObjectById(blog.getUserId()), "blog_typeRecommended", blog.getId());
		}

	}

	@Override
	public <T extends IDataObjectBean> void doAddCallback(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		super.doAddCallback(compParameter, temgr, t, data, beanClazz);

		final Blog blog = (Blog) t;
		final IAccount account = OrgUtils.am().queryForObjectById(blog.getUserId());
		AccountContext.update(account, "blog_add", blog.getId());

		MySpaceUtils.addSapceLog(compParameter, null, EFunctionModule.blog, blog.getId());
	}

	@Override
	public <T extends IDataObjectBean> void doDelete(final ComponentParameter compParameter, final IDataObjectValue ev, final Class<T> beanClazz) {
		compParameter
				.setRequestAttribute("__delete_blogs", IDataObjectQuery.Utils.toList(getTableEntityManager(compParameter).query(ev, Blog.class)));
		super.doDelete(compParameter, ev, beanClazz);
	}

	@Override
	public <T extends IDataObjectBean> void doDeleteCallback(final ComponentParameter compParameter, final IDataObjectValue dataObjectValue,
			final Class<T> beanClazz) {
		super.doDeleteCallback(compParameter, dataObjectValue, beanClazz);
		final List<?> l = (List<?>) compParameter.getRequestAttribute("__delete_blogs");
		if (l != null) {
			final Map<ID, BlogCatalog> catalogs = new HashMap<ID, BlogCatalog>();
			for (final Object o : l) {
				final BlogCatalog blogCatalog = getCatalogById(compParameter, ((Blog) o).getCatalogId());
				if (blogCatalog != null) {
					blogCatalog.setBlogs(blogCatalog.getBlogs() - 1);
					catalogs.put(blogCatalog.getId(), blogCatalog);
				}
			}
			getTableEntityManager(compParameter, BlogCatalog.class).update(catalogs.values().toArray());
		}
	}

	@Override
	public String getViewUrl(final ComponentParameter compParameter, final NewsBean news) {
		return getApplicationModule().getBlogViewUrl(compParameter, (Blog) news);
	}

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("showCheckbox".equals(beanProperty)) {
			return isEditable(compParameter);
		} else if ("jobView".equals(beanProperty)) {
			return IJob.sj_anonymous;
		} else if (beanProperty != null && beanProperty.startsWith("job")) {
			return IJob.sj_account_normal;
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	protected abstract boolean isEditable(final PageRequestResponse requestResponse);

	protected abstract class BlogTablePagerData extends NewsTablePagerData {
		protected String c;

		public BlogTablePagerData(final ComponentParameter compParameter) {
			super(compParameter);
			c = WebUtils.toLocaleString(compParameter.getRequestParameter("c"));
		}

		@Override
		public Map<String, TablePagerColumn> getTablePagerColumns() {
			final Map<String, TablePagerColumn> columns = cloneTablePagerColumns();
			columns.get("topic").setSort(false);
			columns.remove("status");
			columns.remove("createdate");
			columns.remove("author");
			columns.remove("remarks");
			columns.remove("views");
			if (!isEditable(compParameter)) {
				columns.remove("action");
			}
			return columns;
		}

		protected abstract String getTitle(final Blog blog);

		@Override
		protected Map<Object, Object> getRowData(final Object dataObject) {
			final Blog blog = (Blog) dataObject;
			final Map<Object, Object> rowData = new HashMap<Object, Object>();
			rowData.put("topic", getTitle(blog));
			rowData.put("views", wrapNum(blog.getViews()));
			rowData.put("remarks", wrapNum(blog.getRemarks()));
			rowData.put("action", ACTIONc);
			return rowData;
		}
	}

	/********************* blog attention *********************/
	@Override
	public EFunctionModule getFunctionModule() {
		return EFunctionModule.blog;
	}
}
