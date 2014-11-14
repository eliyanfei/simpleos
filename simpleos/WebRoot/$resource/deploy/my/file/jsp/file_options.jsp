<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div>
<fieldset><legend>#(file_options.0)</legend>
<p>#(file_options.1)</p>
<div><input id="fileResizeText" name="fileResizeText" type="text" value="100"
	style="width: 60px;" /> <a onclick="$Actions['ajaxFileResize']('jf=' + $F('fileResizeText'));">#(file_options.2)</a></div>
</fieldset>
</div>
