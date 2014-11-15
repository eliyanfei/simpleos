package net.simpleframework.web.page.component.ui.portal;

import java.util.Map;

import net.simpleframework.web.page.ReflectUtils;
import net.simpleframework.web.page.component.AbstractComponentHandle;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractPortalHandle extends AbstractComponentHandle implements IPortalHandle {
	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("jobManager".equals(beanProperty)) {
			return ReflectUtils.jobManager;
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, PortalUtils.BEAN_ID);
		return parameters;
	}
}
