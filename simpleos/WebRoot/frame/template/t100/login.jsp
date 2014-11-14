<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<style>
#__default_login input[type="text"],#__default_login input[type="password"]
	{
	height: 24px;
	line-height: 24px;
	-moz-border-radius: 4px;
	-webkit-border-radius: 4px;
	border-radius: 4px;
}

input[type="text"]:FOCUS,input[type="password"]:FOCUS {
	color: #333;
	font-weight: 700;
	border: 1px solid #84b4fc;
}

.lr {
	width: 80%;
	border: 1px solid #aaa;
	background-color: #fff;
	-webkit-box-shadow: 0 0 10px #aaa;
	-moz-box-shadow: 0 0 10px #aaa;
	box-shadow: 0 0 10px #aaa;
}

.ll {
	float: left;
	border: 1px solid #aaa;
	background-color: white;
}

#__default_login {
	padding: 20px;
	margin-top: 50px;
}
</style>

<div class="LoginPage" style="overflow: hidden;" align="center">
	<div class="lr" id="__default_login">
	</div>
</div>