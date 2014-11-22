package net.simpleos.module.complaint;

import java.util.Date;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.IUserBeanAware;
import net.simpleframework.util.IConstants;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleos.SimpleosUtil;

/**
 * 
 * @author 李岩飞 
 * @date 2014年11月19日 下午5:19:00 
 * @Description: 投诉表格的实体对象
 *
 */
public class ComplaintBean extends AbstractIdDataObjectBean implements IUserBeanAware {
	private EFunctionModule refModule;//类型
	private EComplaint complaint;//投诉类型
	private String content;//投诉原因
	private Date createDate;//投诉时间
	private Date dealDate;//投诉时间
	private ID userId;//投诉用户
	private ID refId;//投诉对象
	private boolean deal = false;//投诉是否处理
	private String dealContent;//处理内容

	public ComplaintBean() {
		this.createDate = new Date();
		this.dealDate = new Date();
	}

	public void setDealDate(Date dealDate) {
		this.dealDate = dealDate;
	}

	public Date getDealDate() {
		return dealDate == null ? new Date() : dealDate;
	}

	public void setDeal(boolean deal) {
		this.deal = deal;
	}

	public boolean isDeal() {
		return deal;
	}

	public void setDealContent(String dealContent) {
		this.dealContent = dealContent;
	}

	public String getDealContent() {
		return dealContent;
	}

	public EFunctionModule getRefModule() {
		return refModule;
	}

	public void setRefModule(EFunctionModule refModule) {
		this.refModule = refModule;
	}

	public EComplaint getComplaint() {
		return complaint;
	}

	public void setComplaint(EComplaint complaint) {
		this.complaint = complaint;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public ID getUserId() {
		return userId;
	}

	public void setUserId(ID userId) {
		this.userId = userId;
	}

	public ID getRefId() {
		return refId;
	}

	public void setRefId(ID refId) {
		this.refId = refId;
	}

	@Override
	public String getUserText() {
		final IUser user = SimpleosUtil.getUserById(getUserId());
		return user != null ? user.getText() : IConstants.HTML_BLANK_STRING;
	}

	public String wrapRefTitle(final PageRequestResponse requestResponse) {
		return "";
	}
}
