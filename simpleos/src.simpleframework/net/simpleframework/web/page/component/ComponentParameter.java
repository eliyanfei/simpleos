package net.simpleframework.web.page.component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.util.BeanUtils;
import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.PageRequestResponse;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public final class ComponentParameter extends PageParameter {
	public AbstractComponentBean componentBean;

	public ComponentParameter(final HttpServletRequest request, final HttpServletResponse response,
			final AbstractComponentBean componentBean) {
		super(request, response, componentBean != null ? componentBean.getPageDocument() : null);
		this.componentBean = componentBean;
	}

	@Override
	public PageDocument getPageDocument() {
		if (pageDocument == null) {
			pageDocument = componentBean.getPageDocument();
		}
		return pageDocument;
	}

	public IComponentHandle getComponentHandle() {
		return componentBean != null ? componentBean.getComponentHandle(this) : null;
	}

	@Override
	public Object getBeanProperty(final String beanProperty) {
		final IComponentHandle handle = getComponentHandle();
		if (handle != null) {
			return handle.getBeanProperty(this, beanProperty);
		} else {
			return BeanUtils.getProperty(componentBean, beanProperty);
		}
	}

	public static ComponentParameter get(final PageRequestResponse requestResponse,
			final AbstractComponentBean componentBean) {
		return get(requestResponse.request, requestResponse.response, componentBean);
	}

	public static ComponentParameter get(final PageRequestResponse requestResponse,
			final String beanId) {
		return get(requestResponse.request, requestResponse.response, beanId);
	}

	public static ComponentParameter get(final HttpServletRequest request,
			final HttpServletResponse response, final String beanId) {
		return get(request, response, AbstractComponentBean.getComponentBeanByRequestId(
				new PageRequestResponse(request, response), beanId));
	}

	public static ComponentParameter get(final HttpServletRequest request,
			final HttpServletResponse response, final AbstractComponentBean componentBean) {
		if (componentBean == null) {
			return new ComponentParameter(request, response, null);
		}
		final String key = "componentparameter_" + componentBean.hashId();
		ComponentParameter nComponentParameter = (ComponentParameter) request.getAttribute(key);
		if (nComponentParameter == null) {
			request.setAttribute(key, nComponentParameter = new ComponentParameter(request, response,
					componentBean));
		}
		nComponentParameter.request = request;
		nComponentParameter.response = response;
		return nComponentParameter;
	}

	public String getComponentName() {
		return componentBean.getName();
	}
}
