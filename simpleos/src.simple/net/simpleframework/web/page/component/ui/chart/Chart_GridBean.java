package net.simpleframework.web.page.component.ui.chart;

import net.simpleframework.util.StringUtils;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class Chart_GridBean extends AbstractChartBean {

	private String color = "#999", backgroundColor;

	private boolean verticalLines = true, horizontalLines = true;

	private int outlineWidth = 1;

	public Chart_GridBean(final Element dom4jElement, final ChartBean chartBean) {
		super(dom4jElement, chartBean);
	}

	public String getColor() {
		return color;
	}

	public void setColor(final String color) {
		this.color = color;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(final String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public boolean isVerticalLines() {
		return verticalLines;
	}

	public void setVerticalLines(final boolean verticalLines) {
		this.verticalLines = verticalLines;
	}

	public boolean isHorizontalLines() {
		return horizontalLines;
	}

	public void setHorizontalLines(final boolean horizontalLines) {
		this.horizontalLines = horizontalLines;
	}

	public int getOutlineWidth() {
		return outlineWidth;
	}

	public void setOutlineWidth(final int outlineWidth) {
		this.outlineWidth = outlineWidth;
	}

	@Override
	protected String options() {
		final StringBuilder sb = new StringBuilder();
		sb.append("{");
		String str = getColor();
		if (StringUtils.hasText(str)) {
			sb.append("color: '").append(str).append("',");
		}
		str = getBackgroundColor();
		if (StringUtils.hasText(str)) {
			sb.append("backgroundColor: '").append(str).append("',");
		}
		sb.append("verticalLines: ").append(isVerticalLines()).append(",");
		sb.append("horizontalLines: ").append(isHorizontalLines()).append(",");
		sb.append("outlineWidth: ").append(getOutlineWidth());
		sb.append("}");
		return sb.toString();
	}
}
