package net.simpleframework.web.page.component.base.validation;

import java.util.Iterator;

import javax.servlet.ServletContext;

import net.simpleframework.util.StringUtils;
import net.simpleframework.util.script.IScriptEval;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.AbstractComponentRender;
import net.simpleframework.web.page.component.AbstractComponentResourceProvider;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ValidationRegistry extends AbstractComponentRegistry {
	public static final String validation = "validation";

	public ValidationRegistry(final ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public String getComponentName() {
		return validation;
	}

	@Override
	protected Class<ValidationBean> getBeanClass() {
		return ValidationBean.class;
	}

	@Override
	protected Class<? extends AbstractComponentRender> getRenderClass() {
		return ValidationRender.class;
	}

	@Override
	protected Class<? extends AbstractComponentResourceProvider> getResourceProviderClass() {
		return ValidationResourceProvider.class;
	}

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pageParameter,
			final Element component) {
		final ValidationBean validationBean = (ValidationBean) super.createComponentBean(
				pageParameter, component);
		final IScriptEval scriptEval = pageParameter.getScriptEval();
		final Iterator<?> it = component.elementIterator("validator");
		while (it.hasNext()) {
			final Element element = (Element) it.next();
			final ValidatorBean validatorBean = new ValidatorBean(element);
			validatorBean.parseElement(scriptEval);
			final String message = element.elementText("message");
			if (StringUtils.hasText(message)) {
				validatorBean.setMessage(message.trim());
			}
			validationBean.getValidators().add(validatorBean);
		}
		return validationBean;
	}
}
