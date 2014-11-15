package net.itsite.user;

import java.util.Date;

import net.a.ItSiteUtil;
import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.IUserBeanAware;
import net.simpleframework.util.IConstants;
import net.simpleframework.web.EFunctionModule;

/**
 * 用户搜索
 * @author 李岩飞
 */
public class UserSearchBean extends AbstractIdDataObjectBean implements IUserBeanAware {
	private String content;//内容
	private ID catalogId;//
	private ID userId;//用户
	private EFunctionModule functionModule;
	private Date createDate;

	public UserSearchBean() {
		this.createDate = new Date();
	}

	public void setCatalogId(ID catalogId) {
		this.catalogId = catalogId;
	}

	public ID getCatalogId() {
		return catalogId;
	}

	public void setFunctionModule(EFunctionModule functionModule) {
		this.functionModule = functionModule;
	}

	public EFunctionModule getFunctionModule() {
		return functionModule;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	@Override
	public ID getUserId() {
		return userId;
	}

	@Override
	public void setUserId(ID userId) {
		this.userId = userId;
	}

	@Override
	public String getUserText() {
		final IUser user = ItSiteUtil.getUserById(getUserId());
		return user != null ? user.getText() : IConstants.HTML_BLANK_STRING;
	}

}
