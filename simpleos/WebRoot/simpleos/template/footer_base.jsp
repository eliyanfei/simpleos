<%@page import="net.simpleos.SimpleosUtil"%>
<%@page import="net.simpleos.utils.StringsUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
Powered by
<%=StringsUtils.trimNull(SimpleosUtil.attrMap.get("site.site_name"), "")%>&nbsp;&nbsp;&nbsp;版权所有
©<%=StringsUtils.trimNull(SimpleosUtil.attrMap.get("site.site_copyright"), "")%>&nbsp;&nbsp;
<a href="http://www.miitbeian.gov.cn/" target="_blank"><%=StringsUtils.trimNull(SimpleosUtil.attrMap.get("site.site_icp"), "")%></a>
<%=StringsUtils.trimNull(SimpleosUtil.attrMap.get("stat.stat_code"), "")%>