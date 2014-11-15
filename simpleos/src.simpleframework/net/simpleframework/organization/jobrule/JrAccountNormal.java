package net.simpleframework.organization.jobrule;

import net.simpleframework.organization.IUser;
import net.simpleframework.organization.account.EAccountStatus;
import net.simpleframework.organization.account.IAccount;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class JrAccountNormal extends JrAnonymous {
	public static JrAccountNormal jr = new JrAccountNormal();

	@Override
	public boolean isMember(final IUser user, final Object... objects) {
		if (user != null) {
			final IAccount account = user.account();
			return account != null && account.getStatus() == EAccountStatus.normal;
		}
		return false;
	}
}
