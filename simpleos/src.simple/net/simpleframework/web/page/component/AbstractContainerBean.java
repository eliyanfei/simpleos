package net.simpleframework.web.page.component;

import net.simpleframework.web.page.PageDocument;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractContainerBean extends AbstractComponentBean {
	private String containerId;

	private String width, height;

	public AbstractContainerBean(final IComponentRegistry componentRegistry,
			final PageDocument pageDocument, final Element element) {
		super(componentRegistry, pageDocument, element);
	}

	public String getContainerId() {
		return containerId;
	}

	public void setContainerId(final String containerId) {
		this.containerId = containerId;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(final String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(final String height) {
		this.height = height;
	}
}
