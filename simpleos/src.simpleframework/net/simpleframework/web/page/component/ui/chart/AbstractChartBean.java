package net.simpleframework.web.page.component.ui.chart;

import java.io.IOException;

import net.simpleframework.core.AbstractElementBean;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractChartBean extends AbstractElementBean {
	protected final ChartBean chartBean;

	public AbstractChartBean(final Element dom4jElement, final ChartBean chartBean) {
		super(dom4jElement);
		this.chartBean = chartBean;
	}

	public ChartBean getChartBean() {
		return chartBean;
	}

	protected abstract String options() throws IOException;
}
