package net.simpleframework.web.page.component.ui.picshow;

import java.util.Iterator;

import javax.servlet.ServletContext;

import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.AbstractComponentRender;
import net.simpleframework.web.page.component.AbstractComponentResourceProvider;

import org.dom4j.Element;

public class PicShowRegistry extends AbstractComponentRegistry {
	public static final String picShow = "picShow";

	public PicShowRegistry(final ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public String getComponentName() {
		return picShow;
	}

	@Override
	protected Class<? extends AbstractComponentBean> getBeanClass() {
		return PicShowBean.class;
	}

	@Override
	protected Class<? extends AbstractComponentRender> getRenderClass() {
		return PicShowRender.class;
	}

	@Override
	protected Class<? extends AbstractComponentResourceProvider> getResourceProviderClass() {
		return PicShowResourceProvider.class;
	}

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pageParameter, final Element component) {
		final PicShowBean picShow = (PicShowBean) super.createComponentBean(pageParameter, component);
		final Iterator<?> it = component.elementIterator("picItem");
		while (it.hasNext()) {
			final Element element = (Element) it.next();
			final PicItem picItem = new PicItem(element);
//			setProperty(pageParameter, element, picItem);
//			setChildren(pageParameter, element, picItem, new String[] { "desc" });
			picShow.getPicItems().add(picItem);
			picShow.setOverride(true);
		}
		return picShow;
	}
}
