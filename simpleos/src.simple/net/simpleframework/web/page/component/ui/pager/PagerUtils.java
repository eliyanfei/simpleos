package net.simpleframework.web.page.component.ui.pager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.IPageConstants;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class PagerUtils {
	public static final String BEAN_ID_NAME = "__pager_beanId_name";

	public static final String BEAN_ID = "pager_@bid";

	private static String beanId(final HttpServletRequest request) {
		final String beanIdName = request.getParameter(BEAN_ID_NAME);
		return StringUtils.hasText(beanIdName) ? beanIdName : BEAN_ID;
	}

	public static ComponentParameter getComponentParameter(
			final PageRequestResponse requestResponse) {
		return ComponentParameter.get(requestResponse,
				beanId(requestResponse.request));
	}

	public static ComponentParameter getComponentParameter(
			final HttpServletRequest request, final HttpServletResponse response) {
		return ComponentParameter.get(request, response, beanId(request));
	}

	public static List<?> getPagerList(final HttpServletRequest request) {
		final List<?> l = (List<?>) request
				.getAttribute(IPagerHandle.PAGER_LIST);
		return l != null ? l : new ArrayList<Object>();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void setPageAttributes(
			final ComponentParameter nComponentParameter, final String key,
			final Object value) {
		final String beanId = nComponentParameter.componentBean.hashId();
		Map<String, Object> attributes = (Map) nComponentParameter
				.getSessionAttribute(beanId);
		if (attributes == null) {
			nComponentParameter.setSessionAttribute(beanId,
					attributes = new HashMap<String, Object>());
		}
		attributes.put(key, value);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static int getPageItems(final ComponentParameter nComponentParameter) {
		final String pageItemsParameterName = (String) nComponentParameter
				.getBeanProperty("pageItemsParameterName");
		final String pageItems = nComponentParameter
				.getRequestParameter(pageItemsParameterName);
		final int items;
		if (StringUtils.hasText(pageItems)) {
			items = ConvertUtils.toInt(pageItems, 0);
		} else {
			final String beanId = nComponentParameter.componentBean.hashId();
			final Map<String, Object> attri = (Map) nComponentParameter
					.getSessionAttribute(beanId);
			items = (attri == null ? 0 : ConvertUtils.toInt(
					attri.get(pageItemsParameterName), 0));
		}
		return items == 0 ? (Integer) nComponentParameter
				.getBeanProperty("pageItems") : items;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static int getPageNumber(final ComponentParameter nComponentParameter) {
		final String pageNumberParameterName = (String) nComponentParameter
				.getBeanProperty("pageNumberParameterName");
		final String pageNumber = nComponentParameter
				.getRequestParameter(pageNumberParameterName);
		if (StringUtils.hasText(pageNumber)) {
			return ConvertUtils.toInt(pageNumber, 0);
		} else {
			final Map<String, Object> attri = (Map) nComponentParameter
					.getSessionAttribute(nComponentParameter.componentBean
							.hashId());
			return attri != null ? ConvertUtils.toInt(
					attri.get(pageNumberParameterName), 0) : 0;
		}
	}

	public static void resetPageNumber(
			final ComponentParameter nComponentParameter) {
		final String pageNumberParameterName = (String) nComponentParameter
				.getBeanProperty("pageNumberParameterName");
		setPageAttributes(nComponentParameter, pageNumberParameterName, 0);
	}

	public static String getHomePath() {
		return AbstractComponentRegistry.getRegistry(PagerRegistry.pager)
				.getResourceHomePath();
	}

	public static String getCssPath(final PageRequestResponse requestResponse) {
		return AbstractComponentRegistry.getRegistry(PagerRegistry.pager)
				.getCssResourceHomePath(requestResponse);
	}

	/***************************** utils for jsp ****************************/

	public static String nbar(final ComponentParameter compParameter,
			final EPagerPosition pagerPosition, final int pageItems,
			final Map<String, Integer> pageVar) {
		final StringBuilder sb = new StringBuilder();
		final int pageCount = ConvertUtils.toInt(pageVar.get("pageCount"), 0);
		final String href = ((IPagerHandle) compParameter.getComponentHandle())
				.getPagerUrl(compParameter, pagerPosition, pageItems, pageVar);
		if (StringUtils.hasText(href)) {
			final StringBuffer temp = new StringBuffer();
			temp.append("<a")
					.append(href.toLowerCase().startsWith("javascript:") ? " onclick"
							: " href").append("=\"").append(href).append("\"");
			if (pagerPosition == EPagerPosition.left2) {
				sb.append(temp)
						.append(" title=\"#(pager_head.3)\" class=\"pn\">#(pager_head.3)</a>");
			} else if (pagerPosition == EPagerPosition.left) {
				sb.append(temp)
						.append(" title=\"#(pager_head.4)\" class=\"pn\">#(pager_head.4)</a>");
			} else if (pagerPosition == EPagerPosition.number) {
				sb.append(temp);
				final int pageNumber = ConvertUtils.toInt(
						pageVar.get("pageNumber"), 0);
				final int currentPageNumber = ConvertUtils.toInt(
						pageVar.get("currentPageNumber"), 0);
				if (pageNumber != currentPageNumber) {
					sb.append(" class=\"num\">");
				} else {
					sb.append(" class=\"current num\">");
				}
				sb.append(pageNumber).append("</a>");
			} else if (pagerPosition == EPagerPosition.right) {
				int currentPageNumber = ConvertUtils.toInt(
						pageVar.get("currentPageNumber"), 0) + 3;
				final List<Integer> pages = new ArrayList<Integer>();
				final Random random = new Random();
				for (int i = 0; i < 3; i++) {
					try {
						int page = random.nextInt(currentPageNumber + 10)
								+ currentPageNumber;
						if (page > currentPageNumber && !pages.contains(page)
								&& page > 9 && page < pageCount) {
							pages.add(page);
						}
					} catch (Exception e) {
					}
				}
				Collections.sort(pages);
				for (int page : pages) {
					pageVar.put("pageNumber", page);
					final StringBuffer temp1 = new StringBuffer();
					temp1.append("<a")
							.append(href.toLowerCase()
									.startsWith("javascript:") ? " onclick"
									: " href").append("=\"");
					final String href1 = ((IPagerHandle) compParameter
							.getComponentHandle()).getPagerUrl(compParameter,
							EPagerPosition.number, pageItems, pageVar);
					temp1.append(href1).append("\"");
					sb.append(temp1);
					sb.append(" class=\"pnum\">");
					sb.append(page).append("</a>");
				}
				sb.append(temp);
				sb.append(" title=\"#(pager_head.5)\" class=\"pn\">#(pager_head.5)</a>");
			} else if (pagerPosition == EPagerPosition.right2) {
				// sb.append(temp);
				// sb.append(" title=\"#(pager_head.6)\" class=\"pn\">#(pager_head.6)</a>");
			}
		}
		return sb.toString();
	}

	public static String getPagerActions(
			final ComponentParameter compParameter, final int count,
			final int pageItems, final Map<String, Integer> pageVar) {
		final int itemCount = ConvertUtils.toInt(pageVar.get("itemCount"), 0);
		final int pageCount = ConvertUtils.toInt(pageVar.get("pageCount"), 0);
		final int lastItem = ConvertUtils.toInt(pageVar.get("lastItem"), 0);
		final int currentPageNumber = ConvertUtils.toInt(
				pageVar.get("currentPageNumber"), 0);

		final StringBuilder sb = new StringBuilder();
		final boolean showEditPageItems = (Boolean) compParameter
				.getBeanProperty("showEditPageItems");
		final boolean showEditPageNumber = false;// (Boolean)
													// compParameter.getBeanProperty("showEditPageNumber");
		// 不支持跳转页面
		if (showEditPageNumber && lastItem < itemCount) {
			sb.append("<span>跳到");
			sb.append("</span>");
			sb.append("<input type=\"text\" style=\"width: 25px;text-align:center;\" title=\"#(pager_head.0)\" ");
			sb.append("value=\"").append(currentPageNumber)
					.append("\" onkeydown=\"");
			sb.append("if ((event.which ? event.which : event.keyCode) == Event.KEY_RETURN)");
			sb.append("{ ");
			final String href = ((IPagerHandle) compParameter
					.getComponentHandle()).getPagerUrl(compParameter,
					EPagerPosition.pageNumber, pageItems, pageVar);
			if (href.toLowerCase().startsWith("javascript:")) {
				sb.append(href.substring(11));
			}
			sb.append(" }\" />");
			sb.append("<span>");
			sb.append("页</span>");
		}
		if (count > pageItems) {
			sb.append("<span>")
					.append(LocaleI18n.getMessage("pager_head.1", itemCount,
							pageCount));
			sb.append("</span>");
			if (showEditPageItems) {
				sb.append("<span style=\"margin: 0px 2px;\">/</span>");
			}
		} else {
			sb.append("<span>");
			sb.append(LocaleI18n.getMessage("pager_head.1", count,
					LocaleI18n.getMessage("pager_head.2")));
			sb.append("</span>");
			if (showEditPageItems) {
				sb.append("<span style=\"margin: 0px 2px;\">/</span>");
			}
		}
		if (showEditPageItems) {
			sb.append("<select onchange=\"");
			final String href = ((IPagerHandle) compParameter
					.getComponentHandle()).getPagerUrl(compParameter,
					EPagerPosition.pageItems, pageItems, pageVar);
			if (href.toLowerCase().startsWith("javascript:")) {
				sb.append(href.substring(11));
			} else {
				final String pageItemsParameterName = (String) compParameter
						.getBeanProperty("pageItemsParameterName");
				sb.append("$Actions.loc('")
						.append(PageUtils.addParameters(href,
								pageItemsParameterName + "="))
						.append("' + $F(this));");
			}
			sb.append("\" title=\"更改当前页显示数目\" style=\"vertical-align: middle;\">");
			final int[] pages = { 15, 30, 50, 100 };
			for (int p : pages) {
				sb.append("<option value=\"" + p + "\" "
						+ (p == pageItems ? "selected=\"selected\"" : "") + ">"
						+ p + "</option>");
			}
			sb.append("</select>");
		}
		String exportAction = (String) compParameter
				.getBeanProperty("exportAction");
		if (StringUtils.hasText(exportAction)
				&& (!"true".equals(exportAction) && !"false"
						.equals(exportAction))) {
			if (!exportAction.startsWith("$Actions")) {
				exportAction = "$Actions[" + exportAction + "]();";
			}
			sb.append("<span class=\"csv_icon\" onclick=\"")
					.append(exportAction).append("\"></span>");
		}
		return sb.toString();
	}

	static String getXmlPathParameter(final ComponentParameter compParameter) {
		final String dataPath = (String) compParameter
				.getBeanProperty("dataPath");
		if (StringUtils.hasText(dataPath)) {
			final String xmlPath = PageUtils.doPageUrl(compParameter,
					StringUtils.stripFilenameExtension(dataPath) + ".xml");
			if (new File(compParameter.getServletContext().getRealPath(xmlPath))
					.exists()) {
				return IPageConstants.XMLPATH_PARAMETER + "=" + xmlPath;
			}
		}
		return null;
	}
}
