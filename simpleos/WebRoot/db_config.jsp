<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<style>
#db_config {
	
}
</style>
</head>
<body>
	<div id="ddd"></div>
	<center>
		<div style="width: 400px; vertical-align: middle; margin-top: 150px;">
			<div>
				<h1>数据库配置</h1>
			</div>
			<div id="db_config"></div>
			<div style="height: 24px; margin-top: 5px;">
				<div style="float: right;">
					<input type="button" value="测试连接" class="valiBtn"
						onclick="$Actions['doConnectionTest']();"> <input
						type="button" class="button2 valiBtn" value="保存" id="dbSaveId"
						onclick="this.disabled = 'disabled';$Actions['doConnectionSave']();">
				</div>
			</div>
		</div>
	</center>
</body>
</html>