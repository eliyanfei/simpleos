package net.simpleframework.web.page.component.ui.validatecode;

import net.simpleframework.util.GenId;
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
public class ValidateCodeRender extends AbstractComponentJavascriptRender {

	public ValidateCodeRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getJavascriptCode(final ComponentParameter compParameter) {
		final ValidateCodeBean validateCode = (ValidateCodeBean) compParameter.componentBean;

		final String actionFunc = validateCode.getActionFunction();
		final StringBuilder sb = new StringBuilder();
		sb.append(ComponentRenderUtils.initContainerVar(compParameter));
		sb.append(actionFunc).append(".validateCode = new $Comp.ValidateCode(")
				.append(ComponentRenderUtils.VAR_CONTAINER).append(", {");
		sb.append(ComponentRenderUtils.jsonHeightWidth(compParameter));
		final int textWidth = validateCode.getTextWidth();
		if (textWidth > 0) {
			sb.append("textWidth: ").append(textWidth).append(",");
		}
		final String textName = validateCode.getTextName();
		if (StringUtils.hasText(textName)) {
			sb.append("textName: \"").append(textName).append("\",");
		}
		sb.append("path: \"").append(getResourceHomePath(compParameter))
				.append("/jsp/validatecode.jsp?");
		sb.append(ValidateCodeUtils.BEAN_ID).append("=").append(validateCode.hashId());
		sb.append("&r=").append(GenId.genUID()).append("\"");
		sb.append("});");
		sb.append(actionFunc).append(".refresh = function() {");
		sb.append(actionFunc).append(".validateCode.refresh();");
		sb.append("};");
		return ComponentRenderUtils.wrapActionFunction(compParameter, sb.toString());
	}
}
