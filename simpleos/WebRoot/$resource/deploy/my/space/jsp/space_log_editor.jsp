<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="space_log_editor">
	<table style="width: 100%;" cellpadding="2" cellspacing="2">
		<tr>
			<td class="f2">
				#(space_log.0)
			</td>
			<td style="font-size: 14px;" align="right">
				#(space_log.1)
				<span class="f2" style="font-family: candara;">140</span>#(space_log.2)
			</td>
		</tr>
	</table>
	<div class="simple_toolbar1">
		<textarea rows="5" id="ta_space_log_editor" name="ta_space_log_editor"></textarea>
	</div>
	<table style="width: 100%;" cellpadding="0" cellspacing="0">
		<tr>
			<td valign="top">
				<div class="ins">
					<a class="emotion" onclick="$Actions['smileySpaceLogDict']();">#(space_log.3)</a><a
						class="img" onclick="$Actions['imgSelectedWindow']();">#(space_log.4)</a><a
						class="vote" onclick="$Actions['voteSelectedWindow']();">#(space_log.6)</a>
				</div>
			</td>
			<td width="80" align="right">
				<span class="btn" onclick="$Actions['ajaxSpaceLogSave']();">#(space_log.5)</span>
			</td>
		</tr>
	</table>
</div>
<script type="text/javascript">
(function() {
	$$(".space_log_editor textarea").invoke("observe", "keyup", function(evn) {
		var editor = $(this);
		var txt = editor.up(".space_log_editor").down("span.f2");
		if (txt) {
			var v = $F(editor);
			if (v.length > 140) {
				editor.value = v.substring(0, 140);
			} else {
				txt.innerHTML = 140 - v.length;
			}
		}
	});
})();
function spacevote() {

}
</script>