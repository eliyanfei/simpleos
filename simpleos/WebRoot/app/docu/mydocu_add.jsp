<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page
	import="net.itsite.docu.EDocuFunction"%><%@page
	import="net.itsite.docu.DocuUtils"%>


<div id="docuForm">
	<input type="hidden" id="docuId" name="docuId">
	<table width="100%'">
		<tbody>
			<tr>
				<td class="l">
					#(Docu.add.0)：
				</td>
				<td>
					<input type="text" id="docu_title" name="docu_title"
						style="width: 100%" value="">
				</td>
			</tr>
			<tr>
				<td class="l" valign="top">
					#(Docu.add.1)：
				</td>
				<td>
					<textarea id="docu_content" name="docu_content" rows="5"
						style="width: 99%"></textarea>
				</td>
			</tr>
			<tr>
				<td class="l">
					#(Docu.add.2)：
				</td>
				<td>
					<input type="text" id="docu_keyworks" name="docu_keyworks"
						style="width: 100%">
					<div id="docu_tag"></div>
				</td>
			</tr>
			<tr>
				<td class="l">
					#(Docu.add.3)：
				</td>
				<td>
					<div id="td_docu_catalog" style="width: 100%"></div>
					<input type="hidden" name="docu_catalog" id="docu_catalog">
				</td>
			</tr>
		</tbody>
	</table>
</div>
<div align="center">
	<input type="button" class="button2" id="__docuAddBtn"
		onclick="$IT.A('docuEditSaveAct');" value="#(Docu.add.4)">
</div>
<style type="text/css">
#docuForm .l {
	width: 100px;
	text-align: right;
}

#docuForm {
	background: #f7f7ff;
	border: 1px solid #ddd;
	padding: 6px 8px;
}

#docuForm .ccontent {
	border-bottom: 1px dotted #ccc;
}
</style>
<script type="text/javascript">
function selectDocuAttr(t) {
	if ('code' == t.value) {
		$$('.code_catalog_id').each(function(c) {
			c.style.display = '';
		});
	} else {
		$$('.code_catalog_id').each(function(c) {
			c.style.display = 'none';
		});
	}
}
$ready(function() {
	addTextButton("docu_catalog_text", "docuCatalogAct", "td_docu_catalog",
			false);
});
</script>