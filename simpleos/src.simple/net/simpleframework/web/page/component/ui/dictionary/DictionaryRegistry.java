package net.simpleframework.web.page.component.ui.dictionary;

import javax.servlet.ServletContext;

import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.script.IScriptEval;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.AbstractComponentRender;
import net.simpleframework.web.page.component.AbstractComponentResourceProvider;
import net.simpleframework.web.page.component.ComponentException;
import net.simpleframework.web.page.component.ui.colorpalette.ColorPaletteBean;
import net.simpleframework.web.page.component.ui.dictionary.DictionaryBean.DictionaryColorBean;
import net.simpleframework.web.page.component.ui.dictionary.DictionaryBean.DictionaryFontBean;
import net.simpleframework.web.page.component.ui.dictionary.DictionaryBean.DictionaryListBean;
import net.simpleframework.web.page.component.ui.dictionary.DictionaryBean.DictionarySmileyBean;
import net.simpleframework.web.page.component.ui.dictionary.DictionaryBean.DictionaryTreeBean;
import net.simpleframework.web.page.component.ui.listbox.ListboxBean;
import net.simpleframework.web.page.component.ui.tree.TreeBean;
import net.simpleframework.web.page.component.ui.window.WindowRender;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DictionaryRegistry extends AbstractComponentRegistry {
	public static final String dictionary = "dictionary";

	public DictionaryRegistry(final ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public String getComponentName() {
		return dictionary;
	}

	@Override
	protected Class<? extends AbstractComponentBean> getBeanClass() {
		return DictionaryBean.class;
	}

	@Override
	protected Class<? extends AbstractComponentRender> getRenderClass() {
		return WindowRender.class;
	}

	@Override
	protected Class<? extends AbstractComponentResourceProvider> getResourceProviderClass() {
		return DictionaryResourceProvider.class;
	}

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pageParameter,
			final Element component) {
		final DictionaryBean dictionaryBean = (DictionaryBean) super.createComponentBean(
				pageParameter, component);
		final IScriptEval scriptEval = pageParameter.getScriptEval();
		final String dictionaryId = dictionaryBean.hashId();
		Element element = component.element("tree");
		if (element != null) {
			final DictionaryTreeBean tree = new DictionaryTreeBean(dictionaryBean, element);
			tree.parseElement(scriptEval);
			final TreeBean treeBean = (TreeBean) pageParameter.getComponentBean(tree.getRef());
			if (treeBean == null) {
				if (!isComponentInCache(pageParameter, tree.getRef())) {
					throw ComponentException.getComponentRefException();
				}
			} else {
				treeBean.setCheckboxesThreeState(false);
				treeBean.setRunImmediately(false);
				treeBean.setWidth("100%");
				treeBean.setContainerId("tree" + dictionaryId);
				treeBean.setJsDblclickCallback("selected" + dictionaryId + "(branch, ev);");
				treeBean.setJsCheckCallback("treeCheck" + dictionaryId + "(branch, checked, ev);");
				final String selector = dictionaryBean.getSelector();
				if (!"false".equalsIgnoreCase(selector)) {
					treeBean.setSelector(selector);
				}
			}
		} else if ((element = component.element("list")) != null) {
			final DictionaryListBean list = new DictionaryListBean(dictionaryBean, element);
			list.parseElement(scriptEval);
			final ListboxBean listBean = (ListboxBean) pageParameter.getComponentBean(list.getRef());
			if (listBean == null) {
				if (!isComponentInCache(pageParameter, list.getRef())) {
					throw ComponentException.getComponentRefException();
				}
			} else {
				listBean.setContainerId("list" + dictionaryId);
				listBean.setWidth("100%");
				listBean.setRunImmediately(false);
				listBean.setJsDblclickCallback("selected" + dictionaryId + "(item, json, ev);");
			}
		} else if ((element = component.element("color")) != null) {
			final DictionaryColorBean color = new DictionaryColorBean(dictionaryBean, element);
			color.parseElement(scriptEval);
			final ColorPaletteBean colorPalette = (ColorPaletteBean) pageParameter
					.getComponentBean(color.getRef());
			if (colorPalette == null) {
				if (!isComponentInCache(pageParameter, color.getRef())) {
					throw ComponentException.getComponentRefException();
				}
			} else {
				colorPalette.setContainerId("color" + dictionaryId);
				colorPalette.setRunImmediately(false);
				colorPalette.setChangeCallback("change_" + dictionaryId + "(value);");
				dictionaryBean.setWidth(435);
				dictionaryBean.setHeight(340);
				dictionaryBean.setResizable(false);
				dictionaryBean.setDestroyOnClose(true);
				dictionaryBean.setTitle(LocaleI18n.getMessage("createComponentBean.0"));
			}
		} else if ((element = component.element("font")) != null) {
			final DictionaryFontBean font = new DictionaryFontBean(dictionaryBean, element);
			font.parseElement(scriptEval);
			dictionaryBean.setWidth(280);
			dictionaryBean.setHeight(220);
			dictionaryBean.setResizable(false);
			dictionaryBean.setDestroyOnClose(true);
			dictionaryBean.setTitle(LocaleI18n.getMessage("createComponentBean.1"));
		} else if ((element = component.element("smiley")) != null) {
			final DictionarySmileyBean smiley = new DictionarySmileyBean(dictionaryBean, element);
			smiley.parseElement(scriptEval);
			dictionaryBean.setResizable(false);
			dictionaryBean.setWidth(440);
			dictionaryBean.setHeight(240);
			dictionaryBean.setTitle(LocaleI18n.getMessage("createComponentBean.2"));
		}

		// 虚拟一个AjaxRequestBean
		DictionaryUtils.createAjaxRequest(dictionaryBean);
		return dictionaryBean;
	}
}
