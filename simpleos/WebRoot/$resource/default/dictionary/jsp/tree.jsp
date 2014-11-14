<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.dictionary.DictionaryBean"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.dictionary.DictionaryBean.DictionaryTreeBean"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.dictionary.DictionaryUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentRenderUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%
	final ComponentParameter nComponentParameter = DictionaryUtils
			.getComponentParameter(request, response);
	DictionaryBean dictionaryBean = (DictionaryBean) nComponentParameter.componentBean;
	final String beanId = dictionaryBean.hashId();
	final String name = (String) nComponentParameter
			.getBeanProperty("name");
	final DictionaryTreeBean tree = (DictionaryTreeBean) dictionaryBean
			.getDictionaryTypeBean();
%>
<div class="dictionary" style="padding: 2px 0px 2px 4px;">
	<%=ComponentRenderUtils.genParameters(nComponentParameter)%>
	<div class="tree">
		<div id="tree<%=beanId%>"></div>
	</div>
	<jsp:include page="okcancel_inc.jsp" flush="true"></jsp:include></div>

<script type="text/javascript">
  treeCheck<%=beanId%> = function(branch, checked, ev) {  
	  if (!TafelTreeManager.ctrlOn(ev)) {
	    branch._manageCheckThreeState(branch, checked);
			branch._adjustParentCheck();
	  }
  };
      
  selected<%=beanId%> = function(branch, ev) {
		var selects = __tree_getSelects($Actions['<%=tree.getRef()%>'].tree, branch, ev);
    if (selects && selects.length > 0) {
 			<%=DictionaryUtils.genSelectCallback(nComponentParameter, "selects")%>
 		}
	};
  
  $ready(function() {
 		<%if (tree.isUpdateImmediately()) {%>
 	 	$Actions['<%=tree.getRef()%>']();
  	<%}%>
  	var t = $('tree<%=beanId%>').up();
    var w = $Actions['<%=name%>'].window;
		w.content.setStyle("overflow:hidden;");
		var s = function() {
			t.setStyle('height: ' + (w.getSize(true).height - 55) + 'px;');
		};
		s();
		w.observe("resize:ended", s);
	});
</script>
