<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.syntaxhighlighter.SyntaxHighlighterUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page import="net.simpleframework.util.LocaleI18n"%>
<%
	final ComponentParameter nComponentParameter = SyntaxHighlighterUtils.getComponentParameter(request, response);
%>
<div class="sh_window">
	<div class="simple_toolbar clear_float">
		<div style="float: right;">
			<div>
				<input type="checkbox" id="sh_window_opt1" checked />
				<label for="sh_window_opt1">
					#(sh_window.0)
				</label>
			</div>
			<div>
				<input type="checkbox" id="sh_window_opt2" />
				<label for="sh_window_opt2">
					#(sh_window.4)
				</label>
			</div>
		</div>
		<div style="float: left;">
			<table>
				<tr>
					<td>
						#(sh_window.1)
					</td>
					<td>
						<select id="sh_window_lang">
							<%
								for (String lang : new String[] { "Java", "C++", "C", "Javascript", "SQL", "XML", "HTML", "CSS", "PHP", "Python", "Groovy", "C#", "Ruby" }) {
							%>
							<option><%=lang%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<div class="ta">
		<textarea rows="11" id="sh_window_textarea" style="width: 100%;"></textarea>
	</div>
	<div class="btm">
		<input type="button" value="#(sh_window.2)" class="button2"
			onclick="__syntaxhighlighter_insert();" />
		<input type="button" value="#(Button.Cancel)"
			onclick="__syntaxhighlighter_close();" />
	</div>
</div>
<script type="text/javascript">
function __syntaxhighlighter_insert() {
<%String jsSelectedCallback = (String) nComponentParameter.getBeanProperty("jsSelectedCallback");
			if (StringUtils.hasText(jsSelectedCallback)) {
				out.write("var script = '<pre class=\"brush: ' + $F('sh_window_lang').toLowerCase() + "
						+ "'; gutter: ' + $('sh_window_opt1').checked + " + "'; html-script: ' + $('sh_window_opt2').checked + "
						+ "';\">' + $F('sh_window_textarea').escapeHTML() + '</pre>';");
				out.write("var b = (function(script) {");
				out.write(jsSelectedCallback);
				out.write("})(script);");
				out.write("if (b) { __syntaxhighlighter_close(); }");
			} else {
				out.write("alert('" + LocaleI18n.getMessage("sh_window.3") + "');");
			}%>
	}
	
	function __syntaxhighlighter_close() {
		$Actions['window_<%=nComponentParameter.componentBean.hashId()%>'].close();
	}
</script>