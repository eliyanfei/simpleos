package net.simpleframework.web.page.component.ui.videoplayer;

import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.AbstractContainerBean;
import net.simpleframework.web.page.component.IComponentRegistry;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class VideoPlayerBean extends AbstractContainerBean {
	private String videoUrl;

	private boolean autoPlay;

	private String jsLoadedCallback;

	public VideoPlayerBean(final IComponentRegistry componentRegistry,
			final PageDocument pageDocument, final Element element) {
		super(componentRegistry, pageDocument, element);
		setWidth("420");
		setHeight("300");
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(final String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public boolean isAutoPlay() {
		return autoPlay;
	}

	public void setAutoPlay(final boolean autoPlay) {
		this.autoPlay = autoPlay;
	}

	public String getJsLoadedCallback() {
		return jsLoadedCallback;
	}

	public void setJsLoadedCallback(final String jsLoadedCallback) {
		this.jsLoadedCallback = jsLoadedCallback;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsLoadedCallback" };
	}
}
