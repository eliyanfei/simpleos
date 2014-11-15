package net.prj.mvc.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import net.a.ItSiteUtil;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.my.message.EMessageType;
import net.simpleframework.my.message.MessageUtils;
import net.simpleframework.my.message.SimpleMessage;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;

import org.springframework.util.StringUtils;

/**
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-16上午09:14:06
 */
public class MyMessageAction extends AbstractAjaxRequestHandle {
	/**
	 * 删除选择的消息
	 * @param compParameter
	 * @return
	 */
	public IForward deleteMyMessage(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				final String ids = compParameter.getParameter("ids");
				if (StringUtils.hasText(ids)) {
					ITableEntityManager manager = MessageUtils.getTableEntityManager(SimpleMessage.class);
					for (String id : ids.split(",")) {
						SimpleMessage simpleMessage = manager.queryForObjectById(id, SimpleMessage.class);
						if (simpleMessage != null
								&& ItSiteUtil.isManageOrSelf(compParameter, MessageUtils.applicationModule, simpleMessage.getToId())) {
							manager.delete(new ExpressionValue("id=" + simpleMessage.getId()));
						}
					}
				}
			}
		});
	}

	/**
	 * 设置为已读，或未读
	 * @param compParameter
	 * @return
	 */
	public IForward messageread(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				final String ids = compParameter.getParameter("ids");
				final String messageread = compParameter.getParameter("messageread");
				if (StringUtils.hasText(ids)) {
					ITableEntityManager manager = MessageUtils.getTableEntityManager(SimpleMessage.class);
					for (String id : ids.split(",")) {
						SimpleMessage simpleMessage = manager.queryForObjectById(id, SimpleMessage.class);
						if (simpleMessage != null
								&& ItSiteUtil.isManageOrSelf(compParameter, MessageUtils.applicationModule, simpleMessage.getToId())) {
							simpleMessage.setMessageRead(ConvertUtils.toBoolean(messageread, false));
							manager.update(new Object[] { "messageRead" }, simpleMessage);
						}
					}
				}
			}
		});
	}

	/**
	 * 全部设置为已读
	 * @param compParameter
	 * @return
	 */
	public IForward messagereadAll(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				final IAccount account = ItSiteUtil.getLoginAccount(compParameter);
				if (account == null)
					return;
				final int messageType = ConvertUtils.toInt(compParameter.getParameter("messageType"), -1);
				ITableEntityManager manager = MessageUtils.getTableEntityManager(SimpleMessage.class);
				manager.execute(new SQLValue("update simple_my_message set messageRead=1 where messageType=" + messageType
						+ " and  messageread=0 and toid=" + account.getId()));
			}
		});
	}

	/**
	 * 发送消息
	 * @param compParameter
	 * @return
	 */
	public IForward sentMessage(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final IAccount login = AccountSession.getLogin(compParameter.getSession());
				if (login == null) {
					return;
				}
				final ArrayList<SimpleMessage> al = new ArrayList<SimpleMessage>();
				final String[] users = StringUtils.split(compParameter.getRequestParameter("mm_toid"), ",");
				final String textBody = compParameter.getRequestParameter("mm_textBody");
				final String subject = compParameter.getRequestParameter("mm_subject");
				if (users != null && users.length > 0 && StringUtils.hasText(textBody)) {
					for (final String user : users) {
						final IUser userObject = OrgUtils.um().getUserByName(user);
						if (userObject != null) {
							final SimpleMessage sMessage = new SimpleMessage();
							sMessage.setMessageType(EMessageType.user);
							sMessage.setSentId(login.getId());
							sMessage.setSentDate(new Date());
							sMessage.setSubject(subject);
							sMessage.setTextBody(textBody);
							sMessage.setToId(userObject.getId());
							al.add(sMessage);
						}
					}
				}
			}
		});
	}
}
