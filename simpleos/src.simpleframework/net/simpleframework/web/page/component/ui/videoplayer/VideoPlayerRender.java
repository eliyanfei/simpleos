package net.simpleframework.web.page.component.ui.videoplayer;

import javax.servlet.http.HttpServletRequest;

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
public class VideoPlayerRender extends AbstractComponentJavascriptRender {

	public VideoPlayerRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getJavascriptCode(final ComponentParameter compParameter) {
		final VideoPlayerBean videoPlayer = (VideoPlayerBean) compParameter.componentBean;
		final HttpServletRequest request = compParameter.request;

		final StringBuilder sb = new StringBuilder();
		sb.append(ComponentRenderUtils.initContainerVar(compParameter));
		sb.append(ComponentRenderUtils.initHeightWidth(compParameter));
		final String actionFunc = videoPlayer.getActionFunction();
		sb.append(actionFunc).append(".videoPlayer = $f(").append(ComponentRenderUtils.VAR_CONTAINER)
				.append(", {");
		sb.append("buffering: true,");
		sb.append("src: \"").append(request.getContextPath());
		sb.append(videoPlayer.getResourceHomePath()).append("/flash/flowplayer.swf\"");
		sb.append("}, {");
		final String onload = videoPlayer.getJsLoadedCallback();
		if (StringUtils.hasText(onload)) {
			sb.append("onLoad: function() {");
			sb.append(onload);
			sb.append("},");
		}
		sb.append("logo: null,");
		sb.append("clip: {");
		sb.append("autoPlay: ").append(videoPlayer.isAutoPlay()).append(",");
		sb.append("scaling: \"fit\",");
		String videoUrl = null;
		final IVideoPlayerHandle handle = (IVideoPlayerHandle) compParameter.getComponentHandle();
		if (handle != null) {
			videoUrl = handle.getVideoUrl(compParameter);
		}
		if (!StringUtils.hasText(videoUrl)) {
			videoUrl = videoPlayer.getVideoUrl();
		}
		sb.append("url: \"").append(request.getContextPath()).append(videoUrl).append("\"");
		sb.append("}");
		sb.append("});");
		return ComponentRenderUtils.wrapActionFunction(compParameter, sb.toString());
	}
}
