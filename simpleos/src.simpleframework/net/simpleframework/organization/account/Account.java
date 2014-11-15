package net.simpleframework.organization.account;

import java.util.Date;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.SkinUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class Account extends AbstractIdDataObjectBean implements IAccount {
	private static final long serialVersionUID = -2003319378229277570L;

	/* 密码 */
	private String password;

	/* 状态 */
	private EAccountStatus status;

	/* 是否登录 */
	private boolean login;

	/* 创建时间 */
	private Date createDate;

	/* 最后一次登录时间 */
	private Date lastLoginDate;

	/* 最后一次登录IP */
	private String lastLoginIP;

	/* 总登录次数 */
	private int loginTimes;

	/* 总在线时间 */
	private long onlineMillis;

	/* 是否绑定邮件 */
	private boolean mailbinding;

	/* 是否绑定手机号 */
	private boolean mobilebinding;

	/* 积分 */
	private int points;

	/* 经验值 */
	private int exp;

	/* 信誉 */
	private int reputation;

	/* 金钱 */
	private int money;

	//黑名单
	private boolean blacklist;
	//主题
	private String theme;
	//皮肤
	private String skin = SkinUtils.DEFAULT_SKIN;

	public boolean isBlacklist() {
		return blacklist;
	}

	public void setBlacklist(boolean blacklist) {
		this.blacklist = blacklist;
	}

	@Override
	public String getPassword() {
		return StringUtils.blank(password);
	}

	@Override
	public void setPassword(final String password) {
		this.password = password;
	}

	@Override
	public EAccountStatus getStatus() {
		return status;
	}

	@Override
	public void setStatus(final EAccountStatus status) {
		this.status = status;
	}

	@Override
	public boolean isLogin() {
		return login;
	}

	@Override
	public void setLogin(final boolean login) {
		this.login = login;
	}

	@Override
	public int getLoginTimes() {
		return loginTimes;
	}

	@Override
	public void setLoginTimes(final int loginTimes) {
		this.loginTimes = loginTimes;
	}

	@Override
	public Date getCreateDate() {
		return createDate;
	}

	@Override
	public void setCreateDate(final Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	@Override
	public void setLastLoginDate(final Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	@Override
	public String getLastLoginIP() {
		return lastLoginIP;
	}

	@Override
	public void setLastLoginIP(final String lastLoginIP) {
		this.lastLoginIP = lastLoginIP;
	}

	@Override
	public long getOnlineMillis() {
		return onlineMillis;
	}

	@Override
	public void setOnlineMillis(final long onlineMillis) {
		this.onlineMillis = onlineMillis;
	}

	@Override
	public boolean isMailbinding() {
		return mailbinding;
	}

	@Override
	public void setMailbinding(final boolean mailbinding) {
		this.mailbinding = mailbinding;
	}

	@Override
	public boolean isMobilebinding() {
		return mobilebinding;
	}

	@Override
	public void setMobilebinding(final boolean mobilebinding) {
		this.mobilebinding = mobilebinding;
	}

	@Override
	public int getPoints() {
		return points;
	}

	@Override
	public void setPoints(final int points) {
		this.points = points;
	}

	@Override
	public int getExp() {
		return exp;
	}

	@Override
	public void setExp(final int exp) {
		this.exp = exp;
	}

	@Override
	public int getReputation() {
		return reputation;
	}

	@Override
	public void setReputation(final int reputation) {
		this.reputation = reputation;
	}

	@Override
	public int getMoney() {
		return money;
	}

	@Override
	public void setMoney(final int money) {
		this.money = money;
	}

	@Override
	public String toString() {
		final IUser user = user();
		return user != null ? user.getName() : super.toString();
	}

	/*----------------------------------关联操作 --------------------------------*/

	@Override
	public IUser user() {
		return OrgUtils.um().queryForObjectById(getId());
	}

	@Override
	public String getTheme() {
		return theme;
	}

	@Override
	public void setTheme(String theme) {
		this.theme = theme;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}

	public String getSkin() {
		return skin;
	}
}
