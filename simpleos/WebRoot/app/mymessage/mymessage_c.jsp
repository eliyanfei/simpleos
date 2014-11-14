<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<style>
.pager .pager_top_block .pager_title {
	height: 28px;
	line-height: 28px;
	float: left;
	width: 100%;
}

.mymessage_read a,.mymessage_set a {
	margin-right: 6px;
	text-decoration: none;
}

.mymessage_read .selected {
	border-bottom: 2px solid #d00;
}
</style>
<div style="border: 1px solid #aaa;">
	<table
		style="min-height: 550px; border-spacing: 0; table-layout: fixed; width: 100%;">
		<tr>
			<td width="200" valign="top" style="background: #eef;min-height: 400px;">
				<jsp:include page="mymessage_left.jsp" flush="true"></jsp:include>
			</td>
			<td valign="top" style="background-color: #fff;min-height: 400px;">
				<input type="hidden" id="messageType" value="">
				<div id="mymessage_data_id"></div>
			</td>
		</tr>
	</table>
</div>
<script type="text/javascript">
function del_mymessage() {
	var id = "";
	$$('#mymessage_data_id .titem input[type=checkbox]').each(function(c) {
		if (c.checked) {
			var idc = $Actions['myMessageTable'].rowId(c);
			id += idc + ",";
		}
	});
	if (id == "") {
		$IT.alert("\u81f3\u5c11\u9009\u62e9\u4e00\u4e2a\u6761\u76ee");
		return;
	} else {
		$Actions['myMessageDeleteAct']('ids=' + id);
	}
}
function messageread_mymessage(messageread) {
	var id = "";
	$$('#mymessage_data_id .titem input[type=checkbox]').each(function(c) {
		if (c.checked) {
			var idc = $Actions['myMessageTable'].rowId(c);
			id += idc + ",";
		}
	});
	if (id == "") {
		$IT.alert("\u81f3\u5c11\u9009\u62e9\u4e00\u4e2a\u6761\u76ee");
		return;
	} else {
		$Actions['messagereadAct']('ids=' + id+'&messageread='+messageread);
	}
}
</script>