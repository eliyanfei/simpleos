package net.simpleframework.web.page.component.ui.chart;

import java.util.Iterator;

import javax.servlet.ServletContext;

import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.script.IScriptEval;
import net.simpleframework.util.script.ScriptEvalUtils;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.AbstractComponentRender;
import net.simpleframework.web.page.component.AbstractComponentResourceProvider;
import net.simpleframework.web.page.component.ui.chart.BarsBean.BarDataBean;
import net.simpleframework.web.page.component.ui.chart.LinesBean.LineDataBean;
import net.simpleframework.web.page.component.ui.chart.PieBean.PieDataBean;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ChartRegistry extends AbstractComponentRegistry {
	public static final String chart = "chart";

	public ChartRegistry(final ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public String getComponentName() {
		return chart;
	}

	@Override
	protected Class<? extends AbstractComponentBean> getBeanClass() {
		return ChartBean.class;
	}

	@Override
	protected Class<? extends AbstractComponentRender> getRenderClass() {
		return ChartRender.class;
	}

	@Override
	protected Class<? extends AbstractComponentResourceProvider> getResourceProviderClass() {
		return ChartResourceProvider.class;
	}

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pageParameter,
			final Element component) {
		final ChartBean chartBean = (ChartBean) super.createComponentBean(pageParameter, component);
		final IScriptEval scriptEval = pageParameter.getScriptEval();
		Element element = component.element("grid");
		if (element != null) {
			final Chart_GridBean grid = new Chart_GridBean(element, chartBean);
			grid.parseElement(scriptEval);
			chartBean.setGrid(grid);
		}
		element = component.element("xaxis");
		if (element != null) {
			final Chart_AxisBean xaxis = new Chart_AxisBean(element, chartBean);
			xaxis.parseElement(scriptEval);
			chartBean.setXaxis(xaxis);
		}
		element = component.element("yaxis");
		if (element != null) {
			final Chart_AxisBean yaxis = new Chart_AxisBean(element, chartBean);
			yaxis.parseElement(scriptEval);
			chartBean.setYaxis(yaxis);
		}

		if ((element = component.element("lines")) != null) {
			final LinesBean lines = new LinesBean(element, chartBean);
			lines.parseElement(scriptEval);
			chartBean.setGraphType(lines);
			final Iterator<?> it = element.elementIterator("line");
			while (it.hasNext()) {
				final Element lineE = (Element) it.next();
				final LineDataBean lineData = new LineDataBean(lineE, lines);
				lineData.parseElement(scriptEval);
				lines.getLines().add(lineData);
				final Iterator<?> it2 = lineE.elementIterator("data");
				while (it2.hasNext()) {
					final Element xyE = (Element) it2.next();
					final double x = ConvertUtils.toDouble(
							ScriptEvalUtils.replaceExpr(scriptEval, xyE.attributeValue("x")), 0d);
					final double y = ConvertUtils.toDouble(
							ScriptEvalUtils.replaceExpr(scriptEval, xyE.attributeValue("y")), 0d);
					lineData.addData(x, y);
				}
			}
		} else if ((element = component.element("bars")) != null) {
			final BarsBean bars = new BarsBean(element, chartBean);
			bars.parseElement(scriptEval);
			chartBean.setGraphType(bars);
			final Iterator<?> it = element.elementIterator("bar");
			while (it.hasNext()) {
				final Element barE = (Element) it.next();
				final BarDataBean barData = new BarDataBean(barE, bars);
				barData.parseElement(scriptEval);
				bars.getBars().add(barData);
				final Iterator<?> it2 = barE.elementIterator("data");
				while (it2.hasNext()) {
					final Element xyE = (Element) it2.next();
					final String catalog = ScriptEvalUtils.replaceExpr(scriptEval,
							xyE.attributeValue("catalog"));
					final double y = ConvertUtils.toDouble(
							ScriptEvalUtils.replaceExpr(scriptEval, xyE.attributeValue("y")), 0d);
					barData.addData(catalog, y);
				}
			}
		} else if ((element = component.element("pie")) != null) {
			final PieBean pie = new PieBean(element, chartBean);
			pie.parseElement(scriptEval);
			chartBean.setGraphType(pie);
			final Iterator<?> it = element.elementIterator("slice");
			while (it.hasNext()) {
				final Element sliceE = (Element) it.next();
				final PieDataBean slice = new PieDataBean(sliceE, pie);
				slice.parseElement(scriptEval);
				pie.getSlices().add(slice);
			}
		}
		return chartBean;
	}
}
