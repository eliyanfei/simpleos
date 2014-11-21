package net.simpleframework.web.page.component.base.validation;

import java.util.ArrayList;
import java.util.Collection;

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
public class ValidationBean extends AbstractComponentBean {

	private EWarnType warnType;

	private String triggerSelector;
	private String d;

	private Collection<ValidatorBean> validators;

	public ValidationBean(final IComponentRegistry componentRegistry, final PageDocument pageDocument, final Element element) {
		super(componentRegistry, pageDocument, element);
	}

	public Collection<ValidatorBean> getValidators() {
		if (validators == null) {
			validators = new ArrayList<ValidatorBean>();
		}
		return validators;
	}

	public void setD(String d) {
		this.d = d;
	}

	public String getD() {
		return d == null ? "div" : "span";
	}

	public String getTriggerSelector() {
		return triggerSelector;
	}

	public void setTriggerSelector(final String triggerSelector) {
		this.triggerSelector = triggerSelector;
	}

	public EWarnType getWarnType() {
		return warnType == null ? EWarnType.alert : warnType;
	}

	public void setWarnType(final EWarnType warnType) {
		this.warnType = warnType;
	}
}
