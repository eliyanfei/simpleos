package net.simpleframework.applets.notification.component.window;

import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.IComponentHandle;
import net.simpleframework.web.page.component.IComponentRegistry;
import net.simpleframework.web.page.component.ui.window.WindowBean;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MessageWindowBean extends WindowBean {
	private int frequency = 30;

	private int closeDelay = 0;

	public MessageWindowBean(final IComponentRegistry componentRegistry,
			final PageDocument pageDocument, final Element element) {
		super(componentRegistry, pageDocument, element);
		setTitle(LocaleI18n.getMessage("MessageWindowBean.0"));
		setHeight(210);
		setWidth(350);
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(final int frequency) {
		this.frequency = frequency;
	}

	public int getCloseDelay() {
		return closeDelay;
	}

	public void setCloseDelay(final int closeDelay) {
		this.closeDelay = closeDelay;
	}

	@Override
	public boolean isShowCenter() {
		return false;
	}

	@Override
	public boolean isModal() {
		return false;
	}

	@Override
	protected Class<? extends IComponentHandle> getDefaultHandleClass() {
		return DefaultMessageWindowHandle.class;
	}
}
