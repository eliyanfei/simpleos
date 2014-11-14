<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<form class="my_friends_search_form">
	<input type="hidden" name="op" value="search" />
	<table cellpadding="2" cellspacing="0">
		<tr>
			<td class="td1">#(my_friends_search.0)</td>
			<td class="td2"><input id="fs_name" name="fs_name" type="text" />
			</td>
			<td class="td3">#(my_friends_search.1)</td>
			<td class="td4"><input id="fs_text" name="fs_text" type="text" />
			</td>
		</tr>
		<tr>
			<td class="td1">#(my_friends_search.2)</td>
			<td class="td2"><input type="text" id="fs_email" name="fs_email" />
			</td>
			<td class="td3">#(my_friends_search.3)</td>
			<td class="td4"><input type="text" id="fs_mobile" name="fs_mobile" />
			</td>
		</tr>
		<tr>
			<td class="td1">#(my_friends_search.4)</td>
			<td class="td2"><input type="text" id="fs_address" name="fs_address" /></td>
			<td class="td3">#(my_friends_search.5)</td>
			<td class="td4">
				<table style="width: 100%;" cellpadding="0" cellspacing="0">
					<tr>
						<td width="45%"><input type="text" id="fs_birthday_s" name="fs_birthday_s"
							style="text-align: right;" />
						</td>
						<td align="center">~</td>
						<td width="45%"><input type="text" id="fs_birthday_e" name="fs_birthday_e" /></td>
					</tr>
				</table></td>
		</tr>
		<tr>
			<td class="td1">QQ</td>
			<td class="td2"><input type="text" id="fs_qq" name="fs_qq" /></td>
			<td class="td3">MSN</td>
			<td class="td4"><input type="text" id="fs_msn" name="fs_msn" /></td>
		</tr>
		<tr>
			<td></td>
			<td><input type="submit" class="button2" value="#(Search)" id="fs_okbtn"
				onclick="$Actions['idMyFriendsSearchPager']($$Form(this.up('form')));" />
				<input type="reset" />
			</td>
			<td></td>
			<td></td>
		</tr>
	</table>
</form>
<div id="idMyFriendsSearchPager"></div>
<style type="text/css">
#idMyFriendsSearchPager {
	margin-top: 8px;
	padding: 12px;
	border-top: 3px double #aaa;
}

.my_friends_search_form .td1 {
	text-align: right;
	width: 90px;
}

.my_friends_search_form .td3 {
	text-align: right;
	width: 50px;
}

.my_friends_search_form input[type='text'] {
	width: 100%;
}

.my_friends_search_form .td2,.my_friends_search_form .td4 {
	width: 180px;
}
</style>
