package net.simpleframework.workflow.web.component.modellist;

import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.IComponentRegistry;
import net.simpleframework.web.page.component.ui.pager.AbstractTablePagerBean;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ModelListBean extends AbstractTablePagerBean {

	public ModelListBean(final IComponentRegistry componentRegistry,
			final PageDocument pageDocument, final Element element) {
		super(componentRegistry, pageDocument, element);
	}

	@Override
	public String getDataPath() {
		return getResourceHomePath() + "/jsp/model_list.jsp";
	}

	@Override
	public String getHandleClass() {
		return StringUtils.text(super.getHandleClass(), DefaultModelListHandle.class.getName());
	}
}
