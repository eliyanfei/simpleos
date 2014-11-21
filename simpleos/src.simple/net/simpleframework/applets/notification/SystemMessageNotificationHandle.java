package net.simpleframework.applets.notification;

import java.util.Collection;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.applets.notification.component.window.DefaultMessageWindowHandle;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class SystemMessageNotificationHandle extends DefaultMessageWindowHandle {

	@Override
	public Collection<? extends IMessageNotification> getMessageNotifications(
			final ComponentParameter compParameter) {
		final IAccount account = AccountSession.getLogin(compParameter.getSession());
		if (account != null) {
			return IDataObjectQuery.Utils.toList(NotificationUtils.getTableEntityManager(
					SystemMessageNotification.class).query(
					new ExpressionValue("toid=?", new Object[] { account.getId() }),
					SystemMessageNotification.class));
		}
		return null;
	}
}
