<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.dictionary.DictionaryBean"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.dictionary.DictionaryBean.DictionaryColorBean"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.colorpalette.ColorPaletteBean"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.colorpalette.ColorPaletteRender"%>
<%@ page import="net.simpleframework.util.StringUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ui.dictionary.DictionaryUtils"%>
<%@ page
	import="net.simpleframework.web.page.component.ComponentParameter"%>
<%
	final ComponentParameter nComponentParameter = DictionaryUtils
			.getComponentParameter(request, response);
	DictionaryBean dictionaryBean = (DictionaryBean) nComponentParameter.componentBean;
	final String beanId = dictionaryBean.hashId();
	final DictionaryColorBean color = (DictionaryColorBean) dictionaryBean
			.getDictionaryTypeBean();
	final ColorPaletteBean colorPalette = (ColorPaletteBean) nComponentParameter
			.getComponentBean(color.getRef());
	final ColorPaletteRender r = (ColorPaletteRender) colorPalette
			.getComponentRegistry().getComponentRender();
%>

<div class="dictionary" style="padding: 0px;">
	<div id="color<%=beanId%>"><%=r.getResponseText(ComponentParameter.get(request,
					response, colorPalette))%></div>
	<div style="text-align: right;">
		<input type="button" value="#(Button.Cancel)"
			onclick="$Actions['<%=nComponentParameter.getBeanProperty("name")%>'].close();" />
	</div>
</div>
<script type="text/javascript">
	change_<%=beanId%> = function(value) {
	  <%StringBuilder sb = new StringBuilder();
			final String callback = dictionaryBean.getJsSelectCallback();
			String binding = StringUtils.text(dictionaryBean.getBindingId(),
					dictionaryBean.getBindingText());
			if (StringUtils.hasText(callback)) {
				sb.append(callback);
			} else {
				if (StringUtils.hasText(binding)) {
					sb.append("var b = $('").append(binding).append("');");
					sb.append("if (b) b.value = '#' + value;");
				}
			}
			out.write(sb.toString());%>
	};

	$ready(function() {
		var f = $Actions['<%=color.getRef()%>'];
		<%if (StringUtils.hasText(binding)) {
				sb.setLength(0);
				sb.append("var b = $('").append(binding).append("');");
				sb.append("if (b && b.value != '') { f.options = { startHex : ");
				sb.append("b.value.startsWith('#') ? b.value.substring(1) : b.value");
				sb.append("}; }");
				out.write(sb.toString());
			}%>
    f();    
	});
</script>


