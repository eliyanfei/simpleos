package net.simpleframework.web.page.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.IPageResourceProvider;
import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.ComponentRegistryFactory;
import net.simpleframework.web.page.component.IComponentRegistry;
import net.simpleframework.web.page.component.IComponentResourceProvider;

import org.jsoup.nodes.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ResourceBinding extends AbstractParser {

	public void doTag(final PageParameter pageParameter, final Element htmlHead, final Collection<AbstractComponentBean> componentBeans) {
		final PageDocument pageDocument = pageParameter.getPageDocument();

		final IPageResourceProvider prp = pageDocument.getPageResourceProvider();
		final String pageHome = pageParameter.wrapContextPath(prp.getResourceHomePath());

		if (pageParameter.isHttpRequest()) {
			// base
			final String javascriptCode = prp.getInitJavascriptCode(pageParameter);
			if (StringUtils.hasText(javascriptCode)) {
				ParserUtils.addScriptText(htmlHead, javascriptCode);
			}

			String[] jsArr, cssArr;
			jsArr = prp.getJavascriptPath(pageParameter, componentBeans);
			if (jsArr != null) {
				for (final String js : jsArr) {
					ParserUtils.addScriptSRC(pageParameter, htmlHead, pageHome + js);
				}
			}

			cssArr = prp.getCssPath(pageParameter, componentBeans);
			if (cssArr != null) {
				for (final String css : cssArr) {
					ParserUtils.addStylesheet(pageParameter, htmlHead, pageHome + css);
				}
			}
		}

		// page
		final Collection<String> jsColl = pageDocument.getImportJavascript(pageParameter);
		if (jsColl != null) {
			for (String js : jsColl) {
				if (!js.startsWith("/")) {
					js = pageHome + "/" + js;
				} else {
					js = pageParameter.wrapContextPath(js);
				}
				ParserUtils.addScriptSRC(pageParameter, htmlHead, js);
			}
		}

		final Collection<String> cssColl = pageDocument.getImportCSS(pageParameter);
		if (cssColl != null) {
			for (String css : cssColl) {
				if (!css.startsWith("/")) {
					css = pageHome + "/" + css;
				} else {
					css = pageParameter.wrapContextPath(css);
				}
				ParserUtils.addStylesheet(pageParameter, htmlHead, css);
			}
		}
		// component
		final ComponentRegistryFactory factory = ComponentRegistryFactory.getInstance();

		final LinkedHashMap<String, Collection<AbstractComponentBean>> components = new LinkedHashMap<String, Collection<AbstractComponentBean>>();
		for (final AbstractComponentBean componentBean : componentBeans) {
			final String key = componentBean.getComponentRegistry().getComponentName();
			Collection<AbstractComponentBean> coll = components.get(key);
			if (coll == null) {
				components.put(key, coll = new ArrayList<AbstractComponentBean>());
			}
			coll.add(componentBean);
		}

		for (final Map.Entry<String, Collection<AbstractComponentBean>> entry : components.entrySet()) {
			final IComponentRegistry registry = factory.getComponentRegistry(entry.getKey());
			final IComponentResourceProvider crp = registry.getComponentResourceProvider();
			if (crp == null) {
				continue;
			}

			final String[] dependents = crp.getDependentComponents(pageParameter, entry.getValue());
			if (dependents != null && dependents.length > 0) {
				for (final String dependent : dependents) {
					final IComponentRegistry registry2 = factory.getComponentRegistry(dependent);
					if (registry2 == null) {
						continue;
					}
					final IComponentResourceProvider crp2 = registry2.getComponentResourceProvider();
					if (crp2 == null) {
						continue;
					}
					doComponentResource(htmlHead, pageParameter, crp2, pageHome, components.get(dependent));
				}
			}
			doComponentResource(htmlHead, pageParameter, crp, pageHome, entry.getValue());
		}
	}

	private void doComponentResource(final Element htmlHead, final PageParameter pageParameter, final IComponentResourceProvider crp,
			final String pageHome, final Collection<AbstractComponentBean> componentBeans) {
		String[] jsArr = crp.getPageJavascriptPath(pageParameter, componentBeans);
		if (jsArr != null) {
			for (final String js : jsArr) {
				ParserUtils.addScriptSRC(pageParameter, htmlHead, pageHome + js);
			}
		}

		String[] cssArr = crp.getPageCssPath(pageParameter, componentBeans);
		if (cssArr != null) {
			for (final String css : cssArr) {
				ParserUtils.addStylesheet(pageParameter, htmlHead, pageHome + css);
			}
		}

		final String componentHome = pageParameter.wrapContextPath(crp.getResourceHomePath());
		jsArr = crp.getJavascriptPath(pageParameter, componentBeans);
		if (jsArr != null) {
			for (final String js : jsArr) {
				ParserUtils.addScriptSRC(pageParameter, htmlHead, componentHome + js);
			}
		}

		cssArr = crp.getCssPath(pageParameter, componentBeans);
		if (cssArr != null) {
			for (final String css : cssArr) {
				ParserUtils.addStylesheet(pageParameter, htmlHead, componentHome + css);
			}
		}
	}
}
