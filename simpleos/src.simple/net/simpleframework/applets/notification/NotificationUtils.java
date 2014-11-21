package net.simpleframework.applets.notification;

import java.util.Date;

import net.simpleframework.ado.DataObjectManagerUtils;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.IUser;
import net.simpleframework.util.StringUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class NotificationUtils {
	public static INotificationApplicationModule applicationModule;

	public static ITableEntityManager getTableEntityManager(final Class<?> beanClazz) {
		return DataObjectManagerUtils.getTableEntityManager(applicationModule, beanClazz);
	}

	public static ITableEntityManager getTableEntityManager() {
		return DataObjectManagerUtils.getTableEntityManager(applicationModule);
	}

	public static void sendMessage(final AbstractMessageNotification messageNotification) {
		applicationModule.sendMessage(new ISendCallback() {
			@Override
			public AbstractMessageNotification getMessageNotification() {
				return messageNotification;
			}

			@Override
			public boolean isSubscribeNotification(final IUser user) {
				return true;
			}
		});
	}

	public static SystemMessageNotification createSystemMessageNotification(final ID sentId, final ID toId, final String subject,
			final String textBody, final ID messageId) {
		final ITableEntityManager manager = getTableEntityManager(SystemMessageNotification.class);
		SystemMessageNotification messageNotification = manager.queryForObjectById(messageId, SystemMessageNotification.class);
		if (messageNotification == null) {
			messageNotification = new SystemMessageNotification();
			messageNotification.setMessageId(messageId);
			messageNotification.setSentId(sentId);
			messageNotification.setSentDate(new Date());
			messageNotification.setToId(toId);
			messageNotification.setSubject(subject);
			messageNotification.setTextBody(textBody);
			manager.insert(messageNotification);
		}
		return messageNotification;
	}

	public static void updateMessageNotification(final Object messageId, final String subject, final String textBody) {
		final ITableEntityManager manager = getTableEntityManager(SystemMessageNotification.class);
		final SystemMessageNotification messageNotification = manager.queryForObject(new ExpressionValue("messageid=?", new Object[] { messageId }),
				SystemMessageNotification.class);
		if (messageNotification != null) {
			final ID sentId = messageNotification.getSentId();
			final ID toId = messageNotification.getToId();
			messageNotification.setToId(sentId);
			messageNotification.setSentId(toId);
			if (StringUtils.hasText(subject)) {
				messageNotification.setSubject(subject);
			}
			if (StringUtils.hasText(textBody)) {
				messageNotification.setTextBody(textBody);
			}
			messageNotification.setSentDate(new Date());
			manager.update(messageNotification);
		}
	}

	public static int deleteMessageNotificationByMessageId(final Object messageId) {
		return getTableEntityManager(SystemMessageNotification.class).delete(new ExpressionValue("messageid=?", new Object[] { messageId }));
	}

	public static int deleteMessageNotificationByMessageIdAndMe(final Object messageId, final Object userId) {
		return getTableEntityManager(SystemMessageNotification.class).delete(
				new ExpressionValue("messageid=? and toId=?", new Object[] { messageId, userId }));
	}

	public static int deleteMessageNotificationByNotificationId(final Object notificationId) {
		return getTableEntityManager(SystemMessageNotification.class).delete(new ExpressionValue("id=?", new Object[] { notificationId }));
	}
}
