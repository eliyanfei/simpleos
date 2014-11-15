package net.simpleframework.applets.notification;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import net.simpleframework.util.MailUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public final class MailSender implements Serializable {
	private String sentAddress;

	private String smtpServer;

	private int smtpPort = 25;

	private String smtpUsername, smtpPassword;

	public InternetAddress getSentInternetAddress() throws AddressException,
			UnsupportedEncodingException {
		final InternetAddress[] addresses = MailUtils.getInternetAddresses(getSentAddress());
		return addresses != null && addresses.length > 0 ? addresses[0] : null;
	}

	public String getSentAddress() {
		return sentAddress;
	}

	public void setSentAddress(final String sentAddress) {
		this.sentAddress = sentAddress;
	}

	public String getSmtpServer() {
		return smtpServer;
	}

	public void setSmtpServer(final String smtpServer) {
		this.smtpServer = smtpServer;
	}

	public int getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(final int smtpPort) {
		this.smtpPort = smtpPort;
	}

	public String getSmtpUsername() {
		return smtpUsername;
	}

	public void setSmtpUsername(final String smtpUsername) {
		this.smtpUsername = smtpUsername;
	}

	public String getSmtpPassword() {
		return smtpPassword;
	}

	public void setSmtpPassword(final String smtpPassword) {
		this.smtpPassword = smtpPassword;
	}

	private static final long serialVersionUID = 3609491864376732442L;
}
