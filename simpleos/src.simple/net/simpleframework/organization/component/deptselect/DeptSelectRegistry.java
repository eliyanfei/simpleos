package net.simpleframework.organization.component.deptselect;

import javax.servlet.ServletContext;

import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentRender;
import net.simpleframework.web.page.component.ui.dictionary.DictionaryRegistry;
import net.simpleframework.web.page.component.ui.tree.TreeBean;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DeptSelectRegistry extends DictionaryRegistry {
	public static final String deptSelect = "deptSelect";

	public DeptSelectRegistry(final ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public String getComponentName() {
		return deptSelect;
	}

	@Override
	protected Class<? extends AbstractComponentBean> getBeanClass() {
		return DeptSelectBean.class;
	}

	@Override
	protected Class<? extends AbstractComponentRender> getRenderClass() {
		return DeptSelectRender.class;
	}

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pageParameter,
			final Element component) {
		final PageDocument pageDocument = pageParameter.getPageDocument();
		final TreeBean treeBean = new TreeBean(pageDocument, null);
		treeBean.setName("tree_" + treeBean.hashId());
		treeBean.setHandleClass(DeptTree.class.getName());
		pageDocument.addComponentBean(treeBean);

		final Element element = component.addElement("tree");
		element.addAttribute("ref", treeBean.getName());
		final DeptSelectBean deptSelect = (DeptSelectBean) super.createComponentBean(pageParameter,
				component);
		deptSelect.setAttribute("__componentBean", treeBean);
		treeBean.setAttribute("__deptSelect", deptSelect);
		return deptSelect;
	}
}
