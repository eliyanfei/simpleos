<%@page import="net.simpleframework.ado.db.IQueryEntitySet"%>
<%@page import="net.simpleframework.util.ConvertUtils"%>
<%@page import="net.simpleframework.web.page.PageRequestResponse"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="net.itsite.document.docu.DocuBean"%>
<%@page import="net.itsite.document.docu.DocuUtils"%>
<%@page import="net.itsite.document.docu.EDocuStatus"%>
<%@page import="net.itsite.ItSiteUtil"%>
<%@page import="net.simpleframework.util.HTMLBuilder"%>
<%@page import="net.itsite.impl.AbstractCatalog"%>
<%@page import="net.itsite.document.docu.DocuCatalog"%><%@page
	import="net.simpleframework.web.EFunctionModule"%>

<%
	final PageRequestResponse requestResponse = new PageRequestResponse(request, response);
	final DocuBean docuBean = DocuUtils.applicationModule.getViewDocuBean(requestResponse);
	if (docuBean == null) {
		out.print(ItSiteUtil.buildActionLoc("/docu.html"));
		return;
	}
	//管理员可以查看
	if (docuBean.getStatus() != EDocuStatus.publish && !ItSiteUtil.isManage(requestResponse, DocuUtils.applicationModule)) {
		out.print("正在审核!");
	}
%>
<div>
	<table width="100%">
		<tr>
			<td valign="top">
				<div class="show_template" style="background-color: #fff;margin-top: 1px;">
					<div class="nav2 clear_float">
						<div style="float: left;">
							<div class="nav1_image">
								<%=DocuUtils.buildTitle(requestResponse, String.valueOf(docuBean.getCatalogId()))%>
							</div>
						</div>
						<div style="float: right;">
							<%=DocuUtils.applicationModule.getActionHTML(requestResponse, docuBean)%><%=ItSiteUtil.buildComplaint(requestResponse, EFunctionModule.docu, docuBean.getId())%>
							<%=HTMLBuilder.SEP%>
							<a style="color: blue;" href="/mydocu.html?id=myUpload">#(Docu.upload)</a>
						</div>
					</div>
					<div class="sj">
						<div class="f2" style="padding: 10px 0">
							<h3><%=docuBean.getTitle()%>
							</h3>
						</div>
					</div>
					<div style="padding: 4px 0px;" class="inherit_c wrap_text">
						<div id="news_body">
							<%=docuBean.getContent()%>
						</div>
					</div>
				</div>
				<jsp:include page="/app/common/ad_bar_content.jsp"></jsp:include>
				
				<div class="idRemark" id="idNewsRemark">
					<input type="hidden" name="docuId"
						value="<%=request.getParameter("docuId")%>">
					<a name="idNewsRemark" style="display: none;"></a>
					<div id="docu_remark"></div>
				</div>
				
			</td>
			<td width="260" valign="top">
				<jsp:include page="/app/docu/view_layout_right.jsp"></jsp:include>
			</td>
		</tr>
	</table>
</div>
