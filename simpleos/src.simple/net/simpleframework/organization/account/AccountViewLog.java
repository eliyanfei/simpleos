package net.simpleframework.organization.account;

import java.util.Date;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.id.ID;
import net.simpleframework.web.EFunctionModule;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class AccountViewLog extends AbstractIdDataObjectBean {
	private ID accountId;

	private ID viewId;

	private EFunctionModule vtype; //

	private Date createDate, lastUpdate;

	public ID getAccountId() {
		return accountId;
	}

	public void setAccountId(final ID accountId) {
		this.accountId = accountId;
	}

	public ID getViewId() {
		return viewId;
	}

	public void setViewId(final ID viewId) {
		this.viewId = viewId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(final Date createDate) {
		this.createDate = createDate;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(final Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public EFunctionModule getVtype() {
		return vtype;
	}

	public void setVtype(final EFunctionModule vtype) {
		this.vtype = vtype;
	}

	private static final long serialVersionUID = -5438572144680249880L;
}
