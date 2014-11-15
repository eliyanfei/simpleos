package net.simpleframework.web.page.component.ui.portal;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.a.ItSiteUtil;
import net.simpleframework.core.ALoggerAware;
import net.simpleframework.core.Logger;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.util.HTMLUtils;
import net.simpleframework.util.HTTPUtils;
import net.simpleframework.util.IConstants;
import net.simpleframework.util.JavascriptUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.ReflectUtils;
import net.simpleframework.web.page.TextForward;
import net.simpleframework.web.page.UrlForward;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ComponentRenderUtils;
import net.simpleframework.web.page.component.ui.portal.module.IPortalModuleHandle;
import net.simpleframework.web.page.component.ui.portal.module.OptionWindowUI;
import net.simpleframework.web.page.component.ui.portal.module.PortalModule;
import net.simpleframework.web.page.component.ui.portal.module.PortalModuleRegistryFactory;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class PortalUtils {
	static Logger logger = ALoggerAware.getLogger(PortalUtils.class);

	static Collection<ColumnBean> getColumns(final ComponentParameter compParameter) {
		Collection<ColumnBean> columns = null;
		final IPortalHandle lh = (IPortalHandle) compParameter.getComponentHandle();
		if (lh != null) {
			columns = lh.getPortalColumns(compParameter);
		}
		if (columns == null) {
			columns = ((PortalBean) compParameter.componentBean).getColumns();
		}
		return columns == null ? new ArrayList<ColumnBean>() : columns;
	}

	static boolean isManager(final ComponentParameter compParameter) {
		try {
			return ((Boolean) ReflectUtils.methodIsMember.invoke(null, compParameter.getBeanProperty("jobManager"), compParameter.getSession(), null) || (Boolean) compParameter
					.getBeanProperty("showMenu")) && !ConvertUtils.toBoolean(compParameter.getSessionAttribute("_show_tabs"), false);
		} catch (final Exception e) {
			e.printStackTrace();
			logger.warn(e);
			return false;
		}
	}

	public static String renderHtml(final ComponentParameter compParameter) {
		final Collection<ColumnBean> columns = getColumns(compParameter);
		final StringBuilder sb = new StringBuilder();
		String columnIds = "";
		for (final ColumnBean column : columns) {
			columnIds += column.id() + " ";
		}
		final PortalBean portalBean = (PortalBean) compParameter.componentBean;
		sb.append("<div id=\"layout_").append(portalBean.hashId());
		sb.append("\" class=\"portal\"");
		final boolean isManager = isManager(compParameter);
		if (isManager) {
			sb.append(" isDraggable=\"");
			sb.append(compParameter.getBeanProperty("draggable"));
			sb.append("\"");
		}
		sb.append(" isManager=\"").append(isManager);
		sb.append("\" columnIds=\"").append(columnIds).append("\">");
		sb.append(ComponentRenderUtils.genParameters(compParameter));

		for (final ColumnBean c : columns) {
			sb.append("<div class=\"column\" style=\"width:").append(c.getWidth()).append("\">");
			sb.append("<ul id=\"").append(c.id()).append("\" class=\"sortable\">");
			final ArrayList<PageletBean> pagelets = c.getPagelets();
			if (pagelets.size() == 0) {
				final String module = (String) compParameter.getBeanProperty("autoPagelet");
				if (StringUtils.hasText(module) && !module.equals("false")) {
					final PageletBean pagelet = new PageletBean(c);
					pagelet.setModule(module);
					pagelets.add(pagelet);
				}
			}
			for (final PageletBean pagelet : pagelets) {
				sb.append(createPagelet(compParameter, pagelet, isManager));
			}
			sb.append("</ul></div>");
		}
		sb.append("</div>");
		return sb.toString();
	}

	static String createPagelet(final ComponentParameter compParameter, final PageletBean pagelet) {
		return createPagelet(compParameter, pagelet, isManager(compParameter));
	}

	static String createPagelet(final ComponentParameter compParameter, final PageletBean pagelet, final boolean isManager) {
		final StringBuilder sb = new StringBuilder();
		final IPortalModuleHandle lmHandle = pagelet.getModuleHandle();
		if (lmHandle == null) {
			return sb.toString();
		}
		sb.append("<div class=\"move\">");
		sb.append("<table style=\"width: 100%;\" cellpadding=\"0\" cellspacing=\"0\">");
		sb.append("<tr>");
		sb.append("<td class=\"titlebar\">").append(getTitleString(compParameter, pagelet)).append("</td>");
		sb.append("<td class=\"act\">");
		if (isManager) {
			sb.append("<div class=\"tb\" style=\"display:none;\">");
			sb.append("<div class=\"action_delete\"></div>");
			sb.append("<div class=\"action_refresh\"></div>");
			sb.append("<div class=\"action_menu\"></div>");
			sb.append("</div>");
		} else {
			sb.append("<div class=\"tb\" style=\"display:none;\">");
			sb.append("<div class=\"action_refresh\" style=\"display:none;\"></div>");
			sb.append("</div>");
		}
		sb.append("</td></tr>");
		sb.append("</table>");
		sb.append("</div>");
		final int h = pagelet.getHeight();
		sb.append("<div class=\"content\" style=\"height:");
		sb.append(h > 0 ? h + "px" : "auto").append(";text-align:").append(pagelet.getAlign()).append("\">");

		final String pageletId = pagelet.id();
		if (pagelet.isSync()) {
			final IForward forward = lmHandle.getPageletContent(compParameter);
			if (forward instanceof UrlForward) {
				final UrlForward uForward = (UrlForward) forward;
				uForward.setUrl(PageUtils.addParameters(uForward.getUrl(), PortalUtils.PAGELET_ID + "=" + pageletId));
			}
			if (forward != null) {
				String responseText = forward.getResponseText(compParameter);
				if (TextForward.class.equals(forward.getClass())) {
					responseText = responseText.substring(7);
				}
				sb.append(responseText);
			}
		} else {
			sb.append(LocaleI18n.getMessage("LayoutUtils.renderHtml.0"));
		}
		sb.append("</div>");

		final ArrayList<String> al = new ArrayList<String>();
		al.add("class=pagelet");
		al.add("id=" + pageletId);
		al.add("sync=" + pagelet.isSync());
		al.add("showMenu=" + isManager);
		final IPortalModuleHandle mh = PortalModuleRegistryFactory.getInstance().getModuleHandle(pagelet);
		if (mh != null) {
			if (mh.getPageletOptionContent(compParameter) != null) {
				al.add("showOption=true");
			}
			OptionWindowUI optionWindowUI;
			if ((optionWindowUI = mh.getPageletOptionUI(compParameter)) != null) {
				final String title = optionWindowUI.getTitle();
				if (StringUtils.hasText(title)) {
					al.add("window_title=" + title);
				}
				final int height = optionWindowUI.getHeight();
				if (height > 0) {
					al.add("window_height=" + height);
				}
				final int width = optionWindowUI.getWidth();
				if (width > 0) {
					al.add("window_width=" + width);
				}
			}
		}
		return HTMLBuilder.tag("li", wrapRound(compParameter, sb.toString()), al.toArray(new String[al.size()]));
	}

	public static String getTitleString(final ComponentParameter compParameter, final PageletBean pagelet) {
		String titleValue = null;
		final PageletTitle pageletTitle = pagelet.getTitle();
		final String value = pageletTitle != null ? pageletTitle.getValue() : null;
		if (StringUtils.hasText(value)) {
			titleValue = value;
		} else {
			final PortalModule moduleBean = pagelet.getModuleBean();
			if (moduleBean != null) {
				final String text = moduleBean.getText();
				if (StringUtils.hasText(text)) {
					titleValue = text;
				}
			}
		}
		titleValue = StringUtils.text(titleValue, IConstants.HTML_BLANK_STRING);
		final StringBuilder sb = new StringBuilder();
		if (pageletTitle != null) {
			final String id = pagelet.id();
			sb.append("<table style=\"width: 100%;\" id=\"title_").append(id);
			sb.append("\" cellpadding=\"0\" cellspacing=\"0\"><tr>");
			final String icon = pageletTitle.getIcon();
			if (StringUtils.hasText(icon)) {
				sb.append("<td class=\"icon\">");
				sb.append("<img src=\"").append(compParameter.request.getContextPath());
				sb.append(pagelet.getColumnBean().getPortalBean().getResourceHomePath());
				sb.append("/jsp/icons/").append(icon).append("\"/>");
				sb.append("</td>");
			}
			sb.append("<td class=\"title\">");
			final String link = pageletTitle.getLink();
			if (StringUtils.hasText(link)) {
				sb.append("<a href=\"").append(link).append("\" target=\"_blank\" style=\"");
				sb.append(pageletTitle.getFontStyle()).append("\">").append(titleValue).append("</a>");
			} else {
				sb.append("<span");
				final String fontStyle = pageletTitle.getFontStyle();
				if (StringUtils.hasText(fontStyle)) {
					sb.append(" style=\"").append(fontStyle).append("\"");
				}
				sb.append(">").append(titleValue).append("</span>");
			}
			sb.append("</td>");
			sb.append("</tr></table>");
			final StringBuilder js = new StringBuilder();
			final String desc = pageletTitle.getDescription();
			if (StringUtils.hasText(desc)) {
				js.append("new Tip($(\"title_").append(id).append("\"), \"");
				js.append(JavascriptUtils.escape(HTMLUtils.convertHtmlLines(desc)));
				js.append("\", { delay: 1 });");
			}
			if (js.length() > 0) {
				sb.append(JavascriptUtils.wrapScriptTag(js.toString()));
			}
		} else {
			sb.append(titleValue);
		}
		return sb.toString();
	}

	private static String wrapRound(final ComponentParameter compParameter, final String s) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"jbox\">");
		final float ieVersion = HTTPUtils.getIEVersion(compParameter.request);
		if (ieVersion > 6f) {
			sb.append("<div class=\"j1\"></div><div class=\"j2\"></div>");
		}
		sb.append(s);
		if (ieVersion > 6f) {
			sb.append("<div class=\"j3\"></div><div class=\"j4\"></div>");
		}
		sb.append("</div>");
		return sb.toString();
	}

	public static void savePortal(final ComponentParameter compParameter) {
		savePortal(compParameter, null);
	}

	public static void savePortal(final ComponentParameter compParameter, final Collection<ColumnBean> columns) {
		savePortal(compParameter, columns, (Boolean) compParameter.getBeanProperty("draggable"));
	}

	public static void savePortal(final ComponentParameter compParameter, Collection<ColumnBean> columns, final boolean draggable) {
		final IPortalHandle lh = (IPortalHandle) compParameter.getComponentHandle();
		if (lh == null) {
			return;
		}
		if (columns == null) {
			columns = getColumns(compParameter);
		}
		lh.updatePortal(compParameter, columns, draggable);
	}

	public static ColumnBean getColumnBeanByHashId(final ComponentParameter compParameter, final String hashId) {
		if (hashId != null) {
			for (final ColumnBean column : getColumns(compParameter)) {
				if (hashId.equals(column.id())) {
					return column;
				}
			}
		}
		return null;
	}

	public static PageletBean getPageletByHashId(final ComponentParameter compParameter, final String hashId) {
		if (hashId != null) {
			for (final ColumnBean column : getColumns(compParameter)) {
				for (final PageletBean pagelet : column.getPagelets()) {
					if (hashId.equals(pagelet.id())) {
						return pagelet;
					}
				}
			}
		}
		return null;
	}

	public static final String PAGELET_ID = "pageletId";

	public static PageletBean getPageletBean(final PageRequestResponse requestResponse) {
		final ComponentParameter nComponentParameter = getComponentParameter(requestResponse);
		return getPageletByHashId(nComponentParameter, nComponentParameter.getRequestParameter(PAGELET_ID));
	}

	public static final String BEAN_ID = "portal_@bid";

	public static ComponentParameter getComponentParameter(final PageRequestResponse requestResponse) {
		return ComponentParameter.get(requestResponse, BEAN_ID);
	}

	public static ComponentParameter getComponentParameter(final HttpServletRequest request, final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}

	public static String getHomePath() {
		return AbstractComponentRegistry.getRegistry(PortalRegistry.portal).getResourceHomePath();
	}

	public static String getCssPath(final PageRequestResponse requestResponse) {
		return AbstractComponentRegistry.getRegistry(PortalRegistry.portal).getCssResourceHomePath(requestResponse);
	}
}
