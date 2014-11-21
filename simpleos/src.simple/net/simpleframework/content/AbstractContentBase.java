package net.simpleframework.content;

import java.util.Date;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.bean.IOrderBeanAware;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.IUserBeanAware;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.web.page.PageRequestResponse;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public abstract class AbstractContentBase extends AbstractIdDataObjectBean implements
		IOrderBeanAware, IUserBeanAware {

	private Date createDate;

	private ID userId;

	private Date lastUpdate;

	private ID lastUserId;

	private EContentStatus status;

	private long oorder;

	public Date getCreateDate() {
		if (createDate == null) {
			createDate = new Date();
		}
		return createDate;
	}

	public void setCreateDate(final Date createDate) {
		this.createDate = createDate;
	}

	public Date getLastUpdate() {
		return lastUpdate != null ? lastUpdate : getCreateDate();
	}

	public void setLastUpdate(final Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public ID getLastUserId() {
		return lastUserId;
	}

	public void setLastUserId(final ID lastUserId) {
		this.lastUserId = lastUserId;
	}

	@Override
	public ID getUserId() {
		return userId;
	}

	@Override
	public String getUserText() {
		final IUser user = OrgUtils.um().queryForObjectById(getUserId());
		return user != null ? user.getText() : "";
	}

	@Override
	public void setUserId(final ID userId) {
		this.userId = userId;
	}

	public EContentStatus getStatus() {
		return status == null ? EContentStatus.edit : status;
	}

	public void setStatus(final EContentStatus status) {
		this.status = status;
	}

	@Override
	public long getOorder() {
		return oorder;
	}

	@Override
	public void setOorder(final long oorder) {
		this.oorder = oorder;
	}

	public void initThis(final PageRequestResponse requestResponse) {
		setCreateDate(new Date());
		final IAccount account = AccountSession.getLogin(requestResponse.getSession());
		if (account != null) {
			setUserId(account.getId());
		}
	}

	public void updateLastUpdate(final PageRequestResponse requestResponse) {
		setLastUpdate(new Date());
		final IAccount account = AccountSession.getLogin(requestResponse.getSession());
		if (account != null) {
			setLastUserId(account.getId());
		}
	}
}
