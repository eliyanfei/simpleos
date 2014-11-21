package net.simpleframework.content;

import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.IComponentRegistry;
import net.simpleframework.web.page.component.ui.pager.AbstractTablePagerBean;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class AbstractContentPagerBean extends AbstractTablePagerBean {
	private boolean showValidateCode = true;

	private boolean openBlank = true;

	public AbstractContentPagerBean(final IComponentRegistry componentRegistry,
			final PageDocument pageDocument, final Element element) {
		super(componentRegistry, pageDocument, element);
	}

	public boolean isShowValidateCode() {
		return showValidateCode;
	}

	public void setShowValidateCode(final boolean showValidateCode) {
		this.showValidateCode = showValidateCode;
	}

	public boolean isOpenBlank() {
		return openBlank;
	}

	public void setOpenBlank(final boolean openBlank) {
		this.openBlank = openBlank;
	}
}
