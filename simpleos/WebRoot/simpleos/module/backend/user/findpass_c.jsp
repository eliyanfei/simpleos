<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>

<div align="center" style="margin-top: 50px;">
	<form id="_userpwd_form">
		<input type="hidden" id="sid" name="sid"
			value="<%=request.getParameter("sid")%>" />
		<div id="_userpwd"></div>
	</form>
</div>


