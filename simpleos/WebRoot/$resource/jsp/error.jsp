<%@page import="net.simpleframework.web.page.PageUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.web.page.IPageConstants"%>
<%@ page import="net.simpleframework.util.ConvertUtils"%>
<%@ page import="net.simpleframework.util.HTMLUtils"%>
<%
	Throwable th = (Throwable) session
			.getAttribute(IPageConstants.THROWABLE);
	if (th == null) {
%>
<script type="text/javascript">
	window.onload = function() {
		$Actions.loc('/');
	};
</script>
<%
	return;
	}
	session.removeAttribute(IPageConstants.THROWABLE);
%>
<div align="center">
  <div class="simple_toolbar1"
    style="width: 640px; margin-top: 100px; text-align: left;">
    <div style="height: 24px;">
      <div style="float: right;">
        <input type="button" value="#(error.1)"
          onclick="history.back();" /> <input type="button"
          value="#(error.2)" onclick="$Actions.loc('/');" />
      </div>
      <div style="float: left" class="f3">#(error.0)</div>
    </div>
    <div class="simple_toolbar wrap_text"
      style="margin: 6px 0px; color: #8D3212;"><%=HTMLUtils.convertHtmlLines(HTMLUtils
					.htmlEscape(PageUtils.pageConfig.getThrowableMessage(th)))%></div>
    <div class="simple_toolbar1" style="padding: 4px;">
      <textarea
        style="height: 300px; border: 0; width: 100%; background-image: none;"
        readonly><%=HTMLUtils.htmlEscape(ConvertUtils.toString(th))%></textarea>
    </div>
  </div>
</div>
