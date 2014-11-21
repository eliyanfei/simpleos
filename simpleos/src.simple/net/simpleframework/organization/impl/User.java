package net.simpleframework.organization.impl;

import java.io.InputStream;
import java.util.Collection;
import java.util.Date;

import net.simpleframework.core.id.ID;
import net.simpleframework.organization.EUserStatus;
import net.simpleframework.organization.IDepartment;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.sysmgr.dict.DictUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class User extends AbstractOrderBean implements IUser {
	private static final long serialVersionUID = -4938630954415307539L;

	private ID departmentId;

	private EUserStatus status;

	@Override
	public EUserStatus getStatus() {
		return status == null ? EUserStatus.normal : status;
	}

	@Override
	public void setStatus(final EUserStatus status) {
		this.status = status;
	}

	@Override
	public ID getDepartmentId() {
		return departmentId;
	}

	@Override
	public void setDepartmentId(final ID departmentId) {
		this.departmentId = departmentId;
	}

	/* ----------------------基本资料------------------------- */
	/** 性别 **/
	private String sex;

	/** 生日 **/
	private Date birthday;

	/** 血型 **/
	private EBlood blood;

	/** 所在地 **/
	private String hometown;

	/* ----------------------联系方式------------------------- */
	/** 邮件 **/
	private String email;

	/** 家庭电话 **/
	private String homePhone;

	/** 办公电话 **/
	private String officePhone;

	/** 移动电话 **/
	private String mobile;
	/**个人主页**/
	private String homepage;

	/** 地址 **/
	private String address;

	/** 邮政编码 **/
	private String postcode;
	/**
	 * 个性签名
	 */
	private String signature;

	private String qq;

	private String msn;
	/**
	 * 是否内置
	 */
	private boolean buildIn = false;

	public String getSignature() {
		return signature;
	}

	public boolean isBuildIn() {
		return buildIn;
	}

	public void setBuildIn(boolean buildIn) {
		this.buildIn = buildIn;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	@Override
	public String getSex() {
		return sex;
	}

	@Override
	public Date getBirthday() {
		return birthday;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getHomepage() {
		return homepage;
	}

	@Override
	public void setBirthday(final Date birthday) {
		this.birthday = birthday;
	}

	public void setSex(final String sex) {
		this.sex = sex;
	}

	@Override
	public String getHometown() {
		return hometown;
	}

	@Override
	public String getHometownText() {
		return DictUtils.buildSysDict(this.hometown);
	}

	public void setHometown(final String hometown) {
		this.hometown = hometown;
	}

	public EBlood getBlood() {
		return blood;
	}

	public void setBlood(final EBlood blood) {
		this.blood = blood;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public void setEmail(final String email) {
		this.email = email;
	}

	@Override
	public String getMobile() {
		return mobile;
	}

	@Override
	public void setMobile(final String mobile) {
		this.mobile = mobile;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(final String homePhone) {
		this.homePhone = homePhone;
	}

	public String getOfficePhone() {
		return officePhone;
	}

	public void setOfficePhone(final String officePhone) {
		this.officePhone = officePhone;
	}

	@Override
	public String getAddress() {
		return address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(final String postcode) {
		this.postcode = postcode;
	}

	@Override
	public String getQq() {
		return qq;
	}

	public void setQq(final String qq) {
		this.qq = qq;
	}

	@Override
	public String getMsn() {
		return msn;
	}

	public void setMsn(final String msn) {
		this.msn = msn;
	}

	@Override
	public InputStream getPhoto() {
		return OrgUtils.um().getPhotoLob(this);
	}

	/*----------------------------------关联操作 --------------------------------*/

	@Override
	public IAccount account() {
		return OrgUtils.am().queryForObjectById(getId());
	}

	@Override
	public IDepartment department() {
		return OrgUtils.dm().queryForObjectById(departmentId);
	}

	@Override
	public Collection<IJob> jobs() {
		return OrgUtils.um().jobs(this, true);
	}

	@Override
	public IJob primary() {
		return OrgUtils.um().primary(this);
	}
}
