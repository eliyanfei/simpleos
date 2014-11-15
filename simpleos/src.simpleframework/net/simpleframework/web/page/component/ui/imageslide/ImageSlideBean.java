package net.simpleframework.web.page.component.ui.imageslide;

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
public class ImageSlideBean extends AbstractContainerBean {

	private Collection<ImageItem> imageItems;

	private int titleHeight = 35;

	private float titleOpacity = 0.6f;

	private float frequency = 4f;

	private boolean autoStart = true;

	private int start = 0;

	private boolean showPreAction = true, showNextAction = true;

	public ImageSlideBean(final IComponentRegistry componentRegistry,
			final PageDocument pageDocument, final Element element) {
		super(componentRegistry, pageDocument, element);
		setWidth("400");
		setHeight("280");
	}

	public Collection<ImageItem> getImageItems() {
		if (imageItems == null) {
			imageItems = new ArrayList<ImageItem>();
		}
		return imageItems;
	}

	public int getTitleHeight() {
		return titleHeight;
	}

	public void setTitleHeight(final int titleHeight) {
		this.titleHeight = titleHeight;
	}

	public float getTitleOpacity() {
		return titleOpacity;
	}

	public void setTitleOpacity(final float titleOpacity) {
		this.titleOpacity = titleOpacity;
	}

	public float getFrequency() {
		return frequency;
	}

	public void setFrequency(final float frequency) {
		this.frequency = frequency;
	}

	public boolean isAutoStart() {
		return autoStart;
	}

	public void setAutoStart(final boolean autoStart) {
		this.autoStart = autoStart;
	}

	public int getStart() {
		return start;
	}

	public void setStart(final int start) {
		this.start = start;
	}

	public boolean isShowPreAction() {
		return showPreAction;
	}

	public void setShowPreAction(final boolean showPreAction) {
		this.showPreAction = showPreAction;
	}

	public boolean isShowNextAction() {
		return showNextAction;
	}

	public void setShowNextAction(final boolean showNextAction) {
		this.showNextAction = showNextAction;
	}
}
