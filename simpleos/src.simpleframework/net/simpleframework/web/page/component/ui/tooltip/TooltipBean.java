package net.simpleframework.web.page.component.ui.tooltip;

import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.IComponentRegistry;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TooltipBean extends AbstractComponentBean {

	private Collection<TipBean> tips;

	public TooltipBean(final IComponentRegistry componentRegistry, final PageDocument pageDocument,
			final Element element) {
		super(componentRegistry, pageDocument, element);
	}

	public Collection<TipBean> getTips() {
		if (tips == null) {
			tips = new ArrayList<TipBean>();
		}
		return tips;
	}
}
