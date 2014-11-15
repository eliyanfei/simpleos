package net.simpleframework.web.page.component.base.submit;

import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.IComponentRegistry;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class SubmitBean extends AbstractComponentBean {

	private String formName;

	private boolean binary;

	private String handleMethod;

	private String confirmMessage;

	private String jobExecute;

	public SubmitBean(final IComponentRegistry componentRegistry, final PageDocument pageDocument,
			final Element element) {
		super(componentRegistry, pageDocument, element);
		setRunImmediately(false);
		setIncludeRequestData("pa");
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(final String formName) {
		this.formName = formName;
	}

	public String getHandleMethod() {
		return handleMethod;
	}

	public void setHandleMethod(final String handleMethod) {
		this.handleMethod = handleMethod;
	}

	public boolean isBinary() {
		return binary;
	}

	public void setBinary(final boolean binary) {
		this.binary = binary;
	}

	public String getConfirmMessage() {
		return confirmMessage;
	}

	public void setConfirmMessage(final String confirmMessage) {
		this.confirmMessage = confirmMessage;
	}

	public String getJobExecute() {
		return jobExecute;
	}

	public void setJobExecute(final String jobExecute) {
		this.jobExecute = jobExecute;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "confirmMessage" };
	}
}
