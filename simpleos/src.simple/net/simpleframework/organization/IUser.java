package net.simpleframework.organization;

import java.io.InputStream;
import java.util.Collection;
import java.util.Date;

import net.simpleframework.core.bean.IDescriptionBeanAware;
import net.simpleframework.core.bean.ITextBeanAware;
import net.simpleframework.core.bean.IUniqueNameBeanAware;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.account.IAccount;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IUser extends IUniqueNameBeanAware, ITextBeanAware, IDescriptionBeanAware {
	static final String admin = "admin";

	static final String birthdayDateFormat = "yyyy-MM-dd";

	public static enum EBlood {
		o,

		a,

		b,

		ab,

		other
	}

	EUserStatus getStatus();

	void setStatus(EUserStatus status);

	ID getDepartmentId();

	void setDepartmentId(final ID departmentId);

	String getEmail();

	void setEmail(String email);

	String getMobile();

	void setMobile(final String mobile);

	Date getBirthday();

	void setBirthday(final Date birthday);

	String getHomepage();

	void setHomepage(final String homepage);

	String getSex();

	void setSex(final String sex);

	String getAddress();

	void setAddress(final String address);

	String getQq();

	void setQq(final String qq);

	String getMsn();

	void setMsn(final String msn);

	InputStream getPhoto();

	String getHometown();

	String getHometownText();

	void setHometown(final String hometown);

	String getPostcode();

	void setPostcode(final String postcode);

	EBlood getBlood();

	void setBlood(final EBlood blood);

	String getHomePhone();

	void setHomePhone(final String homePhone);

	String getOfficePhone();

	void setOfficePhone(final String officePhone);

	String getSignature();

	void setSignature(String signature);

	/*----------------------------------关联操作 --------------------------------*/

	IAccount account();

	IDepartment department();

	Collection<IJob> jobs();

	IJob primary();
}