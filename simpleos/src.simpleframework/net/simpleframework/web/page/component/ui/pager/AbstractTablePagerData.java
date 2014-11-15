package net.simpleframework.web.page.component.ui.pager;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import net.simpleframework.core.AbstractXmlDocument;
import net.simpleframework.core.bean.IIdBeanAware;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.page.component.ComponentParameter;

import org.apache.commons.collections.map.ListOrderedMap;
import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractTablePagerData {
	public final static String ACTIONc = "<a class=\"m down_menu_image\"></a>";

	public static final TablePagerColumn action = new TablePagerColumn("action", "<a class=\"m2 down_menu_image\"></a>");

	public static final TablePagerColumn icon = new TablePagerColumn("icon");

	static {
		icon.setSort(false);
		icon.setHeaderStyle("width: 20px");
		icon.setStyle("width: 20px; text-align: right;");
		icon.setColumnText("&nbsp;");
		icon.setDefaultExport(false);

		action.setSort(false);
		action.setHeaderStyle("width: 25px");
		action.setStyle("width: 25px; text-align: center");
		action.setSeparator(true);
		action.setDefaultExport(false);
	}

	protected final ComponentParameter compParameter;

	protected AbstractTablePagerData(final ComponentParameter compParameter) {
		this.compParameter = compParameter;
	}

	public Map<String, TablePagerColumn> getTablePagerColumns() {
		return getTablePagerColumnsInternal();
	}

	protected Map<String, TablePagerColumn> cloneTablePagerColumns() {
		@SuppressWarnings("unchecked")
		final Map<String, TablePagerColumn> columns = new ListOrderedMap();
		columns.putAll(getTablePagerColumnsInternal());
		return columns;
	}

	private Map<String, TablePagerColumn> getTablePagerColumnsInternal() {
		if (compParameter.componentBean instanceof TablePagerBean) {
			compParameter.componentBean.parseElement();
			if (tablePagerColumns.size() == 0) {
				tablePagerColumns.putAll(((TablePagerBean) compParameter.componentBean).getColumns());
			}
			return tablePagerColumns;
		} else {
			return getTablePagerColumnsFromXML();
		}
	}

	private final Map<String, TablePagerColumn> tablePagerColumns = new LinkedHashMap<String, TablePagerColumn>();

	private Map<String, TablePagerColumn> getTablePagerColumnsFromXML() {
		if (tablePagerColumns.size() > 0) {
			return tablePagerColumns;
		}
		synchronized (ACTIONc) {
			final InputStream inputStream = BeanUtils.getResourceRecursively(getClass(), "columns.xml");
			if (inputStream != null) {
				new AbstractXmlDocument(inputStream) {
					@Override
					protected void init() throws Exception {
						final Iterator<?> it = getRoot().elementIterator();
						while (it.hasNext()) {
							final Element element = (Element) it.next();
							final String columnName = element.attributeValue("columnName");
							if ("icon".equals(columnName)) {
								tablePagerColumns.put(columnName, icon);
							} else if ("action".equals(columnName)) {
								tablePagerColumns.put(columnName, action);
							} else {
								final TablePagerColumn tablePagerColumn = new TablePagerColumn();
								tablePagerColumns.put(columnName, tablePagerColumn);
								final Iterator<?> attributes = element.attributeIterator();
								while (attributes.hasNext()) {
									final Attribute attribute = (Attribute) attributes.next();
									BeanUtils.setProperty(tablePagerColumn, attribute.getName(), LocaleI18n.replaceI18n(attribute.getValue()));
								}
							}
						}
					}
				};
			}
			return tablePagerColumns;
		}
	}

	protected abstract Map<Object, Object> getRowData(Object dataObject);

	public Map<String, TablePagerColumn> getTablePagerExportColumns() {
		final Map<String, TablePagerColumn> columns = new LinkedHashMap<String, TablePagerColumn>();
		for (final Map.Entry<String, TablePagerColumn> entry : getTablePagerColumns().entrySet()) {
			final TablePagerColumn oCol = entry.getValue();
			if (!oCol.isDefaultExport()) {
				continue;
			}
			columns.put(entry.getKey(), oCol);
		}
		return columns;
	}

	protected Map<String, Object> getExportRowData(final Object dataObject) {
		return getExportRowData(null, dataObject);
	}

	protected Map<String, Object> getExportRowData(Map<String, TablePagerColumn> columns, final Object dataObject) {
		if (columns == null) {
			columns = getTablePagerExportColumns();
		}
		final Map<String, Object> rowData = new LinkedHashMap<String, Object>();
		for (final Map.Entry<String, TablePagerColumn> entry : columns.entrySet()) {
			final String key = entry.getKey();
			Object value;
			try {
				value = BeanUtils.getProperty(dataObject, key);
			} catch (final Exception e) {
				value = "<ERROR>";
			}
			rowData.put(key, value);
		}
		return rowData;
	}

	public static final String ROW_ID = "rowId";

	protected Map<Object, Object> getRowAttributes(final Object dataObject) {
		final Map<Object, Object> attributes = new HashMap<Object, Object>();
		if (dataObject instanceof IIdBeanAware) {
			attributes.put(ROW_ID, ((IIdBeanAware) dataObject).getId());
		} else if (dataObject instanceof Map<?, ?>) {
			attributes.put(ROW_ID, ((Map<?, ?>) dataObject).get("ID"));
		}
		return attributes;
	}

	@SuppressWarnings("unchecked")
	protected void putColumn(final Map<String, TablePagerColumn> columns, final int index, final String key, final TablePagerColumn column) {
		if (columns instanceof ListOrderedMap) {
			((ListOrderedMap) columns).put(index, key, column);
		} else {
			final ListOrderedMap columns2 = new ListOrderedMap();
			columns2.putAll(columns);
			columns2.put(index, key, column);
			columns.clear();
			columns.putAll(columns2);
		}
	}

	protected String wrapNum(final long num) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<span class=\"nnum\">").append(num).append("</span>");
		return sb.toString();
	}
}
