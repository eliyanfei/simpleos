package net.simpleframework.web.page.component;

import javax.servlet.http.HttpSession;

import net.simpleframework.util.StringUtils;
import net.simpleframework.util.script.IScriptEval;
import net.simpleframework.web.page.EScriptEvalType;
import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.SessionCache;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class ComponentBeanUtils {
	public static void evalComponentBean(final PageParameter pageParameter) {
		final PageDocument pageDocument = pageParameter.getPageDocument();
		if (pageDocument.isFirstCreated() || pageDocument.getPageBean().getScriptEval() != EScriptEvalType.multiple) {
			return;
		}

		final IScriptEval scriptEval = pageParameter.createScriptEval();
		final String scriptInit = pageDocument.getScriptInit(pageParameter);
		if (StringUtils.hasText(scriptInit)) {
			scriptEval.eval(scriptInit);
		}

		final HttpSession httpSession = pageParameter.getSession();
		for (final AbstractComponentBean componentBean : pageDocument.getComponentBeans(pageParameter)) {
			final Element element = componentBean.getElement();
			if (element == null) {
				continue;
			}
			cacheComponentBean(httpSession, componentBean.getComponentRegistry().createComponentBean(pageParameter, element));
		}
	}

	static void cacheComponentBean(final HttpSession httpSession, final AbstractComponentBean componentBean) {
		SessionCache.put(httpSession, componentBean.hashId(), componentBean);
	}

	public static AbstractComponentBean getComponentBeanInCache(final HttpSession httpSession, final String hashId) {
		return (AbstractComponentBean) SessionCache.get(httpSession, hashId);
	}

	static AbstractComponentBean getComponentBeanByHashId(final PageRequestResponse requestResponse, final String hashId) {
		if (hashId == null) {
			return null;
		}
		final AbstractComponentBean componentBean2 = getComponentBeanInCache(requestResponse.getSession(), hashId);
		if (componentBean2 != null) {
			return componentBean2;
		}
		return AbstractComponentBean.allComponents.get(hashId);
	}

	public static AbstractComponentBean getRunningComponentBean(final HttpSession httpSession, final AbstractComponentBean componentBean) {
		final AbstractComponentBean componentBean2 = getComponentBeanInCache(httpSession, componentBean.hashId());
		return componentBean2 != null ? componentBean2 : componentBean;
	}

	public static String getResourceHomePath(final AbstractComponentBean componentBean) {
		return AbstractComponentRegistry.getRegistry(componentBean.getName()).getComponentResourceProvider().getResourceHomePath();
	}

	public static String getCssResourceHomePath(final ComponentParameter cp) {
		return AbstractComponentRegistry.getRegistry(cp.componentBean.getName()).getComponentResourceProvider().getCssResourceHomePath(cp);
	}

}
