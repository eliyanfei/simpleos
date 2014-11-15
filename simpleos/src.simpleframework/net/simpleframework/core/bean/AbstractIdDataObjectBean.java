package net.simpleframework.core.bean;

import net.a.ItSiteUtil;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public abstract class AbstractIdDataObjectBean extends AbstractDataObjectBean implements IIdBeanAware {
	private ID id;

	@Override
	public ID getId() {
		return id;
	}

	@Override
	public boolean isBuildIn() {
		return false;
	}

	@Override
	public void setId(final ID id) {
		this.id = id;
	}

	public void setObject(Object obj) {
	}

	public String getUserText(final ID userId) {
		return getUserText(userId, false);
	}

	public String getUserText(final ID userId, boolean title) {
		IUser user = OrgUtils.um().queryForObjectById(userId);
		if (user == null)
			return ItSiteUtil.anonymity;
		if (title)
			return ("<span title='" + userId + "'>" + user.getText() + "</span>");
		return user.getText();
	}

	@Override
	public boolean equals2(final IIdBeanAware idBean) {
		return idBean != null && idBean.getId().equals2(getId());
	}
}
