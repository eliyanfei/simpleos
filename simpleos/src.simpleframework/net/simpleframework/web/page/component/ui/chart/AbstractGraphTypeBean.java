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
public abstract class AbstractGraphTypeBean extends AbstractChartBean {
	private int lineWidth = 1;

	private boolean fill;

	private String fillColor;

	private double fillOpacity;

	private boolean showPoints = true;

	private boolean mouseTrack = true;

	public AbstractGraphTypeBean(final Element dom4jElement, final ChartBean chartBean) {
		super(dom4jElement, chartBean);
	}

	public int getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(final int lineWidth) {
		this.lineWidth = lineWidth;
	}

	public boolean isFill() {
		return fill;
	}

	public void setFill(final boolean fill) {
		this.fill = fill;
	}

	public String getFillColor() {
		return fillColor;
	}

	public void setFillColor(final String fillColor) {
		this.fillColor = fillColor;
	}

	public double getFillOpacity() {
		return fillOpacity;
	}

	public void setFillOpacity(final double fillOpacity) {
		this.fillOpacity = fillOpacity;
	}

	public boolean isMouseTrack() {
		return mouseTrack;
	}

	public void setMouseTrack(final boolean mouseTrack) {
		this.mouseTrack = mouseTrack;
	}

	public boolean isShowPoints() {
		return showPoints;
	}

	public void setShowPoints(final boolean showPoints) {
		this.showPoints = showPoints;
	}

	protected abstract String data();

	@Override
	protected String options() {
		final StringBuilder sb = new StringBuilder();
		sb.append("{lineWidth: ").append(getLineWidth()).append(",");
		sb.append("fill: ").append(isFill()).append(",");
		final String str = getFillColor();
		if (StringUtils.hasText(str)) {
			sb.append("fillColor: '").append(str).append("',");
		}
		sb.append("fillOpacity: ").append(getFillOpacity()).append(",");
		sb.append("showPoints: ").append(isShowPoints()).append(",");
		sb.append("mouseTrack: ").append(isMouseTrack()).append(",");
		append_options(sb);
		sb.append("show: true}");
		return sb.toString();
	}

	protected void options_points(final StringBuilder sb) {
		if (isShowPoints()) {
			sb.append("points: { show: true },");
		}
	}

	protected void options_mouse(final StringBuilder sb) {
		if (isMouseTrack()) {
			sb.append("mouse: { track: true");
			if (this instanceof BarsBean) {
				sb.append(", trackFormatter: function(obj) { return obj.y; }");
			}
			sb.append("},");
		}
	}

	protected void append_options(final StringBuilder sb) {
	}
}
