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
	<input type="hidden" id="messageType" value="">
	<div id="message_data_id"></div>
</div>
<script type="text/javascript">
function del_mymessage() {
	var id = "";
	$$('#message_data_id .titem input[type=checkbox]').each(function(c) {
		if (c.checked) {
			var idc = $Actions['messageTable'].rowId(c);
			id += idc + ",";
		}
	});
	if (id == "") {
		$IT.alert("\u81f3\u5c11\u9009\u62e9\u4e00\u4e2a\u6761\u76ee");
		return;
	} else {
		$Actions['messageDeleteAct']('ids=' + id);
	}
}
</script>