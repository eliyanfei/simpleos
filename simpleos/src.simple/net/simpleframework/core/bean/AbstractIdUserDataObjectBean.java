package net.simpleframework.core.bean;

import net.simpleframework.core.id.ID;
import net.simpleframework.organization.IUserBeanAware;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public abstract class AbstractIdUserDataObjectBean extends AbstractIdDataObjectBean implements IUserBeanAware {
	private ID userId;

	@Override
	public ID getUserId() {
		return userId;
	}

	@Override
	public void setUserId(ID userId) {
		this.userId = userId;
	}

	public String getUserText() {
		return getUserText(userId);
	}

}
