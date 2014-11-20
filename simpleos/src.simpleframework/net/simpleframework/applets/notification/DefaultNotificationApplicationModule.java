package net.simpleframework.applets.notification;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;

import net.itsite.ItSiteUtil;
import net.itsite.utils.LangUtils;
import net.prj.manager.PrjMgrUtils;
import net.simpleframework.core.IInitializer;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.organization.IUser;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.web.AbstractWebApplication;
import net.simpleframework.web.AbstractWebApplicationModule;
import net.simpleframework.web.IWebApplication;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultNotificationApplicationModule extends AbstractWebApplicationModule implements INotificationApplicationModule {

	@Override
	protected void putTables(final Map<Class<?>, Table> tables) {
		tables.put(NotificationLogBean.class, new Table("simple_notification_log"));
		tables.put(SystemMessageNotification.class, new Table("simple_message_notification"));
	}

	private ApplicationContext applicationContext;

	@Override
	public void init(final IInitializer initializer) {
		super.init(initializer);
		NotificationUtils.applicationModule = this;

		final IWebApplication application = getApplication();
		final File file = new File(application.getServletContext().getRealPath("/WEB-INF/jms.xml"));
		final ApplicationContext parent = ((AbstractWebApplication) application).getApplicationContext();
		if (file.exists()) {
			applicationContext = new FileSystemXmlApplicationContext(new String[] { "file:" + file.getAbsolutePath() }, parent);
		} else {
			applicationContext = new ClassPathXmlApplicationContext(new String[] { BeanUtils.getResourceClasspath(
					DefaultNotificationApplicationModule.class, "jms.xml") }, parent);
		}
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public JmsTemplate getJmsTemplate() {
		return (JmsTemplate) applicationContext.getBean("jmsTemplate");
	}

	@Override
	public void sendMessage(final ISendCallback callback) {
		final IMessageNotification messageNotification = callback.getMessageNotification();
		if (messageNotification instanceof MailMessageNotification) {
			final MailMessageNotification mmn = (MailMessageNotification) messageNotification;
			final MailSender sender = mmn.getSender();
			if (sender == null) {
				try {
					final Object bean = getApplicationContext().getBean(mmn.getClass().getSimpleName());
					if (bean instanceof MailSenderList) {
						final List<MailSender> senderList = ((MailSenderList) bean).getSenderList();
						if (senderList.size() > 0) {
							final MailSender sender2 = senderList.remove(0);
							mmn.setSender(sender2);
							senderList.add(sender2);
						}
					} else if (bean instanceof MailSender) {
						mmn.setSender((MailSender) bean);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (sender == null || PrjMgrUtils.sysMail) {
				if (LangUtils.toBoolean(ItSiteUtil.attrMap.get("sys.sys_mail"), false)) {
					final MailSender mailSender = new MailSender();
					mailSender.setSentAddress("<" + ItSiteUtil.attrMap.get("sys.sys_mail_sentAddress") + ">");
					mailSender.setSmtpServer(ItSiteUtil.attrMap.get("sys.sys_mail_smtpServer"));
					mailSender.setSmtpUsername(ItSiteUtil.attrMap.get("sys.sys_mail_smtpUsername"));
					mailSender.setSmtpPassword(ItSiteUtil.attrMap.get("sys.sys_mail_smtpPassword"));
					mmn.setSender(mailSender);
				}
				PrjMgrUtils.sysMail = false;
			}

			final ArrayList<String> al = new ArrayList<String>();
			final Collection<Object> to = mmn.getTo();
			for (final Object obj : to) {
				if (obj instanceof IUser) {
					final IUser user = (IUser) obj;
					if (callback.isSubscribeNotification(user)) {
						if (mmn instanceof MailMessageNotification) {
							al.add(user.toString() + " <" + user.getEmail() + ">");
						}
					}
				} else if (obj instanceof String) {
					al.add((String) obj);
				}
			}
			to.clear();
			to.addAll(al);

			if (to.size() == 0) {
				return;
			}

			final Queue mailQueue = (Queue) getApplicationContext().getBean("notificationQueue");
			getJmsTemplate().convertAndSend(mailQueue, mmn, defaultMessagePostProcessor);
		}
	}

	protected MessagePostProcessor defaultMessagePostProcessor = new MessagePostProcessor() {
		@Override
		public Message postProcessMessage(final Message message) throws JMSException {
			return message;
		}
	};
}
