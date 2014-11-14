<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.sysmgr.dict.DictUtils"%>
<%@ page import="net.simpleframework.sysmgr.dict.SysDict"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page import="net.simpleframework.util.HTMLUtils"%>
<%
	final SysDict sysDict = DictUtils.getSysDictById(request
			.getParameter("itemId"));
%>
<div class="sys_dict_item_c">
	<div class="t"><%=sysDict.getText()%>
		(
		<%=sysDict.getName()%>
		)
	</div>
	<table style="width: 60%;" cellspacing="1" class="tbl">
		<tr>
			<td class="l">#(dict_item_c.0)</td>
			<td class="v"><%=DictUtils.getSysDictById(sysDict.getDocumentId())
					.getText()%></td>
		</tr>
		<tr>
			<td class="l">#(dict_item_c.1)</td>
			<td class="v"><%=HTMLUtils.convertHtmlLines(StringUtils.blank(sysDict
					.getDescription()))%></td>
		</tr>
	</table>
</div>
<style type="text/css">
.sys_dict_item_c .tbl {
	background: #ddd;
}

.sys_dict_item_c .tbl .l {
	background: #f7f7ff;
	width: 100px;
	text-align: right;
	padding: 4px;
}

.sys_dict_item_c .tbl .v {
	background: #fff;
	padding: 4px;
}
</style>