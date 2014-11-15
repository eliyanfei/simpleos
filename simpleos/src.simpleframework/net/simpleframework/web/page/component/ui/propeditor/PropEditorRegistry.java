package net.simpleframework.web.page.component.ui.propeditor;

import java.util.Iterator;

import javax.servlet.ServletContext;

import net.simpleframework.util.script.IScriptEval;
import net.simpleframework.util.script.ScriptEvalUtils;
import net.simpleframework.web.page.EJavascriptEvent;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.AbstractComponentRender;
import net.simpleframework.web.page.component.AbstractComponentResourceProvider;
import net.simpleframework.web.page.component.ComponentException;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PropEditorRegistry extends AbstractComponentRegistry {
	public static final String propEditor = "propEditor";

	public PropEditorRegistry(final ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public String getComponentName() {
		return propEditor;
	}

	@Override
	protected Class<? extends AbstractComponentBean> getBeanClass() {
		return PropEditorBean.class;
	}

	@Override
	protected Class<? extends AbstractComponentRender> getRenderClass() {
		return PropEditorRender.class;
	}

	@Override
	protected Class<? extends AbstractComponentResourceProvider> getResourceProviderClass() {
		return PropEditorResourceProvider.class;
	}

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pageParameter,
			final Element component) {
		final PropEditorBean formEditor = (PropEditorBean) super.createComponentBean(pageParameter,
				component);
		final IScriptEval scriptEval = pageParameter.getScriptEval();
		final Iterator<?> it = component.elementIterator("field");
		while (it.hasNext()) {
			final Element element = (Element) it.next();
			final PropField propField = new PropField(element);
			propField.parseElement(scriptEval);
			formEditor.getFormFields().add(propField);

			final Iterator<?> it2 = element.elementIterator("component");
			while (it2.hasNext()) {
				final Element element2 = (Element) it2.next();
				final FieldComponent fieldComponent = new FieldComponent(element2);
				fieldComponent.parseElement(scriptEval);

				final Iterator<?> it3 = element2.elementIterator("event");
				while (it3.hasNext()) {
					final Element element3 = (Element) it3.next();
					try {
						final String name = ScriptEvalUtils.replaceExpr(scriptEval,
								element3.attributeValue("name"));
						final EJavascriptEvent e = Enum.valueOf(EJavascriptEvent.class, name);
						fieldComponent.getEventCallback().put(e,
								ScriptEvalUtils.replaceExpr(scriptEval, element3.getTextTrim()));
					} catch (final Exception e) {
						throw ComponentException.wrapComponentException(e);
					}
				}
				propField.getFieldComponents().add(fieldComponent);
			}
		}
		return formEditor;
	}
}
