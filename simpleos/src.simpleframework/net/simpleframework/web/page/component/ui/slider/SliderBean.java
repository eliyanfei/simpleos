package net.simpleframework.web.page.component.ui.slider;

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
public class SliderBean extends AbstractContainerBean {

	private int xminValue = 0, xmaxValue = 100, yminValue = 0, ymaxValue = 0;

	private String arrowImage;

	private String jsChangeCallback;

	public int getXminValue() {
		return xminValue;
	}

	public SliderBean(final IComponentRegistry componentRegistry, final PageDocument pageDocument,
			final Element element) {
		super(componentRegistry, pageDocument, element);
	}

	public void setXminValue(final int xminValue) {
		this.xminValue = xminValue;
	}

	public int getXmaxValue() {
		return xmaxValue;
	}

	public void setXmaxValue(final int xmaxValue) {
		this.xmaxValue = xmaxValue;
	}

	public int getYminValue() {
		return yminValue;
	}

	public void setYminValue(final int yminValue) {
		this.yminValue = yminValue;
	}

	public int getYmaxValue() {
		return ymaxValue;
	}

	public void setYmaxValue(final int ymaxValue) {
		this.ymaxValue = ymaxValue;
	}

	public String getArrowImage() {
		return arrowImage;
	}

	public void setArrowImage(final String arrowImage) {
		this.arrowImage = arrowImage;
	}

	public String getJsChangeCallback() {
		return jsChangeCallback;
	}

	public void setJsChangeCallback(final String jsChangeCallback) {
		this.jsChangeCallback = jsChangeCallback;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsChangeCallback" };
	}
}
