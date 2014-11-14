<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
	<head>
		<title>多元建站系统</title>
		<style>
.error {
	width: 502px;
	border: 1px solid #DDD;
	border-radius: 4px;
	-moz-border-radius: 4px;
	-webkit-border-radius: 4px;
	padding: 4px;
}

.error .h {
	border-bottom: 1px dashed #DDD;
}
</style>
	</head>
	<body>
		<table style="width: 100%; height: 100%;">
			<tr>
				<td align="center">
					<table class="error">
						<tr>
							<td class="h" class="logobar" valign="top">
								<img alt="项目管理系统" src="/default/images/logo.png">
							</td>
							<td class="h">
								<p>
									真的不希望您光临该页面,碰到这种情况我们深表遗憾。如果您真的在找一个很重要的资料或者内容，请留言联系我们，我们会近最大的努力帮您找到。
								</p>
							</td>
						</tr>
						<tr>
							<td class="h" colspan="2">
								<img
									src="<%=request.getContextPath()%>/simple/template/images/404.jpg">
							</td>
						</tr>
						<tr>
							<td align="center" colspan="2">
								<input type="button" value="返回首页" onclick="window.location='/';" />
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</body>
</html>