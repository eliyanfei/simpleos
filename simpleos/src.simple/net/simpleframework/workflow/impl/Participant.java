package net.simpleframework.workflow.impl;

import net.simpleframework.organization.IJob;
import net.simpleframework.organization.IUser;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class Participant {
	IUser user;

	IJob job;

	public Participant(final IUser user, final IJob job) {
		this.user = user;
		this.job = job == null ? user.primary() : job;
	}

	public Participant(final IUser user) {
		this(user, null);
	}
}
