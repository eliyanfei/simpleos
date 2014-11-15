package net.simpleframework.my.home;

import java.util.Date;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.id.ID;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class HomeTabsBean extends AbstractIdDataObjectBean {
	private String tabText;

	private ID userId;

	private Date createDate;

	private boolean defaulttab;

	private int views;

	private String description;

	public String getTabText() {
		return tabText;
	}

	public void setTabText(final String tabText) {
		this.tabText = tabText;
	}

	public ID getUserId() {
		return userId;
	}

	public void setUserId(final ID userId) {
		this.userId = userId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(final Date createDate) {
		this.createDate = createDate;
	}

	public boolean isDefaulttab() {
		return defaulttab;
	}

	public void setDefaulttab(final boolean defaulttab) {
		this.defaulttab = defaulttab;
	}

	public int getViews() {
		return views;
	}

	public void setViews(final int views) {
		this.views = views;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	private static final long serialVersionUID = 8142966150870802569L;
}
