<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	
%>
<div class="block_layout1">
	<div class="t1 f4">
		<span class="ts">#(Docu.hotdocu)</span>
	</div>
	<div class="c">
		<jsp:include page="docu_list_show.jsp" flush="true">
			<jsp:param value="view" name="type" />
			<jsp:param value="dot1" name="dot" />
		</jsp:include>
	</div>
</div>
