package net.simpleframework.web.page.component.ui.picshow;

import net.simpleframework.core.AbstractElementBean;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.StringUtils;

import org.dom4j.Element;

public class PicItem extends AbstractElementBean {
	private String desc;
	private String img_x;
	private String img_x50;
	private String img_x160;
	private String title;
	private String date;
	private String id;
	private String jsClickCallBack;

	public String getJsClickCallBack() {
		return jsClickCallBack;
	}

	public void setJsClickCallBack(String jsClickCallBack) {
		this.jsClickCallBack = jsClickCallBack;
	}

	public PicItem(final Element dom4jElement) {
		super(dom4jElement);
		if (dom4jElement != null) {
			initPicItemParseElement(dom4jElement);
		}
	}

	private void initPicItemParseElement(final Element dom4jElement) {
		BeanUtils.setProperty(this, "img_x", dom4jElement.attributeValue("img_x"));
		BeanUtils.setProperty(this, "img_x50", dom4jElement.attributeValue("img_x50"));
		BeanUtils.setProperty(this, "img_x160", dom4jElement.attributeValue("img_x160"));
		BeanUtils.setProperty(this, "title", dom4jElement.attributeValue("title"));
		BeanUtils.setProperty(this, "id", dom4jElement.attributeValue("id"));
		BeanUtils.setProperty(this, "desc", dom4jElement.element("desc") != null ? dom4jElement.element("desc").getTextTrim() : "");
		BeanUtils.setProperty(this, "jsClickCallBack", dom4jElement.element("jsClickCallBack") != null ? dom4jElement.element("jsClickCallBack")
				.getTextTrim() : "");
	}

	public PicItem() {
		super(null);
	}

	public String getImg_x() {
		return img_x;
	}

	public void setImg_x(String img_x) {
		this.img_x = img_x;
	}

	public String getImg_x50() {
		return StringUtils.text(img_x50, img_x);
	}

	public void setImg_x50(String img_x50) {
		this.img_x50 = img_x50;
	}

	public String getImg_x160() {
		return StringUtils.text(img_x160, img_x);
	}

	public void setImg_x160(String img_x160) {
		this.img_x160 = img_x160;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
}
