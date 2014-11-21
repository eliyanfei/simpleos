package net.simpleframework.core;

import java.util.Iterator;
import java.util.Map;

import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.LangUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.util.script.IScriptEval;
import net.simpleframework.util.script.ScriptEvalUtils;

import org.apache.commons.collections.map.AbstractReferenceMap;
import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractElementBean extends AAttributeAware implements IElementBean {
	private Element element;

	public AbstractElementBean(final Element element) {
		super(AbstractReferenceMap.HARD, AbstractReferenceMap.WEAK);
		this.element = element;
	}

	public Element getElement() {
		return element;
	}

	public void setElement(final Element element) {
		this.element = element;
	}

	protected void removeChildren(final Element element, final String name) {
		if (element == null) {
			return;
		}
		for (final Object ele : element.elements(name)) {
			element.remove((Element) ele);
		}
	}

	protected void removeChildren(final String name) {
		removeChildren(getElement(), name);
	}

	protected void addElement(final AbstractElementBean bean) {
		addElement(getElement(), bean);
	}

	protected void addElement(final Element element, final AbstractElementBean bean) {
		if (element == null || bean == null) {
			return;
		}
		final Element ele = bean.getElement();
		if (ele != null) {
			if (ele.getParent() != null) {
				ele.setParent(null);
			}
			element.add(ele);
		}
	}

	protected void setElementAttribute(final String[] names) {
		if (names == null) {
			return;
		}
		for (final String name : names) {
			setElementAttribute(name, BeanUtils.getProperty(this, name));
		}
	}

	protected void setElementAttribute(final String name, final Object object) {
		final Element element = getElement();
		if (element == null) {
			return;
		}
		final String value = ConvertUtils.toString(object);
		final Attribute attribute = element.attribute(name);
		if (StringUtils.hasText(value)) {
			if (attribute != null) {
				attribute.setValue(value);
			} else {
				element.addAttribute(name, value);
			}
		} else {
			if (attribute != null) {
				element.remove(attribute);
			}
		}
	}

	protected void setElementContent(final String name, final Object object) {
		final Element element = getElement();
		if (element == null) {
			return;
		}
		final String value = ConvertUtils.toString(object);
		final Element ele = element.element(name);
		if (StringUtils.hasText(value)) {
			if (ele != null) {
				ele.clearContent();
				ele.addCDATA(value);
			} else {
				element.addElement(name).addCDATA(value);
			}
		} else {
			element.remove(ele);
		}
	}

	protected String[] elementAttributes() {
		return null;
	}

	public void syncElement() {
		final Map<?, ?> data = BeanUtils.toMap(this, true);
		for (final Map.Entry<?, ?> entry : data.entrySet()) {
			final Object o = entry.getValue();
			if (o instanceof AbstractElementBean || o instanceof Element) {
				continue;
			}
			final String key = (String) entry.getKey();
			final String[] arr = elementAttributes();
			if (arr != null && LangUtils.contains(arr, key)) {
				setElementContent(key, o);
			} else {
				setElementAttribute(key, o);
			}
		}
	}

	public void parseElement() {
		parseElement(null);
	}

	protected void beforeParse() {
	}
	protected void endParse() {
	}

	public void parseElement(final IScriptEval scriptEval) {
		final Element element = getElement();
		beforeParse();
		if (element == null) {
			return;
		}
		final Iterator<?> it = element.attributeIterator();
		while (it.hasNext()) {
			final Attribute attribute = (Attribute) it.next();
			String val = LocaleI18n.replaceI18n(attribute.getValue());
			if (scriptEval != null) {
				val = ScriptEvalUtils.replaceExpr(scriptEval, val);
			}
			final String name = attribute.getName();
			if (!name.contains(":")) {
				BeanUtils.setProperty(this, name, val);
			}
		}
		final String[] arr = elementAttributes();
		if (arr != null && arr.length > 0) {
			setBeanFromElementAttributes(scriptEval, arr);
		}
		endParse();
	}

	public void setBeanFromElementAttributes(final String[] attributes) {
		setBeanFromElementAttributes(null, attributes);
	}

	public void setBeanFromElementAttributes(final IScriptEval scriptEval, final String[] attributes) {
		if (attributes == null) {
			return;
		}
		final Element element = getElement();
		for (final String attribute : attributes) {
			String text = element.elementText(attribute);
			if (StringUtils.hasText(text)) {
				text = LocaleI18n.replaceI18n(text.trim());
				if (scriptEval != null) {
					text = ScriptEvalUtils.replaceExpr(scriptEval, text);
				}
				BeanUtils.setProperty(this, attribute, text);
			}
		}
	}
}
