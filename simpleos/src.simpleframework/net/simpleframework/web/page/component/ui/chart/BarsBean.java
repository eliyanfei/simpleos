package net.simpleframework.web.page.component.ui.chart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.simpleframework.util.StringUtils;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class BarsBean extends AbstractGraphTypeBean {
	private Collection<BarDataBean> bars;

	private double barWidth = 1;

	public BarsBean(final Element dom4jElement, final ChartBean chartBean) {
		super(dom4jElement, chartBean);
		setFill(true);
		setFillOpacity(0.4);
		chartBean.getYaxis().setMin(0d);
		final Chart_AxisBean xaxis = chartBean.getXaxis();
		xaxis.setMin(0d);
	}

	public Collection<BarDataBean> getBars() {
		if (bars == null) {
			bars = new ArrayList<BarDataBean>();
		}
		return bars;
	}

	public double getBarWidth() {
		return barWidth;
	}

	public void setBarWidth(final double barWidth) {
		this.barWidth = barWidth;
	}

	private void initTicks() {
		final HashMap<String, ArrayList<Object[]>> m = new LinkedHashMap<String, ArrayList<Object[]>>();
		for (final BarDataBean barData : getBars()) {
			for (final Object[] objs : barData.getData()) {
				final String key = (String) objs[0];
				if (!StringUtils.hasText(key)) {
					continue;
				}
				ArrayList<Object[]> d = m.get(key);
				if (d == null) {
					m.put(key, d = new ArrayList<Object[]>());
				}
				d.add(objs);
			}
		}
		double barWidth = getBarWidth();
		final double space = barWidth * 2;
		barWidth += barWidth / 3;
		double d = 0d;
		for (final Map.Entry<String, ArrayList<Object[]>> entry : m.entrySet()) {
			for (final Object[] objs : entry.getValue()) {
				d = d + barWidth;
				objs[1] = d;
			}
			d = d + space;
		}
		final Chart_AxisBean xaxis = chartBean.getXaxis();
		xaxis.setMax(d - space + barWidth);
		final ArrayList<Object> ticks = new ArrayList<Object>();
		for (final Map.Entry<String, ArrayList<Object[]>> entry : m.entrySet()) {
			final ArrayList<Object[]> al = entry.getValue();
			final Object[] o1 = al.get(0);
			final Object[] o2 = al.get(al.size() - 1);
			ticks.add(new Object[] { ((Double) o1[1] + (Double) o2[1]) / 2, entry.getKey() });
		}
		chartBean.getXaxis().setTicks(ticks.toArray());
	}

	@Override
	protected String data() {
		initTicks();

		final StringBuilder sb = new StringBuilder();
		sb.append("[");
		int i = 0;
		for (final BarDataBean barData : getBars()) {
			if (i++ > 0) {
				sb.append(",");
			}
			sb.append("{");
			barData.appendAttr(sb);
			sb.append("bars: ").append(options()).append(",");
			options_mouse(sb);
			sb.append("data: [");
			int j = 0;
			for (final Object[] objs : barData.getData()) {
				if (j++ > 0) {
					sb.append(",");
				}
				sb.append("[").append(objs[1]).append(", ").append(objs[2]).append("]");
			}
			sb.append("]");
			sb.append("}");
		}
		sb.append("]");
		return sb.toString();
	}

	@Override
	protected void append_options(final StringBuilder sb) {
		sb.append("barWidth: ").append(getBarWidth()).append(",");
	}

	public static class BarDataBean extends AbstractGraphData {
		private List<Object[]> data;

		public BarDataBean(final Element dom4jElement, final AbstractGraphTypeBean graphType) {
			super(dom4jElement, graphType);
		}

		public List<Object[]> getData() {
			if (data == null) {
				data = new ArrayList<Object[]>();
			}
			return data;
		}

		public void addData(final String catalog, final Number y) {
			getData().add(new Object[] { catalog, 0, y });
		}
	}
}
