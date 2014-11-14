<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.util.LocaleI18n"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%
	String jn = StringUtils.blank(request.getParameter("job"));
	String jname;
	if (jn.startsWith("#")) {
		jname = LocaleI18n.getMessage("job_http_access.4");
		jn = jn.substring(1);
	} else {
		jname = LocaleI18n.getMessage("job_http_access.5");
	}
%>
<div class="job_ajax_access">
  <div class="simple_toolbar">
    <div class="error_image"></div>
    <div class="detail">
      <div class="t1"><%=LocaleI18n.getMessage("job_ajax_access.0")%><span
          class="s1"><%=LocaleI18n.getMessage("job_ajax_access.2",
					request.getParameter("v"))%></span>
      </div>
      <div class="t2"><%=LocaleI18n.getMessage("job_http_access.1", jname, jn)%></div>
    </div>
  </div>
  <div class="bbar">
    <input type="button"
      value="<%=LocaleI18n.getMessage("Button.Close")%>"
      onclick="$Actions['jobAccessWindow'].close();" />
  </div>
</div>
