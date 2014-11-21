package net.itsite.docu;

import java.util.Date;

import net.itsite.ItSiteUtil;
import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.IUserBeanAware;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月21日 下午4:17:41 
 * @Description: 用户下载信息
 *
 */
public class DocuLogBean extends AbstractIdDataObjectBean implements IUserBeanAware {
	private ID userId;//下载者
	private ID docuId;//下载文档
	private Date downDate;//下载日期
	public String ip;

	public DocuLogBean() {
		this.downDate = new Date();
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		return ip;
	}

	public ID getUserId() {
		return userId;
	}

	public void setUserId(ID userId) {
		this.userId = userId;
	}

	public ID getDocuId() {
		return docuId;
	}

	public void setDocuId(ID docuId) {
		this.docuId = docuId;
	}

	public Date getDownDate() {
		return downDate;
	}

	public void setDownDate(Date downDate) {
		this.downDate = downDate;
	}

	@Override
	public String getUserText() {
		final IUser user = ItSiteUtil.getUserById(userId);
		return user == null ? "" : user.getText();
	}

}
