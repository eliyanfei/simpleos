<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page import="net.simpleframework.content.component.vote.VoteUtils"%>

<form id="ve3FormEditor">
	<input type="hidden" id="<%=VoteUtils.VOTE_GROUP_ID%>"
		name="<%=VoteUtils.VOTE_GROUP_ID%>"
		value="<%=StringUtils.blank(request
					.getParameter(VoteUtils.VOTE_GROUP_ID))%>" />
	<input type="hidden" id="<%=VoteUtils.VOTE_ITEM_ID%>"
		name="<%=VoteUtils.VOTE_ITEM_ID%>"
		value="<%=StringUtils.blank(request
					.getParameter(VoteUtils.VOTE_ITEM_ID))%>" />
</form>
<div style="text-align: right; padding-top: 6px;">
	<input type="button" class="button2" value="#(Button.Save)"
		id="ve3Save" onclick="__vote_edit_submit(3);" /> <input type="button"
		value="#(Button.Cancel)"
		onclick="$Actions['voteEdit3Window'].close();" />
</div>