package net.simpleframework.web.page.component.ui.listbox;

import java.util.Iterator;

import javax.servlet.ServletContext;

import net.simpleframework.util.script.IScriptEval;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.AbstractComponentRender;
import net.simpleframework.web.page.component.AbstractComponentResourceProvider;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ListboxRegistry extends AbstractComponentRegistry {
	public static final String listbox = "listbox";

	public ListboxRegistry(final ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public String getComponentName() {
		return listbox;
	}

	@Override
	protected Class<? extends AbstractComponentBean> getBeanClass() {
		return ListboxBean.class;
	}

	@Override
	protected Class<? extends AbstractComponentRender> getRenderClass() {
		return ListboxRender.class;
	}

	@Override
	protected Class<? extends AbstractComponentResourceProvider> getResourceProviderClass() {
		return ListboxResourceProvider.class;
	}

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pageParameter,
			final Element component) {
		final ListboxBean listboxBean = (ListboxBean) super.createComponentBean(pageParameter,
				component);
		final IScriptEval scriptEval = pageParameter.getScriptEval();
		final Iterator<?> it = component.elementIterator("item");
		while (it.hasNext()) {
			final Element element = (Element) it.next();
			final ListItem item = new ListItem(element, listboxBean, null);
			item.parseElement(scriptEval);
			listboxBean.getListItems().add(item);
		}
		return listboxBean;
	}
}
