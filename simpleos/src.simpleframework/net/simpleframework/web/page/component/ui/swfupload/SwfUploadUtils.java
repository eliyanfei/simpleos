package net.simpleframework.web.page.component.ui.swfupload;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.core.SimpleException;
import net.simpleframework.util.IConstants;
import net.simpleframework.util.JSONUtils;
import net.simpleframework.util.JavascriptUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.IMultipartFile;
import net.simpleframework.web.page.MultipartPageRequest;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ComponentRenderUtils;
import net.simpleframework.web.page.component.HandleException;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class SwfUploadUtils {
	public static final String BEAN_ID = "upload_@bid";

	public static ComponentParameter getComponentParameter(final PageRequestResponse requestResponse) {
		return ComponentParameter.get(requestResponse, BEAN_ID);
	}

	public static ComponentParameter getComponentParameter(final HttpServletRequest request,
			final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}

	public static String genJavascript(final ComponentParameter compParameter) {
		final SwfUploadBean swfUpload = (SwfUploadBean) compParameter.componentBean;
		final String beanId = swfUpload.hashId();
		final String homePath = swfUpload.getResourceHomePath(compParameter);
		final StringBuilder sb = new StringBuilder();
		final String actionFunc = swfUpload.getActionFunction();
		sb.append(actionFunc).append(".swf=").append("new SWFUpload({");
		sb.append("upload_url: \"").append(homePath).append("/jsp/swfupload_action.jsp;jsessionid=")
				.append(compParameter.getSession().getId()).append("?").append(BEAN_ID).append("=")
				.append(beanId).append("\",");
		sb.append("flash_url: \"").append(homePath).append("/flash/swfupload.swf\",");

		final String fileSizeLimit = (String) compParameter.getBeanProperty("fileSizeLimit");
		if (StringUtils.hasText(fileSizeLimit)) {
			sb.append("file_size_limit: \"").append(fileSizeLimit).append("\",");
		}
		sb.append("file_queue_limit: ").append(compParameter.getBeanProperty("fileQueueLimit"))
				.append(",");

		final String fileTypes = (String) compParameter.getBeanProperty("fileTypes");
		if (StringUtils.hasText(fileTypes)) {
			sb.append("file_types: \"").append(fileTypes).append("\",");
			sb.append("file_types_description: \"")
					.append(compParameter.getBeanProperty("fileTypesDesc")).append("\",");
		}

		sb.append("button_placeholder_id: \"placeholder_").append(beanId).append("\",");
		sb.append("button_window_mode: \"transparent\",");
		sb.append("button_width: \"110\",");
		sb.append("button_height: \"18\",");
		sb.append("button_cursor: SWFUpload.CURSOR.HAND,");
		sb.append("button_text: \"<span class='swf-button'>");
		sb.append(LocaleI18n.getMessage("SwfUploadUtils.0"));
		sb.append("<\\/span>\",");
		sb.append("button_text_style: \".swf-button { color: #014060; text-align: center; }\",");
		if (swfUpload.isMultiFileSelected()) {
			sb.append("button_action: SWFUpload.BUTTON_ACTION.SELECT_FILES,");
		} else {
			sb.append("button_action: SWFUpload.BUTTON_ACTION.SELECT_FILE,");
		}

		// file_queued_handler
		sb.append("file_queued_handler: function(file) {");
		sb.append("var queue = $(\"fileQueue_").append(beanId).append("\");");
		sb.append("var html =\"<div id='item_\" + file.id + \"' class='item simple_toolbar1'>");
		sb.append("<table width='100%' cellpadding='0' cellspacing='0'>");
		sb.append("<tr><td>");
		sb.append("<table width='100%' cellpadding='0' cellspacing='0'><tr>");
		sb.append("<td><span>\" + file.name + \"<\\/span>");
		sb.append("<span class='fs'>\" + file.size.toFileString() + \"<\\/span><\\/td>");
		sb.append("<td width='30px;' align='center'>");
		sb.append("<div class='delete_image'><\\/div>");
		sb.append("<\\/td>");
		sb.append("<\\/tr><\\/table>");
		sb.append("<\\/td><\\/tr>");
		sb.append("<tr><td>");
		sb.append("<div class='bar'><\\/div>");
		sb.append("<\\/td><\\/tr>");
		sb.append("<tr><td>");
		sb.append("<div class='message'><\\/div>");
		sb.append("<\\/td><\\/tr>");
		sb.append("<\\/table><\\/div>\";");
		sb.append("queue.insert(html);");
		sb.append("var item = $(\"item_\" + file.id);");
		sb.append("item.down(\".delete_image\").observe(\"click\", function(e) {");
		sb.append("var swf = ").append(actionFunc).append(".swf;");
		sb.append("var fo = swf.getFile(file.id); if (!fo) return;");
		sb.append("if (fo.filestatus == SWFUpload.FILE_STATUS.IN_PROGRESS && !confirm(\"");
		sb.append(LocaleI18n.getMessage("SwfUploadUtils.5")).append("\")) return;");
		sb.append("swf.cancelUpload(file.id);");
		sb.append("item.$remove();");
		sb.append("});");
		sb.append("item.bar = new $Comp.ProgressBar(item.down(\".bar\"), {");
		sb.append("maxProgressValue: file.size,");
		sb.append("startAfterCreate: false,");
		sb.append("showAbortAction: false,");
		sb.append("showDetailAction: false");
		sb.append("});");
		sb.append("},");

		// upload_progress_handler
		sb.append("upload_progress_handler: function(file, bytesComplete, bytesTotal) {");
		sb.append("var item = $(\"item_\" + file.id);");
		sb.append("if (item.bar) {");
		sb.append("item.bar.setProgress(bytesComplete);");
		sb.append("}");
		sb.append("if (bytesComplete >= bytesTotal) {");
		sb.append("item.down(\".message\").update(\"");
		sb.append(LocaleI18n.getMessage("SwfUploadUtils.1"));
		sb.append("\");}");
		sb.append("},");

		// file_queue_error_handler
		sb.append("file_queue_error_handler: function(file, errorCode, message) {");
		sb.append("var msgc = $(\"message_").append(beanId).append("\");");
		sb.append("if (errorCode == SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT) {");
		sb.append("msgc.update('\"' + file.name + '\" ")
				.append(
						JavascriptUtils.escape(LocaleI18n.getMessage("SwfUploadUtils.2", fileSizeLimit)))
				.append("');");
		sb.append("} else if (errorCode == SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE) {");
		sb.append("msgc.update(\"")
				.append(JavascriptUtils.escape(LocaleI18n.getMessage("SwfUploadUtils.3")))
				.append("\");");
		sb.append("} else if (errorCode == SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED) {");
		sb.append("msgc.update(\"")
				.append(JavascriptUtils.escape(LocaleI18n.getMessage("SwfUploadUtils.4")))
				.append("\");");
		sb.append("} else { msgc.update(message); }");
		sb.append("(function() {msgc.update(\"\");}).delay(2);");
		sb.append("},");

		// upload_error_handler
		sb.append("upload_error_handler: function(file, errorCode, message) {");
		sb.append("var msgc = $(\"message_").append(beanId).append("\");");
		sb.append("msgc.update(message);");
		sb.append("(function() {msgc.update(\"\");}).delay(2);");
		sb.append("},");

		// upload_success_handler
		sb.append("upload_success_handler: function(file, serverData, responseReceived) {");
		sb.append("var swf = ").append(actionFunc).append(".swf;");
		sb.append("var json = serverData.evalJSON();");
		sb.append("var message = $(\"item_\" + file.id).down(\".message\");");
		sb.append("if (json[\"error\"]) { message.update(json[\"error\"]); } else {");
		sb.append("message.update(\"");
		sb.append(LocaleI18n.getMessage("SwfUploadUtils.6")).append("\");");
		sb.append("(function() {message.update(\"\");}).delay(2); }");
		sb.append("var hasQueued = message.up(\".queue\").select(\".item\").any(function(item) {");
		sb.append("var fo = swf.getFile(item.id.substring(5));");
		sb.append("return fo && fo.filestatus == SWFUpload.FILE_STATUS.QUEUED;");
		sb.append("});");
		sb.append(StringUtils.blank(compParameter.getBeanProperty("jsCompleteCallback")));
		sb.append("},");

		// upload_complete_handler
		sb.append("upload_complete_handler: function(file) {");
		sb.append("var queue = $(\"fileQueue_").append(beanId).append("\");");
		sb.append("var swf = ").append(actionFunc).append(".swf;");
		sb.append("queue.select(\".item\").any(function(item) {");
		sb.append("var fo = swf.getFile(item.id.substring(5));");
		sb.append("if (fo && fo.filestatus == SWFUpload.FILE_STATUS.QUEUED) {");
		sb.append("swf.startUpload(fo.id); return true; }});");
		sb.append("}");

		sb.append("});");

		sb.append(actionFunc).append(".updateUI = function() {");
		sb.append("$$(\"#fileQueue_").append(beanId).append(" .item\").each(function(item) {");
		sb.append("if (item.bar) item.bar.updateUI();");
		sb.append("});");
		sb.append("};");

		final String selector = (String) compParameter.getBeanProperty("selector");
		if (StringUtils.hasText(selector)) {
			sb.append(actionFunc).append(".paramsObject = \"\".addSelectorParameter(\"");
			sb.append(selector).append("\").toQueryParams();");
		}
		return JavascriptUtils.wrapWhenReady(ComponentRenderUtils.wrapActionFunction(compParameter,
				sb.toString()));
	}

	public static String upload(final HttpServletRequest request, final HttpServletResponse response) {
		final HashMap<String, Object> json = new HashMap<String, Object>();
		final ComponentParameter nComponentParameter = getComponentParameter(request, response);
		final ISwfUploadHandle uHandle = (ISwfUploadHandle) nComponentParameter.getComponentHandle();
		if (uHandle != null) {
			try {
				request.setCharacterEncoding(IConstants.UTF8);
			} catch (final UnsupportedEncodingException e) {
			}
			nComponentParameter.request = PageUtils.pageContext.createMultipartPageRequest(request);
			final IMultipartFile multipartFile = ((MultipartPageRequest) nComponentParameter.request)
					.getFile("Filedata");
			try {
				uHandle.upload(nComponentParameter, multipartFile, json);
			} catch (final Throwable ex) {
				json.put("error", PageUtils.pageConfig.getThrowableMessage(SimpleException.getCause(
						HandleException.class, ex)));
			}
		}
		return JSONUtils.toJSON(json);
	}
}
