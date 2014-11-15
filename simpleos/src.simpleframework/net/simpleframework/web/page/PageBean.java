package net.simpleframework.web.page;

import net.simpleframework.core.AbstractElementBean;
import net.simpleframework.util.StringUtils;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PageBean extends AbstractElementBean {
	private EScriptEvalType scriptEval;

	private String scriptInit;

	private String resourceProvider;

	private String[] importPage, importJavascript, importCSS;

	private String title;

	private String favicon;

	private String responseCharacterEncoding;

	private String handleClass, handleMethod;

	private String jsLoadedCallback;

	private String jobView;

	private final PageDocument pageDocument;

	public PageBean(final PageDocument pageDocument, final Element element) {
		super(element);
		this.pageDocument = pageDocument;
	}

	public PageBean(final PageDocument pageDocument) {
		this(pageDocument, null);
	}

	public PageDocument getPageDocument() {
		return pageDocument;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getFavicon() {
		return favicon;
	}

	public void setFavicon(final String favicon) {
		this.favicon = favicon;
	}

	public String getResourceProvider() {
		return resourceProvider;
	}

	public void setResourceProvider(final String resourceProvider) {
		this.resourceProvider = resourceProvider;
	}

	public String getHandleClass() {
		return StringUtils.text(handleClass, DefaultPageHandle.class.getName());
	}

	public void setHandleClass(final String handleClass) {
		this.handleClass = handleClass;
	}

	public String getHandleMethod() {
		return handleMethod;
	}

	public void setHandleMethod(final String handleMethod) {
		this.handleMethod = handleMethod;
	}

	public String getJsLoadedCallback() {
		return jsLoadedCallback;
	}

	public void setJsLoadedCallback(final String jsLoadedCallback) {
		this.jsLoadedCallback = jsLoadedCallback;
	}

	public String[] getImportPage() {
		return importPage;
	}

	public void setImportPage(final String[] importPage) {
		this.importPage = importPage;
	}

	public EScriptEvalType getScriptEval() {
		return scriptEval == null ? EScriptEvalType.none : scriptEval;
	}

	public void setScriptEval(final EScriptEvalType scriptEval) {
		this.scriptEval = scriptEval;
	}

	public String getScriptInit() {
		return scriptInit;
	}

	public void setScriptInit(final String scriptInit) {
		this.scriptInit = scriptInit;
	}

	public String[] getImportJavascript() {
		return importJavascript;
	}

	public void setImportJavascript(final String[] importJavascript) {
		this.importJavascript = importJavascript;
	}

	public String[] getImportCSS() {
		return importCSS;
	}

	public void setImportCSS(final String[] importCSS) {
		this.importCSS = importCSS;
	}

	public String getResponseCharacterEncoding() {
		return responseCharacterEncoding;
	}

	public void setResponseCharacterEncoding(final String responseCharacterEncoding) {
		this.responseCharacterEncoding = responseCharacterEncoding;
	}

	public String getJobView() {
		return defaultjob(jobView);
	}

	public void setJobView(final String jobView) {
		this.jobView = jobView;
	}

	protected String defaultjob(final String job) {
		return StringUtils.text(job, PageUtils.pageConfig.getDefaultjob());
	}
}
