package net.simpleframework.content.component.remark;

import net.simpleframework.content.AbstractContentBean;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.page.PageDocument;
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
public class RemarkBean extends AbstractContentBean {
	private String actionText = LocaleI18n.getMessage("remark.0");

	private String title = LocaleI18n.getMessage("remark.2");

	private boolean showValidateCode = true;

	private boolean showHigh = true;
	
	private boolean showReply = true;

	private boolean showSupportOpposition = true;

	public RemarkBean(final IComponentRegistry componentRegistry, final PageDocument pageDocument,
			final Element element) {
		super(componentRegistry, pageDocument, element);
	}

	@Override
	protected Class<? extends IComponentHandle> getDefaultHandleClass() {
		return DefaultRemarkHandle.class;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public boolean isShowValidateCode() {
		return showValidateCode;
	}

	public void setShowValidateCode(final boolean showValidateCode) {
		this.showValidateCode = showValidateCode;
	}

	public boolean isShowReply() {
		return showReply;
	}

	public void setShowHigh(boolean showHigh) {
		this.showHigh = showHigh;
	}
	
	public boolean isShowHigh() {
		return showHigh;
	}
	
	public void setShowReply(final boolean showReply) {
		this.showReply = showReply;
	}

	public boolean isShowSupportOpposition() {
		return showSupportOpposition;
	}

	public void setShowSupportOpposition(final boolean showSupportOpposition) {
		this.showSupportOpposition = showSupportOpposition;
	}

	public String getActionText() {
		return actionText;
	}

	public void setActionText(final String actionText) {
		this.actionText = actionText;
	}
}
