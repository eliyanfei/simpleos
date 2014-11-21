package net.simpleframework.example;

import java.util.Random;

import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.chart.AbstractChartHandle;
import net.simpleframework.web.page.component.ui.chart.AbstractGraphTypeBean;
import net.simpleframework.web.page.component.ui.chart.ChartBean;
import net.simpleframework.web.page.component.ui.chart.LinesBean;
import net.simpleframework.web.page.component.ui.chart.LinesBean.LineDataBean;

public class MyLineChartHandle extends AbstractChartHandle {
	private static Random random = new Random();

	@Override
	public AbstractGraphTypeBean getGraphType(final ComponentParameter compParameter) {
		final LinesBean lines = new LinesBean((ChartBean) compParameter.componentBean);
		final LineDataBean data = new LineDataBean(lines);
		lines.getLines().add(data);
		int j = 0;
		for (int i = 0; i < 30; i++) {
			j = j + random.nextInt(5);
			data.addData(j, random.nextInt(20));
		}
		return lines;
	}
}
