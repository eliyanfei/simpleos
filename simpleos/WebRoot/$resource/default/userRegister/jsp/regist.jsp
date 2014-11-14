<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.organization.component.register.UserRegisterUtils"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%
	final ComponentParameter nComponentParameter = UserRegisterUtils.getComponentParameter(request, response);
	final String beanId = nComponentParameter.componentBean.hashId();
	final String termsUrl = (String) nComponentParameter.getBeanProperty("termsUrl");
	final String closeAction = (String) nComponentParameter.getBeanProperty("closeAction");
%>
<style>
.AbstractRegisterPage input[type="text"],.AbstractRegisterPage  input[type="password"]
	{
	height: 19px;
	line-height: 19px;
}
</style>
<form id="_registForm">
	<input type="hidden" id="<%=UserRegisterUtils.BEAN_ID%>"
		name="<%=UserRegisterUtils.BEAN_ID%>" value="<%=beanId%>" />
	<table class="AbstractRegisterPage">
		<tbody>
			<tr>
				<td width="32%" valign="top">
					<div class="cl">
						<p>
							已有帐号？ 直接
							<a href="/login.html" hidefocus="hidefocus">登录</a>
						</p>
						<h3>
							注意事项？
						</h3>
						<ul>
							<li>
								邮箱是获取激活码的方式
							</li>
							<li>
								收到邮件后，需要先激活才能登入
							</li>
						</ul>
					</div>
				</td>
				<td width="68%" valign="top">
					<table class="RegisterPage">
						<tbody>
							<tr>
								<td class="l">
									用户名
								</td>
								<td class="v">
									<div class="dv">
										<input type="text" id="user_account" name="user_account"
											class="ifocus" placeholder="用户名，唯一">
										<span class="icon_user"></span>
									</div>
								</td>
							</tr>
							<tr>
								<td class="l">
									密码
								</td>
								<td class="v">
									<div class="dv">
										<input type="password" id="user_password" name="user_password"
											class="ifocus" placeholder="密码">
										<span class="icon_pwd"></span>
									</div>
								</td>
							</tr>
							<tr>
								<td class="l">
									确认密码
								</td>
								<td class="v">
									<div class="dv">
										<input type="password" id="user_password2"
											name="user_password2" placeholder="重复密码" class="ifocus">
										<span class="icon_pwd"></span>
									</div>
								</td>
							</tr>
							<tr>
								<td class="l">
									电子邮箱
								</td>
								<td class="v">
									<div class="dv">
										<input type="text" id="user_email" name="user_email"
											class="ifocus" placeholder="邮箱，用于验证">
										<span class="icon_email"></span>
									</div>
								</td>
							</tr>
							<tr>
								<td class="l">
									昵称
								</td>
								<td class="v">
									<div class="dv">
										<input type="text" id="user_text" name="user_text"
											class="ifocus" placeholder="昵称">
										<span class="icon_text"></span>
									</div>
								</td>
							</tr>
							<tr>
								<td class="l">
									验证码
								</td>
								<td class="v">
									<div id="idRegisterPage_code">
										<div class="simple_validatecode">
											<div class="b2">
												<div id="regist_validateCode"></div>
											</div>
											<div class="clearfix"></div>
										</div>
									</div>
								</td>
							</tr>
							<tr>
								<td class="l"></td>
								<td class="v">
									<div class="bb">
										<input id="idRegisterPage_save" type="button" class="button2"
											value="同意协议并注册" onclick="$Actions['ajaxRegistAccount']();">
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
		</tbody>
	</table>
</form>

<script type="text/javascript">
function _registCallback(json) {
	Validation.clearInsert("textRegistValidateCode");
	if (json["validateCode"]) {
		Validation.insertAfter("textRegistValidateCode", json["validateCode"]);
	} else if (json["account"]) {
		Validation.insertAfter("user_account", json["account"]);
	} else if (json["email"]) {
		Validation.insertAfter("user_email", json["email"]);
	}else {
		$('textRegistValidateCode').clear();
		if (json["jsCallback"])
			$eval(json["jsCallback"]);
	}
}
</script>
