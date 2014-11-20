package net.simpleframework.web.page;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.simpleframework.core.ALoggerAware;
import net.simpleframework.core.ApplicationConfig;
import net.simpleframework.core.IApplication;
import net.simpleframework.core.SimpleException;
import net.simpleframework.organization.IJob;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.HTTPUtils;
import net.simpleframework.util.IConstants;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.HandleException;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PageConfig extends ALoggerAware {
	private final PageContext pageContext;

	public PageConfig(final PageContext pageContext) {
		this.pageContext = pageContext;
	}

	private Object applicationConfig(final String property, final Object defaultValue) {
		final IApplication application = pageContext.getApplication();
		Object value = null;
		ApplicationConfig config;
		if (application != null && (config = application.getApplicationConfig()) != null) {
			value = BeanUtils.getProperty(config, property);
		}
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}

	public String getCharset() {
		return (String) applicationConfig("charset", IConstants.UTF8);
	}

	public boolean isResourceCompress() {
		return (Boolean) applicationConfig("resourceCompress", true);
	}

	public String getThrowableMessage(Throwable th) {
		th = SimpleException.convertThrowable(th);
		String message = defaultThrowableMessage(th);
		if (message != null) {
			return message;
		}
		while (th != null) {
			if(!(th instanceof HandleException)){
				return "网络异常";
			}
			message = th.getMessage();
			if (message == null) {
				final Throwable th0 = th.getCause();
				if (th0 != null) {
					th = th0;
				} else {
					return (message = defaultThrowableMessage(th)) != null ? message : StringUtils.substring(ConvertUtils.toString(th), 80, true);
				}
			} else {
				return message;
			}
		}
		return null;
	}

	protected String defaultThrowableMessage(final Throwable th) {
		final String className = getClass().getName();
		if ("java.lang.NullPointerException".equals(className)) {
			return LocaleI18n.getMessage("PageConfig.0");
		}
		if ("org.springframework.dao.DuplicateKeyException".equals(className)) {
			return LocaleI18n.getMessage("PageConfig.1", th.getCause().getMessage());
		}
		return null;
	}

	public String getErrorPage() {
		return PageUtils.getResourcePath() + "/jsp/error.jsp";
	}

	public boolean isGzipResponse() {
		return false;
	}

	public boolean isEffect(final HttpServletRequest request) {
		final float ver = HTTPUtils.getIEVersion(request);
		return ver == -1f || ver >= 8f;
	}

	public int getServletPort(final HttpServletRequest request) {
		return request.getServerPort();
	}

	public String getDefaultjob() {
		return IJob.sj_anonymous;
	}

	/*----------------------------- AbstractPageView -----------------------------*/

	public String getPagePath() {
		return "/app";
	}

	public Map<String, String> getPagePackages() {
		return null;
	}

	public Class<? extends AbstractPageView> getHomePage() {
		return null;
	}
}
