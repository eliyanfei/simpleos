package net.simpleframework.my.message;

import java.util.Date;

import net.simpleframework.ado.DataObjectManagerUtils;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.core.id.ID;
import net.simpleframework.util.HTMLUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.tabs.TabHref;
import net.simpleframework.web.page.component.ui.tabs.TabsUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class MessageUtils {
	public static IMessageApplicationModule applicationModule;

	public static String deployPath;

	public static String getCssPath(final PageRequestResponse requestResponse) {
		final StringBuilder sb = new StringBuilder();
		sb.append(deployPath).append("css/").append(applicationModule.getSkin(requestResponse));
		return sb.toString();
	}

	public static ITableEntityManager getTableEntityManager(final Class<?> beanClazz) {
		return DataObjectManagerUtils.getTableEntityManager(applicationModule, beanClazz);
	}

	public static ITableEntityManager getTableEntityManager() {
		return DataObjectManagerUtils.getTableEntityManager(applicationModule);
	}

	public static void createNotifation(final ComponentParameter compParameter, final String topic, final String textBody, final ID loginId,
			final ID userId) {
		createNotifation(compParameter, topic, textBody, loginId, userId, EMessageType.notification);
	}

	public static void createNotifation(final ComponentParameter compParameter, final String topic, final String textBody, final ID loginId,
			final ID userId, EMessageType messageType) {
		try {
			final ITableEntityManager temgr = MessageUtils.getTableEntityManager(SimpleMessage.class);
			final SimpleMessage sMessage = new SimpleMessage();
			sMessage.setMessageType(messageType);
			sMessage.setSentId(loginId);
			sMessage.setSubject(HTMLUtils.stripScripts(topic));
			sMessage.setSentDate(new Date());
			sMessage.setTextBody(HTMLUtils.stripScripts(textBody));
			sMessage.setToId(userId);
			temgr.insertTransaction(sMessage, new TableEntityAdapter() {
				@Override
				public void afterInsert(final ITableEntityManager manager, final Object[] objects) {
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getTextBody(final String textBody) {
		String c = StringUtils.blank(textBody);
		c = HTMLUtils.htmlEscape(c);
		c = HTMLUtils.autoLink(c);
		c = HTMLUtils.convertHtmlLines(c);
		return c;
	}

	public static String tabs(final PageRequestResponse requestResponse) {
		return TabsUtils.tabs(requestResponse, new TabHref("#(MessageUtils.0)", applicationModule.getApplicationUrl(requestResponse)), new TabHref(
				"我的对话", "/message_dialog.html"), new TabHref("#(MessageUtils.1)", applicationModule.getSystemMessageUrl(requestResponse)),
				new TabHref("举报消息", "/message_comp.html"));
	}
}
