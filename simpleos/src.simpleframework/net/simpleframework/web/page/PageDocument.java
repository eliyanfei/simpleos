package net.simpleframework.web.page;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Vector;

import javax.servlet.ServletContext;

import net.simpleframework.core.AbstractXmlDocument;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.util.script.IScriptEval;
import net.simpleframework.util.script.ScriptEvalUtils;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.ComponentBeanUtils;
import net.simpleframework.web.page.component.ComponentException;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ComponentRegistryFactory;
import net.simpleframework.web.page.component.IComponentRegistry;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PageDocument extends AbstractXmlDocument {
	private ServletContext servletContext;

	private Class<?> pageviewClass;

	private File documentFile;

	private PageBean pageBean;

	private long lastModified;

	private boolean firstCreated = true;

	private Vector<AbstractComponentBean> componentBeans;

	public PageDocument(final File documentFile, final PageRequestResponse requestResponse)
			throws IOException {
		super(new FileInputStream(documentFile));
		this.documentFile = documentFile;
		this.lastModified = documentFile.lastModified();
		init(requestResponse);
	}

	public PageDocument(final Class<?> pageviewClass, final InputStream inputStream,
			final PageRequestResponse requestResponse) throws IOException {
		createXmlDocument(inputStream);
		this.pageviewClass = pageviewClass;
		init(requestResponse);
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public File getDocumentFile() {
		return documentFile;
	}

	public PageBean getPageBean() {
		return pageBean;
	}

	private AbstractPageView abstractPageView;

	public AbstractPageView getPageView() {
		if (abstractPageView == null && pageviewClass != null
				&& !Modifier.isAbstract(pageviewClass.getModifiers())) {
			abstractPageView = (AbstractPageView) BeanUtils.newInstance(pageviewClass);
			abstractPageView.init(getServletContext(), this);
		}
		return abstractPageView;
	}

	public boolean isFirstCreated() {
		return firstCreated;
	}

	public void setFirstCreated(final boolean firstCreated) {
		this.firstCreated = firstCreated;
	}

	private void init(final PageRequestResponse requestResponse) {
		servletContext = requestResponse.getServletContext();
		pageBean = new PageBean(this, getRoot());
		componentBeans = new Vector<AbstractComponentBean>();

		final PageParameter pageParameter = PageParameter.get(requestResponse, this);
		IScriptEval scriptEval = null;
		final Element root = getRoot();
		Element element = root.element(IPageConstants.TAG_SCRIPT_EVAL);
		if (element != null) {
			final String s = element.getTextTrim();
			final EScriptEvalType evalType = StringUtils.hasText(s) ? EScriptEvalType.valueOf(s)
					: EScriptEvalType.none;
			pageBean.setScriptEval(evalType);
			if (evalType != EScriptEvalType.none) {
				scriptEval = pageParameter.createScriptEval();
			}
		}

		element = root.element(IPageConstants.TAG_HANDLE_CLASS);
		if (element != null) {
			final String handleClass = ScriptEvalUtils.replaceExpr(scriptEval, element.getTextTrim());
			if (StringUtils.hasText(handleClass)) {
				pageBean.setHandleClass(handleClass);
			}
		}

		// TAG_HANDLE_CLASS
		getPageHandle().documentInit(pageParameter);

		Iterator<?> it = root.elementIterator();
		while (it.hasNext()) {
			element = (Element) it.next();
			final String name = element.getName();
			if (name.equals(IPageConstants.TAG_SCRIPT_EVAL)
					|| name.equals(IPageConstants.TAG_HANDLE_CLASS)
					|| name.equals(IPageConstants.TAG_COMPONENTS)) {
				continue;
			}
			if (name.equals(IPageConstants.TAG_IMPORT_PAGE)
					|| name.equals(IPageConstants.TAG_IMPORT_JAVASCRIPT)
					|| name.equals(IPageConstants.TAG_IMPORT_CSS)) {
				final Set<String> l = new LinkedHashSet<String>();
				final Iterator<?> values = element.elementIterator(IPageConstants.TAG_VALUE);
				while (values.hasNext()) {
					final String value = ScriptEvalUtils.replaceExpr(scriptEval,
							((Element) values.next()).getTextTrim());
					l.add(PageUtils.doPageUrl(pageParameter, value));
				}
				final int size = l.size();
				if (size == 0) {
					continue;
				}
				final String[] strings = l.toArray(new String[size]);
				if (name.equals(IPageConstants.TAG_IMPORT_PAGE)) {
					pageBean.setImportPage(strings);

				} else if (name.equals(IPageConstants.TAG_IMPORT_JAVASCRIPT)) {
					pageBean.setImportJavascript(strings);
				} else {
					pageBean.setImportCSS(strings);
				}
			} else {
				final String value = element.getTextTrim();
				if (name.equals(IPageConstants.TAG_SCRIPT_INIT)) {
					if (scriptEval != null && StringUtils.hasText(value)) {
						pageBean.setScriptInit(value);
						scriptEval.eval(value);
					}
				} else {
					BeanUtils
							.setProperty(pageBean, name, ScriptEvalUtils.replaceExpr(scriptEval, value));
				}
			}
		}

		final Element components = root.element(IPageConstants.TAG_COMPONENTS);
		if (components == null) {
			return;
		}
		final ComponentRegistryFactory factory = ComponentRegistryFactory.getInstance();
		it = components.elementIterator();
		while (it.hasNext()) {
			final Element component = (Element) it.next();
			final String componentName = component.getName();
			final IComponentRegistry registry = factory.getComponentRegistry(componentName);
			if (registry == null) {
				throw ComponentException.getNotRegisteredException(componentName);
			}
			if (!registry.getPageResourceProvider().equals(getPageResourceProvider())) {
				throw ComponentException.getPageResourceProviderException();
			}

			final AbstractComponentBean componentBean = registry.createComponentBean(pageParameter,
					component);
			if (componentBean != null) {
				final ComponentParameter nComponentParameter = ComponentParameter.get(requestResponse,
						componentBean);
				if (!StringUtils.hasText((String) nComponentParameter.getBeanProperty("name"))) {
					throw ComponentException.getNullComponentNameException();
				}
				addComponentBean(componentBean);
			}
		}
	}

	private Collection<PageDocument> getImportDocuments(final PageParameter pageParameter) {
		final String rKey = "documents_" + hashId();
		@SuppressWarnings("unchecked")
		ArrayList<PageDocument> documents = (ArrayList<PageDocument>) pageParameter
				.getRequestAttribute(rKey);
		if (documents == null) {
			final IPageResourceProvider prp = getPageResourceProvider();
			documents = new ArrayList<PageDocument>();
			final String[] importPages = (String[]) pageParameter.getBeanProperty("importPage");
			if (importPages != null) {
				for (final String importPage : importPages) {
					final PageDocument document = PageDocumentFactory.getPageDocumentAndCreate(new File(
							servletContext.getRealPath(importPage)), pageParameter);
					if (document != null) {
						if (!document.getPageResourceProvider().equals(prp)) {
							throw ComponentException.getPageResourceProviderException();
						}
						documents.add(document);
					}
				}
			}
			if (pageviewClass != null) {
				final Class<?> superClass = pageviewClass.getSuperclass();
				if (!AbstractPageView.class.equals(superClass)) {
					final PageDocument document = PageDocumentFactory.getPageDocumentAndCreate(
							superClass, pageParameter);
					if (document != null) {
						if (!document.getPageResourceProvider().equals(prp)) {
							throw ComponentException.getPageResourceProviderException();
						}
						documents.add(document);
					}
				}
			}
			pageParameter.setRequestAttribute(rKey, documents);
		}
		return documents;
	}

	public Collection<AbstractComponentBean> getComponentBeans(final PageParameter pageParameter) {
		final LinkedHashSet<AbstractComponentBean> beans = new LinkedHashSet<AbstractComponentBean>();
		for (final PageDocument document : getImportDocuments(pageParameter)) {
			final PageParameter nPageParameter = PageParameter.get(pageParameter, document);
			ComponentBeanUtils.evalComponentBean(nPageParameter);
			beans.addAll(document.getComponentBeans(nPageParameter));
		}
		beans.addAll(componentBeans);
		return beans;
	}

	boolean isModified() {
		final File documentFile = getDocumentFile();
		return documentFile != null && documentFile.lastModified() != lastModified;
	}

	public void addComponentBean(final AbstractComponentBean componentBean) {
		componentBeans.add(componentBean);
		AbstractComponentBean.allComponents.put(componentBean.hashId(), componentBean);
	}

	public Collection<String> getImportJavascript(final PageParameter pageParameter) {
		final LinkedHashSet<String> jsColl = new LinkedHashSet<String>();
		for (final PageDocument document : getImportDocuments(pageParameter)) {
			final Collection<String> coll = document.getImportJavascript(PageParameter.get(
					pageParameter, document));
			if (coll != null) {
				jsColl.addAll(coll);
			}
		}
		final String[] importJavascript = (String[]) pageParameter
				.getBeanProperty("importJavascript");
		if (importJavascript != null) {
			for (final String js : importJavascript) {
				jsColl.add(js);
			}
		}
		return jsColl;
	}

	public Collection<String> getImportCSS(final PageParameter pageParameter) {
		final LinkedHashSet<String> cssColl = new LinkedHashSet<String>();
		for (final PageDocument document : getImportDocuments(pageParameter)) {
			final Collection<String> coll = document.getImportCSS(PageParameter.get(pageParameter,
					document));
			if (coll != null) {
				cssColl.addAll(coll);
			}
		}
		final String[] importCSS = (String[]) pageParameter.getBeanProperty("importCSS");
		if (importCSS != null) {
			for (final String css : importCSS) {
				cssColl.add(css);
			}
		}
		return cssColl;
	}

	public String getTitle(final PageParameter pageParameter) {
		final String title = (String) pageParameter.getBeanProperty("title");
		if (!StringUtils.hasText(title)) {
			for (final PageDocument document : getImportDocuments(pageParameter)) {
				final String title2 = document.getTitle(PageParameter.get(pageParameter, document));
				if (StringUtils.hasText(title2)) {
					return title2;
				}
			}
		}
		return title;
	}

	public String getJsLoadedCallback(final PageParameter pageParameter) {
		String jsLoadedCallback = StringUtils
				.blank(pageParameter.getBeanProperty("jsLoadedCallback"));
		for (final PageDocument document : getImportDocuments(pageParameter)) {
			final String js = document.getJsLoadedCallback(PageParameter.get(pageParameter, document));
			if (StringUtils.hasText(js)) {
				jsLoadedCallback += js;
			}
		}
		return jsLoadedCallback;
	}

	public String getScriptInit(final PageParameter pageParameter) {
		String scriptInit = StringUtils.blank(pageParameter.getBeanProperty("scriptInit"));
		for (final PageDocument document : getImportDocuments(pageParameter)) {
			final String script = document.getScriptInit(PageParameter.get(pageParameter, document));
			if (StringUtils.hasText(script)) {
				scriptInit += script;
			}
		}
		return scriptInit;
	}

	public IPageResourceProvider getPageResourceProvider() {
		return PageResourceProviderRegistry.getInstance(servletContext).getPageResourceProvider(
				pageBean.getResourceProvider());
	}

	private IPageHandle pageHandle;

	public IPageHandle getPageHandle() {
		if (pageHandle == null) {
			final String handleClass = pageBean.getHandleClass();
			if (StringUtils.hasText(handleClass)) {
				pageHandle = (IPageHandle) BeanUtils.newInstance(handleClass);
			}
		}
		return pageHandle;
	}

	private String _hashId;

	public String hashId() {
		if (_hashId == null) {
			_hashId = pageviewClass != null ? pageviewClass.getName() : StringUtils
					.hash(getDocumentFile());
		}
		return _hashId;
	}

	@Override
	public boolean equals(final Object obj) {
		if (documentFile != null && (obj instanceof PageDocument)) {
			return documentFile.equals(((PageDocument) obj).documentFile);
		} else {
			return super.equals(obj);
		}
	}
}
