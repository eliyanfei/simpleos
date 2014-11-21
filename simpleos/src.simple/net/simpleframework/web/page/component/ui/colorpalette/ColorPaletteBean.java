package net.simpleframework.web.page.component.ui.colorpalette;

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
public class ColorPaletteBean extends AbstractContainerBean {

	private EColorMode startMode;

	private String startHex = "CCCCCC";

	private String changeCallback;

	public ColorPaletteBean(final IComponentRegistry componentRegistry,
			final PageDocument pageDocument, final Element element) {
		super(componentRegistry, pageDocument, element);
	}

	public EColorMode getStartMode() {
		return startMode == null ? EColorMode.h : startMode;
	}

	public void setStartMode(final EColorMode startMode) {
		this.startMode = startMode;
	}

	public String getStartHex() {
		return startHex;
	}

	public void setStartHex(final String startHex) {
		this.startHex = startHex;
	}

	public String getChangeCallback() {
		return changeCallback;
	}

	public void setChangeCallback(final String changeCallback) {
		this.changeCallback = changeCallback;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "changeCallback" };
	}
}
