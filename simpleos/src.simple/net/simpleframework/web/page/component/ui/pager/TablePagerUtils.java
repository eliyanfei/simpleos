package net.simpleframework.web.page.component.ui.pager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.core.ado.db.Column;
import net.simpleframework.core.ado.db.EFilterRelation;
import net.simpleframework.core.bean.IIdBeanAware;
import net.simpleframework.organization.IDepartment;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.impl.Department;
import net.simpleframework.sysmgr.dict.SysDict;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.util.HTMLUtils;
import net.simpleframework.util.HTTPUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleos.SimpleosUtil;
import net.simpleos.utils.SysDictUtils;
import net.simpleos.utils.UID;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class TablePagerUtils {
	public static final String PAGER_SORT_COL = "sort_col";

	public static final String PAGER_SORT = "sort";

	public static final String PAGER_FILTER_COL = "filter_col";

	public static final String PAGER_FILTER_CUR_COL = "filter_cur_col";

	public static final String PAGER_FILTER = "filter";

	public static final String PAGER_FILTER_NONE = "none";

	public static String renderTable(final ComponentParameter compParameter) {
		final String groupColumn;
		if ((compParameter.componentBean instanceof GroupTablePagerBean)
				&& StringUtils.hasText(groupColumn = (String) compParameter.getBeanProperty("groupColumn"))) {
			return renderTable(compParameter, new RenderTable() {
				@Override
				String renderBody(final ComponentParameter compParameter, final AbstractTablePagerData tablePagerData,
						final Map<String, Object> beanAttributes) {
					final List<?> data = PagerUtils.getPagerList(compParameter.request);
					if (data == null) {
						return "";
					}
					final StringBuilder sb = new StringBuilder();
					final Map<String, List<Object>> convert = new LinkedHashMap<String, List<Object>>();
					for (final Object bean : data) {
						String key = ConvertUtils.toString(BeanUtils.getProperty(bean, groupColumn));
						if (!StringUtils.hasText(key)) {
							key = "Other";
						}
						List<Object> l = convert.get(key);
						if (l == null) {
							convert.put(key, l = new ArrayList<Object>());
						}
						l.add(bean);
					}
					final ITablePagerHandle gHandle = (ITablePagerHandle) compParameter.getComponentHandle();
					for (final Map.Entry<String, List<Object>> entry : convert.entrySet()) {
						final List<Object> l = entry.getValue();
						sb.append("<div class=\"group_t\">");
						sb.append("<table style=\"width: 100%;\" cellpadding=\"0\"><tr>");
						sb.append("<td class=\"toggle\"><img src=\"");
						sb.append(compParameter.componentBean.getCssResourceHomePath(compParameter));
						sb.append("/images/toggle.png\" /></td>");
						sb.append("<td class=\"t\">");
						final String groupValue = entry.getKey();
						final GroupWrapper gw = (gHandle instanceof IGroupTablePagerHandle) ? ((IGroupTablePagerHandle) gHandle).getGroupWrapper(
								compParameter, groupValue) : null;
						sb.append(gw != null ? gw.getLeftTitle(l) : groupValue);
						sb.append("</td>");
						String rTitle;
						if (gw != null && StringUtils.hasText(rTitle = gw.getRightTitle(l))) {
							sb.append("<td class=\"t2\">").append(rTitle).append("</td>");
						}
						sb.append("</tr></table></div>");
						sb.append("<div class=\"group_c\">");
						for (int i = 0; i < l.size(); i++) {
							sb.append(buildRow(compParameter, beanAttributes, l.get(i), tablePagerData, i, false));
						}
						sb.append("</div>");
					}
					return sb.toString();
				}
			});
		}
		return renderTable(compParameter, new RenderTable());
	}

	public static AbstractTablePagerData getTablePagerData(final ComponentParameter compParameter) {
		AbstractTablePagerData tablePagerData = (AbstractTablePagerData) compParameter.getRequestAttribute("table_pager_data");
		if (tablePagerData == null) {
			final ITablePagerHandle pHandle = (ITablePagerHandle) compParameter.getComponentHandle();
			compParameter.setRequestAttribute("table_pager_data", tablePagerData = pHandle.createTablePagerData(compParameter));
		}
		return tablePagerData;
	}

	public static String renderTable(final ComponentParameter compParameter, final RenderTable rHandle) {
		final AbstractTablePagerData tablePagerData = getTablePagerData(compParameter);
		final StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"tablepager\" style=\"overflow: auto;");
		final String height = StringUtils.text(compParameter.getRequestParameter("tbl_height"), (String) compParameter.getBeanProperty("height"));
		if (StringUtils.hasText(height)) {
			sb.append("height:").append(height).append(";");
		}
		final String width = StringUtils.text(compParameter.getRequestParameter("tbl_width"), (String) compParameter.getBeanProperty("width"));
		if (StringUtils.hasText(width)) {
			sb.append("width:").append(width).append(";");
		}
		sb.append("\">");
		final Object firstBean = compParameter.getRequestAttribute(ITablePagerHandle.FIRST_ITEM);
		final Object lastBean = compParameter.getRequestAttribute(ITablePagerHandle.LAST_ITEM);
		if (firstBean instanceof IIdBeanAware && lastBean instanceof IIdBeanAware) {
			sb.append(HTMLBuilder.inputHidden("firstRow", ((IIdBeanAware) firstBean).getId()));
			sb.append(HTMLBuilder.inputHidden("lastRow", ((IIdBeanAware) lastBean).getId()));
		}
		final Map<String, Object> beanAttributes = createBeanAttributes(compParameter);
		sb.append("<table style=\"width: 100%;\" cellpadding=\"0\" cellspacing=\"0\">");
		sb.append("<tr><td>");
		sb.append(rHandle.renderHeader(compParameter, tablePagerData, beanAttributes));
		sb.append("</td></tr>");
		sb.append("<tr><td>");
		sb.append(rHandle.renderFilter(compParameter, tablePagerData, beanAttributes));
		sb.append("</td></tr>");
		sb.append("<tr><td>");
		sb.append(rHandle.renderBody(compParameter, tablePagerData, beanAttributes));
		sb.append("</td></tr>");
		sb.append("</table></div>");
		return sb.toString();
	}

	public static class RenderTable {

		String renderHeader(final ComponentParameter compParameter, final AbstractTablePagerData tablePagerData,
				final Map<String, Object> beanAttributes) {
			final StringBuilder sb = new StringBuilder();
			final int rowMargin = ConvertUtils.toInt(compParameter.getBeanProperty("rowMargin"), 0);
			String style = "";
			if (compParameter.componentBean != null) {
				if (compParameter.componentBean instanceof PagerBean) {
					if (!SimpleosUtil.isManage(compParameter) && !((PagerBean) compParameter.componentBean).isShowHeader()) {
						style = "display:none;";
					}
				}
			}
			sb.append("<div class=\"thead\" style=\"" + style + "margin-bottom: ");
			sb.append(4 - rowMargin).append("px;\">");
			sb.append("<table style=\"width: 100%;\" class=\"fixed_table\" cellpadding=\"0\" cellspacing=\"0\"><tr>");
			if ((Boolean) beanAttributes.get("showLineNo")) {
				final int pageNumber = (Integer) beanAttributes.get("pageNumber");
				final int pageItems = (Integer) beanAttributes.get("pageItems");
				sb.append("<th class=\"rownum\" width=\"").append(getRownumWidth(compParameter, pageNumber, pageItems)).append("\">&nbsp;</th>");
			}
			final String componentName = (String) compParameter.getBeanProperty("name");
			if (ConvertUtils.toBoolean(compParameter.getBeanProperty("showCheckbox"), false)) {
				sb.append("<th class=\"cb\">");
				sb.append("<input type=\"checkbox\" ");
				sb.append("onclick=\"$Actions['").append(componentName);
				sb.append("'].checkAll(this);\" />");
				sb.append("</th>");
			}

			for (final Map.Entry<String, TablePagerColumn> entry : tablePagerData.getTablePagerColumns().entrySet()) {
				final TablePagerColumn pagerColumn = entry.getValue();
				final String key = pagerColumn.getColumnName();
				if (!pagerColumn.isVisible()) {
					continue;
				}
				sb.append("<th");
				if (pagerColumn.isSeparator()) {
					sb.append(" class=\"sep\"");
				}
				final String headerStyle = pagerColumn.getHeaderStyle();
				if (StringUtils.hasText(headerStyle)) {
					sb.append(" style=\"").append(headerStyle).append("\"");
				}
				sb.append(">");
				/*if (pagerColumn.isFilter()) {
					sb.append("<a class=\"image_filter\" style=\"float: left;\" title=\"#(TablePagerUtils.7)\"");
					sb.append(" onclick=\"$Actions['tablePagerColumnFilterWindow']('").append(PAGER_FILTER_CUR_COL).append("=");
					sb.append(key).append("&").append(TablePagerUtils.PAGER_FILTER).append("=');\"></a>");
					final Map<String, Column> columns = pHandle.getFilterColumns(compParameter);
					if (columns != null && columns.containsKey(key)) {
						sb.append("<a class=\"del\" style=\"float: left;\" title=\"#(TablePagerUtils.8)\"")
								.append(" onclick=\"$Actions['ajaxTablePagerFilterDelete']('").append(PAGER_FILTER_CUR_COL).append("=");
						sb.append(key).append("');\"></a>");
					}
				}*/
				sb.append("<div style=\"clear: left;\" class=\"lbl\">");
				final String columnText = StringUtils.blank(pagerColumn.getColumnText());
				if (pagerColumn.isSort()) {
					final String col2 = compParameter.getRequestParameter(PAGER_SORT_COL);
					StringBuilder img = null;
					if (key.equals(col2)) {
						final String sort = compParameter.getRequestParameter(PAGER_SORT);
						if ("up".equals(sort) || "down".equals(sort)) {
							img = new StringBuilder();
							img.append("<img style=\"margin-left: 4px;\" src=\"");
							img.append(compParameter.request.getContextPath()).append(PageUtils.getResourcePath());
							img.append("/images/").append(sort).append(".png\" />");
						}
					}
					sb.append("<a onclick=\"$Actions['").append(componentName);
					sb.append("'].sort('").append(key).append("');\">");
					sb.append(columnText).append("</a>");
					if (img != null) {
						sb.append(img);
					}
				} else {
					sb.append(columnText);
				}
				sb.append("</div>");
				final String title = pagerColumn.getTitle();
				if (StringUtils.hasText(title)) {
					sb.append("<div style=\"display: none;\">");
					sb.append(HTMLUtils.convertHtmlLines(title)).append("</div>");
				}
				sb.append("</th>");
			}
			sb.append("</tr></table></div>");
			return sb.toString();
		}

		String renderFilter(final ComponentParameter compParameter, final AbstractTablePagerData tablePagerData,
				final Map<String, Object> beanAttributes) {
			boolean flag = false;
			final StringBuilder sb = new StringBuilder();
			final int rowMargin = ConvertUtils.toInt(compParameter.getBeanProperty("rowMargin"), 0);
			String style = "";
			sb.append("<div class=\"tfilter\" style=\"" + style + "margin-bottom: ");
			sb.append(4 - rowMargin).append("px;\">");
			sb.append("<table style=\"width: 100%;\" class=\"fixed_table\" cellpadding=\"0\" cellspacing=\"0\"><tr>");
			if ((Boolean) beanAttributes.get("showLineNo")) {
				final int pageNumber = (Integer) beanAttributes.get("pageNumber");
				final int pageItems = (Integer) beanAttributes.get("pageItems");
				sb.append("<td class=\"rownum\" width=\"").append(getRownumWidth(compParameter, pageNumber, pageItems)).append("\">&nbsp;</td>");
			}
			if (ConvertUtils.toBoolean(compParameter.getBeanProperty("showCheckbox"), false)) {
				sb.append("<td class=\"cb\">");
				sb.append("</td>");
			}
			for (final Map.Entry<String, TablePagerColumn> entry : tablePagerData.getTablePagerColumns().entrySet()) {
				final TablePagerColumn pagerColumn = entry.getValue();
				if (!pagerColumn.isVisible()) {
					continue;
				}
				final String columnName = pagerColumn.getColumnName();
				sb.append("<td");
				if (pagerColumn.isSeparator()) {
					sb.append(" class=\"sep\"");
				}
				final String headerStyle = pagerColumn.getHeaderStyle();
				if (StringUtils.hasText(headerStyle)) {
					sb.append(" style=\"").append(headerStyle).append("\"");
				}
				sb.append("><div class=\"box\">");
				if (pagerColumn.isFilter()) {
					String val = null;
					flag = true;
					final String val2 = columnName.equals(compParameter.getParameter(PAGER_FILTER_CUR_COL)) ? compParameter
							.getParameter(PAGER_FILTER) : compParameter.getParameter(filterColumnKey(columnName));
					if (StringUtils.hasText(val2) && !PAGER_FILTER_NONE.equals(val2)) {
						val = filterDecode(StringUtils.split(val2)[1]);
					}
					final String filterHTML = toFilterHTML(compParameter, pagerColumn, val);
					if (StringUtils.hasText(filterHTML)) {
						sb.append(filterHTML);
					}
				}
				sb.append("</div></td>");
			}
			sb.append("</tr></table></div>");
			if (!flag) {
				return "";
			}
			return sb.toString();
		}

		String renderBody(final ComponentParameter compParameter, final AbstractTablePagerData tablePagerData,
				final Map<String, Object> beanAttributes) {
			final StringBuilder sb = new StringBuilder();
			sb.append("<div class=\"tbody\">");
			final ITablePagerHandle pHandle = (ITablePagerHandle) compParameter.getComponentHandle();
			final List<?> d2t = pHandle.getData2Top(compParameter);
			if (d2t != null) {
				for (int i = 0; i < d2t.size(); i++) {
					sb.append(buildRow(compParameter, beanAttributes, d2t.get(i), tablePagerData, i, true));
				}
				if (d2t.size() > 0) {
					sb.append("<div style=\"height: 9px;\"></div>");
				}
			}
			final List<?> data = PagerUtils.getPagerList(compParameter.request);
			if (data != null) {
				for (int i = 0; i < data.size(); i++) {
					sb.append(buildRow(compParameter, beanAttributes, data.get(i), tablePagerData, i, false));
				}
			}
			sb.append("</div>");
			return sb.toString();
		}

		String buildRow(final ComponentParameter compParameter, final Map<String, Object> beanAttributes, final Object bean,
				final AbstractTablePagerData tablePagerData, final int index, final boolean data2Top) {
			final StringBuilder sb = new StringBuilder();
			final Map<Object, Object> rowData = tablePagerData.getRowData(bean);
			if (rowData == null) {
				return sb.toString();
			}
			sb.append("<div class=\"titem " + (index % 2 != 0 ? "even" : "") + "");
			if (data2Top) {
				sb.append(" ttop");
			}
			final int rowMargin = (Integer) beanAttributes.get("rowMargin");
			sb.append("\" style=\"margin-top: ").append(-1 + rowMargin).append("px;\"");
			final Map<Object, Object> rowAttributes = tablePagerData.getRowAttributes(bean);
			if (rowAttributes != null) {
				for (final Map.Entry<Object, Object> entry : rowAttributes.entrySet()) {
					sb.append(" ").append(entry.getKey()).append("=");
					sb.append("\"").append(entry.getValue()).append("\"");
				}
			}
			sb.append("><table style=\"width: 100%;\" class=\"fixed_table\" cellpadding=\"0\" cellspacing=\"0\">");
			sb.append("<tr class=\"itr\">");
			if ((Boolean) beanAttributes.get("showLineNo")) {
				final int pageNumber = (Integer) beanAttributes.get("pageNumber");
				final int pageItems = (Integer) beanAttributes.get("pageItems");
				sb.append("<td width=\"").append(getRownumWidth(compParameter, pageNumber, pageItems)).append("\" class=\"rownum\">")
						.append(Math.max(pageNumber - 1, 0) * pageItems + index + 1).append("</td>");
			}
			if ((Boolean) beanAttributes.get("showCheckbox")) {
				sb.append("<td class=\"cb\"><input type=\"checkbox\" value=\"");
				if (bean instanceof IIdBeanAware) {
					sb.append(((IIdBeanAware) bean).getId());
				} else {
					sb.append(StringUtils.hash(bean));
				}
				sb.append("\" /></td>");
			}

			final Map<String, TablePagerColumn> pagerColumns = tablePagerData.getTablePagerColumns();
			for (final Map.Entry<String, TablePagerColumn> entry : pagerColumns.entrySet()) {
				final String key = entry.getKey();
				final TablePagerColumn pagerColumn = entry.getValue();
				if (!pagerColumn.isVisible()) {
					continue;
				}
				final Object value = rowData.get(key);
				if (index == 0 && !StringUtils.hasText(pagerColumn.getBeanPropertyType()) && value != null) {
					pagerColumn.setBeanPropertyType(value.getClass().getName());
				}
				sb.append("<td");
				if (pagerColumn.isSeparator()) {
					sb.append(" class=\"sep");
					if ((Boolean) beanAttributes.get("showVerticalLine")) {
						sb.append(" sep-color");
					}
					sb.append("\"");
				}
				final String style = pagerColumn.getStyle();
				if (StringUtils.hasText(style)) {
					sb.append(" style=\"").append(style).append("\"");
				}
				sb.append(">").append(StringUtils.blank(value));
				sb.append("</td>");
			}
			sb.append("</tr></table></div>");
			return sb.toString();
		}
	}

	public static String toFilterHTML(final ComponentParameter cp, final TablePagerColumn pagerColumn, final String val) {
		final ITablePagerHandle pHandle = (ITablePagerHandle) cp.getComponentHandle();
		final StringBuilder sb = new StringBuilder();
		final String componentName = (String) cp.getBeanProperty("name");
		final String columnName = pagerColumn.getColumnName();
		final Class<?> pClass = pagerColumn.propertyClass();
		final Enum<?>[] arr = pagerColumn.getEnumConstants();
		boolean bool = false;
		if (SysDict.class.isAssignableFrom(pClass)) {
			sb.append("<select onchange=\"$Actions['").append(componentName).append("']('").append(PAGER_FILTER_CUR_COL).append("=")
					.append(columnName).append("&").append(PAGER_FILTER).append("==;").append("' + $F(this));\">");
			sb.append("<option value='").append(TablePagerUtils.PAGER_FILTER_NONE).append("'>").append("所有").append("</option>");
			Collection<SysDict> al = SysDictUtils.getSysDictChilds(pagerColumn.getBeanPropertyValue());
			for (final SysDict e : al) {
				sb.append("<option value='");
				final String temp = e.getName();
				sb.append(temp).append("'");
				if (temp.equals(val)) {
					sb.append(" selected");
				}
				sb.append(">").append(e).append("</option>");
			}
			sb.append("</select>");
		} else if (Department.class.isAssignableFrom(pClass)) {
			sb.append("<select onchange=\"$Actions['").append(componentName).append("']('").append(PAGER_FILTER_CUR_COL).append("=")
					.append(columnName).append("&").append(PAGER_FILTER).append("==;").append("' + $F(this));\">");
			sb.append("<option value='").append(TablePagerUtils.PAGER_FILTER_NONE).append("'>").append("所有").append("</option>");
			IQueryEntitySet<IDepartment> qs = OrgUtils.dm().query();
			IDepartment department;
			while ((department = qs.next()) != null) {
				sb.append("<option value='");
				final String temp = department.getId().toString();
				sb.append(temp).append("'");
				if (temp.equals(val)) {
					sb.append(" selected");
				}
				sb.append(">").append(department).append("</option>");
			}
			sb.append("</select>");
		} else if ((arr != null && arr.length > 0) || (bool = Boolean.class.isAssignableFrom(pClass))) {
			sb.append("<select onchange=\"$Actions['").append(componentName).append("']('").append(PAGER_FILTER_CUR_COL).append("=")
					.append(columnName).append("&").append(PAGER_FILTER).append("==;").append("' + $F(this));\">");
			sb.append("<option value='").append(TablePagerUtils.PAGER_FILTER_NONE).append("'>").append("所有").append("</option>");
			if (bool) {
				sb.append("<option value='true'");
				if ("true".equals(val)) {
					sb.append(" selected");
				}
				sb.append(">").append(LocaleI18n.getMessage("Yes")).append("</option>");
				sb.append("<option value='false'");
				if ("false".equals(val)) {
					sb.append(" selected");
				}
				sb.append(">").append(LocaleI18n.getMessage("No")).append("</option>");
			} else {
				for (final Enum<?> e : arr) {
					sb.append("<option value='");
					final String temp = e.ordinal() + "";
					sb.append(temp).append("'");
					if (temp.equals(val)) {
						sb.append(" selected");
					}
					sb.append(">").append(e).append("</option>");
				}
			}
			sb.append("</select>");
		} else {
			String id = UID.asString();
			String opr = String.class.isAssignableFrom(pClass) ? "like" : "=";
			sb.append("<input id=\"" + id + "\" fun=\"$Actions['").append(componentName).append("']('").append(PAGER_FILTER_CUR_COL).append("=")
					.append(columnName).append("&").append(PAGER_FILTER).append("=" + opr + ";")
					.append("' + $F('" + id + "'));\" type=\"text\" params=\"").append(PAGER_FILTER_CUR_COL).append("=").append(columnName)
					.append("\"");
			if (Date.class.isAssignableFrom(pClass)) {
				sb.append(" readonly");
			}
			final String val2 = pagerColumn.getFilterVal(val);
			sb.append(" value=\"");
			if (val2 != null) {
				sb.append(val2);
			}
			sb.append("\"");
			sb.append(" />");
			final Map<String, Column> columns = pHandle.getFilterColumns(cp);
			if (columns != null && columns.containsKey(columnName)) {
				sb.append("<a class=\"del\" style=\"float: left;\" title=\"#(TablePagerUtils.8)\"")
						.append(" onclick=\"$Actions['ajaxTablePagerFilterDelete']('").append(PAGER_FILTER_CUR_COL).append("=");
				sb.append(columnName).append("');\"></a>");
			} else {
				sb.append("<a class=\"image_filter\" title=\"#(TablePagerUtils.7)\"");
				sb.append("<a class=\"image_filter\" style=\"float: left;\" title=\"#(TablePagerUtils.7)\"");
				sb.append(" onclick=\"$Actions['tablePagerColumnFilterWindow']('").append(PAGER_FILTER_CUR_COL).append("=");
				sb.append(columnName).append("&").append(TablePagerUtils.PAGER_FILTER).append("=');\"></a>");
			}
		}
		return sb.toString();
	}

	private static int getRownumWidth(final ComponentParameter compParameter, final int pageNumber, final int pageItems) {
		return String.valueOf(Math.max(pageNumber, 1) * pageItems).length() * (HTTPUtils.isWebKit(compParameter.request) ? 11 : 8);
	}

	private static Map<String, Object> createBeanAttributes(final ComponentParameter compParameter) {
		final Map<String, Object> beanAttributes = new HashMap<String, Object>();
		beanAttributes.put("rowMargin", ConvertUtils.toInt(compParameter.getBeanProperty("rowMargin"), 0));
		beanAttributes.put("showCheckbox", ConvertUtils.toBoolean(compParameter.getBeanProperty("showCheckbox"), false));
		beanAttributes.put("showVerticalLine", ConvertUtils.toBoolean(compParameter.getBeanProperty("showVerticalLine"), false));
		final boolean showLineNo = ConvertUtils.toBoolean(compParameter.getBeanProperty("showLineNo"), false);
		if (showLineNo) {
			beanAttributes.put("pageNumber", PagerUtils.getPageNumber(compParameter));
			beanAttributes.put("pageItems", PagerUtils.getPageItems(compParameter));
		}
		beanAttributes.put("showLineNo", showLineNo);
		return beanAttributes;
	}

	static String filterColumnKey(final String col) {
		return PAGER_FILTER + "_" + col;
	}

	public static TablePagerColumn getSelectedColumn(final ComponentParameter nComponentParameter) {
		return TablePagerUtils.getTablePagerData(nComponentParameter).getTablePagerColumns()
				.get(nComponentParameter.getRequestParameter(PAGER_FILTER_CUR_COL));
	}

	/***************************** utils for jsp ****************************/

	public static String renderJavascriptEvent(final ComponentParameter nComponentParameter) {
		final StringBuilder sb = new StringBuilder();
		final String jsRowClick = (String) nComponentParameter.getBeanProperty("jsRowClick");
		final String jsRowDblclick = (String) nComponentParameter.getBeanProperty("jsRowDblclick");
		final boolean b1 = StringUtils.hasText(jsRowClick);
		final boolean b2 = StringUtils.hasText(jsRowDblclick);
		if (b1 || b2) {
			sb.append("action.pager.select(\".titem\").invoke(\"observe\", \"");
			sb.append(b1 ? "click" : "dblclick").append("\", function(evn) {");
			sb.append("(function click(item) {");
			sb.append(b1 ? jsRowClick : jsRowDblclick).append("})(this);");
			sb.append("});");
		}
		if (nComponentParameter.componentBean instanceof GroupTablePagerBean) {
			sb.append("action.pager.select(\".toggle img\").each(function(img) {");
			sb.append("var toggle = img.up(\".group_t\").next();");
			sb.append("$Comp.imageToggle(img, toggle);");
			sb.append("});");
		}
		return sb.toString();
	}

	public static String getHomePath() {
		return AbstractComponentRegistry.getRegistry(TablePagerRegistry.tablePager).getResourceHomePath();
	}

	public static String filterOp(final TablePagerColumn col) {
		final StringBuilder sb = new StringBuilder();
		final Class<?> clazz = col.beanPropertyType();
		if (String.class.isAssignableFrom(clazz)) {
			sb.append("<option value=\"").append(EFilterRelation.like).append("\">#(TablePagerUtils.6)</option>");
		}
		sb.append("<option value=\"").append(EFilterRelation.equal).append("\">#(TablePagerUtils.0)</option>");
		sb.append("<option value=\"").append(EFilterRelation.not_equal).append("\">#(TablePagerUtils.1)</option>");
		if (Number.class.isAssignableFrom(clazz) || Date.class.isAssignableFrom(clazz)) {
			sb.append("<option value=\"").append(EFilterRelation.gt).append("\">#(TablePagerUtils.2)</option>");
			sb.append("<option value=\"").append(EFilterRelation.gt_equal).append("\">#(TablePagerUtils.3)</option>");
			sb.append("<option value=\"").append(EFilterRelation.lt).append("\">#(TablePagerUtils.4)</option>");
			sb.append("<option value=\"").append(EFilterRelation.lt_equal).append("\">#(TablePagerUtils.5)</option>");
		}
		return sb.toString();
	}

	static int fixWidth(final PageRequestResponse rRequest, final int width) {
		// 当table-layout=fixed时，webkit的解释尽然去掉了padding，怪异
		// return HttpUtils.isWebKit(rRequest.request) ? width + 8 : width;
		return width;
	}

	static String filterEncode(final String filter) {
		if (filter == null) {
			return "";
		}
		return "#" + filter;
		//		return "#" + StringUtils.encodeHex(filter.getBytes());
	}

	static String filterDecode(final String filter) {
		if (filter == null) {
			return "";
		}
		return filter.startsWith("#") ? filter.substring(1) : filter;
		//		return filter.startsWith("#") ? StringUtils.decodeHexString(filter.substring(1)) : filter;
	}
}
