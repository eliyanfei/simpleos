package net.simpleframework.web.page.component.ui.imageslide;

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
public class ImageSlideRegistry extends AbstractComponentRegistry {
	public static final String imageSlide = "imageSlide";

	public ImageSlideRegistry(final ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public String getComponentName() {
		return imageSlide;
	}

	@Override
	protected Class<? extends AbstractComponentBean> getBeanClass() {
		return ImageSlideBean.class;
	}

	@Override
	protected Class<? extends AbstractComponentRender> getRenderClass() {
		return ImageSlideRender.class;
	}

	@Override
	protected Class<? extends AbstractComponentResourceProvider> getResourceProviderClass() {
		return ImageSlideResourceProvider.class;
	}

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pageParameter,
			final Element component) {
		final ImageSlideBean imageSlide = (ImageSlideBean) super.createComponentBean(pageParameter,
				component);
		final IScriptEval scriptEval = pageParameter.getScriptEval();
		final Iterator<?> it = component.elementIterator("imageitem");
		while (it.hasNext()) {
			final Element element = (Element) it.next();
			final ImageItem imageItem = new ImageItem(element);
			imageItem.parseElement(scriptEval);
			imageSlide.getImageItems().add(imageItem);
		}
		return imageSlide;
	}
}
