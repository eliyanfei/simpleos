<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page import="net.itsite.utils.StringsUtils"%><%@page
	import="net.simpleos.backend.links.LinksUtils"%><%@page
	import="net.simpleos.backend.links.LinksBean"%>

<style>
#l_title,#l_days,#l_url,#l_description {
	border-width: 0;
	width: 99%;
	background-image: none;
}
</style>
<%
	final String linksId = request.getParameter("linksId");
	LinksBean linksBean = LinksUtils.appModule.getBean(LinksBean.class, linksId);
	if (linksBean == null) {
		linksBean = new LinksBean();
	}
%>
<div id="linksForm">
	<div id="__np__newsAddForm">
		<input id="linksId" name="linksId" type="hidden"
			value="<%=request.getParameter("linksId")%>" />
		<table cellspacing="0" class="tbl tbl_first">
			<tr>
				<td class="lbl">
					#(links.add.1)
				</td>
				<td>
					<input type="text" id="l_title" name="l_title">
				</td>
			</tr>
		</table>
		<table cellspacing="0" class="tbl">
			<tr>
				<td class="lbl">
					#(links.add.2)
				</td>
				<td>
					<input type="text" id="l_url" name="l_url" />
				</td>
			</tr>
		</table>
		<table cellspacing="0" class="tbl">
			<tr>
				<td class="lbl">
					#(links.add.3)
				</td>
				<td>
					<textarea rows="4" style="width: 99%;" id="l_description"
						name="l_description"></textarea>
				</td>
			</tr>
		</table>

		<table class="tbl" cellspacing="0">
			<tr>
				<td class="lbl">
					#(links.add.5)
				</td>
				<td colspan="3">
					<div class="clear_float" style="padding: 4px;">
						<div style="float: right; width: 100%;">
							<div class="btn">
								<table width="100%">
									<tr>
										<td align="left">
											<input type="hidden" name="l_color" id="l_color">
											<div align="left"
												style="float: left;width: 20px; height: 20px; background-color: <%=StringsUtils.trimNull(linksBean.getColor(), "black")%>;"
												id="l_color_" onclick="$IT.A('paletteWin');"></div>
										</td>
										<td>
											<input type="button" id="validBtn" value="#(Itsite.c.ok)"
												class="button2" onclick="$IT.A('linksSave');" />
											<input type="button" value="#(Itsite.c.close)"
												onclick="$IT.C('linksAddWin');" />
										</td>
									</tr>
								</table>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
</div>
<script type="text/javascript">
</script>
