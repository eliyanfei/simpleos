package net.simpleframework.organization.account;

import java.util.Date;

import net.simpleframework.core.bean.IIdBeanAware;
import net.simpleframework.organization.IUser;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IAccount extends IIdBeanAware {
	/* 密码 */
	String getPassword();

	void setPassword(final String password);

	/* 状态 */
	EAccountStatus getStatus();

	void setStatus(final EAccountStatus status);

	/* 是否登录 */
	boolean isLogin();

	void setLogin(boolean login);

	/* 创建时间 */
	Date getCreateDate();

	void setCreateDate(final Date createDate);

	/* 最后一次登录时间 */
	Date getLastLoginDate();

	void setLastLoginDate(final Date lastLoginDate);

	/* 最后一次登录IP */
	String getLastLoginIP();

	void setLastLoginIP(final String lastLoginIP);

	/* 总登录次数 */
	int getLoginTimes();

	void setLoginTimes(final int loginTimes);

	/* 总在线时间 */
	long getOnlineMillis();

	void setOnlineMillis(final long onlineMillis);

	boolean isMailbinding();

	void setMailbinding(boolean mailbinding);

	boolean isMobilebinding();

	void setMobilebinding(boolean mobilebinding);

	int getPoints();

	void setPoints(final int points);

	int getExp();

	void setExp(int exp);

	int getReputation();

	void setReputation(final int reputation);

	int getMoney();

	void setMoney(final int money);

	public String getSkin();

	public void setSkin(String skin);

	public String getTheme();

	public void setTheme(String theme);

	boolean isBlacklist();

	void setBlacklist(boolean blacklist);

	/*----------------------------------关联操作 --------------------------------*/

	public abstract static class InsertCallback {
		public void init(final IAccount account) {
		}

		public abstract void insert(final IUser user);
	}

	IUser user();
}
