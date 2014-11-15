package net.simpleframework.content.component.filepager;

import javax.servlet.ServletContext;

import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentResourceProvider;
import net.simpleframework.web.page.component.ui.pager.PagerRegistry;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class FilePagerRegistry extends PagerRegistry {
	public static final String filePager = "filePager";

	public FilePagerRegistry(final ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public String getComponentName() {
		return filePager;
	}

	@Override
	protected Class<? extends AbstractComponentBean> getBeanClass() {
		return FilePagerBean.class;
	}

	@Override
	protected Class<? extends AbstractComponentResourceProvider> getResourceProviderClass() {
		return FilePagerResourceProvider.class;
	}
}
