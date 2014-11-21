package net.simpleframework.web.page.component.ui.portal.module;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import net.simpleframework.core.AbstractXmlDocument;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.ui.portal.PageletBean;

import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PortalModuleRegistryFactory {
	public static final String DEFAULT_MODULE_NAME = "rss";

	static final String MODULE_FILE = "portal_module.xml";

	static final String WEB_MODULE_PATH = "/WEB-INF/portal_module.xml";

	static PortalModuleRegistryFactory instance;
	static boolean load = false;

	public static PortalModuleRegistryFactory getInstance() {
		if (instance == null) {
			instance = new PortalModuleRegistryFactory();
		}
		return instance;
	}

	public void init(final ServletContext servletContext) {
		if (load) {
			return;
		}
		load = true;
		InputStream inStream;
		try {
			inStream = new FileInputStream(servletContext.getRealPath(WEB_MODULE_PATH));
		} catch (final FileNotFoundException e) {
			String path = 
					BeanUtils.getResourceClasspath(PortalModuleRegistryFactory.class, MODULE_FILE);
			inStream = PortalModuleRegistryFactory.class.getClassLoader().getResourceAsStream(path);
		}
		if (inStream != null) {
			new ModuleDocument(inStream, instance);
		}
	}

	static class ModuleDocument extends AbstractXmlDocument {
		protected ModuleDocument(final InputStream inputStream, final PortalModuleRegistryFactory factory) {
			super(inputStream);
			final Iterator<?> it = getRoot().elementIterator("module");
			while (it.hasNext()) {
				final Element element = (Element) it.next();
				final PortalModule layoutModule = new PortalModule();
				final Iterator<?> attris = element.attributeIterator();
				while (attris.hasNext()) {
					final Attribute attribute = (Attribute) attris.next();
					try {
						final String value = attribute.getValue();
						if (value != null) {
							BeanUtils.setProperty(layoutModule, attribute.getName(), LocaleI18n.replaceI18n(value));
						}
						final String desc = element.elementText("description");
						if (StringUtils.hasText(desc)) {
							layoutModule.setDescription(LocaleI18n.replaceI18n(desc));
						}
					} catch (final Exception ex) {
						logger.warn(ex);
					}
				}
				factory.regist(layoutModule);
			}
		}
	}

	private final Map<String, Collection<PortalModule>> layoutModules = new LinkedHashMap<String, Collection<PortalModule>>();

	public PortalModule getModule(final String name) {
		if (StringUtils.hasText(name)) {
			for (final Collection<PortalModule> coll : layoutModules.values()) {
				for (final PortalModule layoutModule : coll) {
					if (name.equals(layoutModule.getName())) {
						return layoutModule;
					}
				}
			}
		}
		return null;
	}

	public Map<String, Collection<PortalModule>> getModulesByCatalog() {
		return layoutModules;
	}

	public void regist(final PortalModule layoutModule) {
		if (layoutModule != null) {
			final String catalog = layoutModule.getCatalog();
			Collection<PortalModule> coll = layoutModules.get(catalog);
			if (coll == null) {
				layoutModules.put(catalog, coll = new ArrayList<PortalModule>());
			}
			coll.add(layoutModule);
		}
	}

	public IPortalModuleHandle getModuleHandle(final PageletBean pagelet) {
		if (pagelet == null) {
			return null;
		}
		return pagelet.getModuleHandle();
	}

	public static void regist(final Class<? extends AbstractPortalModuleHandle> clazz, final String name, final String text, final String catalog,
			final String icon, final String description) {
		final PortalModule layoutModule = new PortalModule();
		layoutModule.setHandleClass(clazz.getName());
		layoutModule.setName(name);
		layoutModule.setText(text);
		layoutModule.setCatalog(catalog);
		layoutModule.setIcon(icon);
		layoutModule.setDescription(description);
		PortalModuleRegistryFactory.getInstance().regist(layoutModule);
	}
}
