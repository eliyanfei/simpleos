package net.simpleframework.web.page.component.ui.portal.module;

import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.IConstants;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.portal.PageletBean;
import net.simpleframework.web.page.component.ui.portal.PortalUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractPortalModuleHandle implements IPortalModuleHandle {
	private final PageletBean pagelet;

	public AbstractPortalModuleHandle(final PageletBean pagelet) {
		this.pagelet = pagelet;

		final String defaultOptions = StringUtils.join(getDefaultOptions(), IConstants.NEWLINE);
		if (!StringUtils.hasText(pagelet.getOptions())) {
			pagelet.setOptions(defaultOptions);
		} else {
			Properties properties = ConvertUtils.toProperties(defaultOptions);
			if (properties == null) {
				properties = new Properties();
			}
			properties.putAll(pagelet.getOptionProperties());
			pagelet.setOptions(ConvertUtils.toString(properties));
		}
	}

	protected abstract String[] getDefaultOptions();

	@Override
	public PortalModule getModuleBean() {
		return PortalModuleRegistryFactory.getInstance().getModule(pagelet.getModule());
	}

	@Override
	public PageletBean getPagelet() {
		return pagelet;
	}

	@Override
	public IForward getPageletOptionContent(final ComponentParameter compParameter) {
		return null;
	}

	@Override
	public void optionSave(final ComponentParameter compParameter) {
		final PageletBean pagelet = getPagelet();
		final Properties properties = pagelet.getOptionProperties();
		if (properties == null) {
			return;
		}
		final Enumeration<?> names = properties.propertyNames();
		while (names.hasMoreElements()) {
			final String name = (String) names.nextElement();
			properties.setProperty(name, StringUtils.blank(compParameter.getRequestParameter(name)));
		}
		pagelet.setOptions(ConvertUtils.toString(properties));
		PortalUtils.savePortal(compParameter);
	}

	@Override
	public void optionLoaded(final PageParameter pageParameter, final Map<String, Object> dataBinding) {
		final PageletBean pagelet = getPagelet();
		final Properties properties = pagelet.getOptionProperties();
		if (properties == null) {
			return;
		}
		for (final Map.Entry<Object, Object> entry : properties.entrySet()) {
			dataBinding.put((String) entry.getKey(), entry.getValue());
		}
	}

	@Override
	public OptionWindowUI getPageletOptionUI(final ComponentParameter compParameter) {
		return new OptionWindowUI(getPagelet());
	}

	@Override
	public String getOptionUITitle(final ComponentParameter compParameter) {
		return null;
	}

	protected String getResourceHomePath() {
		return pagelet.getColumnBean().getPortalBean().getResourceHomePath();
	}
}
