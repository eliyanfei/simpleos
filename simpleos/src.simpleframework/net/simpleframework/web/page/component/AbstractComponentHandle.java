package net.simpleframework.web.page.component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.simpleframework.core.AAttributeAware;
import net.simpleframework.core.AbstractXmlDocument;
import net.simpleframework.core.IApplicationModule;
import net.simpleframework.core.IApplicationModuleAware;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.ReflectUtils;
import net.simpleframework.web.page.component.ui.menu.MenuBean;
import net.simpleframework.web.page.component.ui.menu.MenuItem;

import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractComponentHandle extends AAttributeAware implements IComponentHandle {
	@Override
	public void handleCreated(final PageRequestResponse requestResponse,
			final AbstractComponentBean componentBean) {
	}

	@Override
	public void beforeRender(final ComponentParameter compParameter) {
	}

	@Override
	public Map<String, Object> toJSON(final ComponentParameter compParameter) {
		return null;
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = new HashMap<String, Object>();
		return parameters;
	}

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		final AbstractComponentBean componentBean = compParameter.componentBean;
		if ("name".equals(beanProperty)) {
			return componentBean.getName();
		} else if ("runImmediately".equals(beanProperty)) {
			return componentBean.isRunImmediately();
		} else if ("selector".equals(beanProperty)) {
			return componentBean.getSelector();
		} else if ("handleClass".equals(beanProperty)) {
			return componentBean.getHandleClass();
		} else if ("handleScope".equals(beanProperty)) {
			return componentBean.getHandleScope();
		} else if ("includeRequestData".equals(beanProperty)) {
			return componentBean.getIncludeRequestData();
		} else if ("effects".equals(beanProperty)) {
			return componentBean.isEffects();
		} else if (beanProperty.startsWith("job")) {
			return StringUtils.text((String) BeanUtils.getProperty(componentBean, beanProperty),
					getDefaultjob(compParameter));
		} else if (componentBean instanceof AbstractContainerBean) {
			final AbstractContainerBean containerBean = (AbstractContainerBean) componentBean;
			if ("containerId".equals(beanProperty)) {
				return containerBean.getContainerId();
			} else if ("width".equals(beanProperty)) {
				return containerBean.getWidth();
			} else if ("height".equals(beanProperty)) {
				return containerBean.getHeight();
			}
		}
		return BeanUtils.getProperty(componentBean, beanProperty);
	}

	protected String getDefaultjob(final ComponentParameter compParameter) {
		return PageUtils.pageConfig.getDefaultjob();
	}

	/*------------------------------ utils ---------------------------------*/
	@Override
	public String getManager(final ComponentParameter compParameter) {
		IApplicationModule aModule;
		if (this instanceof IApplicationModuleAware
				&& (aModule = ((IApplicationModuleAware) this).getApplicationModule()) != null) {
			return aModule.getManager(compParameter);
		}
		return ReflectUtils.jobManager;
	}

	@Override
	public List<MenuItem> getContextMenu(final ComponentParameter compParameter,
			final MenuBean menuBean) {
		return createXmlMenu("menu.xml", menuBean);
	}

	protected List<MenuItem> createXmlMenu(final String filename, final MenuBean menuBean) {
		final InputStream inputStream = BeanUtils.getResourceRecursively(getClass(), filename);
		final ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
		if (inputStream != null) {
			new AbstractXmlDocument(inputStream) {
				@Override
				protected void init() throws Exception {
					final Iterator<?> it = getRoot().elementIterator("item");
					while (it.hasNext()) {
						_create(menuItems, (Element) it.next());
					}
				}

				private void _create(final Collection<MenuItem> children, final Element element) {
					final MenuItem item = new MenuItem(menuBean);
					Iterator<?> it = element.attributeIterator();
					while (it.hasNext()) {
						final Attribute attribute = (Attribute) it.next();
						BeanUtils.setProperty(item, attribute.getName(),
								LocaleI18n.replaceI18n(attribute.getValue()));
						item.setJsSelectCallback(element.elementTextTrim("jsSelectCallback"));
					}
					children.add(item);

					it = element.elementIterator("item");
					while (it.hasNext()) {
						_create(item.getChildren(), (Element) it.next());
					}
				}
			};
		}
		return menuItems;
	}

	protected void putParameter(final ComponentParameter compParameter,
			final Map<String, Object> parameters, final String key) {
		putParameter(compParameter, parameters, key,
				PageUtils.toLocaleString(compParameter.getRequestParameter(key)));
	}

	protected void putParameter(final ComponentParameter compParameter,
			final Map<String, Object> parameters, final String key, final Object value) {
		if (value != null) {
			parameters.put(key, StringUtils.blank(value));
		}
	}

	protected void setObjectFromMap(final Object object, final Map<String, Object> data) {
		if (object == null || data == null) {
			return;
		}
		for (final Map.Entry<String, Object> entry : data.entrySet()) {
			final Object value = entry.getValue();
			if (value != null) {
				BeanUtils.setProperty(object, entry.getKey(), value);
			}
		}
	}
}
