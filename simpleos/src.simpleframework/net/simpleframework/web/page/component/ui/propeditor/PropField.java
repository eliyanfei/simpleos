package net.simpleframework.web.page.component.ui.propeditor;

import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.core.AbstractElementBean;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PropField extends AbstractElementBean {

	private String label;

	private String labelStyle = "width: 100px;";

	private String description;

	private Collection<FieldComponent> fieldComponents;

	public PropField(final Element dom4jElement) {
		super(dom4jElement);
	}

	public PropField() {
		this(null);
	}

	public Collection<FieldComponent> getFieldComponents() {
		if (fieldComponents == null) {
			fieldComponents = new ArrayList<FieldComponent>();
		}
		return fieldComponents;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(final String label) {
		this.label = label;
	}

	public String getLabelStyle() {
		return labelStyle;
	}

	public void setLabelStyle(final String labelStyle) {
		this.labelStyle = labelStyle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "description" };
	}
}
