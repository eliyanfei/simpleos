package net.simpleframework.web.page.component.ui.pager.db;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.sql.DataSource;

import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.AbstractQueryEntitySet;
import net.simpleframework.ado.db.AbstractQueryEntitySet.ResultSetMetaDataCallback;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.core.IApplicationModule;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.ado.db.Column;
import net.simpleframework.core.ado.db.FilterItem;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.core.bean.ITextBeanAware;
import net.simpleframework.core.bean.ITreeBeanAware;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.LangUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.util.db.SQLParserUtils;
import net.simpleframework.web.page.ETextAlign;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.HandleException;
import net.simpleframework.web.page.component.ado.IDbComponentHandle.IDbComponentWrapperCallback;
import net.simpleframework.web.page.component.ui.pager.AbstractTablePagerData;
import net.simpleframework.web.page.component.ui.pager.AbstractTablePagerHandle;
import net.simpleframework.web.page.component.ui.pager.TablePagerColumn;
import net.simpleframework.web.page.component.ui.pager.TablePagerUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractDbTablePagerHandle extends AbstractTablePagerHandle implements IDbTablePagerHandle, IDbComponentWrapperCallback {
	@Override
	public AbstractTablePagerData createTablePagerData(final ComponentParameter compParameter) {
		return new DefaultDbTablePagerData(compParameter);
	}

	protected class DefaultDbTablePagerData extends DefaultTablePagerData {
		protected DefaultDbTablePagerData(final ComponentParameter compParameter) {
			super(compParameter);
		}

		@Override
		public Map<String, TablePagerColumn> getTablePagerColumns() {
			final Map<String, TablePagerColumn> columns = super.getTablePagerColumns();
			if (columns.size() == 0) {
				final AbstractQueryEntitySet<?> qs = (AbstractQueryEntitySet<?>) getDataObjectQuery(compParameter);
				qs.doResultSetMetaData(new ResultSetMetaDataCallback() {
					@Override
					public Object doResultSetMetaData(final ResultSetMetaData metaData) throws SQLException {
						for (int i = 1; i <= metaData.getColumnCount(); i++) {
							final String name = metaData.getColumnName(i);
							final String label = metaData.getColumnLabel(i);
							final TablePagerColumn col = new TablePagerColumn(name, label);
							if (i > 1) {
								col.setSeparator(true);
							}
							col.setWidth(Math.max(label.length() * 12, 60));
							col.setTextAlign(ETextAlign.center);
							col.setFilter(true);
							columns.put(name, col);
						}
						return null;
					}
				});
			}
			return columns;
		}
	}

	protected void doSortSQL(final ComponentParameter compParameter, final AbstractQueryEntitySet<?> qs) {
		final Column column = getSortColumn(compParameter);
		if (column != null) {
			final SQLValue sqlValue = qs.getSqlValue();
			sqlValue.setSql(SQLParserUtils.addOrderBy(qs.getEntityManager().getDataSource(), sqlValue.getSql(), column));
		}
	}

	protected void doFilterSQL(final ComponentParameter compParameter, final AbstractQueryEntitySet<?> qs) {
		final Map<String, Column> filterColumns = getFilterColumns(compParameter);
		if (filterColumns != null && filterColumns.size() > 0) {
			doFilterSQLInternal(compParameter, qs, filterColumns);
		}
	}

	protected void doFilterSQLInternal(final ComponentParameter compParameter, final AbstractQueryEntitySet<?> qs,
			final Map<String, Column> filterColumns) {
		final ArrayList<Object> params = new ArrayList<Object>();
		final ArrayList<String> lExpr = new ArrayList<String>();
		final Map<String, TablePagerColumn> columns = TablePagerUtils.getTablePagerData(compParameter).getTablePagerColumns();
		for (final Map.Entry<String, Column> entry : filterColumns.entrySet()) {
			final TablePagerColumn oCol = columns.get(entry.getKey());
			if (oCol == null) {
				continue;
			}

			final Iterator<FilterItem> items = entry.getValue().getFilterItems().iterator();
			if (!items.hasNext()) {
				continue;
			}
			final StringBuilder sb = new StringBuilder();
			final String columnSql = oCol.getColumnSqlName();
			sb.append(columnSql).append(filterItemExpr(items.next(), params));
			if (items.hasNext()) {
				sb.append(columnSql).append(filterItemExpr(items.next(), params));
				sb.insert(0, "(");
				sb.append(")");
			}
			lExpr.add(sb.toString());
		}
		if (lExpr.size() > 0) {
			final DataSource dataSource = qs.getEntityManager().getDataSource();
			final String expr = StringUtils.join(lExpr, " and ");

			final SQLValue sqlValue = qs.getSqlValue();
			sqlValue.setSql(SQLParserUtils.addCondition(dataSource, sqlValue.getSql(), expr));
			sqlValue.addValues(params.toArray());

			final SQLValue countSQL = qs.getQueryDialect().getCountSQL();
			if (countSQL != null) {
				countSQL.setSql(SQLParserUtils.addCondition(dataSource, countSQL.getSql(), expr));
				countSQL.addValues(params.toArray());
			}
		}
	}

	@Override
	protected void doCount(final ComponentParameter compParameter, final IDataObjectQuery<?> dataQuery) {
		if (dataQuery instanceof AbstractQueryEntitySet) {
			doFilterSQL(compParameter, (AbstractQueryEntitySet<?>) dataQuery);
		}
		super.doCount(compParameter, dataQuery);
	}

	@Override
	protected void doResult(final ComponentParameter compParameter, final IDataObjectQuery<?> dataQuery, final int start) {
		if (dataQuery instanceof AbstractQueryEntitySet) {
			doSortSQL(compParameter, (AbstractQueryEntitySet<?>) dataQuery);
		}
		super.doResult(compParameter, dataQuery, start);
	}

	@Override
	public IApplicationModule getApplicationModule() {
		return null;
	}

	@Override
	public String getJavascriptCallback(final ComponentParameter compParameter, final String jsAction, final Object bean) {
		String jsCallback = "";
		if (LangUtils.contains(new String[] { "add", "edit", "delete", "undelete", "exchange", "move2", "upload" }, jsAction)) {
			jsCallback += "var act = $Actions['" + compParameter.componentBean.getName() + "']; if (act) act.refresh();";
		}
		return jsCallback;
	}

	@Override
	protected String getDefaultjob(final ComponentParameter compParameter) {
		return getManager(compParameter);
	}

	static final String PAGER_ID = "__pager_Id";

	@Override
	public String getIdParameterName(final ComponentParameter compParameter) {
		return PAGER_ID;
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, getCatalogIdName(compParameter), getCatalogId(compParameter));
		return parameters;
	}

	public static final String CATALOG_ID = "__catalog_Id";

	@Override
	public String getCatalogIdName(final PageRequestResponse requestResponse) {
		return CATALOG_ID;
	}

	@Override
	public Object getCatalogId(final ComponentParameter compParameter) {
		return ConvertUtils.toLong(StringUtils.text(compParameter.getRequestParameter(getCatalogIdName(compParameter)),
				(String) compParameter.getBeanProperty("catalogId")));
	}

	@Override
	public ITextBeanAware getCatalogById(final ComponentParameter compParameter, final Object id) {
		return null;
	}

	@Override
	public Collection<? extends ITreeBeanAware> getMove2Catalogs(final ComponentParameter compParameter, final ITreeBeanAware parent) {
		return null;
	}

	@Override
	public Collection<? extends ITreeBeanAware> getSelectedCatalogs(final ComponentParameter compParameter, final ITreeBeanAware parent) {
		return null;
	}

	protected void throwCatalogNull(final ComponentParameter compParameter) {
		if (ConvertUtils.toBoolean(compParameter.getBeanProperty("assertCatalogNull"), false)) {
			throw HandleException.wrapException(LocaleI18n.getMessage("AbstractTablePagerHandle.0"));
		}
	}

	@Override
	public Class<? extends IDataObjectBean> getEntityBeanClass() {
		return null;
	}

	/*---------------------- IDbComponentHandle ----------------*/

	private final DbComponentWrapper wrapper = new DbComponentWrapper(this);

	@Override
	public ITableEntityManager getTableEntityManager(final ComponentParameter compParameter, final Class<?> beanClazz) {
		return wrapper.getTableEntityManager(compParameter, beanClazz);
	}

	@Override
	public ITableEntityManager getTableEntityManager(final ComponentParameter compParameter) {
		return getTableEntityManager(compParameter, getEntityBeanClass());
	}

	@Override
	public <T extends IDataObjectBean> T getEntityBeanByRequest(final ComponentParameter compParameter) {
		return getEntityBeanById(compParameter, compParameter.getRequestParameter(getIdParameterName(compParameter)));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends IDataObjectBean> T getEntityBeanById(final ComponentParameter compParameter, final Object id) {
		return (T) getEntityBeanById(compParameter, id, getEntityBeanClass());
	}

	@Override
	public <T extends IDataObjectBean> T getEntityBeanById(final ComponentParameter compParameter, final Object id, final Class<T> beanClazz) {
		return wrapper.getEntityBeanById(compParameter, id, beanClazz);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends IDataObjectBean> T doEdit(final ComponentParameter compParameter, final Object id, final Map<String, Object> data) {
		return (T) doEdit(compParameter, id, data, getEntityBeanClass());
	}

	@Override
	public <T extends IDataObjectBean> T doEdit(final ComponentParameter compParameter, final Object id, final Map<String, Object> data,
			final Class<T> beanClazz) {
		return wrapper.doEdit(compParameter, id, data, beanClazz);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends IDataObjectBean> T doAdd(final ComponentParameter compParameter, final Map<String, Object> data) {
		return (T) doAdd(compParameter, data, getEntityBeanClass());
	}

	@Override
	public <T extends IDataObjectBean> T doAdd(final ComponentParameter compParameter, final Map<String, Object> data, final Class<T> beanClazz) {
		return wrapper.doAdd(compParameter, data, beanClazz);
	}

	@Override
	public void doDelete(final ComponentParameter compParameter, final IDataObjectValue ev) {
		doDelete(compParameter, ev, getEntityBeanClass());
	}

	@Override
	public <T extends IDataObjectBean> void doDelete(final ComponentParameter compParameter, final IDataObjectValue ev, final Class<T> beanClazz) {
		wrapper.doDelete(compParameter, ev, beanClazz);
	}

	@Override
	public void doUnDelete(final ComponentParameter compParameter, final IDataObjectValue ev) {
		doUnDelete(compParameter, ev, getEntityBeanClass());
	}

	@Override
	public <T extends IDataObjectBean> void doUnDelete(final ComponentParameter compParameter, final IDataObjectValue ev, final Class<T> beanClazz) {
	}

	@Override
	public void doExchange(final ComponentParameter compParameter, final Object id, final Object id2, final boolean up) {
		doExchange(compParameter, id, id2, up, getEntityBeanClass());
	}

	@Override
	public <T extends IDataObjectBean> void doExchange(final ComponentParameter compParameter, final Object id, final Object id2, final boolean up,
			final Class<T> beanClazz) {
		wrapper.doExchange(compParameter, id, id2, up, beanClazz);
	}

	/*---------------------- IDbComponentWrapperCallback ----------------*/

	@Override
	public void putTables(final Map<Class<?>, Table> tables) {
	}

	@Override
	public <T extends IDataObjectBean> void doBeforeEdit(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
	}

	@Override
	public <T extends IDataObjectBean> void doEditCallback(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
	}

	@Override
	public <T extends IDataObjectBean> void doBeforeAdd(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
	}

	@Override
	public <T extends IDataObjectBean> void doAddCallback(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
	}

	@Override
	public <T extends IDataObjectBean> void doBeforeDelete(final ComponentParameter compParameter, final IDataObjectValue dataObjectValue,
			final Class<T> beanClazz) {
	}

	@Override
	public <T extends IDataObjectBean> void doDeleteCallback(final ComponentParameter compParameter, final IDataObjectValue dataObjectValue,
			final Class<T> beanClazz) {
	}

	@Override
	public long getPostInterval() {
		return 0;
	}
}
