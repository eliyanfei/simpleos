<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.ArrayList"%>
<%@ page
	import="net.simpleframework.applets.notification.component.window.IMessageWindowHandle"%>
<%@ page
	import="net.simpleframework.applets.notification.IMessageNotification"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page import="net.simpleframework.util.ConvertUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%@ page
	import="net.simpleframework.applets.notification.component.window.MessageWindowUtils"%><%@page
	import="net.simpleframework.applets.notification.SystemMessageNotification"%>
<%
	final ComponentParameter nComponentParameter = MessageWindowUtils.getComponentParameter(request, response);
	final IMessageWindowHandle mh = (IMessageWindowHandle) nComponentParameter.getComponentHandle();
	if (mh == null) {
		return;
	}

	final Collection<? extends IMessageNotification> l = mh.getMessageNotifications(nComponentParameter);
	if (l == null || l.size() == 0) {
%>
<div class="messageWindow_no_result">
	#(message_window.0)
</div>
<%
	return;
	}
	final int i = ConvertUtils.toInt(request.getParameter("mIndex"), 0);
	final String next = i < l.size() - 1 ? "next" : "next2";
	final String pre = i > 0 ? "pre" : "pre2";
	if (i >= l.size()) {
		return;
	}
	final IMessageNotification mn = new ArrayList<IMessageNotification>(l).get(i);
%>
<div class="messageWindow" id="messageWindow1">
	<div class="tb">
		<table style="width: 100%;" cellpadding="0" cellspacing="0">
			<tr>
				<td width="40px;">
					<div class="icon"></div>
				</td>
				<td class="subject"><%=StringUtils.blank(mn.getSubject())%></td>
				<td width="70px;">
					<div class="<%=next%>" title="#(message_window.1)"
						id="<%="next2".equals(next) && "pre2".equals(pre) ? "" : "_pre_1"%>"
						onclick="__message_view_click(this, <%=i + 1%>);"></div>
					<div class="<%=pre%>" title="#(message_window.2)"
						id="<%="next2".equals(next) && "pre2".equals(pre) ? "" : "_pre_0"%>"
						onclick="__message_view_click(this, <%=i - 1%>);"></div>

					<%
						if (mn instanceof SystemMessageNotification) {
					%>
					<script type="text/javascript">
$ready(function() {
	$Actions['deleteNotificationAct']
			('notificationId=<%=((SystemMessageNotification) mn).getId()%>');
});
</script>
					<%
						}
					%>
				</td>
			</tr>
		</table>
	</div>
	<div class="c"><%=StringUtils.blank(mn.getTextBody())%></div>
</div>
<script type="text/javascript">
function __message_view_click(obj, i) {
if (obj.hasClassName("next") || obj.hasClassName("pre")) {
			$Actions["<%=nComponentParameter.getBeanProperty("name")%>"].refreshContentRef("mIndex=" + i);
		}
	}

$ready(function(){
	var obj = $('messageWindow1');
	for(var i=0;i<5;i++){
		obj = obj.parentNode;
		if(obj){
			if(obj.className.indexOf('ui-window')!=-1){
				obj.style.position = 'fixed'; 
				obj.style.bottom = 0; 
				obj.style.right = 0; 
				break;
			}
		}else{
			break;
		}
	}
});
</script>
