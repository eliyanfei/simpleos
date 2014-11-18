<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<form class="my_friends_search_form">
	<input type="hidden" name="op" value="search" />
	<table cellpadding="2" cellspacing="0">
		<tr>
			<td class="td2"><input id="fs_name" name="fs_name" type="text" />
			</td>
			<td class="td3"><input type="submit" class="button2" value="#(Search)" id="fs_okbtn"
				onclick="$Actions['idMyFriendsSearchPager']($$Form(this.up('form')));" />
			</td>
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
