package net.simpleframework.organization.jobrule;

import net.simpleframework.organization.IUser;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class JrAdmin extends JrAnonymous {
	public static JrAdmin jr = new JrAdmin();

	@Override
	public boolean isMember(final IUser user, final Object... objects) {
		if (user != null) {
			if (user.getName().equals(IUser.admin)) {
				return true;
			}
		}
		return false;
	}
}
