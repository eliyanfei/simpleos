package net.simpleframework.organization.component.userpager;

import java.util.Collection;

import net.simpleframework.organization.IUser;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.db.IDbTablePagerHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IUserPagerHandle extends IDbTablePagerHandle {

	void doSentMail(ComponentParameter compParameter, Collection<IUser> users, String topic,
			String content);
}
