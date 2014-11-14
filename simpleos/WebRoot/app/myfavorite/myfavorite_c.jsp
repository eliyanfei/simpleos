<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<style>
.pager .pager_top_block .pager_title {
	height: 28px;
	line-height: 28px;
	float: left;
	width: 100%;
}
</style>
<div style="border: 1px solid #aaa;">
	<table
		style="min-height: 550px; border-spacing: 0; table-layout: fixed; width: 100%;">
		<tr>
			<td width="200" valign="top" style="background: #eef;">
				<jsp:include page="myfavorite_left.jsp" flush="true"></jsp:include>
			</td>
			<td valign="top" style="background-color: #fff;">
				<div id="myfavorite_data_id"></div>
			</td>
		</tr>
	</table>
</div>