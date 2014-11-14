<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.organization.component.login.LoginUtils"%>
<table style="width: 100%; height: 100%;" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" valign="top">
			<div style="text-align: center; padding: 60px 0px 20px 0px; text-shadow: 3px 3px 7px #aaa;"
				class="f2">#(default_login_c.0)</div>
			<div style="padding: 25px 16px; width: 320px;" class="simple_toolbar3">
				<table style="width: 100%;" cellpadding="0" cellspacing="0">
					<tr>
						<td style="padding: 20px 10px 40px 0px; border-right: 3px double #ccc;"><img
							src="<%=LoginUtils.getHomePath() + "/images/login.png"%>" /></td>
						<td style="padding-left: 10px;" id="__default_login"></td>
					</tr>
				</table>
			</div></td>
	</tr>
</table>
