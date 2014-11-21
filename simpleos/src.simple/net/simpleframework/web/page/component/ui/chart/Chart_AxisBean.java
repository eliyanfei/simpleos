package net.simpleframework.web.page.component.ui.chart;

import net.simpleframework.util.JSONUtils;
import net.simpleframework.util.StringUtils;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class Chart_AxisBean extends AbstractChartBean {

	private boolean showLabels = true;

	private Double min, max;

	private String title;

	private int noTicks = 5;

	private Object[] ticks;

	public Chart_AxisBean(final Element dom4jElement, final ChartBean chartBean) {
		super(dom4jElement, chartBean);
	}

	public boolean isShowLabels() {
		if (chartBean.getGraphType() instanceof PieBean) {
			return false;
		}
		return showLabels;
	}

	public void setShowLabels(final boolean showLabels) {
		this.showLabels = showLabels;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public Double getMin() {
		return min;
	}

	public void setMin(final Double min) {
		this.min = min;
	}

	public Double getMax() {
		return max;
	}

	public void setMax(final Double max) {
		this.max = max;
	}

	public int getNoTicks() {
		return noTicks;
	}

	public void setNoTicks(final int noTicks) {
		this.noTicks = noTicks;
	}

	public Object[] getTicks() {
		return ticks;
	}

	public void setTicks(final Object[] ticks) {
		this.ticks = ticks;
	}

	@Override
	protected String options() {
		final StringBuilder sb = new StringBuilder();
		sb.append("{");
		final String title = getTitle();
		if (StringUtils.hasText(title)) {
			sb.append("title: '").append(title).append("',");
		}
		sb.append("showLabels: ").append(isShowLabels()).append(",");
		Double m = getMin();
		if (m != null) {
			sb.append("min: ").append(m).append(",");
		}
		m = getMax();
		if (m != null) {
			sb.append("max: ").append(m).append(",");
		}
		sb.append("noTicks: ").append(getNoTicks()).append(",");
		sb.append("ticks: ").append(JSONUtils.toJSON(getTicks()));
		sb.append("}");
		return sb.toString();
	}
}
