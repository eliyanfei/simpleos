<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page import="net.a.ItSiteUtil"%>

<%
	String jobId = request.getParameter("jobId");
	String jobName = request.getParameter("jobName");
	ItSiteUtil.addMenuNav(request.getSession(), null, "权限管理", false);
	
%>
<div class="ramse_block ramse_center" id="treeContainer"
	style="margin: 8px 6px; width: 300px; display: none">
	<div style="margin-bottom: 10px;">
		<input type="button" value="确定" onclick="updateMenusAction();">
	</div>
	<div id="platformMenuTree"></div>
	<input type="hidden" id="jobId" name="jobId" value="<%=jobId%>">
	<input type="hidden" id="jobName" name="jobName" value="<%=jobName%>">
</div>
<script type="text/javascript">

function updateMenusAction() {
	var treeArray = $Actions['menuTreeRef'].tree.getCheckedBranches();
	var checkedTree = new Array();
	treeArray.each(function(treeNode) {
		if (checkedTree.indexOf(treeNode.getText()) == -1) {
			checkedTree.push(treeNode.getText());
		}
		var parentTreeNode = treeNode.getParent();
		if (parentTreeNode) {
			var parentText = treeNode.getParent().getText();
			if ('功能菜单' != parentText) {
				if (checkedTree.indexOf(parentText) == -1) {
					checkedTree.push(parentText);
				}
			}
		}

	});
	$Actions['bindMenuWithJob']('jobId=' + $F('jobId') + '&jobName='
			+ $F('jobName') + '&menuNames=' + checkedTree);
}
</script>
