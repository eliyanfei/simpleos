<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div id="homeAddTabForm">
	<input type="hidden" id="tab_id" name="tab_id" />
	<table style="width: 100%;" cellspacing="1" class="tbl">
		<tr>
			<td class="l">#(my_home_add_tab.0)</td>
			<td class="v"><input type="text" id="tab_name" name="tab_name" />
			</td>
		</tr>
		<tr>
			<td class="l">#(my_home_add_tab.1)</td>
			<td class="v"><textarea id="tab_description"
					name="tab_description" rows="6"></textarea>
			</td>
		</tr>
	</table>
	<div style="text-align: right; margin-top: 6px;">
		<input type="button" id="tab_saveBtn" value="#(Button.Submit)"
			class="button2" onclick="$Actions['ajaxAddMyhomeTab']();" /> <input
			type="button" value="#(Button.Cancel)"
			onclick="$Actions['addMyhomeTabWin'].close();" />
	</div>
</div>
<style type="text/css">
#homeAddTabForm .tbl {
	background: #ddd;
}

#homeAddTabForm .tbl .l {
	background: #f7f7ff;
	width: 100px;
	text-align: right;
	padding: 4px;
}

#homeAddTabForm .tbl .v {
	background: #fff;
	padding: 4px;
}

#homeAddTabForm #tab_name,
#homeAddTabForm #tab_description {
	border-width: 0;
	width: 99%;
	background-image: none;
}
</style>