<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>

<div style="float: left"><input type="button" value="#(Button.Save2)" id="jcSaveAndNew"
	onclick="$Actions['ajaxJobChartSave']('next=true');" /></div>
<div style="float: right"><input type="button" class="button2" value="#(Button.Save)" id="jcSave"
	onclick="$Actions['ajaxJobChartSave']();" /> <input type="button" value="#(Button.Cancel)"
	onclick="$Actions['jcEditWindow'].close();" /></div>
<%
	final String departmentIdParameterName = OrgUtils.dm()
			.getDepartmentIdParameterName();
	final String jobChartIdParameterName = OrgUtils.jcm()
			.getJobChartIdParameterName();
%>
<form id="jcFormEditor" style="clear: both; padding-top: 6px;"><input type="hidden"
	id="<%=departmentIdParameterName%>" name="<%=departmentIdParameterName%>"
	value="<%=StringUtils.blank(request
					.getParameter(departmentIdParameterName))%>" /> <input
	type="hidden" id="<%=jobChartIdParameterName%>" name="<%=jobChartIdParameterName%>"
	value="<%=StringUtils.blank(request
					.getParameter(jobChartIdParameterName))%>" /></form>
