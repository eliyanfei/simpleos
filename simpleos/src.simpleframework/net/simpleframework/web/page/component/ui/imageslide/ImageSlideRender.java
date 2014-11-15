package net.simpleframework.web.page.component.ui.imageslide;

import java.util.Collection;

import net.simpleframework.util.ConvertUtils;
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
public class ImageSlideRender extends AbstractComponentJavascriptRender {

	public ImageSlideRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getJavascriptCode(final ComponentParameter compParameter) {
		final ImageSlideBean imageSlide = (ImageSlideBean) compParameter.componentBean;
		Collection<ImageItem> items;
		final IImageSlideHandle handle = (IImageSlideHandle) compParameter.getComponentHandle();
		if (handle != null) {
			items = handle.getImageItems(compParameter);
		} else {
			items = imageSlide.getImageItems();
		}
		final String actionFunc = imageSlide.getActionFunction();
		final StringBuilder sb = new StringBuilder();
		sb.append(ComponentRenderUtils.initContainerVar(compParameter));
		sb.append(actionFunc).append(".imageSlide = new $Comp.ImageSlide(")
				.append(ComponentRenderUtils.VAR_CONTAINER).append(", [");
		if (items != null) {
			int i = 0;
			for (final ImageItem item : items) {
				if (i++ > 0) {
					sb.append(",");
				}
				sb.append("{");
				final String link = item.getLink();
				if (StringUtils.hasText(link)) {
					sb.append("link: \"").append(compParameter.wrapContextPath(link)).append("\",");
				}
				sb.append("imageUrl: \"").append(compParameter.wrapContextPath(item.getImageUrl()))
						.append("\",");
				sb.append("title: \"").append(JavascriptUtils.escape(item.getTitle())).append("\"");
				sb.append("}");
			}
		}
		sb.append("], {");

		final int titleHeight = ConvertUtils.toInt(compParameter.getBeanProperty("titleHeight"), 0);
		if (titleHeight > 0) {
			sb.append("titleHeight: ").append(titleHeight).append(",");
		}
		sb.append("titleOpacity: ").append(compParameter.getBeanProperty("titleOpacity")).append(",");
		sb.append("frequency: ").append(compParameter.getBeanProperty("frequency")).append(",");
		sb.append("showNextAction: ").append(compParameter.getBeanProperty("showNextAction"))
				.append(",");
		sb.append("showPreAction: ").append(compParameter.getBeanProperty("showPreAction"))
				.append(",");
		sb.append("autoStart: ").append(compParameter.getBeanProperty("autoStart")).append(",");
		sb.append("start: ").append(compParameter.getBeanProperty("start")).append(",");
		sb.append(ComponentRenderUtils.jsonHeightWidth(compParameter));
		sb.append("effects: Browser.effects && ").append(compParameter.getBeanProperty("effects"));
		sb.append("});");
		return ComponentRenderUtils.wrapActionFunction(compParameter, sb.toString());
	}
}
