package net.simpleframework.desktop;

import java.util.Date;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.id.ID;

/**
 */
public class DesktopUIBean extends AbstractIdDataObjectBean {
	private ID userId;//
	private String tabId;
	private String uiName;
	private int oorder;
	public Date createDate;

	public DesktopUIBean() {
		this.createDate = new Date();
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public ID getUserId() {
		return userId;
	}

	public void setUserId(ID userId) {
		this.userId = userId;
	}

	public String getTabId() {
		return tabId;
	}

	public void setTabId(String tabId) {
		this.tabId = tabId;
	}

	public String getUiName() {
		return uiName;
	}

	public void setUiName(String uiName) {
		this.uiName = uiName;
	}

	public int getOorder() {
		return oorder;
	}

	public void setOorder(int oorder) {
		this.oorder = oorder;
	}

}
