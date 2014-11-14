<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.web.page.component.ui.tree.TreeUtils"%>
<%@ page import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page import="net.simpleframework.web.page.component.AbstractComponentBean"%>
<%
	final ComponentParameter nComponentParameter = new ComponentParameter(
			request, response, null);
	nComponentParameter.componentBean = AbstractComponentBean
			.getComponentBeanByRequestId(nComponentParameter, TreeUtils.BEAN_ID);
	out.write(TreeUtils.nodeHandle(nComponentParameter));
	out.flush();
%>
