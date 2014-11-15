package net.simpleframework.web.page.component.ui.dictionary;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.util.JavascriptUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.web.page.component.ui.dictionary.DictionaryBean.AbstractDictionaryTypeBean;
import net.simpleframework.web.page.component.ui.dictionary.DictionaryBean.DictionaryFontBean;
import net.simpleframework.web.page.component.ui.dictionary.DictionaryBean.DictionaryListBean;
import net.simpleframework.web.page.component.ui.dictionary.DictionaryBean.DictionaryTreeBean;
import net.simpleframework.web.page.component.ui.listbox.ListboxBean;
import net.simpleframework.web.page.component.ui.tree.TreeBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class DictionaryUtils {
	public static final String BEAN_ID = "dictionary_@bid";

	public static ComponentParameter getComponentParameter(final PageRequestResponse requestResponse) {
		return ComponentParameter.get(requestResponse, BEAN_ID);
	}

	public static ComponentParameter getComponentParameter(final HttpServletRequest request,
			final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}

	public static AjaxRequestBean createAjaxRequest(final DictionaryBean dictionaryBean) {
		final String ajaxRequestName = "ajaxRequest_" + dictionaryBean.hashId();
		dictionaryBean.setContentRef(ajaxRequestName);
		dictionaryBean.setContent(AbstractComponentRegistry.getLoadingContent());
		final PageDocument pageDocument = dictionaryBean.getPageDocument();
		final AjaxRequestBean ajaxRequest = new AjaxRequestBean(pageDocument, null);
		ajaxRequest.setName(ajaxRequestName);
		ajaxRequest.setShowLoading(false);
		ajaxRequest.setHandleClass(DictionaryAction.class.getName());
		ajaxRequest.setParameters(DictionaryUtils.BEAN_ID + "=" + dictionaryBean.hashId());
		pageDocument.addComponentBean(ajaxRequest);
		return ajaxRequest;
	}

	public static String genSelectCallback(final ComponentParameter compParameter,
			final String selects) {
		return genSelectCallback(compParameter, selects, ", ", false);
	}

	public static String genSelectCallback(final ComponentParameter compParameter,
			final String selects, final String sep, final boolean insert) {
		final StringBuilder sb = new StringBuilder();
		final String actionName = (String) compParameter.getBeanProperty("name");

		final String callback = (String) compParameter.getBeanProperty("jsSelectCallback");
		if (StringUtils.hasText(callback)) {
			sb.append("var b = ").append(JavascriptUtils.wrapFunction(callback));
			sb.append("; if (b) { $Actions['").append(actionName);
			sb.append("'].close(); }");
		} else {
			final String id = (String) compParameter.getBeanProperty("bindingId");
			if (StringUtils.hasText(id)) {
				sb.append("$Actions.setValue($Actions['").append(actionName)
						.append("'].bindingId || '").append(id);
				sb.append("', ").append(selects).append(".map(function(m) { return m.id; })");
				sb.append(".join('").append(sep).append("'));");
			}
			final String text = (String) compParameter.getBeanProperty("bindingText");
			if (StringUtils.hasText(text)) {
				sb.append("$Actions.setValue($Actions['").append(actionName)
						.append("'].bindingText || '").append(text);
				sb.append("', ").append(selects).append(".map(function(m) { return m.text; })");
				sb.append(".join('").append(sep).append("'), ").append(insert).append(");");
			}
			sb.append("if (").append(selects).append(".length > 0) { $Actions['");
			sb.append(actionName).append("'].close(); }");
		}
		return sb.toString();
	}

	public static boolean isMultiple(final ComponentParameter compParameter) {
		final AbstractDictionaryTypeBean dictionaryType = ((DictionaryBean) compParameter.componentBean)
				.getDictionaryTypeBean();
		if (dictionaryType instanceof DictionaryTreeBean) {
			final TreeBean treeBean = (TreeBean) compParameter
					.getComponentBean(((DictionaryTreeBean) dictionaryType).getRef());
			return treeBean.isCheckboxes();
		} else if (dictionaryType instanceof DictionaryListBean) {
			final ListboxBean listBean = (ListboxBean) compParameter
					.getComponentBean(((DictionaryListBean) dictionaryType).getRef());
			return listBean.isCheckbox();
		} else if (dictionaryType instanceof DictionaryFontBean) {
			return true;
		}
		return false;
	}

	public static String getActions(final ComponentParameter compParameter) {
		final DictionaryBean dictionaryBean = (DictionaryBean) compParameter.componentBean;

		final ArrayList<String> al = new ArrayList<String>();
		final String hashThis = dictionaryBean.hashId();
		final String clearAction = dictionaryBean.getClearAction(compParameter);
		if (StringUtils.hasText(clearAction)) {
			final StringBuilder sb = new StringBuilder();
			sb.append("<a onclick=\"clear_").append(hashThis).append("();\">#(Clear)</a>");
			sb.append("<script type=\"text/javascript\">");
			sb.append("function clear_").append(hashThis).append("() {");
			sb.append(clearAction).append("}");
			sb.append("</script>");
			al.add(sb.toString());
		}
		final String refreshAction = dictionaryBean.getRefreshAction(compParameter);
		if (StringUtils.hasText(refreshAction)) {
			final StringBuilder sb = new StringBuilder();
			sb.append("<a onclick=\"refresh_").append(hashThis).append("();\">#(Refresh)</a>");
			sb.append("<script type=\"text/javascript\">");
			sb.append("function refresh_").append(hashThis).append("() {");
			sb.append(refreshAction).append("}");
			sb.append("</script>");
			al.add(sb.toString());
		}
		return al.size() > 0 ? StringUtils.join(al, HTMLBuilder.SEP) : "";
	}

	public static String fontSelected(final ComponentParameter compParameter) {
		final DictionaryBean dictionaryBean = (DictionaryBean) compParameter.componentBean;

		final StringBuilder sb = new StringBuilder();
		sb.append("function selected").append(dictionaryBean.hashId()).append("() {");
		sb.append("var v = {");
		sb.append("'font-family': $F('dict_font_family'),");
		sb.append("'font-style': $F('dict_font_style'),");
		sb.append("'font-weight': $F('dict_font_weight'),");
		sb.append("'font-size': $F('dict_font_size'),");
		sb.append("'color': $F('dict_font_color')");
		sb.append("};");
		sb.append("v.toCSS = function() {var a = [];");
		sb.append("Object.keys(v).each(function(k) {var v2 = v[k];");
		sb.append("if (Object.isString(v2) && v2 != '') a.push(k+':'+v2);");
		sb.append("});");
		sb.append("return a.join(';');");
		sb.append("};");
		sb.append("if((function(value) {");
		final String callback = dictionaryBean.getJsSelectCallback();
		final String binding = StringUtils.text(dictionaryBean.getBindingId(),
				dictionaryBean.getBindingText());
		if (StringUtils.hasText(callback)) {
			sb.append(callback);
		} else if (StringUtils.hasText(binding)) {
			sb.append("var b = $('").append(binding).append("');");
			sb.append("if (b) b.value = value.toCSS();");
			sb.append("return true;");
		}
		sb.append("})(v))");
		sb.append("$Actions['").append(compParameter.getBeanProperty("name")).append("'].close();");
		sb.append("}");
		sb.append("function _dict_fontEditor_loaded() {");
		if (StringUtils.hasText(binding)) {
			sb.append("var b = $('").append(binding).append("');");
			sb.append("if (!b) return;");
			sb.append("var m = { 'font-family': 'dict_font_family', ");
			sb.append("'font-style': 'dict_font_style', ");
			sb.append("'font-weight': 'dict_font_weight', ");
			sb.append("'font-size': 'dict_font_size' };");
			sb.append("$A($F(b).split(';')).each(function(c) {");
			sb.append("var v = c.split(':');if(v.length != 2) return;");
			sb.append("if (v[0] == 'color') {");
			sb.append("$('dict_font_color').setValue(v[1]);");
			sb.append("$('d_dict_font_color').setStyle('background-color:' + v[1]);");
			sb.append("} else {");
			sb.append("$(m[v[0]]).setValue(v[1]);");
			sb.append("}});");
		}
		sb.append("}");
		return sb.toString();
	}

	public static String getHomePath() {
		return AbstractComponentRegistry.getRegistry(DictionaryRegistry.dictionary)
				.getResourceHomePath();
	}
}
