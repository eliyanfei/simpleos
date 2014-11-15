package net.simpleframework.web.page.component.base.validation;

import java.util.Collection;

import net.simpleframework.util.JavascriptUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.AbstractComponentJavascriptRender;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ComponentRenderUtils;
import net.simpleframework.web.page.component.IComponentRegistry;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ValidationRender extends AbstractComponentJavascriptRender {
	public ValidationRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getJavascriptCode(final ComponentParameter compParameter) {
		final ValidationBean validationBean = (ValidationBean) compParameter.componentBean;

		Collection<ValidatorBean> validators;
		final IValidationHandle validationHandle = (IValidationHandle) compParameter
				.getComponentHandle();
		if (validationHandle != null) {
			validators = validationHandle.getValidators(compParameter);
		} else {
			validators = validationBean.getValidators();
		}
		if (validators == null) {
			return null;
		}

		final StringBuilder sb = new StringBuilder();
		sb.append(validationBean.getActionFunction()).append(".validation = new Validation(\"");
		sb.append(validationBean.getTriggerSelector()).append("\", [");
		int i = 0;
		for (final ValidatorBean validator : validators) {
			if (i++ > 0) {
				sb.append(", ");
			}
			sb.append("{");
			sb.append("selector: \"").append(validator.getSelector()).append("\", ");
			final String args = validator.getArgs();
			if (StringUtils.hasText(args)) {
				sb.append("args: \"").append(args).append("\", ");
			}
			final String message = validator.getMessage();
			if (StringUtils.hasText(message)) {
				sb.append("message: \"");
				sb.append(JavascriptUtils.escape(message)).append("\", ");
			}
			final EWarnType warnType = validator.getWarnType();
			if (warnType != null) {
				sb.append("warnType: \"").append(warnType).append("\",");
			}
			sb.append("method: \"").append(validator.getMethod()).append("\"");
			sb.append("}");
		}
		sb.append("] ,{");
		sb.append("warnType: \"").append(validationBean.getWarnType()).append("\",");
		sb.append("d: \"").append(validationBean.getD()).append("\"");
		sb.append("});");
		return ComponentRenderUtils.wrapActionFunction(compParameter, sb.toString());
	}
}
