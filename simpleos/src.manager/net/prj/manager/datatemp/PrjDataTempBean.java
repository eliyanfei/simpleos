package net.prj.manager.datatemp;

import java.util.Date;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;

/**
 * @author 模板对象
 * 2013-3-28上午09:48:16
 */
public class PrjDataTempBean extends AbstractIdDataObjectBean {
	private String title;//名称
	private String description;//描述

	private Date createdDate;

	public PrjDataTempBean() {
		this.createdDate = new Date();
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}
