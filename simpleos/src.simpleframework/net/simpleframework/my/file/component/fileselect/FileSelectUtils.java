package net.simpleframework.my.file.component.fileselect;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.content.component.filepager.FileBean;
import net.simpleframework.content.component.filepager.FilePagerBean;
import net.simpleframework.content.component.filepager.IFilePagerHandle;
import net.simpleframework.util.AlgorithmUtils;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class FileSelectUtils {
	public static final String DOWNLOAD_FLAG = "##download_";

	public static final String BEAN_ID = "fileselect_@bid";

	public static ComponentParameter getComponentParameter(final PageRequestResponse requestResponse) {
		return ComponentParameter.get(requestResponse, BEAN_ID);
	}

	public static ComponentParameter getComponentParameter(final HttpServletRequest request,
			final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}

	public static FilePagerBean getFilePager(final PageRequestResponse requestResponse) {
		return (FilePagerBean) AbstractComponentBean.getComponentBeanByName(requestResponse,
				FileSelectUtils.xmlDownload(), "__my_folderfile_select");
	}

	public static String decodeDownloadParameter(final HttpServletRequest request) {
		final byte[] dl = AlgorithmUtils.base64Decode(request.getParameter("dl"));
		return dl == null ? "" : new String(dl);
	}

	public static FileBean getFileBean(final PageRequestResponse requestResponse) {
		return getFileBean(requestResponse,
				WebUtils.toQueryParams(decodeDownloadParameter(requestResponse.request)));
	}

	public static FileBean getFileBean(final PageRequestResponse requestResponse,
			final Map<String, Object> params) {
		final ComponentParameter nComponentParameter = ComponentParameter.get(requestResponse,
				FileSelectUtils.getFilePager(requestResponse));
		final IFilePagerHandle fHandle = (IFilePagerHandle) nComponentParameter.getComponentHandle();
		return fHandle.getEntityBeanById(nComponentParameter,
				params.get(fHandle.getIdParameterName(nComponentParameter)));
	}

	public static String getHomePath() {
		return AbstractComponentRegistry.getRegistry(FileSelectRegistry.myFileSelect)
				.getResourceHomePath();
	}

	public static String jspTooltip() {
		return getHomePath() + "/jsp/file_tooltip.jsp";
	}

	public static String xmlDownload() {
		return getHomePath() + "/jsp/__dl.xml";
	}
}
