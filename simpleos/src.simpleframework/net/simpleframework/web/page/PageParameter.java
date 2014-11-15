package net.simpleframework.web.page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.util.script.IScriptEval;
import net.simpleframework.util.script.ScriptEvalFactory;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PageParameter extends PageRequestResponse {

	private IScriptEval scriptEval;

	public int start;

	public PageDocument pageDocument;

	public PageParameter(final HttpServletRequest request, final HttpServletResponse response, final PageDocument pageDocument) {
		super(request, response);
		this.pageDocument = pageDocument;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getStart() {
		return start;
	}

	public PageDocument getPageDocument() {
		return pageDocument;
	}

	public IScriptEval getScriptEval() {
		return scriptEval;
	}

	public IScriptEval createScriptEval() {
		scriptEval = ScriptEvalFactory.createDefaultScriptEval(PageUtils.createVariables(this));
		return scriptEval;
	}

	public AbstractComponentBean getComponentBean(final String componentName) {
		if (!StringUtils.hasText(componentName)) {
			return null;
		}
		final PageDocument document = getPageDocument();
		for (final AbstractComponentBean componentBean : document.getComponentBeans(PageParameter.get(this, document))) {
			final ComponentParameter nComponentParameter = ComponentParameter.get(this, componentBean);
			if (componentName.equals(nComponentParameter.getBeanProperty("name"))) {
				return componentBean;
			}
		}
		return null;
	}

	public IPageHandle getPageHandle() {
		return getPageDocument().getPageHandle();
	}

	public Object getBeanProperty(final String beanProperty) {
		final IPageHandle pageHandle = getPageHandle();
		if (pageHandle != null) {
			return pageHandle.getBeanProperty(this, beanProperty);
		} else {
			return BeanUtils.getProperty(pageDocument.getPageBean(), beanProperty);
		}
	}

	public static PageParameter get(final PageRequestResponse requestResponse, final PageDocument pageDocument) {
		return get(requestResponse.request, requestResponse.response, pageDocument);
	}

	public static PageParameter get(final HttpServletRequest request, final HttpServletResponse response, final PageDocument pageDocument) {
		final String key = "pageparameter_" + pageDocument.hashId();
		PageParameter nPageParameter = (PageParameter) request.getAttribute(key);
		if (nPageParameter == null) {
			request.setAttribute(key, nPageParameter = new PageParameter(request, response, pageDocument));
		}
		return nPageParameter;
	}
}
