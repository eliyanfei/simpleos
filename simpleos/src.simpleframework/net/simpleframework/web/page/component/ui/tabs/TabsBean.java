package net.simpleframework.web.page.component.ui.tabs;

import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.AbstractContainerBean;
import net.simpleframework.web.page.component.IComponentRegistry;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TabsBean extends AbstractContainerBean {

	private Collection<TabItem> tabItems;

	private int activeIndex = 0;

	public TabsBean(final IComponentRegistry componentRegistry, final PageDocument pageDocument,
			final Element element) {
		super(componentRegistry, pageDocument, element);
	}

	public Collection<TabItem> getTabItems() {
		if (tabItems == null) {
			tabItems = new ArrayList<TabItem>();
		}
		return tabItems;
	}

	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(final int activeIndex) {
		this.activeIndex = activeIndex;
	}
}
