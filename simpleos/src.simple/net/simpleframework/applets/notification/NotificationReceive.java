package net.simpleframework.applets.notification;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import net.simpleframework.core.ALoggerAware;
import net.simpleframework.util.IConstants;
import net.simpleframework.util.StringUtils;

import org.springframework.jms.listener.SessionAwareMessageListener;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class NotificationReceive extends ALoggerAware implements
		SessionAwareMessageListener<Message> {

	@Override
	public void onMessage(final Message message, final Session session) {
		try {
			final AbstractMessageNotification notificationMessage = (AbstractMessageNotification) ((ObjectMessage) message)
					.getObject();
			if (notificationMessage instanceof MailMessageNotification) {
				doMail((MailMessageNotification) notificationMessage);
			}
		} catch (final Exception ex) {
			logger.warn(ex);
			throw MessageNotificationException.wrapException(ex);
		}
	}

	private void doMail(final MailMessageNotification notificationMessage)
			throws MessagingException, UnsupportedEncodingException {
		final MailSender sender = notificationMessage.getSender();
		if (sender == null) {
			return;
		}

		Transport transport = null;
		try {
			final InternetAddress from = sender.getSentInternetAddress();
			if (from == null) {
				return;
			}

			final ArrayList<InternetAddress> to = new ArrayList<InternetAddress>();
			for (final Object o : notificationMessage.getTo()) {
				to.add(new InternetAddress(String.valueOf(o)));
			}
			if (to.size() == 0) {
				return;
			}

			final Properties props = new Properties();
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.auth", "true");
			final javax.mail.Session session = javax.mail.Session.getInstance(props);

			transport = session.getTransport();
			transport.connect(sender.getSmtpServer(), sender.getSmtpPort(), sender.getSmtpUsername(),
					sender.getSmtpPassword());

			final MimeMessage mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(from);

			mimeMessage.addRecipients(javax.mail.Message.RecipientType.TO,
					to.toArray(new InternetAddress[to.size()]));

			final String cc = notificationMessage.getCc();
			if (StringUtils.hasText(cc)) {
				mimeMessage.addRecipients(javax.mail.Message.RecipientType.CC, cc);
			}
			mimeMessage.setSubject(StringUtils.text(notificationMessage.getSubject(),
					from.getPersonal(), from.toString()));
			mimeMessage.setSentDate(notificationMessage.getSentDate());

			final Multipart multipart = new MimeMultipart();
			MimeBodyPart bodyPart = new MimeBodyPart();
			final String body = StringUtils.blank(notificationMessage.getTextBody());
			if (notificationMessage.isHtmlContent()) {
				final String charset = NotificationUtils.applicationModule.getApplication()
						.getApplicationConfig().getCharset();
				bodyPart.setContent(body, "text/html;charset=" + charset);
			} else {
				bodyPart.setText(body);
			}
			multipart.addBodyPart(bodyPart);
			final File[] attactments = notificationMessage.getAttactments();
			if (attactments != null) {
				for (final File attactment : attactments) {
					bodyPart = new MimeBodyPart();
					bodyPart.setFileName(MimeUtility.encodeText(attactment.getName(), IConstants.UTF8,
							"B"));
					bodyPart.setDataHandler(new DataHandler(new FileDataSource(attactment)));
					multipart.addBodyPart(bodyPart);
				}
			}
			mimeMessage.setContent(multipart);
			transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
		} finally {
			try {
				if (transport != null) {
					transport.close();
				}
			} catch (final MessagingException e) {
			}
		}
	}
}
