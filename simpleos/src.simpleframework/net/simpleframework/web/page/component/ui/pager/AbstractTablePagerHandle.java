package net.simpleframework.web.page.component.ui.pager;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.ado.ListDataObjectQuery;
import net.simpleframework.core.ado.db.Column;
import net.simpleframework.core.ado.db.EFilterOpe;
import net.simpleframework.core.ado.db.EFilterRelation;
import net.simpleframework.core.ado.db.EOrder;
import net.simpleframework.core.ado.db.FilterItem;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.CSVWriter;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.HandleException;
import net.simpleframework.web.page.component.base.validation.EValidatorMethod;
import net.simpleframework.web.page.component.base.validation.ValidatorBean;
import net.simpleframework.web.page.component.ui.menu.MenuBean;
import net.simpleframework.web.page.component.ui.menu.MenuItem;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractTablePagerHandle extends AbstractPagerHandle
		implements ITablePagerHandle {

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter,
			final String beanProperty) {
		if ("exportAction".equals(beanProperty)) {
			final AbstractComponentBean bean = compParameter.componentBean;
			final String export = ((AbstractTablePagerBean) bean)
					.getExportAction();
			if (bean instanceof AbstractTablePagerBean
					&& !"true".equalsIgnoreCase(export)
					&& !"false".equalsIgnoreCase(export)) {
				final StringBuilder sb = new StringBuilder();
				sb.append("$Actions['")
						.append(getBeanProperty(compParameter, "name"))
						.append("'].exportFile();");
				return sb.toString();
			}
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public AbstractTablePagerData createTablePagerData(
			final ComponentParameter compParameter) {
		return new DefaultTablePagerData(compParameter);
	}

	protected class DefaultTablePagerData extends AbstractTablePagerData {
		protected DefaultTablePagerData(final ComponentParameter compParameter) {
			super(compParameter);
		}

		@Override
		protected Map<Object, Object> getRowData(final Object dataObject) {
			return getTableRow(compParameter, dataObject);
		}

		@Override
		protected Map<Object, Object> getRowAttributes(final Object dataObject) {
			final Map<Object, Object> attributes = super
					.getRowAttributes(dataObject);
			final Map<Object, Object> attributes2 = getTableRowAttributes(
					compParameter, dataObject);
			if (attributes2 != null) {
				attributes.putAll(attributes2);
			}
			return attributes;
		}
	}

	@SuppressWarnings("unchecked")
	protected Map<Object, Object> getTableRow(
			final ComponentParameter compParameter, final Object dataObject) {
		return BeanUtils.toMap(dataObject, true);
	}

	protected Map<Object, Object> getTableRowAttributes(
			final ComponentParameter compParameter, final Object dataObject) {
		return null;
	}

	@Override
	public List<MenuItem> getHeaderMenu(final ComponentParameter compParameter,
			final MenuBean menuBean) {
		return createXmlMenu("menu2.xml", menuBean);
	}

	@Override
	public Map<String, Object> getFormParameters(
			final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super
				.getFormParameters(compParameter);

		putParameter(compParameter, parameters, TablePagerUtils.PAGER_SORT_COL);
		putParameter(compParameter, parameters, TablePagerUtils.PAGER_SORT);
		putParameter(compParameter, parameters, "tbl_height");
		putParameter(compParameter, parameters, "tbl_width");

		final Map<String, Column> columns = getFilterColumns(compParameter);
		if (columns != null) {
			final StringBuilder sb = new StringBuilder();
			int i = 0;
			for (final Column column : columns.values()) {
				final String ovalue = (String) column.getAttribute("ovalue");
				if (StringUtils.hasText(ovalue)) {
					final String col = column.getColumnName();
					if (i++ > 0) {
						sb.append(";");
					}
					sb.append(col);
					parameters
							.put(TablePagerUtils.filterColumnKey(col), ovalue);
				}
			}
			if (sb.length() > 0) {
				parameters.put(TablePagerUtils.PAGER_FILTER_COL, sb.toString());
			}
		}
		return parameters;
	}

	@Override
	public List<?> getData2Top(final ComponentParameter compParameter) {
		return null;
	}

	// implements IPagerHandle

	private void doSortList(final ComponentParameter compParameter,
			final ListDataObjectQuery<?> dataQuery) {
		final Column column = getSortColumn(compParameter);
		if (column == null) {
			return;
		}
		final String col = column.getColumnName();
		Collections.sort(dataQuery.getList(), new Comparator<Object>() {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public int compare(final Object o1, final Object o2) {
				try {
					final Comparable c1 = (Comparable) BeanUtils.getProperty(
							o1, col);
					final Comparable c2 = (Comparable) BeanUtils.getProperty(
							o2, col);
					if (column.getOrder() == EOrder.desc) {
						return c1.compareTo(c2);
					} else {
						return c2.compareTo(c1);
					}
				} catch (final Exception e) {
				}
				return 0;
			}
		});
	}

	@Override
	public Column getSortColumn(final ComponentParameter compParameter) {
		final String col = compParameter
				.getRequestParameter(TablePagerUtils.PAGER_SORT_COL);
		final TablePagerColumn oCol;
		if (StringUtils.hasText(col)
				&& (oCol = TablePagerUtils.getTablePagerData(compParameter)
						.getTablePagerColumns().get(col)) != null) {
			final String sort = compParameter
					.getRequestParameter(TablePagerUtils.PAGER_SORT);
			if ("up".equals(sort) || "down".equals(sort)) {
				final Column column = new Column(col);
				column.setColumnSqlName(oCol.getColumnSqlName());
				column.setOrder("up".equals(sort) ? EOrder.desc : EOrder.asc);
				return column;
			}
		}
		return null;
	}

	protected Object convert(final TablePagerColumn oCol, final String val) {
		return ConvertUtils.convert(val, oCol.beanPropertyType());
	}

	@Override
	public Map<String, Column> getFilterColumns(
			final ComponentParameter compParameter) {
		@SuppressWarnings("unchecked")
		Map<String, Column> columns = (Map<String, Column>) compParameter
				.getRequestAttribute("filter_columns");
		if (columns != null) {
			return columns;
		}

		final String col = compParameter
				.getRequestParameter(TablePagerUtils.PAGER_FILTER_COL);
		final String ccol = compParameter
				.getRequestParameter(TablePagerUtils.PAGER_FILTER_CUR_COL);
		if (!StringUtils.hasText(ccol) && !StringUtils.hasText(col)) {
			return null;
		}

		final HashSet<String> sets = new LinkedHashSet<String>(
				Arrays.asList(StringUtils.split(StringUtils.blank(col))));
		String filter = null;
		if (StringUtils.hasText(ccol)) {
			filter = compParameter
					.getRequestParameter(TablePagerUtils.PAGER_FILTER);
			if (filter != null && filter.endsWith("none")) {
				sets.remove(ccol);
			} else {
				filter = StringUtils.text(filter, compParameter
						.getRequestParameter(TablePagerUtils
								.filterColumnKey(ccol)));
				if (StringUtils.hasText(filter)) {
					sets.add(ccol);
				} else {
					sets.remove(ccol);
				}
			}
		}
		if (sets.size() == 0) {
			return null;
		}

		final Map<String, TablePagerColumn> tableColumns = TablePagerUtils
				.getTablePagerData(compParameter).getTablePagerColumns();
		columns = new LinkedHashMap<String, Column>();
		for (final String str : sets) {
			final TablePagerColumn oCol = tableColumns.get(str);
			if (oCol == null) {
				continue;
			}
			final String vStr = str.equals(ccol) ? filter : compParameter
					.getRequestParameter(TablePagerUtils.filterColumnKey(str));
			final String[] vals = StringUtils.split(vStr);
			if (vals == null || vals.length < 2) {
				continue;
			}
			final Column column = new Column(str);
			column.setBeanPropertyType(oCol.beanPropertyType());
			columns.put(str, column);
			column.setAttribute("ovalue", vStr);
			String val = vals[1];
			// String val = StringUtils.decodeHexString(vals[1]);
			FilterItem item = new FilterItem(EFilterRelation.get(vals[0]),
					convert(oCol, val));
			item.setOvalue(val);
			column.getFilterItems().add(item);
			if (vals.length == 5) {
				item.setOpe(EFilterOpe.get(vals[2]));
				val = vals[4];
				// val = StringUtils.decodeHexString(vals[4]);
				item = new FilterItem(EFilterRelation.get(vals[3]), convert(
						oCol, val));
				item.setOvalue(val);
				column.getFilterItems().add(item);
			}
		}
		compParameter.setRequestAttribute("filter_columns", columns);
		return columns;
	}

	protected String filterItemExpr(final FilterItem item,
			final Collection<Object> params) {
		final StringBuilder sb = new StringBuilder();
		final EFilterRelation relation = item.getRelation();
		sb.append(" ").append(relation);
		if (relation == EFilterRelation.like) {
			sb.append(" '%").append(item.getValue()).append("%'");
		} else {
			sb.append(" ?");
			params.add(item.getValue());
		}
		final EFilterOpe ope = item.getOpe();
		if (ope != null) {
			sb.append(" ").append(ope).append(" ");
		}
		return sb.toString();
	}

	@Override
	public Collection<ValidatorBean> getFilterColumnValidators(
			final ComponentParameter compParameter, final TablePagerColumn oCol) {
		final ArrayList<ValidatorBean> al = new ArrayList<ValidatorBean>();
		if (oCol == null)
			return al;
		if (Number.class.isAssignableFrom(oCol.beanPropertyType())) {
			final ValidatorBean validator = new ValidatorBean(null);
			validator.setMethod(EValidatorMethod.number);
			validator.setSelector("#tp_filter_v1, #tp_filter_v2");
			al.add(validator);
		}
		return al;
	}

	private static String keyFilterList = "__tablepager_filter_list";

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void doFilterList(final ComponentParameter compParameter,
			final ListDataObjectQuery<?> dataQuery) {
		final Map<String, Column> filterColumns = getFilterColumns(compParameter);
		List oData = (List) compParameter.getSessionAttribute(keyFilterList);
		if (filterColumns != null && filterColumns.size() > 0) {
			if (oData == null) {
				compParameter.setSessionAttribute(keyFilterList,
						oData = new ArrayList(dataQuery.getList()));
			}
			final ArrayList result = new ArrayList(oData);
			for (final Column oCol : filterColumns.values()) {
				final Iterator<FilterItem> it = oCol.getFilterItems()
						.iterator();
				final FilterItem item1 = it.hasNext() ? it.next() : null;
				if (item1 == null) {
					continue;
				}
				final String propertyName = oCol.getColumnName();
				final Class<?> propertyType = oCol.getBeanPropertyType();
				final FilterItem item2 = it.hasNext() ? it.next() : null;
				ArrayList result2 = new ArrayList(result);
				result.clear();
				for (final Object o : result2) {
					final Object v = BeanUtils.getProperty(o, propertyName);
					boolean delete = item1.isDelete(v, propertyType);
					if (item1.getOpe() == EFilterOpe.or && item2 != null) {
						delete = item2.isDelete(v, propertyType);
					}
					if (!delete) {
						result.add(o);
					}
				}
				if (item1.getOpe() == EFilterOpe.and && item2 != null) {
					result2 = new ArrayList(result);
					result.clear();
					for (final Object o : result2) {
						final Object v = BeanUtils.getProperty(o, propertyName);
						if (!item2.isDelete(v, propertyType)) {
							result.add(o);
						}
					}
				}
			}
			final List data = dataQuery.getList();
			if (data.size() > result.size()) {
				data.clear();
				data.addAll(result);
			}
		} else {
			if (oData != null) {
				final List data = dataQuery.getList();
				data.clear();
				data.addAll(oData);
				compParameter.removeSessionAttribute(keyFilterList);
			}
		}
	}

	@Override
	protected void doCount(final ComponentParameter compParameter,
			final IDataObjectQuery<?> dataQuery) {
		if (dataQuery instanceof ListDataObjectQuery) {
			doFilterList(compParameter, (ListDataObjectQuery<?>) dataQuery);
		}
		super.doCount(compParameter, dataQuery);
	}

	@Override
	protected void doResult(final ComponentParameter compParameter,
			final IDataObjectQuery<?> dataQuery, final int start) {
		if (dataQuery instanceof ListDataObjectQuery) {
			doSortList(compParameter, (ListDataObjectQuery<?>) dataQuery);
		}
		super.doResult(compParameter, dataQuery, start);
	}

	protected void addFirstAndLastDataItem(
			final ComponentParameter compParameter,
			final IDataObjectQuery<?> dataQuery) {
		dataQuery.move(-1);
		compParameter.setRequestAttribute(FIRST_ITEM, dataQuery.next());
		dataQuery.move(dataQuery.getCount() - 2);
		compParameter.setRequestAttribute(LAST_ITEM, dataQuery.next());
	}

	@Override
	public void export(final ComponentParameter compParameter,
			final EExportFileType filetype,
			final Map<String, TablePagerColumn> columns) {
		final IDataObjectQuery<?> dQuery = createDataObjectQuery(compParameter);
		dQuery.setFetchSize(0);
		final AbstractTablePagerData tablePagerData = TablePagerUtils
				.getTablePagerData(compParameter);
		try {
			if (filetype == EExportFileType.csv) {
				doCSVExport(compParameter, dQuery, tablePagerData, columns);
			} else if (filetype == EExportFileType.excel) {
				doExcelExport(compParameter, dQuery, tablePagerData, columns);
			}
		} catch (final IOException e) {
			throw HandleException.wrapException(e);
		}
	}

	protected void doCSVExport(final ComponentParameter compParameter,
			final IDataObjectQuery<?> dQuery,
			final AbstractTablePagerData tablePagerData,
			final Map<String, TablePagerColumn> columns) throws IOException {
		CSVWriter csvWriter = null;
		try {
			compParameter
					.setSessionAttribute(
							TablePagerExportProgressBar.MAX_PROGRESS,
							dQuery.getCount());
			csvWriter = new CSVWriter(new OutputStreamWriter(
					compParameter.getFileOutputStream("export.csv", 0),
					PageUtils.pageConfig.getCharset()));
			final ArrayList<String> al = new ArrayList<String>();
			for (final TablePagerColumn column : columns.values()) {
				al.add(column.getColumnText());
			}
			csvWriter.writeNext(al.toArray(new String[al.size()]));
			Object object;
			int i = 1;
			while ((object = dQuery.next()) != null) {
				al.clear();
				for (final Object o : tablePagerData.getExportRowData(columns,
						object).values()) {
					al.add(StringUtils.blank(o));
				}
				csvWriter.writeNext(al.toArray(new String[al.size()]));
				compParameter.setSessionAttribute(
						TablePagerExportProgressBar.STEP, i++);
			}
		} finally {
			try {
				if (csvWriter != null) {
					csvWriter.close();
				}
			} catch (final IOException e) {
			}
		}
	}

	protected void doExcelExport(final ComponentParameter compParameter,
			final IDataObjectQuery<?> dQuery,
			final AbstractTablePagerData tablePagerData,
			final Map<String, TablePagerColumn> columns) throws IOException {
	}
}
