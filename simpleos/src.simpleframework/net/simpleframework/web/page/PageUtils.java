package net.simpleframework.web.page;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import net.simpleframework.core.ALoggerAware;
import net.simpleframework.core.IApplication;
import net.simpleframework.core.Logger;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.HTTPUtils;
import net.simpleframework.util.IoUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.util.db.DbCreator;
import net.simpleframework.util.db.DbUtils;
import net.simpleframework.util.db.DbUtils._DatabaseMetaData;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.HandleException;
import net.simpleframework.web.page.component.IComponentRegistry;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class PageUtils {
	static Logger logger = ALoggerAware.getLogger(PageUtils.class);

	public static Map<String, Object> createVariables(final PageParameter pageParameter) {
		final Map<String, Object> variable = new HashMap<String, Object>();
		variable.put("pageParameter", pageParameter);
		variable.put("request", pageParameter.request);
		variable.put("response", pageParameter.response);
		final HttpSession session = pageParameter.getSession();
		variable.put("session", session);
		variable.put("application", session.getServletContext());
		variable.put("pagePath", pageConfig.getPagePath());
		if (ReflectUtils.methodGetLogin != null) {
			try {
				variable.put("login", ReflectUtils.methodGetLogin.invoke(null, session));
			} catch (final Exception e) {
				logger.warn(e);
			}
		}

		final PageDocument pageDocument = pageParameter.getPageDocument();
		if (pageDocument != null) {
			variable.put("document", pageDocument);
			final IPageResourceProvider prp = pageDocument.getPageResourceProvider();
			variable.put("skin", prp.getCurrentSkin(pageParameter));
			variable.put("resourcePath", prp.getResourceHomePath());

			final AbstractPageView pageView = pageDocument.getPageView();
			if (pageView != null) {
				variable.putAll(pageView.createVariables(pageParameter));
			}
		}
		return variable;
	}

	public static void setBrowserLocationPageDocument(final HttpSession httpSession,
			final PageDocument pageDocument) {
		httpSession.setAttribute(IPageConstants.SESSION_PAGE_DOCUMENT, pageDocument.hashId());
	}

	public static PageDocument getBrowserLocationPageDocument(final HttpSession httpSession) {
		final String hashId = (String) httpSession.getAttribute(IPageConstants.SESSION_PAGE_DOCUMENT);
		if (StringUtils.hasText(hashId)) {
			return PageDocumentFactory.getPageDocument(hashId);
		}
		return null;
	}

	private final static String RESOURCE_HOME = "/$resource";

	public static String getResourcePath() {
		return RESOURCE_HOME;
	}

	public static String getResourceImagesPath(final PageRequestResponse requestResponse) {
		return requestResponse.wrapContextPath(getResourcePath() + "/images/");
	}

	public static String doPageUrl(final PageParameter pageParameter, final String url) {
		final PageDocument pageDocument = pageParameter.getPageDocument();
		if (StringUtils.hasText(url) && !HTTPUtils.isAbsoluteUrl(url) && !url.startsWith("/")) {
			final File documentFile = pageDocument.getDocumentFile();
			if (documentFile != null) {
				String lookupPath = documentFile.getAbsolutePath()
						.substring(pageParameter.getServletContext().getRealPath("/").length())
						.replace(File.separatorChar, '/');
				final int pos = lookupPath.lastIndexOf("/");
				if (pos > -1) {
					lookupPath = lookupPath.substring(0, pos + 1) + url;
					return lookupPath.charAt(0) == '/' ? lookupPath : "/" + lookupPath;
				}
			} else {
				AbstractPageView abstractPageView;
				if ((abstractPageView = pageDocument.getPageView()) != null) {
					final String lookupPath = abstractPageView.getLookupPath();
					return lookupPath.substring(0, lookupPath.lastIndexOf("/") + 1) + url;
				}
			}
		}
		return url;
	}

	public static final IPageContext pageContext = IPageContext.Instance.getPageContext();

	public static final PageConfig pageConfig = pageContext.getPageConfig();
	static {
		final Map<String, String> packages = pageConfig.getPagePackages();
		if (packages != null) {
			for (final String key : new ArrayList<String>(packages.keySet())) {
				if (!key.endsWith("/")) {
					packages.put(key + "/", packages.remove(key));
				}
			}
		}
	}

	public static void removeSystemParameters(final Map<?, ?> parameters) {
		parameters.remove(IForward.REQUEST_ID);
		parameters.remove(IPageConstants.XMLPATH_PARAMETER);
	}

	public static String toLocaleString(final String str) {
		return HTTPUtils.toLocaleString(str, pageConfig.getCharset());
	}

	public static Map<String, Object> toQueryParams(final String queryString) {
		return HTTPUtils.toQueryParams(queryString, pageConfig.getCharset());
	}

	public static String toQueryString(final Map<String, Object> params) {
		return HTTPUtils.toQueryString(params, pageConfig.getCharset());
	}

	public static String addParameters(final String url, final String queryString) {
		return HTTPUtils.addParameters(url, queryString, pageConfig.getCharset());
	}

	public static void doDatabase(final Class<?> rClass, final AbstractComponentBean componentBean) {
		// if (!WebUtils.isCreateTableAutomatically()) {
		// return;
		// }
		IApplication application;
		if (rClass == null || (application = pageContext.getApplication()) == null) {
			return;
		}
		final String cp = BeanUtils.getResourceClasspath(rClass, "sql-script.zip");
		final InputStream inputStream = rClass.getClassLoader().getResourceAsStream(cp);
		if (inputStream == null) {
			return;
		}
		final DataSource dataSource = application.getDataSource();
		final _DatabaseMetaData metaData = DbUtils.getDatabaseMetaData(dataSource);
		final String script = StringUtils.stripFilenameExtension(StringUtils.getFilename(cp));
		final IComponentRegistry registry = componentBean.getComponentRegistry();
		final StringBuilder target = new StringBuilder();
		target.append(
				registry.getServletContext().getRealPath(
						registry.getComponentResourceProvider().getResourceHomePath()))
				.append(File.separator).append(script).append(File.separator)
				.append(StringUtils.hash(metaData.url));
		try {
			IoUtils.unzip(inputStream, target.toString(), false);

			target.append(File.separator).append(metaData.databaseProductName.toLowerCase())
					.append(File.separator).append(script).append(".xml");
			DbCreator.executeSql(dataSource, target.toString());
		} catch (final IOException e) {
			throw PageException.wrapException(e);
		}
	}

	public static void setObjectFromRequest(final Object object, final HttpServletRequest request,
			final String prefix, final String[] properties) {
		if (object == null || properties == null) {
			return;
		}
		for (final String property : properties) {
			String value = request.getParameter(StringUtils.blank(prefix) + property);
			if ("".equals(value)) {
				value = null;
			}
			try {
				BeanUtils.setProperty(object, property, value);
			} catch (final Exception e) {
				throw HandleException.wrapException(e);
			}
		}
	}
}
