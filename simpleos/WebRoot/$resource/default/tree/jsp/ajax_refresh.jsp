<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.web.page.component.AbstractComponentBean"%>
<%@ page import="net.simpleframework.web.page.component.ui.tree.TreeRender"%>
<%@ page import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page import="net.simpleframework.web.page.component.ui.tree.TreeUtils"%>
<%
	final ComponentParameter nComponentParameter = new ComponentParameter(
			request, response, null);
	nComponentParameter.componentBean = AbstractComponentBean
			.getComponentBeanByRequestId(nComponentParameter, TreeUtils.BEAN_ID);
	final TreeRender render = (TreeRender) nComponentParameter.componentBean
			.getComponentRegistry().getComponentRender();
	out.write(render.jsonData(nComponentParameter));
	out.flush();
%>