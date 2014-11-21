package net.simpleframework.web.page.component.ui.pwdstrength;

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
public class PwdStrengthRender extends AbstractComponentJavascriptRender {
	public PwdStrengthRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getJavascriptCode(final ComponentParameter compParameter) {
		final PwdStrengthBean pwdStrength = (PwdStrengthBean) compParameter.componentBean;

		final StringBuilder sb = new StringBuilder();
		sb.append(ComponentRenderUtils.initContainerVar(compParameter));
		final String actionFunc = pwdStrength.getActionFunction();
		sb.append(actionFunc).append(".pwdstrength = new $Comp.PwdStrength(")
				.append(ComponentRenderUtils.VAR_CONTAINER).append(", {");
		final String pwdInput = pwdStrength.getPwdInput();
		if (StringUtils.hasText(pwdInput)) {
			sb.append("\"pwdInput\": \"").append(pwdInput).append("\",");
		}
		sb.append(ComponentRenderUtils.jsonHeightWidth(compParameter));
		sb.append("\"effects\": Browser.effects && ")
				.append(compParameter.getBeanProperty("effects"));
		sb.append("});");
		return ComponentRenderUtils.wrapActionFunction(compParameter, sb.toString());
	}
}
