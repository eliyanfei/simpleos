<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.dictionary.DictionaryBean"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.dictionary.DictionaryBean.DictionaryListBean"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.dictionary.DictionaryUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%
	final ComponentParameter nComponentParameter = DictionaryUtils
			.getComponentParameter(request, response);
	DictionaryBean dictionaryBean = (DictionaryBean) nComponentParameter.componentBean;
	final String beanId = dictionaryBean.hashId();
	final String name = (String) nComponentParameter
			.getBeanProperty("name");
	final DictionaryListBean list = (DictionaryListBean) dictionaryBean
			.getDictionaryTypeBean();
%>
<div class="dictionary" style="padding: 0px;">
	<div id="list<%=beanId%>"></div>
	<jsp:include page="okcancel_inc.jsp" flush="true"></jsp:include></div>
<script type="text/javascript">
	var selected<%=beanId%> = function(item, json, ev) {
		var selects = [];
   	var create = function(b) {
   		return Object.extend(
   	   		{'id':b.getId(),'text':b.getText(),'item':b}, 
          b.data.attributes || {}
     	);
   	};
   	if (item) {
     	if (item.checkbox) item.check(true);
     	else selects.push(create(item));   
   	}
   	var listbox = $Actions['<%=list.getRef()%>'].listbox;
   	listbox.getCheckedItems().each(function(b) { selects.push(create(b)); });
   	<%=DictionaryUtils.genSelectCallback(nComponentParameter, "selects")%>
	};

	$ready(function() {
		var listAction = $Actions['<%=list.getRef()%>'];
		listAction();
		var win = $Actions['<%=name%>'].window;
		win.content.setStyle("overflow:hidden;");
		var endedAction = function() {
			listAction.listbox.setStyle('height: ' + (win.getSize(true).height - 57) + 'px;');
   	};
   	endedAction();
  	win.observe("resize:ended", endedAction);
 	});
</script>
	