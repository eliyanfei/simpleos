<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="formGetpwd" class="simple_toolbar">
<div style="margin: 2px 0px;">#(get_pwd.0)</div>
<div><input type="text" style="width: 99%;" id="textGetpwd" name="textGetpwd" /></div>
<div style="margin-top: 8px;">#(get_pwd.1)</div>
<div id="getpwdValidateCode"></div>
</div>
<div style="margin-top: 8px; text-align: right;"><input type="button" id="btnGetpwd"
	value="#(get_pwd.2)" onclick="$Actions['ajaxGetpwd']()" /></div>