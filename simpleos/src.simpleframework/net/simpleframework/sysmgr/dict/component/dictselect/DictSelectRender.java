package net.simpleframework.sysmgr.dict.component.dictselect;

import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.IComponentJavascriptRender;
import net.simpleframework.web.page.component.IComponentRegistry;
import net.simpleframework.web.page.component.ui.listbox.ListboxBean;
import net.simpleframework.web.page.component.ui.listbox.ListboxRegistry;
import net.simpleframework.web.page.component.ui.tree.TreeBean;
import net.simpleframework.web.page.component.ui.tree.TreeRegistry;
import net.simpleframework.web.page.component.ui.window.WindowRender;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DictSelectRender extends WindowRender {
	public DictSelectRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getJavascriptCode(final ComponentParameter compParameter) {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.getJavascriptCode(compParameter));
		final AbstractComponentBean rComponent = (AbstractComponentBean) compParameter.componentBean
				.getAttribute("__componentBean");
		IComponentJavascriptRender jsRender = null;
		if (rComponent instanceof TreeBean) {
			jsRender = (IComponentJavascriptRender) AbstractComponentRegistry.getRegistry(
					TreeRegistry.tree).getComponentRender();
		} else if (rComponent instanceof ListboxBean) {
			jsRender = (IComponentJavascriptRender) AbstractComponentRegistry.getRegistry(
					ListboxRegistry.listbox).getComponentRender();
		}
		if (jsRender != null) {
			sb.append(jsRender.getJavascriptCode(ComponentParameter.get(compParameter, rComponent)));
		}
		return sb.toString();
	}
}
