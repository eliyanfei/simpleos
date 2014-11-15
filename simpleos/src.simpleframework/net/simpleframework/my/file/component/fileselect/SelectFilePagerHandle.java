package net.simpleframework.my.file.component.fileselect;

import java.util.Map;

import net.simpleframework.content.component.filepager.FileBean;
import net.simpleframework.my.file.AbstractMyFilePagerHandle;
import net.simpleframework.util.HTMLUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractTablePagerData;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class SelectFilePagerHandle extends AbstractMyFilePagerHandle {
	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("title".equals(beanProperty)) {
			return getPagerTitle(compParameter, "__my_folderfile_select");
		} else if ("containerId".equals(beanProperty)) {
			final ComponentParameter nComponentParameter = FileSelectUtils
					.getComponentParameter(compParameter);
			if (nComponentParameter.componentBean != null) {
				return "__my_folderfile_select_" + nComponentParameter.componentBean.hashId();
			}
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, FileSelectUtils.BEAN_ID);
		return parameters;
	}

	@Override
	public String getDownloadPath(final ComponentParameter compParameter, final FileBean fileBean) {
		final StringBuilder sb = new StringBuilder();
		sb.append(FileSelectUtils.getHomePath()).append("/jsp/dl.jsp?");
		sb.append(getIdParameterName(compParameter)).append("=").append(fileBean.getId());
		return sb.toString();
	}

	@Override
	public AbstractTablePagerData createTablePagerData(final ComponentParameter compParameter) {
		return new FileTablePagerData(compParameter) {
			@Override
			protected Map<Object, Object> getRowAttributes(final Object dataObject) {
				final Map<Object, Object> attributes = super.getRowAttributes(dataObject);
				final FileBean file = (FileBean) dataObject;
				String downloadUrl = getDownloadPath(compParameter, file);
				final ComponentParameter nComponentParameter = FileSelectUtils
						.getComponentParameter(compParameter);
				if (nComponentParameter.componentBean != null) {
					final String jobDownload = (String) nComponentParameter
							.getBeanProperty("jobDownload");
					if (StringUtils.hasText(jobDownload)) {
						downloadUrl = WebUtils.addParameters(downloadUrl, "job=" + jobDownload);
					}
				}
				if (StringUtils.hasText(downloadUrl)) {
					final StringBuilder sb = new StringBuilder();
					sb.append("<a href=\"");
					sb.append(FileSelectUtils.DOWNLOAD_FLAG).append(downloadUrl).append("\">");
					sb.append(StringUtils.text(file.getTopic(), file.getFilename())).append("</a>");
					attributes.put("download", HTMLUtils.htmlEscape(sb.toString()));
				}
				return attributes;
			}
		};
	}
}
