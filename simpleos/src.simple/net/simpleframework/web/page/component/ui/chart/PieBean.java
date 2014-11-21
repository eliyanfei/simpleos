package net.simpleframework.web.page.component.ui.chart;

import java.util.ArrayList;
import java.util.Collection;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PieBean extends AbstractGraphTypeBean {

	private double sizeRatio = 0.7;

	private Collection<PieDataBean> slices;

	public PieBean(final Element dom4jElement, final ChartBean chartBean) {
		super(dom4jElement, chartBean);
		setFill(true);
		setFillOpacity(0.6);
		chartBean.getXaxis().setShowLabels(false);
		chartBean.getYaxis().setShowLabels(false);
	}

	public Collection<PieDataBean> getSlices() {
		if (slices == null) {
			slices = new ArrayList<PieDataBean>();
		}
		return slices;
	}

	public double getSizeRatio() {
		return sizeRatio;
	}

	public void setSizeRatio(final double sizeRatio) {
		this.sizeRatio = sizeRatio;
	}

	@Override
	protected String data() {
		final StringBuilder sb = new StringBuilder();
		sb.append("[");
		int i = 0;
		for (final PieDataBean slice : getSlices()) {
			if (i++ > 0) {
				sb.append(",");
			}
			sb.append("{");
			slice.appendAttr(sb);
			sb.append("pie: ").append(options()).append(",");
			options_mouse(sb);
			sb.append("data: [[0, ").append(slice.getData()).append("]]");
			sb.append("}");
		}
		sb.append("]");
		return sb.toString();
	}

	@Override
	protected void append_options(final StringBuilder sb) {
		sb.append("explode: 4,");
		sb.append("sizeRatio: ").append(getSizeRatio()).append(",");
	}

	public static class PieDataBean extends AbstractGraphData {

		private double data;

		public PieDataBean(final Element dom4jElement, final AbstractGraphTypeBean graphType) {
			super(dom4jElement, graphType);
		}

		public double getData() {
			return data;
		}

		public void setData(final double data) {
			this.data = data;
		}
	}
}
