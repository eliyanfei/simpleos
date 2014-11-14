<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%><%@page import="net.simpleframework.web.page.PageRequestResponse"%>

<%
	PageRequestResponse requestResponse = new PageRequestResponse(request, response);
%>
<%=OrgUtils.tabs(requestResponse)%>
<div id="jcTree" style="padding: 8px;">
</div>
<%
	final String idParameterName = OrgUtils.jcm()
			.getJobChartIdParameterName();
%>
<script type="text/javascript">
	function __jobmgr(branch) {
		var act = $Actions['__jobTree'];
		var treeAct = act.getTree();
		var params = "<%=idParameterName%>=" + branch.getId();
		if (treeAct) {
			$Actions.addHidden($("__job_tree_c").down("form"), params);
			treeAct.refresh(params);
		} else {
			act(params);
		}
		$("__job_detail").update("");
	}

	function __jobchart_branch(item) {
		return $Actions['jcTree'].getBranch($target(item));
	}

	function __jobchart_add(item) {
		var branch = __jobchart_branch(item);
		if (!branch)
			return;
		var deptId;
		var id = branch.getId();
		if (id.startsWith("d_")) {
			deptId = id.substring(2);
		}	else {
			var pBranch = branch.getParent();
			if (pBranch) {
				if ((id = pBranch.getId()).startsWith("d_")) {
					deptId = id.substring(2);
				}	
			}
		}
		var win = $Actions["jcEditWindow"];
		if (deptId) {
			win("<%=OrgUtils.dm().getDepartmentIdParameterName()%>=" + deptId);
		} else {
			win();
		}		
	}

	function __jobchart_edit(item) {
		var branch = __jobchart_branch(item);
		if (!branch)
			return;
		$Actions["jcEditWindow"]("<%=idParameterName%>=" + branch.getId());
	}

	function __jobchart_delete(item) {
		var branch = __jobchart_branch(item);
		if (!branch)
			return;
		$Actions["ajaxJobChartDelete"]("<%=idParameterName%>=" + branch.getId());
	}

	function __jobchart_move(item, up) {
		var p = __tree_move(__jobchart_branch(item), up);
		if (p) {
			$Actions["ajaxJobChartMove"](p);
		}
	}

	function __jobchart_move2(item, up) {
		var p = __tree_move2(__jobchart_branch(item), up);
		if (p) {
			$Actions["ajaxJobChartMove"](p);
		}
	}
</script>
