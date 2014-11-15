package net.simpleframework.web.page.component.ui.chart;

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
public abstract class AbstractGraphData extends AbstractElementBean {
	private final AbstractGraphTypeBean graphType;

	private String label;

	private String color;

	public AbstractGraphData(final Element dom4jElement, final AbstractGraphTypeBean graphType) {
		super(dom4jElement);
		this.graphType = graphType;
	}

	public AbstractGraphTypeBean getGraphType() {
		return graphType;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(final String label) {
		this.label = label;
	}

	public String getColor() {
		return color;
	}

	public void setColor(final String color) {
		this.color = color;
	}

	protected void appendAttr(final StringBuilder sb) {
		String str = getLabel();
		if (StringUtils.hasText(str)) {
			sb.append("label: \"").append(str).append("\",");
		}
		str = getColor();
		if (StringUtils.hasText(str)) {
			sb.append("color: \"").append(str).append("\",");
		}
	}
}
