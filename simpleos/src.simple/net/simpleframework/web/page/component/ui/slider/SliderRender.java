package net.simpleframework.web.page.component.ui.slider;

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
public class SliderRender extends AbstractComponentJavascriptRender {

	public SliderRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getJavascriptCode(final ComponentParameter compParameter) {
		final SliderBean sliderBean = (SliderBean) compParameter.componentBean;

		final StringBuilder sb = new StringBuilder();
		sb.append(ComponentRenderUtils.initContainerVar(compParameter));
		final String actionFunc = sliderBean.getActionFunction();
		sb.append(actionFunc).append(".slider = ").append("new $Comp.Slider(")
				.append(ComponentRenderUtils.VAR_CONTAINER).append(", {");
		sb.append("xMinValue: ").append(sliderBean.getXminValue()).append(",");
		sb.append("xMaxValue: ").append(sliderBean.getXmaxValue()).append(",");
		sb.append("yMinValue: ").append(sliderBean.getYminValue()).append(",");
		sb.append("yMaxValue: ").append(sliderBean.getYmaxValue()).append(",");
		sb.append(ComponentRenderUtils.jsonHeightWidth(compParameter));

		sb.append("arrowImage: \"");
		final String arrowImage = sliderBean.getArrowImage();
		if (StringUtils.hasText(arrowImage)) {
			sb.append(compParameter.wrapContextPath(arrowImage));
		} else {
			sb.append(getCssResourceHomePath(compParameter));
			sb.append("/images/");
			final int xmaxValue = sliderBean.getXmaxValue();
			final int ymaxValue = sliderBean.getYmaxValue();
			if (xmaxValue > 0 && ymaxValue > 0) {
				sb.append("arrows_point.gif");
			} else {
				if (xmaxValue > 0) {
					sb.append("arrows_h.gif");
				} else {
					sb.append("arrows_v.gif");
				}
			}
		}
		sb.append("\"");
		sb.append("});");
		final String changeCallback = sliderBean.getJsChangeCallback();
		if (StringUtils.hasText(changeCallback)) {
			sb.append(actionFunc).append(".slider.onValuesChanged = function(slider) {");
			sb.append("var callback = function(x, y) {");
			sb.append(changeCallback);
			sb.append("};");
			sb.append("callback(slider.xValue, slider.yValue);");
			sb.append("};");
		}
		return ComponentRenderUtils.wrapActionFunction(compParameter, sb.toString());
	}
}
