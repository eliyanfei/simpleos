package net.simpleframework.workflow.web.component.modellist;

import javax.servlet.ServletContext;

import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentResourceProvider;
import net.simpleframework.web.page.component.ui.pager.PagerRegistry;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ModelListRegistry extends PagerRegistry {
	public static final String modellist = "wf_modellist";

	public ModelListRegistry(final ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public String getComponentName() {
		return modellist;
	}

	@Override
	protected Class<? extends AbstractComponentBean> getBeanClass() {
		return ModelListBean.class;
	}

	@Override
	protected Class<? extends AbstractComponentResourceProvider> getResourceProviderClass() {
		return ModelListResourceProvider.class;
	}

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pageParameter,
			final Element component) {
		final ModelListBean modellist = (ModelListBean) super.createComponentBean(pageParameter,
				component);
		return modellist;
	}
}
