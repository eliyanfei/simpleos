<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.content.component.newspager.NewsPagerUtils"%>
<%@ page
	import="net.simpleframework.content.component.newspager.INewsPagerHandle"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page
	import="net.simpleframework.content.component.newspager.NewsBean"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentRenderUtils"%><%@page import="net.simpleos.SimpleosUtil"%>

<%
	final ComponentParameter nComponentParameter = NewsPagerUtils
	.getComponentParameter(request, response);
	final INewsPagerHandle nHandle = (INewsPagerHandle) nComponentParameter
	.getComponentHandle();
	final NewsBean newsBean = nHandle
	.getEntityBeanByRequest(nComponentParameter);
	final boolean showValidateCode = (Boolean) nComponentParameter
	.getBeanProperty("showValidateCode");
%>
<div id="__np__newsAddForm">
	<%=ComponentRenderUtils.genParameters(nComponentParameter)%>
	<table class="tbl tbl_first" cellspacing="0">
		<tr>
			<td class="lbl">#(np_edit.1)</td>
			<td><input type="text" id="np_topic" name="np_topic" />
			</td>
			<td class="lbl2">#(np_edit.8)</td>
			<td width="200px"><div id="td_np_catalog"></div> <input
				type="hidden" id="np_catalog" name="np_catalog" />
			</td>
		</tr>
	</table>
	<table class="tbl" cellspacing="0">
		<tr>
			<td class="lbl">#(np_edit.2)</td>
			<td><input type="text" id="np_keywords" name="np_keywords" />
			</td>
			<td class="lbl2">#(np_edit.4)</td>
			<td width="150px"><input type="text" id="np_source"
				name="np_source" />
			</td>
			<td class="lbl2">#(np_edit.3)</td>
			<td width="200px"><input type="text" id="np_author"
				name="np_author" />
			</td>
		</tr>
	</table>
	<table class="tbl" cellspacing="0">
		<tr>
			<td class="lbl">#(np_edit.5)</td>
			<td style="padding: 3px;">
				<div class="clear_float" style="padding-bottom: 3px;">
					<div style="float: right;"><%=SimpleosUtil.getHtmlEditorToolbar(nComponentParameter, "news", "np_content")%></div>
					<div style="float: left" id="np_content_info" class="important-tip">#(CKEditor.0)</div>
				</div>
				<div>
					<textarea id="np_content" name="np_content" style="display: none;"></textarea>
				</div></td>
		</tr>
	</table>
	<table class="tbl" cellspacing="0">
		<tr>
			<td class="lbl">#(Description)</td>
			<td><textarea id="np_description" name="np_description" rows="2"></textarea>
			</td>
		</tr>
	</table>
	<table class="tbl" cellspacing="0">
		<tr>
			<td class="lbl">
				<%
					if (showValidateCode) {
						out.write("#(np_edit.6)");
					}
				%>
			</td>
			<td><div class="clear_float" style="padding: 4px;">
					<div style="float: right;">
						<%
							if (newsBean == null) {
						%>
						<div>
							<input type="checkbox" id="np_attention" name="np_attention"
								value="true" /><label for="np_attention">#(topic_edit.4)</label>
						</div>
						<%
							}
						%>
						<div>
							<input type="checkbox" id="np_att2" name="np_att2" value="true" /><label
								for="np_att2" style="margin-right: 4px;">#(topic_edit.6)</label><input
								type="checkbox" id="np_allowComments" name="np_allowComments"
								value="true" /><label for="np_allowComments">#(np_edit.7)</label>
						</div>
						<div class="btn">
							<span class="desc">#(topic_edit.7)</span><input type="button"
								class="button2" value="#(Button.Save)" key="ctrlReturn"
								id="__np_newsAddBtn" onclick="$Actions['ajaxNewspagerAdd']();" />
							<input type="button" value="#(Button.Cancel)"
								onclick="$Actions['addNewspagerWindow'].close();" />
						</div>
					</div>
					<%
						if (showValidateCode) {
					%>
					<div style="float: left;" id="newsEditorValidateCode"></div>
					<%
						}
					%>
				</div>
			</td>
		</tr>
	</table>
</div>
<script type="text/javascript">
	(function() {
		var ele = $Comp.textButton("np_catalog_text", function(ev) {
			$Actions["newsCatalogDict"]();
		});
		$Actions.readonly(ele.textObject);
		$("td_np_catalog").update(ele);

		window.onbeforeunload = function() {
			return "#(CKEditor.1)";
		};
	})();
</script>