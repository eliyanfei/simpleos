<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.organization.component.userselect.UserSelectUtils"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.web.page.component.ui.dictionary.DictionaryUtils"%>
<%@ page import="net.simpleframework.web.page.component.ComponentParameter"%>
<%
	final ComponentParameter nComponentParameter = UserSelectUtils
			.getComponentParameter(request, response);
	final String beanId = nComponentParameter.componentBean.hashId();
	final String idParameterName = OrgUtils.dm()
			.getDepartmentIdParameterName();
	final String textParameterName = OrgUtils.um()
			.getUserTextParameterName();
	final String componentName = (String) nComponentParameter
			.getBeanProperty("name");
%>
<div class="user_select">
	<div class="simple_toolbar">
		<div>
			<input type="text" style="width: 180px;" id="<%=textParameterName%>"
				name="<%=textParameterName%>" />
			<span class="important-tip" style="margin-left: 6px;">#(user_select.4)</span>
		</div>
		<%
			if ((Boolean) nComponentParameter
					.getBeanProperty("showDepartmentFilter")) {
		%>
		<table cellpadding="0" cellspacing="0">
			<tr>
				<td><div class="dept">
						<input type="hidden" id="<%=idParameterName%>" name="<%=idParameterName%>" /><a
							onclick="$Actions['deptSelectDict']('<%=UserSelectUtils.BEAN_ID%>=<%=beanId%>');">#(user_select.0)</a>
					</div></td>
				<td id="dept_text"></td>
			</tr>
		</table>
		<script type="text/javascript">
			function __dept_select_dict_callback(selects) {
				var f = $$("#container_<%=beanId%>")[0].previous();
				var v = f.down("#<%=OrgUtils.dm().getDepartmentIdParameterName()%>");
				v.value = selects[0].id;
				var t = f.down("#dept_text");
				t.innerHTML = "";
				t.insert(selects[0].text).insert(new Element("span", {
					className : "delete2_image",
					style : "margin-left:2px;vertical-align:bottom;display:inline-block;"
				}).observe("click", function() {
					v.value = "";
					t.innerHTML = "";
					__user_select_refresh();
				}));
				__user_select_refresh();
				return true;
			}
		</script>
		<%
			}
		%>
	</div>
	<div id="container_<%=beanId%>" class="tbl"></div>
	<div class="bb">
		<table cellpadding="2" cellspacing="0" style="width: 100%;">
			<tr>
				<td><%=DictionaryUtils.getActions(nComponentParameter)%></td>
				<td align="right">
					<%
						if ((Boolean) nComponentParameter.getBeanProperty("showCheckbox")) {
					%><input type="button" class="button2" value="#(user_select.1)" onclick="__user_select_dblclick();" /> <%
 	}
 %> <input type="button" value="#(Button.Cancel)" onclick="$Actions['<%=componentName%>'].close();" />
				</td>
			</tr>
		</table>
	</div>
</div>
<script type="text/javascript">
	function __user_select_refresh() {
		var p = $Actions["userSelectPager"];
		var f = $$("#container_<%=beanId%>")[0].previous();
		p($$Form(f));
	}
	
	function __user_select_dblclick(d) {
		var ref = $Actions["userSelectPager"];
		var selects = new Array();
		var arr = ref.checkCacheArr($("container_<%=beanId%>"));
		if (arr && arr.length > 0) {
			arr.each(function(d2) {
				selects.push({
					id   : ref.rowId(d2),
					name : d2.getAttribute("userName"),
					text : d2.getAttribute("userText")
				});
			});
		} else {
			if (d) {
				selects.push({
					id   : ref.rowId(d),
					name : d.getAttribute("userName"),
					text : d.getAttribute("userText")
				});
			}
		}
		if (selects.length == 0) {
			alert("#(user_select.2)");
		}
		<%=DictionaryUtils.genSelectCallback(nComponentParameter,
					"selects")%>
	}

	function __user_select_pager_loaded() {
		$$("#container_<%=beanId%> .titem").each(function(d) {
			d.observe("dblclick", function() {
				__user_select_dblclick(d);
			});
		});
		$Actions["userSelectPager"].checkCache($("container_<%=beanId%>"));
	}

	(function() {
		var txt = $("<%=textParameterName%>");
		$Comp.addBackgroundTitle(txt, "#(user_select.3)");
		$Comp.addReturnEvent(txt, __user_select_refresh);
	})();
	
	$ready(function() {
		var tp = $("container_<%=beanId%>");
		var tp_p = tp.previous();
		var w = $Actions['<%=componentName%>'].window;
		w.content.setStyle("overflow:hidden;");
		var s = function() {
			var h = w.getSize(true).height - (tp_p ? tp_p.getHeight() : 0)
					- tp.next().getHeight() - 14;
			tp.setStyle('overflow-y:auto;height: ' + h + 'px;');
		};
		s();
		w.observe("resize:ended", s);
	});
</script>