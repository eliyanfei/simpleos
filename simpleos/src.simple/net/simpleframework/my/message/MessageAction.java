package net.simpleframework.my.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.applets.notification.NotificationUtils;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MessageAction extends AbstractAjaxRequestHandle {

	public IForward sentMessage(final ComponentParameter compParameter) {
		final IAccount login = AccountSession.getLogin(compParameter.getSession());
		if (login == null) {
			return null;
		}
		final ArrayList<SimpleMessage> al = new ArrayList<SimpleMessage>();
		final String[] users = StringUtils.split(compParameter.getRequestParameter("textMessageUsers"), ",");
		final String textBody = compParameter.getRequestParameter("textareaMessageEditor");
		if (users != null && users.length > 0 && StringUtils.hasText(textBody)) {
			for (final String user : users) {
				final IUser userObject = OrgUtils.um().getUserByName(user);
				if (userObject != null) {
					final SimpleMessage sMessage = new SimpleMessage();
					sMessage.setMessageType(EMessageType.user);
					sMessage.setSentId(login.getId());
					sMessage.setSentDate(new Date());
					sMessage.setTextBody(textBody);
					sMessage.setToId(userObject.getId());
					al.add(sMessage);
				}
			}
			if (al.size() > 0) {
				final ITableEntityManager temgr = MessageUtils.getTableEntityManager(SimpleMessage.class);
				temgr.insertTransaction(al.toArray(), new TableEntityAdapter() {
					@Override
					public void afterInsert(final ITableEntityManager manager, final Object[] objects) {
						for (final Object object : objects) {
							final SimpleMessage sMessage = (SimpleMessage) object;
							final StringBuilder tb = new StringBuilder();
							try {
								tb.append("<p>")
										.append(LocaleI18n.getMessage("MessageAction.3", login.user(),
												ConvertUtils.toDateString(sMessage.getSentDate()))).append("</p>");
								tb.append("<a href=\"").append(MessageUtils.applicationModule.getApplicationUrl(compParameter)).append("\">")
										.append(LocaleI18n.getMessage("MessageAction.2")).append("</a>");
							} catch (final Exception e) {
								logger.warn(e);
							}
							NotificationUtils.createSystemMessageNotification(sMessage.getSentId(), sMessage.getToId(),
									LocaleI18n.getMessage("MessageAction.1"), tb.toString(), sMessage.getToId());
							compParameter.removeSessionAttribute(MyMessageList.SESSION_DELETE_MESSAGE);
						}
					}
				});
			}
		}
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				json.put("result", LocaleI18n.getMessage("MessageAction.0", al.size()));
			}
		});
	}

	private void sendSimpleMessage(final ComponentParameter compParameter, final ITableEntityManager temgr, final IUser user, final ID loginId,
			final String sentMessageTopic, final String textBody) {
		final SimpleMessage sMessage = new SimpleMessage();
		sMessage.setMessageType(EMessageType.broadcast);
		sMessage.setSentId(loginId);
		sMessage.setSubject(sentMessageTopic);
		sMessage.setSentDate(new Date());
		sMessage.setTextBody(textBody);
		sMessage.setToId(user.getId());
		temgr.insertTransaction(sMessage, new TableEntityAdapter() {
			@Override
			public void afterInsert(final ITableEntityManager manager, final Object[] objects) {
				/*for (final Object object : objects) {
					final SimpleMessage sMessage = (SimpleMessage) object;
					final StringBuilder tb = new StringBuilder();
					try {
						tb.append("<p>").append("广播通知" + "(" + ConvertUtils.toDateString(sMessage.getSentDate())).append(")</p>");
						tb.append("<a href=\"").append(MessageUtils.applicationModule.getSystemMessageUrl(compParameter)).append("\">")
								.append(sentMessageTopic).append("</a>");
						NotificationUtils.createSystemMessageNotification(sMessage.getSentId(), sMessage.getToId(),
								LocaleI18n.getMessage("MessageAction.1"), tb.toString(), sMessage.getToId());
						compParameter.removeSessionAttribute(MyMessageList.SESSION_DELETE_MESSAGE);
					} catch (final Exception e) {
						logger.warn(e);
					}
				}*/
			}
		});
	}

	public IForward sentNotification(final ComponentParameter compParameter) {
		final IAccount login = OrgUtils.am().getAccountByName("admin");
		if (login == null) {
			return null;
		}
		final String t = compParameter.getRequestParameter("t");
		final String sentMessageTopic = compParameter.getRequestParameter("sentMessageTopic");
		final String textBody = compParameter.getRequestParameter("textareaSentMessageHtmlEditor");
		final ITableEntityManager temgr = MessageUtils.getTableEntityManager(SimpleMessage.class);
		IUser user;
		if ("all".equals(t)) {
			IQueryEntitySet<IUser> qs = OrgUtils.um().query();
			while ((user = qs.next()) != null) {
				sendSimpleMessage(compParameter, temgr, user, login.getId(), sentMessageTopic, textBody);
			}
		} else if ("online".equals(t)) {
			IQueryEntitySet<IAccount> qs = OrgUtils.am().query(new ExpressionValue("login=?", new Object[] { true }));
			IAccount account;
			while ((account = qs.next()) != null) {
				sendSimpleMessage(compParameter, temgr, account.user(), login.getId(), sentMessageTopic, textBody);
			}
		} else {
			final String[] userArr = StringUtils.split(compParameter.getRequestParameter(OrgUtils.um().getUserIdParameterName()));
			if (userArr != null) {
				for (final String userId : userArr) {
					user = OrgUtils.um().queryForObjectById(userId);
					if (user != null) {
						sendSimpleMessage(compParameter, temgr, user, login.getId(), sentMessageTopic, textBody);
					}
				}
			}
		}
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				json.put("result", "通知已经发送!");
			}
		});
	}

	/**
	 * 确认，不在弹出消息
	 * @param compParameter
	 * @return
	 */
	public IForward okMessage(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(final Map<String, Object> json) throws Exception {
				final ITableEntityManager temgr = MessageUtils.getTableEntityManager(SimpleMessage.class);
				final SimpleMessage messageBean = temgr.queryForObjectById(compParameter.getRequestParameter("messageId"), SimpleMessage.class);
				if (messageBean != null) {
					messageBean.setMessageRead(true);
					temgr.update(new Object[] { "messageRead" }, messageBean);
					NotificationUtils.deleteMessageNotificationByMessageId(compParameter.getRequestParameter("messageId"));//删除通知
				}
			}
		});
	}

	public IForward deleteMessage(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				json.put(
						"result",
						MessageUtils.getTableEntityManager(SimpleMessage.class).delete(
								new ExpressionValue("id=?", new Object[] { compParameter.getRequestParameter("messageId") })) > 0);
			}
		});
	}
}
