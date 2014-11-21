package net.itsite.impl;

import java.util.Map;

import net.itsite.i.ISendMail;
import net.itsite.utils.StringsUtils;
import net.simpleframework.organization.IUser;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.IoUtils;
import net.simpleframework.util.script.ScriptEvalUtils;
import net.simpleframework.web.IWebApplication;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.HandleException;

public abstract class AbstractSendMail implements ISendMail {
	final ComponentParameter compParameter;
	final IWebApplication webApplication;

	public AbstractSendMail(final ComponentParameter compParameter, final IWebApplication webApplication) {
		this.compParameter = compParameter;
		this.webApplication = webApplication;
	}

	@Override
	public boolean isSent(IUser toUser) {
		return true;
	}

	@Override
	public String getSubject(IUser toUser) {
		return null;
	}

	@Override
	public String getBody(IUser toUser) {
		return null;
	}

	@Override
	public IUser getUser() {
		return null;
	}

	protected String linkUrl(final String url, final String content) {
		final StringBuilder sb = new StringBuilder();
		final String href = webApplication.getApplicationConfig().getServerUrl() + compParameter.wrapContextPath(url);
		sb.append("<a target=\"_blank\" href=\"").append(href).append("\">");
		sb.append(StringsUtils.trimNull(content, href)).append("</a>");
		return sb.toString();
	}

	protected String getFromTemplate(final Map<String, Object> variable, final Class<?> templateClazz, final String file) {
		try {
			final String readerString = IoUtils.getStringFromInputStream(templateClazz.getClassLoader().getResourceAsStream(
					BeanUtils.getResourceClasspath(templateClazz, file)));
			return ScriptEvalUtils.replaceExpr(variable, readerString);
		} catch (final Exception e) {
			throw HandleException.wrapException(e);
		}
	}
}
