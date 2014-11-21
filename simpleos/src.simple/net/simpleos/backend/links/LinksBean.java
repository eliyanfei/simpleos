package net.simpleos.backend.links;

import java.util.Date;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.id.ID;

/**
 * @author 李岩飞
 * 2013-3-28上午09:48:16
 */
public class LinksBean extends AbstractIdDataObjectBean {
	private ID userId;//创建着
	private int oorder;//顺序
	private String color;//特殊颜色
	private String title;//名称
	private String url;//地址
	private String description;//描述
	private Date createdDate;

	public LinksBean() {
		this.createdDate = new Date();
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getColor() {
		return color;
	}

	public ID getUserId() {
		return userId;
	}

	public void setUserId(ID userId) {
		this.userId = userId;
	}

	public int getOorder() {
		return oorder;
	}

	public void setOorder(int oorder) {
		this.oorder = oorder;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}
