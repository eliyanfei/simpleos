<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div style="float: left"><input type="button" value="#(Button.Save2)" id="jobmemberSaveAndNew"
	onclick="$Actions['ajaxJobmemberSave']('next=true');" /></div>
<div style="float: right"><input type="button" class="button2" value="#(Button.Save)" id="jobmemberSave"
	onclick="$Actions['ajaxJobmemberSave']();" /> <input type="button" value="#(Button.Cancel)"
	onclick="$Actions['jobmemberWindow'].close();" /></div>
<form id="jobmemberFormEditor" style="clear: both; padding-top: 6px;"></form>
