package net.simpleframework.sysmgr.dict.component.dictselect;

import javax.servlet.ServletContext;

import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentRender;
import net.simpleframework.web.page.component.ui.dictionary.DictionaryRegistry;
import net.simpleframework.web.page.component.ui.listbox.ListboxBean;
import net.simpleframework.web.page.component.ui.tree.TreeBean;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DictSelectRegistry extends DictionaryRegistry {
	public static final String dictSelect = "dictSelect";

	public DictSelectRegistry(final ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public String getComponentName() {
		return dictSelect;
	}

	@Override
	protected Class<? extends AbstractComponentBean> getBeanClass() {
		return DictSelectBean.class;
	}

	@Override
	protected Class<? extends AbstractComponentRender> getRenderClass() {
		return DictSelectRender.class;
	}

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pageParameter,
			final Element component) {
		EDictSelectType dictType;
		try {
			dictType = EDictSelectType.valueOf(component.attributeValue("dictType"));
		} catch (final Exception e) {
			dictType = EDictSelectType.tree;
		}

		final PageDocument pageDocument = pageParameter.getPageDocument();
		AbstractComponentBean componentBean = null;
		if (dictType == EDictSelectType.tree) {
			componentBean = new TreeBean(pageDocument, null);
			componentBean.setName("tree_" + componentBean.hashId());
			componentBean.setHandleClass(DictTree.class.getName());
			pageDocument.addComponentBean(componentBean);

			final Element element = component.addElement("tree");
			element.addAttribute("ref", componentBean.getName());
		} else if (dictType == EDictSelectType.list) {
			componentBean = new ListboxBean(pageDocument, null);
			componentBean.setName("list_" + componentBean.hashId());
			componentBean.setHandleClass(DictListbox.class.getName());
			pageDocument.addComponentBean(componentBean);

			final Element element = component.addElement("list");
			element.addAttribute("ref", componentBean.getName());
		}

		final DictSelectBean dictSelect = (DictSelectBean) super.createComponentBean(pageParameter,
				component);
		if (componentBean != null) {
			dictSelect.setAttribute("__componentBean", componentBean);
			componentBean.setAttribute("__dictSelect", dictSelect);
		}
		return dictSelect;
	}
}
