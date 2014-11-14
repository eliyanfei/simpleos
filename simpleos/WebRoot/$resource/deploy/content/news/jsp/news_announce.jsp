<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.content.EContentType"%>
<%@ page import="net.simpleframework.content.news.NewsUtils"%>
<%@ page import="net.simpleframework.content.news.News"%>
<%@ page import="net.simpleframework.core.ado.IDataObjectQuery"%>
<%@ page
	import="net.simpleframework.content.component.newspager.INewsPagerHandle"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page import="net.simpleframework.util.ConvertUtils"%>
<%@ page import="java.util.Random"%>
<%
	final ComponentParameter nComponentParameter = new ComponentParameter(
			request, response, null);
	nComponentParameter.componentBean = NewsUtils.applicationModule
			.getComponentBean(nComponentParameter);
	if (nComponentParameter.componentBean == null) {
		return; 
	}
	final IDataObjectQuery<News> qs = NewsUtils.queryNews(
			nComponentParameter, null, EContentType.announce, null, 0);
	final int rows = ConvertUtils
			.toInt(request.getParameter("rows"), 6);
	if (rows > 0) {
		qs.setCount(rows);
	}
	request.setAttribute("queryNews", qs);
	final INewsPagerHandle nHandle = (INewsPagerHandle) nComponentParameter
			.getComponentHandle();
	final int frequency = ConvertUtils.toInt(
			request.getParameter("frequency"), 5);
	final String img = NewsUtils.deployPath + "images/announce.png";
%>
<div id="_news_announce_marquee">
	<img src="<%=img%>"><span></span>
</div>
<script type="text/javascript">
	(function() {
		var m = $("_news_announce_marquee").down("span");
		var d = [];
<%News news;
			int c = 0;
			while ((news = qs.next()) != null) {
				out.write("d.push('"
						+ nHandle.wrapOpenLink(nComponentParameter, news)
						+ "');");
				c++;
			}
			final int jj = c == 0 ? 1 : new Random().nextInt(c);%>
	if (d.length == 0)
			return;
		var i = <%=jj%>;
		m.update(d[i++]);
		new PeriodicalExecuter(function() {
			if (!m)
				return;
			if (i >= d.length)
				i -= d.length;
			m.$hide({
				duration : 0.8,
				afterFinish : function() {
					m.update(d[i++]);
					m.$show();
				}
			});
		},
<%=frequency%>
	);
	})();
</script>
<style type="text/css">
#_news_announce_marquee img,#_news_announce_marquee a {
	vertical-align: middle;
}

#_news_announce_marquee img {
	cursor: pointer; 
	margin: 0px 2px 0px 6px;
}
</style>