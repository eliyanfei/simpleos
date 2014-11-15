package net.simpleframework.web.page.component.ui.calendar;

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
public class CalendarBean extends AbstractContainerBean {

	private String inputField;

	private String dateFormat = "yyyy-MM-dd";

	private boolean showTime;

	private String closeCallback;

	public CalendarBean(final IComponentRegistry componentRegistry, final PageDocument pageDocument,
			final Element element) {
		super(componentRegistry, pageDocument, element);
	}

	public String getInputField() {
		return inputField;
	}

	public void setInputField(final String inputField) {
		this.inputField = inputField;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(final String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public boolean isShowTime() {
		return showTime;
	}

	public void setShowTime(final boolean showTime) {
		this.showTime = showTime;
	}

	public String getCloseCallback() {
		return closeCallback;
	}

	public void setCloseCallback(final String closeCallback) {
		this.closeCallback = closeCallback;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "closeCallback" };
	}
}
