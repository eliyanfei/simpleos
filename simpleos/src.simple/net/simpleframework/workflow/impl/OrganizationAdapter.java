package net.simpleframework.workflow.impl;

import net.simpleframework.core.ALoggerAware;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class OrganizationAdapter extends ALoggerAware {

	public IJob job(final Object id) {
		return OrgUtils.jm().queryForObjectById(id);
	}

	public IUser user(final Object id) {
		IUser user = OrgUtils.um().queryForObjectById(id);
		if (user == null) {
			user = OrgUtils.um().getUserByName(String.valueOf(id));
		}
		return user;
	}
}
