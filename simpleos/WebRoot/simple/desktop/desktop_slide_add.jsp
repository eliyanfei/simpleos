<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@page import="net.itsite.utils.StringsUtils"%><%@page
	
	import="net.simpleos.backend.links.LinksUtils"%><%@page
	import="net.simpleos.backend.slide.IndexSlideBean"%><%@page
	import="net.simpleos.backend.BackendUtils"%>



<style>
#ds_title,#ds_url {
	border-width: 0;
	width: 99%;
	background-image: none;
}
</style>
<%
	final String navId = request.getParameter("navId");
	IndexSlideBean navBean = BackendUtils.applicationModule.getBean(IndexSlideBean.class, navId);
	if (navBean == null) {
		navBean = new IndexSlideBean();
	}
%>
<div id="saveForm">
	<div id="__np__newsAddForm">
		<input id="navId" name="navId" type="hidden" value="<%=navId%>" />
		<table cellspacing="0" class="tbl tbds_first">
			<tr>
				<td class="lbl">
					标题
				</td>
				<td>
					<input type="text" id="ds_title" name="ds_title"
						value="<%=StringsUtils.trimNull(navBean.getTitle(), "")%>">
				</td>
			</tr>
		</table>
		<table cellspacing="0" class="tbl">
			<tr>
				<td class="lbl">
					连接
				</td>
				<td>
					<input type="text" id="ds_url" name="ds_url"
						value="<%=StringsUtils.trimNull(navBean.getUrl(), "")%>" />
				</td>
			</tr>
		</table>

		<table class="tbl" cellspacing="0">
			<tr>
				<td class="lbl">
				</td>
				<td colspan="3">
					<input type="button" id="validBtn" value="#(Itsite.c.ok)"
						class="button2" onclick="$IT.A('saveNav');" />
					<input type="button" value="#(Itsite.c.close)"
						onclick="$IT.C('slideAddWindow');" />
				</td>
			</tr>
		</table>
	</div>
</div>
<script type="text/javascript">
</script>
