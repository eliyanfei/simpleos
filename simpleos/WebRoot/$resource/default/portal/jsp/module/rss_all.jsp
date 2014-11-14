<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.portal.PortalUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.portal.PageletBean"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.portal.module.PortalModuleRegistryFactory"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.portal.module.RssModuleHandle"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.portal.module.RssUtils.RssChannelItem"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.portal.module.RssUtils.RssChannel"%>
<%@ page import="java.util.Date"%>
<%@ page import="net.simpleframework.util.ConvertUtils"%>
<%@ page import="net.simpleframework.util.JavascriptUtils"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%
	final PageletBean pagelet = PortalUtils
			.getPageletBean(new PageRequestResponse(request, response));
	final RssModuleHandle rssModule = (RssModuleHandle) PortalModuleRegistryFactory
			.getInstance().getModuleHandle(pagelet);
	final RssChannel channel = rssModule.getRssChannel();
	if (channel != null) {
		final StringBuilder sb = new StringBuilder();
		for (RssChannelItem item : channel.getChannelItems()) {
			sb.append("<div class='portal_rss'>");
			sb.append("<a target='_blank' href='")
					.append(item.getLink()).append("'>");
			sb.append(item.getTitle()).append("</a>");
			Date pubDate = item.getPubDate();
			if (pubDate != null) {
				sb.append("<span class='pubDate'> - ")
						.append(ConvertUtils.toDateString(item
								.getPubDate())).append("</span>");
			}
			sb.append("<div class='desc'>").append(
					item.getDescription());
			sb.append("</div>");
			sb.append("<div class='line'></div>");
			sb.append("</div>");
			sb.append("<div class='portal_rss_line'></div>");
		}
		sb.append(JavascriptUtils
				.wrapScriptTag("$Actions['rssContentWindow'].window.setHeader('RSS - "
						+ channel.getTitle() + "');"));
		out.write(sb.toString());
	}
%>

