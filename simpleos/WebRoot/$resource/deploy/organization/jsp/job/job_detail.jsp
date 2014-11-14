<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.organization.OrgUtils"%>
<%@ page import="net.simpleframework.organization.IJob"%>
<%@ page import="net.simpleframework.organization.EJobType"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%
	final String jobIdParameterName = OrgUtils.jm()
			.getJobIdParameterName();
	final String jobId = request.getParameter(jobIdParameterName);
	final IJob job = OrgUtils.jm().queryForObjectById(jobId);
	final EJobType jt = job.getJobType();
%>
<div id="__job_detail_form">
  <input type="hidden" id="<%=jobIdParameterName%>"
    name="<%=jobIdParameterName%>" value="<%=jobId%>" />
</div>
<div class="t"><%=job.getText()%>
  (
  <%=job.getName()%>
  )
</div>
<div class="jd">
  <div class="bar">
    <table style="width: 100%;">
      <tr>
        <td>
          <%
          	if (jt == EJobType.normal) {
          %>#(job_detail.1)<%
          	} else if (jt == EJobType.handle) {
          %>#(job_detail.2)<%
          	} else {
          %>#(job_detail.3)<%
          	}
          %>
        </td>
        <td align="right">
          <%
          	if (jt == EJobType.normal) {
          %> <input type="button" value="#(job_detail.0)"
          onclick="$Actions['jobmemberWindow']();" /> <%
 	} else {
 %> <input type="button" class="button2" value="#(Button.Save)"
          onclick="$Actions['ajaxJobTypeSave']();" /> <%
 	}
 %>
        </td>
      </tr>
    </table>
  </div>
  <div id="__job_detail_form2">
    <%
    	if (jt == EJobType.normal) {
    %>
    <div id="jobViewId"></div>
    <%
    	} else if (jt == EJobType.handle) {
    %>
    <input id="job_ruleValue" name="job_ruleValue" type="text"
      value="<%=StringUtils.blank(job.getRuleHandle())%>" />
    <%
    	} else {
    %>
    <textarea id="job_ruleValue" name="job_ruleValue" rows="20"><%=StringUtils.blank(job.getRuleScript())%></textarea>
    <%
    	}
    %>
  </div>
</div>
