package net.simpleframework.web.page;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.HTTPUtils;
import net.simpleframework.util.StringUtils;
import net.simpleos.backend.BackendUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PageDocumentFactory {
	public static PageDocument getPageDocument(final PageRequestResponse requestResponse) {
		String filename = requestResponse.getRequestParameter(IPageConstants.XMLPATH_PARAMETER);
		if (!StringUtils.hasText(filename)) {
			final HttpServletRequest request = requestResponse.request;
			String contextPath = request.getContextPath();
			if ("/".equals(contextPath)) {
				contextPath = "";
			}
			String lookupPath = HTTPUtils.getRequestURI(request, false);
			if (lookupPath.toLowerCase().startsWith(contextPath.toLowerCase())) {
				lookupPath = lookupPath.substring(contextPath.length());
			}
			final String pagePath = PageUtils.pageConfig.getPagePath();
			if (StringUtils.hasText(pagePath) && lookupPath.startsWith(pagePath)) {
				PageDocument pageDocument = null;
				String clazzName = lookupPath.substring(pagePath.length());
				if (!StringUtils.hasText(clazzName) || "/".equals(clazzName)) {
					pageDocument = getPageDocumentAndCreate(PageUtils.pageConfig.getHomePage(), requestResponse);
				} else if (StringUtils.hasText(clazzName)) {
					int pos;
					if ((pos = clazzName.indexOf(";")) > 0) {
						clazzName = clazzName.substring(0, pos);
					}
					String pagePackage = null;
					if ((pos = clazzName.lastIndexOf("/") + 1) > 0) {
						final Map<String, String> packages = PageUtils.pageConfig.getPagePackages();
						if (packages != null) {
							pagePackage = packages.get(clazzName.substring(0, pos));
						}
						clazzName = clazzName.substring(pos);
					}
					if (pagePackage != null) {
						clazzName = pagePackage + "." + StringUtils.replace(clazzName, "-", ".");
					}
					try {
						pageDocument = getPageDocumentAndCreate(BeanUtils.forName(clazzName), requestResponse);
					} catch (final ClassNotFoundException e) {
					}
				}
				AbstractPageView abstractPageView;
				if (pageDocument != null && (abstractPageView = pageDocument.getPageView()) != null) {
					abstractPageView.setLookupPath(lookupPath);
				}
				return pageDocument;
			}
			final int p = lookupPath.lastIndexOf('.');
			filename = requestResponse.getServletContext().getRealPath(
					((p <= 0) ? lookupPath : lookupPath.substring(0, p)) + IPageConstants.POSTFIX_XML);
		} else {
			filename = requestResponse.getServletContext().getRealPath(filename);
		}
		return getPageDocumentAndCreate(new File(filename), requestResponse);
	}

	private static Map<String, PageDocument> documentMap = new ConcurrentHashMap<String, PageDocument>();

	static PageDocument getPageDocument(final String docHash) {
		return docHash != null ? documentMap.get(docHash) : null;
	}

	public synchronized static PageDocument getPageDocumentAndCreate(final Object pObject, final PageRequestResponse requestResponse) {
		if (pObject == null) {
			return null;
		}
		String docHash = null;
		if (pObject instanceof File && ((File) pObject).exists()) {
			docHash = StringUtils.hash(pObject);
		} else if (pObject instanceof Class<?>) {
			docHash = ((Class<?>) pObject).getName();
		}
		if (docHash == null) {
			return null;
		}
		PageDocument document = documentMap.get(docHash);
		if (document != null && document.isModified()) {
			document = null;
		}
		BackendUtils.initDesktopDocument(new PageParameter(requestResponse.request, requestResponse.response, document));
		if (document == null) {
			try {
				if (pObject instanceof File) {
					documentMap.put(docHash, document = new PageDocument((File) pObject, requestResponse));
				} else if (pObject instanceof Class<?>) {
					document = AbstractPageView.createPageDocument((Class<?>) pObject, requestResponse);
					if (document != null) {
						documentMap.put(docHash, document);
					}
				}
			} catch (final Exception e) {
				throw PageException.wrapException(e);
			}
		} else {
			document.setFirstCreated(false);
		}
		return document;
	}
}
