<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.my.file.MyFileUtils"%>
<%
	final String left = MyFileUtils.deployPath + "jsp/folder_c.jsp";
	final String center = MyFileUtils.deployPath + "jsp/file_c.jsp";
%>
<jsp:include page="/simpleos/include/lc1.jsp" flush="true">
	<jsp:param value="<%=left%>" name="left" />
	<jsp:param value="<%=center%>" name="center" />
</jsp:include>