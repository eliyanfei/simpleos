package net.simpleframework.web.page.component.ui.validatecode;

import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.AbstractContainerBean;
import net.simpleframework.web.page.component.IComponentHandle;
import net.simpleframework.web.page.component.IComponentRegistry;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ValidateCodeBean extends AbstractContainerBean {

	private String textName;

	private int textWidth;

	public ValidateCodeBean(final IComponentRegistry componentRegistry,
			final PageDocument pageDocument, final Element element) {
		super(componentRegistry, pageDocument, element);
	}

	@Override
	protected Class<? extends IComponentHandle> getDefaultHandleClass() {
		return DefaultValidateCodeHandle.class;
	}

	public String getTextName() {
		return textName;
	}

	public void setTextName(final String textName) {
		this.textName = textName;
	}

	public int getTextWidth() {
		return textWidth;
	}

	public void setTextWidth(final int textWidth) {
		this.textWidth = textWidth;
	}
}
