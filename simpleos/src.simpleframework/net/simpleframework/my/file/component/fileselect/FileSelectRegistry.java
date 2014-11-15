package net.simpleframework.my.file.component.fileselect;

import javax.servlet.ServletContext;

import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.AbstractComponentRender;
import net.simpleframework.web.page.component.AbstractComponentResourceProvider;
import net.simpleframework.web.page.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.web.page.component.ui.dictionary.DictionaryUtils;
import net.simpleframework.web.page.component.ui.window.WindowRender;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class FileSelectRegistry extends AbstractComponentRegistry {
	public static final String myFileSelect = "myFileSelect";

	public FileSelectRegistry(final ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public String getComponentName() {
		return myFileSelect;
	}

	@Override
	protected Class<? extends AbstractComponentBean> getBeanClass() {
		return FileSelectBean.class;
	}

	@Override
	protected Class<? extends AbstractComponentRender> getRenderClass() {
		return WindowRender.class;
	}

	@Override
	protected Class<? extends AbstractComponentResourceProvider> getResourceProviderClass() {
		return FileSelectResourceProvider.class;
	}

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pageParameter,
			final Element component) {
		final FileSelectBean fileSelect = (FileSelectBean) super.createComponentBean(pageParameter,
				component);

		final AjaxRequestBean ajaxRequest = DictionaryUtils.createAjaxRequest(fileSelect);
		final StringBuilder sb = new StringBuilder();
		sb.append(getComponentResourceProvider().getResourceHomePath());
		sb.append("/jsp/folder_file_select.jsp?").append(FileSelectUtils.BEAN_ID).append("=")
				.append(fileSelect.hashId());
		ajaxRequest.setUrlForward(sb.toString());
		return fileSelect;
	}
}
