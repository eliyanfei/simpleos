package net.simpleframework.web.page;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletContext;

import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.AbstractComponentBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractResourceProvider extends AbstractPageSupport implements
		IResourceProvider, IHomePathAware {

	public AbstractResourceProvider(final ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public String[] getCssPath(final PageRequestResponse requestResponse,
			final Collection<AbstractComponentBean> componentBeans) {
		return null;
	}

	@Override
	public String[] getJavascriptPath(final PageRequestResponse requestResponse,
			final Collection<AbstractComponentBean> componentBeans) {
		return null;
	}

	@Override
	public String[] getJarPath() {
		return null;
	}

	protected final static String RESOURCE_NAME = "resource.zip";

	@Override
	public ZipInputStream getRequiredResource() throws IOException {
		return getZipInputStream(BeanUtils.getResourceClasspath(getClass(), RESOURCE_NAME));
	}

	@Override
	public IHomePathAware getHomePathAware() {
		return this;
	}

	protected ZipInputStream getZipInputStream(final String classpath) {
		final InputStream inputStream = AbstractResourceProvider.class.getClassLoader()
				.getResourceAsStream(classpath);
		return inputStream == null ? null : new ZipInputStream(inputStream);
	}

	protected String getCssSkin(final PageRequestResponse requestResponse, final String cssFile,
			final String ver) {
		final StringBuilder sb = new StringBuilder();
		sb.append("/css/").append(getCurrentSkin(requestResponse));
		if (StringUtils.hasText(cssFile)) {
			sb.append("/").append(cssFile);
			if (StringUtils.hasText(ver)) {
				sb.append("?v=").append(ver);
			}
		}
		return sb.toString();
	}

	protected String getCssSkin(final PageRequestResponse requestResponse, final String cssFile) {
		return getCssSkin(requestResponse, cssFile, null);
	}

	@Override
	public String getResourceHomePath(final PageRequestResponse requestResponse) {
		return requestResponse.wrapContextPath(getResourceHomePath());
	}

	@Override
	public String getCssResourceHomePath(final PageRequestResponse requestResponse) {
		return getResourceHomePath(requestResponse) + getCssSkin(requestResponse, null);
	}

	@Override
	public String getCurrentSkin(final PageRequestResponse requestResponse) {
		return SkinUtils.getSkin(requestResponse, getSkin());
	}

	private String skin;

	@Override
	public String getSkin() {
		return skin;
	}

	@Override
	public void setSkin(final String skin) {
		this.skin = skin;
	}
}
