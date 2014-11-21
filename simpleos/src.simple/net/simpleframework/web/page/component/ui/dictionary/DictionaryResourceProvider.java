package net.simpleframework.web.page.component.ui.dictionary;

import java.util.Collection;

import net.simpleframework.util.LangUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentResourceProvider;
import net.simpleframework.web.page.component.IComponentRegistry;
import net.simpleframework.web.page.component.ui.dictionary.DictionaryBean.AbstractDictionaryTypeBean;
import net.simpleframework.web.page.component.ui.dictionary.DictionaryBean.DictionaryListBean;
import net.simpleframework.web.page.component.ui.dictionary.DictionaryBean.DictionaryTreeBean;
import net.simpleframework.web.page.component.ui.listbox.ListboxRegistry;
import net.simpleframework.web.page.component.ui.tooltip.TooltipRegistry;
import net.simpleframework.web.page.component.ui.tree.TreeRegistry;
import net.simpleframework.web.page.component.ui.window.WindowRegistry;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DictionaryResourceProvider extends AbstractComponentResourceProvider {

	public DictionaryResourceProvider(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String[] getDependentComponents(final PageRequestResponse requestResponse,
			final Collection<AbstractComponentBean> componentBeans) {
		String[] arr = new String[] { WindowRegistry.window };
		boolean tree = false, list = false, tooltip = false;
		for (final AbstractComponentBean componentBean : componentBeans) {
			if (componentBean instanceof DictionaryBean) {
				final DictionaryBean dict = (DictionaryBean) componentBean;
				tooltip = dict.isShowHelpTooltip();
				final AbstractDictionaryTypeBean dictType = dict.getDictionaryTypeBean();
				if (dictType instanceof DictionaryTreeBean) {
					tree = true;
				} else if (dictType instanceof DictionaryListBean) {
					list = true;
				}
			}
		}
		if (tree) {
			arr = (String[]) LangUtils.add(arr, TreeRegistry.tree);
		}
		if (list) {
			arr = (String[]) LangUtils.add(arr, ListboxRegistry.listbox);
		}
		if (tooltip) {
			arr = (String[]) LangUtils.add(arr, TooltipRegistry.tooltip);
		}
		return arr;
	}

	@Override
	public String[] getPageCssPath(final PageRequestResponse requestResponse,
			final Collection<AbstractComponentBean> componentBeans) {
		return new String[] { "/" + DictionaryRegistry.dictionary
				+ getCssSkin(requestResponse, "dictionary.css") };
	}
}
