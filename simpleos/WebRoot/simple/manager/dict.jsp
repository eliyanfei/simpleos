<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.sysmgr.dict.DictUtils"%>
<%
	final String left = DictUtils.deployPath + "jsp/dict_type.jsp";
	final String center = DictUtils.deployPath + "jsp/dict_item.jsp";
%>
<jsp:include page="/simple/include/lc1.jsp" flush="true">
	<jsp:param value="<%=left%>" name="left" />
	<jsp:param value="<%=center%>" name="center" />
</jsp:include>