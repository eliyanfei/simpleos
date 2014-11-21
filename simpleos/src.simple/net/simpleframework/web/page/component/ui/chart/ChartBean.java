package net.simpleframework.web.page.component.ui.chart;

import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.AbstractContainerBean;
import net.simpleframework.web.page.component.IComponentRegistry;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ChartBean extends AbstractContainerBean {

	private String title;

	private String subtitle;

	private AbstractGraphTypeBean graphType;

	private Chart_GridBean grid;

	private Chart_AxisBean xaxis, yaxis;

	public ChartBean(final IComponentRegistry componentRegistry, final PageDocument pageDocument,
			final Element element) {
		super(componentRegistry, pageDocument, element);
		setHeight("240");
		setWidth("360");
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(final String subtitle) {
		this.subtitle = subtitle;
	}

	public AbstractGraphTypeBean getGraphType() {
		return graphType;
	}

	public void setGraphType(final AbstractGraphTypeBean graphType) {
		this.graphType = graphType;
	}

	public Chart_GridBean getGrid() {
		if (grid == null) {
			grid = new Chart_GridBean(null, this);
		}
		return grid;
	}

	public void setGrid(final Chart_GridBean grid) {
		this.grid = grid;
	}

	public Chart_AxisBean getXaxis() {
		if (xaxis == null) {
			xaxis = new Chart_AxisBean(null, this);
		}
		return xaxis;
	}

	public void setXaxis(final Chart_AxisBean xaxis) {
		this.xaxis = xaxis;
	}

	public Chart_AxisBean getYaxis() {
		if (yaxis == null) {
			yaxis = new Chart_AxisBean(null, this);
		}
		return yaxis;
	}

	public void setYaxis(final Chart_AxisBean yaxis) {
		this.yaxis = yaxis;
	}
}
