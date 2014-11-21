package net.simpleframework.my.friends;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.simpleframework.ado.DataObjectManagerUtils;
import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.applets.invite.InviteUtils;
import net.simpleframework.applets.notification.NotificationUtils;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ui.tabs.TabHref;
import net.simpleframework.web.page.component.ui.tabs.TabsUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class FriendsUtils {
	public static IFriendsApplicationModule applicationModule;

	public static String deployPath;

	public static ITableEntityManager getTableEntityManager(final Class<?> beanClazz) {
		return DataObjectManagerUtils.getTableEntityManager(applicationModule, beanClazz);
	}

	public static ITableEntityManager getTableEntityManager() {
		return DataObjectManagerUtils.getTableEntityManager(applicationModule);
	}

	static FriendsRequest addFriendsRequest(final PageRequestResponse requestResponse,
			final Object toId, final String messageText, final Object groupId) {
		final IAccount login = AccountSession.getLogin(requestResponse.getSession());
		if (login == null) {
			return null;
		}
		final IUser toUser = OrgUtils.um().queryForObjectById(toId);
		if (toUser == null) {
			return null;
		}
		final FriendsRequest fr = new FriendsRequest();
		fr.setSentId(login.getId());
		fr.setSentDate(new Date());
		fr.setMessageText(messageText);
		fr.setToId(toUser.getId());
		final FriendsGroup fg = getTableEntityManager(FriendsGroup.class).queryForObjectById(groupId,
				FriendsGroup.class);
		if (fg != null) {
			fr.setGroupId(fg.getId());
		}

		final ITableEntityManager fr_mgr = getTableEntityManager(FriendsRequest.class);
		fr_mgr.insertTransaction(fr, new TableEntityAdapter() {
			@Override
			public void afterInsert(final ITableEntityManager manager, final Object[] objects) {
				/** 加入到通知队列 */
				final IUser sender = login.user();
				if (sender == null) {
					return;
				}
				final String subject = LocaleI18n.getMessage("FriendsUtils.2", sender.getText());
				final StringBuilder sb = new StringBuilder();
				sb.append("<p>").append("<a class=\"a2\" href=\"")
						.append(applicationModule.getFriendsRequestUrl(null)).append("\">")
						.append(LocaleI18n.getMessage("FriendsUtils.3", sender.getText())).append("</a>")
						.append("</p>");
				if (StringUtils.hasText(messageText)) {
					sb.append("<p>").append(messageText).append("</p>");
				}
				sb.append("<div>").append(ConvertUtils.toDateString(fr.getSentDate())).append("</div>");
				NotificationUtils.createSystemMessageNotification(login.getId(), fr.getToId(), subject,
						sb.toString(), fr.getId());
			}
		});
		return fr;
	}

	static void deleteFriendsRequest(final PageRequestResponse requestResponse, final Object rId) {
		if (rId == null) {
			return;
		}
		getTableEntityManager(FriendsRequest.class).deleteTransaction(
				new ExpressionValue("id=?", new Object[] { rId }), new TableEntityAdapter() {
					@Override
					public void afterDelete(final ITableEntityManager manager,
							final IDataObjectValue dataObjectValue) {
						NotificationUtils.deleteMessageNotificationByMessageId(rId);
					}
				});
	}

	static void setFriendsRequest(final PageRequestResponse requestResponse, final Object rId,
			final ERequestStatus status) {
		if (rId == null || status == null) {
			return;
		}
		final ITableEntityManager temgr = getTableEntityManager(FriendsRequest.class);
		final FriendsRequest fr = temgr.queryForObjectById(rId, FriendsRequest.class);
		if (fr == null) {
			return;
		}
		fr.setRequestStatus(status);
		temgr.updateTransaction(fr, new TableEntityAdapter() {
			@Override
			public void afterUpdate(final ITableEntityManager manager, final Object[] objects) {
				if (status == ERequestStatus.yes) {
					final Friends friends = new Friends();
					friends.setUserId(fr.getSentId());
					friends.setFriendId(fr.getToId());

					final ITableEntityManager fg_mgr = getTableEntityManager(FriendsGroup.class);
					final FriendsGroup fg = fg_mgr.queryForObjectById(fr.getGroupId(),
							FriendsGroup.class);
					if (fg != null) {
						friends.setGroupId(fg.getId());
						fg.setFriends(fg.getFriends() + 1);
						fg_mgr.update(new String[] { "friends" }, fg);
					}

					final Friends friends2 = new Friends();
					friends2.setUserId(fr.getToId());
					friends2.setFriendId(fr.getSentId());
					getTableEntityManager(Friends.class).insert(new Object[] { friends, friends2 });
				}
				NotificationUtils.deleteMessageNotificationByMessageId(rId);
			}
		});
	}

	public static boolean isFriend(final PageRequestResponse requestResponse, final Object friendId) {
		final IAccount login = AccountSession.getLogin(requestResponse.getSession());
		if (login == null) {
			return false;
		}
		return getTableEntityManager(Friends.class)
				.getCount(
						new ExpressionValue("userid=? and friendid=?", new Object[] { login.getId(),
								friendId })) > 0;
	}

	static void deleteFriend(final PageRequestResponse requestResponse, final Object friendId) {
		getTableEntityManager(Friends.class).delete(
				new ExpressionValue("id=?", new Object[] { friendId }));
	}

	public static String tabs(final PageRequestResponse requestResponse) {
		final String frUrl = applicationModule.getFriendsRequestUrl(requestResponse);
		return TabsUtils
				.tabs(requestResponse,
						new TabHref("#(FriendsUtils.0)", applicationModule
								.getApplicationUrl(requestResponse)), new TabHref("#(FriendsUtils.4)",
								applicationModule.getFriendsSearchUrl(requestResponse)), new TabHref(
								"#(FriendsUtils.1)", frUrl),
						new TabHref("#(FriendsUtils.5)", WebUtils.addParameters(frUrl, "op=my")),
						new TabHref(InviteUtils.applicationModule.getApplicationText(),
								InviteUtils.applicationModule.getApplicationUrl(requestResponse)));
	}

	public static boolean isMyFriendsRequest(final HttpServletRequest request) {
		return "my".equals(request.getParameter("op"));
	}

	static void doStatRebuild() {
		final ITableEntityManager friends_mgr = getTableEntityManager(Friends.class);
		final ITableEntityManager friends_group_mgr = getTableEntityManager(FriendsGroup.class);
		friends_group_mgr.execute(new SQLValue("update " + friends_group_mgr.getTablename()
				+ " t set friends=(select count(*) from " + friends_mgr.getTablename()
				+ " where groupId=t.id)"));
		friends_group_mgr.reset();
	}
}
