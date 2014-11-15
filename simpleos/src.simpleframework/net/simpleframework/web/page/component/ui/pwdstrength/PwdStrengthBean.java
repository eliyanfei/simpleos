package net.simpleframework.web.page.component.ui.pwdstrength;

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
public class PwdStrengthBean extends AbstractContainerBean {

	private String pwdInput;

	public PwdStrengthBean(final IComponentRegistry componentRegistry,
			final PageDocument pageDocument, final Element element) {
		super(componentRegistry, pageDocument, element);
	}

	public String getPwdInput() {
		return pwdInput;
	}

	public void setPwdInput(final String pwdInput) {
		this.pwdInput = pwdInput;
	}
}
