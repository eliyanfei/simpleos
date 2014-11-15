package net.simpleframework.workflow;

import java.util.Date;

import net.simpleframework.core.IApplicationModule;
import net.simpleframework.core.IApplicationModuleAware;
import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.id.ID;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public abstract class AbstractWorkflowBean extends AbstractIdDataObjectBean implements
		IApplicationModuleAware {
	private ID userId;

	private Date createDate;

	private ID lastUserId;

	private Date lastUpdate;

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

	public ID getLastUserId() {
		return lastUserId;
	}

	public void setLastUserId(final ID lastUserId) {
		this.lastUserId = lastUserId;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(final Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Override
	public IApplicationModule getApplicationModule() {
		return WorkflowUtils.applicationModule;
	}
}
