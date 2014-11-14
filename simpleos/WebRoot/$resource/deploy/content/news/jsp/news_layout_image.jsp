<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.content.news.NewsUtils"%>
<%@ page
  import="net.simpleframework.web.page.component.AbstractComponentBean"%>
<%@ page import="net.simpleframework.web.page.PageRequestResponse"%>
<%
	final AbstractComponentBean newsPager = NewsUtils.applicationModule
			.getComponentBean(new PageRequestResponse(request, response));
	if (newsPager == null) {
%>
<div style="text-align: center; padding: 8px;">#(news_layout.0)</div>
<%
	return;
	}
%>
<div id="__news_imageSlide"
  style="border: 1px solid #aaa; padding: 2px;"></div>