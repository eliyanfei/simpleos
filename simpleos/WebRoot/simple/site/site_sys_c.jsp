<%@page import="net.itsite.ItSiteUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page import="net.itsite.impl.PrjColumns"%><%@page
	import="net.simpleos.backend.site.SiteUtils"%><%@page
	import="net.simpleos.backend.BackendUtils"%><%@page import="java.util.Map"%><%@page
	import="net.itsite.utils.StringsUtils"%>
<%
	PrjColumns columns = SiteUtils.appModule.getPrjColumns("sys");
	Map<String, String> map = ItSiteUtil.attrMap;
	String language = StringsUtils.trimNull(map.get("sys.sys_language"), columns.getColumnMap().get("sys_language").getDefValue());
%>
<div class="simple_custom" id="site_sys_form">
	<table>
		<tr>
			<th>
				#(Site.Sys.0)
			</th>
			<td>
				<input type="checkbox" id="lang1" name="sys_language" value="zh"
					<%=language.contains("zh,") ? "checked=\"checked\"" : ""%>>
				<label for="lang1">
					简体
				</label>
				<input type="checkbox" id="lang2" name="sys_language" value="zh-tw"
					<%=language.contains("zh-tw,") ? "checked=\"checked\"" : ""%>>
				<label for="lang2">
					繁体
				</label>
				<input type="checkbox" id="lang3" name="sys_language" value="en"
					<%=language.contains("en,") ? "checked=\"checked\"" : ""%>>
				<label for="lang3">
					English
				</label>
			</td>
		</tr>
		<tr>
			<th>
				是否测试系统
			</th>
			<td>
				<input type="checkbox" name="sys_testing" value="true"
					<%="true".equals(map.get("sys.sys_testing")) ? "checked=\"checked\"" : ""%>>
			</td>
		</tr>
		<tr>
			<th>
				是否允许注册
			</th>
			<td>
				<input type="checkbox" name="sys_register" value="true"
					<%="true".equals(map.get("sys.sys_register")) ? "checked=\"checked\"" : ""%>>
			</td>
		</tr>
		<tr>
			<th>
				评论不需要登入
			</th>
			<td>
				<input type="checkbox" name="sys_remark" value="true"
					<%="true".equals(map.get("sys.sys_remark")) ? "checked=\"checked\"" : ""%>>
			</td>
		</tr>
		<tr>
			<th valign="top">
				注册时是否需要激活
			</th>
			<td>
				<input type="checkbox" name="sys_mail" value="true"
					onclick="$('sys_mail_id').style.display=this.checked?'':'none';"
					<%="true".equals(map.get("sys.sys_mail")) ? "checked=\"checked\"" : ""%>>
				<div id="sys_mail_id" style="display: <%="true".equals(map.get("sys.sys_mail")) ? "" : "none"%>;">
					<table>
						<tr>
							<td>
								发送地址
							</td>
							<td>
								<input type="text"
									value="<%=StringsUtils.trimNull(map.get("sys.sys_mail_sentAddress"), "simpleos@qq.com")%>"
									id="sys_mail_sentAddress" name="sys_mail_sentAddress">
							</td>
						</tr>
						<tr>
							<td>
								服务地址
							</td>
							<td>
								<input type="text"
									value="<%=StringsUtils.trimNull(map.get("sys.sys_mail_smtpServer"), "smtp.exmail.qq.com")%>"
									id="sys_mail_smtpServer" name="sys_mail_smtpServer">
							</td>
						</tr>
						<tr>
							<td>
								用户名
							</td>
							<td>
								<input type="text"
									value="<%=StringsUtils.trimNull(map.get("sys.sys_mail_smtpUsername"), "simpleos")%>"
									id="sys_mail_smtpUsername" name="sys_mail_smtpUsername">
							</td>
						</tr>
						<tr>
							<td>
								密码
							</td>
							<td>
								<input type="password"
									value="<%=StringsUtils.trimNull(map.get("sys.sys_mail_smtpPassword"), "simpleos")%>"
									id="sys_mail_smtpPassword" name="sys_mail_smtpPassword">
							</td>
						</tr>
					</table>

				</div>
			</td>
		</tr>
		<tr>
			<th></th>
			<td>
				<input type="button" class="button2" id="site_sys_btn"
					onclick="$IT.A('site_sysAct');" value="#(Itsite.c.ok)">
			</td>
		</tr>
	</table>
</div>