package net.simpleframework.web.page.component.base.validation;

import net.simpleframework.core.AbstractElementBean;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ValidatorBean extends AbstractElementBean {

	private String selector;

	private EWarnType warnType;

	private EValidatorMethod method;

	private String message;

	private String args;

	public ValidatorBean(final Element element) {
		super(element);
	}

	public ValidatorBean() {
		this(null);
	}

	public EValidatorMethod getMethod() {
		return method;
	}

	public void setMethod(final EValidatorMethod method) {
		this.method = method;
	}

	public String getSelector() {
		return selector;
	}

	public void setSelector(final String selector) {
		this.selector = selector;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public String getArgs() {
		return args;
	}

	public void setArgs(final String args) {
		this.args = args;
	}

	public EWarnType getWarnType() {
		return warnType;
	}

	public void setWarnType(final EWarnType warnType) {
		this.warnType = warnType;
	}
}
