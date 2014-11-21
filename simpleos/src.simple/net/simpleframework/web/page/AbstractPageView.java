package net.simpleframework.web.page;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;

import net.simpleframework.core.ALoggerAware;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.IoUtils;
import net.simpleframework.util.JavascriptUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.util.script.ScriptEvalUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.menu.MenuBean;
import net.simpleframework.web.page.component.ui.menu.MenuItem;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractPageView extends ALoggerAware {
	private PageDocument pageDocument;

	private String lookupPath;

	public PageDocument getPageDocument() {
		return pageDocument;
	}

	protected void init(final ServletContext servletContext, final PageDocument pageDocument) {
		this.pageDocument = pageDocument;
		try {
			deployResource(servletContext, getClass());
		} catch (final IOException e) {
			logger.warn(e);
		}
	}

	private final Map<Class<?>, Map<String, String>> htmlViewVariables = new HashMap<Class<?>, Map<String, String>>();

	protected void addHtmlViewVariable(final Class<?> pageClass, final String variable,
			final String htmlFilename) {
		Map<String, String> htmlViews = htmlViewVariables.get(pageClass);
		if (htmlViews == null) {
			htmlViewVariables.put(pageClass, htmlViews = new HashMap<String, String>());
		}
		htmlViews.put(variable, htmlFilename);
	}

	protected void addHtmlViewVariable(final Class<?> pageClass, final String variable) {
		addHtmlViewVariable(pageClass, variable, pageClass.getSimpleName() + ".html");
	}

	public IForward forward(final PageParameter pageParameter) {
		try {
			return new TextForward(getPageForward(getClass(), createVariables(pageParameter)));
		} catch (final IOException e) {
			logger.warn(e);
			return null;
		}
	}

	private String getPageForward(final Class<?> pageClass, final Map<String, Object> variables)
			throws IOException {
		if (!isExtend() || pageClass.getSuperclass().equals(AbstractPageView.class)) {
			final InputStream inputStream = getResource(pageClass, ".html");
			return inputStream == null ? "" : ScriptEvalUtils.replaceExprByMVEL(variables,
					IoUtils.getStringFromInputStream(inputStream, getChartset()));
		} else {
			final Map<String, String> htmlViews = htmlViewVariables.get(pageClass);
			if (htmlViews == null || htmlViews.size() == 0) {
				return getPageForward(pageClass.getSuperclass(), variables);
			} else {
				final Map<String, Object> variables2 = new HashMap<String, Object>(variables);
				for (final Map.Entry<String, String> entry : htmlViews.entrySet()) {
					final InputStream inputStream = getResource(pageClass, entry.getValue());
					if (inputStream != null) {
						variables2.put(
								entry.getKey(),
								ScriptEvalUtils.replaceExprByMVEL(variables2,
										IoUtils.getStringFromInputStream(inputStream, getChartset())));
					}
				}
				return getPageForward(pageClass.getSuperclass(), variables2);
			}
		}
	}

	protected void deployResource(final ServletContext servletContext, final Class<?> pageClass)
			throws IOException {
		if (AbstractPageView.class.equals(pageClass)) {
			return;
		}
		final InputStream inputStream = getResource(pageClass, "resource.zip");
		if (inputStream != null) {
			JavascriptUtils.unzipJsAndCss(inputStream,
					servletContext.getRealPath(getResourceHomePath(pageClass)),
					PageUtils.pageConfig.isResourceCompress());
		}
		deployResource(servletContext, pageClass.getSuperclass());
	}

	public String getResourceHomePath() {
		return getResourceHomePath(getClass());
	}

	protected String getDeployName() {
		return "$page";
	}

	private static Map<Class<?>, String> pathCache = new ConcurrentHashMap<Class<?>, String>();

	public String getResourceHomePath(final Class<?> pageClass) {
		String resourceHomePath = pathCache.get(pageClass);
		if (resourceHomePath == null) {
			final StringBuilder sb = new StringBuilder();
			sb.append(PageUtils.pageContext.getApplication().getApplicationHomePath());
			if (pageClass.equals(getClass())) {
				sb.append(getDeployName());
			} else {
				try {
					final AbstractPageView pageview = (AbstractPageView) pageClass.newInstance();
					sb.append(pageview.getDeployName());
				} catch (final Exception e) {
					sb.append(getDeployName());
				}
			}
			sb.append("/").append(pageClass.getPackage().getName()).append("/");
			pathCache.put(pageClass, resourceHomePath = sb.toString());
		}
		return resourceHomePath;
	}

	public String getCssPath(final PageRequestResponse requestResponse) {
		return getResourceHomePath() + "css/" + SkinUtils.getSkin(requestResponse, null);
	}

	public Map<String, Object> createVariables(final PageParameter pageParameter) {
		final Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("page", this);
		variables.put("pageParameter", pageParameter);
		variables.put("pagePath", getPagePath());
		return variables;
	}

	protected String getPagePath() {
		return PageUtils.pageConfig.getPagePath();
	}

	protected boolean isExtend() {
		return true;
	}

	protected String getChartset() {
		return PageUtils.pageConfig.getCharset();
	}

	public String getLookupPath() {
		return lookupPath;
	}

	public void setLookupPath(final String lookupPath) {
		this.lookupPath = lookupPath;
	}

	/*--------------------------------- Component Utils   ---------------------------------*/
	public String buildInputHidden(final PageParameter pageParameter, String... names) {
		final StringBuilder sb = new StringBuilder();
		if (names != null) {
			for (String name : names) {
				sb.append("<input type='hidden' name='").append(name).append("' value='")
						.append(StringUtils.blank(pageParameter.getRequestParameter(name)))
						.append("' />");
			}
		}
		return sb.toString();
	}

	public String blank(final Object object) {
		return StringUtils.blank(object);
	}

	public String toDateString(Date date) {
		return toDateString(date, null);
	}

	public String toDateString(Date date, String pattern) {
		return ConvertUtils.toDateString(date, pattern == null ? ConvertUtils.defaultDatePattern
				: pattern);
	}

	protected MenuItem createMenuItem(final ComponentParameter compParameter, final String title,
			final String url) {
		final MenuItem menuItem = new MenuItem((MenuBean) compParameter.componentBean);
		menuItem.setTitle(title);
		menuItem.setUrl(url);
		return menuItem;
	}

	protected MenuItem createMenuSepItem(final ComponentParameter compParameter) {
		return createMenuItem(compParameter, "-", null);
	}

	/*--------------------------------- static ---------------------------------*/

	static InputStream getResource(final Class<?> resourceClass, final String filename) {
		return resourceClass.getClassLoader().getResourceAsStream(
				BeanUtils.getResourceClasspath(resourceClass,
						filename.startsWith(".") ? resourceClass.getSimpleName() + filename : filename));
	}

	static PageDocument createPageDocument(final Class<?> pageClass,
			final PageRequestResponse requestResponse) {
		PageDocument pageDocument = null;
		InputStream inputStream = getResource(pageClass, ".xml");
		if (inputStream == null) {
			inputStream = getResource(AbstractPageView.class, "page-null.xml");
		}
		try {
			pageDocument = new PageDocument(pageClass, inputStream, requestResponse);
		} catch (final IOException e) {
		}
		return pageDocument;
	}

	@SuppressWarnings("unchecked")
	public static <T extends AbstractPageView> T getInstance(final Class<T> pageClass) {
		final PageDocument pageDocument = PageDocumentFactory.getPageDocument(pageClass.getName());
		return pageDocument != null ? (T) pageDocument.getPageView() : null;
	}
}
