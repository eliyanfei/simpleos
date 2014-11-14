<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div id="themeDiv" class="simple_toolbar2">
	<table>
		<tr>
			<td>
				隐藏左栏：
			</td>
			<td>
				<input type="checkbox" id="lr_checkbox"
					onclick="setting__(this,'lr_');">
			</td>
		</tr>
		<tr>
			<td>
				自动换肤：
			</td>
			<td>
				<input type="checkbox" id="autotheme_checkbox"
					onclick="setting__(this,'autotheme_');"><span style="color: red;">间隔2分钟,随机切换</span>
			</td>
		</tr>
	</table>
</div>
<div>
	<fieldset class="simple_toolbar2">
		<legend style="color: red;">
			温馨提示
		</legend>
		<ul style="list-style-type: decimal;">
			<li>
				本窗口关闭，会刷新页面
			</li>
			<li>
				F11键可以全屏
			</li>
		</ul>
	</fieldset>
</div>
<script type="text/javascript">
function setting__(t, id) {
	$desktop.setCookies(id, t.checked);
}
$('lr_checkbox').checked = ($desktop.getCookies("lr_") == 'true');
$('autotheme_checkbox').checked = ($desktop.getCookies("autotheme_") == 'true');
</script>