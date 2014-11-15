package net.simpleframework.web.page.component.ui.imageslide;

import net.simpleframework.core.AbstractElementBean;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ImageItem extends AbstractElementBean {

	private String imageUrl;

	private String title;

	private String link;

	public ImageItem(final Element dom4jElement) {
		super(dom4jElement);
	}

	public ImageItem() {
		super(null);
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(final String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(final String link) {
		this.link = link;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "imageUrl", "title", "link" };
	}
}
