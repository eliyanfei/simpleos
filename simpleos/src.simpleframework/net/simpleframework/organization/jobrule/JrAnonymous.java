package net.simpleframework.organization.jobrule;

import java.util.Collection;

import net.simpleframework.organization.IJobRule;
import net.simpleframework.organization.IUser;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class JrAnonymous implements IJobRule {
	public static JrAnonymous jr = new JrAnonymous();

	@Override
	public boolean isMember(final IUser user, final Object... objects) {
		return true;
	}

	@Override
	public Collection<? extends IUser> members(final Object... objects) {
		return null;
	}
}
