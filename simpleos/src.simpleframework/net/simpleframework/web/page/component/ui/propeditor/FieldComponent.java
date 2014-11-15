package net.simpleframework.web.page.component.ui.propeditor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.simpleframework.core.AbstractElementBean;
import net.simpleframework.web.page.EJavascriptEvent;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class FieldComponent extends AbstractElementBean {

	private String name;

	private EComponentType type;

	private String style;

	private String attributes;

	private String defaultValue;

	private Map<EJavascriptEvent, String> eventCallback;

	public FieldComponent(final Element dom4jElement) {
		super(dom4jElement);
	}

	public FieldComponent() {
		this(null);
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public EComponentType getType() {
		return type == null ? EComponentType.text : type;
	}

	public void setType(final EComponentType type) {
		this.type = type;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(final String style) {
		this.style = style;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(final String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(final String attributes) {
		this.attributes = attributes;
	}

	public Map<EJavascriptEvent, String> getEventCallback() {
		if (eventCallback == null) {
			eventCallback = new ConcurrentHashMap<EJavascriptEvent, String>();
		}
		return eventCallback;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "defaultValue" };
	}
}
