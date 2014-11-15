package net.simpleframework.web.page.component.ui.pager;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;

import net.simpleframework.util.script.IScriptEval;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentResourceProvider;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TablePagerRegistry extends PagerRegistry {
	public static final String tablePager = "tablePager";

	public TablePagerRegistry(final ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public String getComponentName() {
		return tablePager;
	}

	@Override
	public String getComponentDeploymentName() {
		return PagerRegistry.pager;
	}

	@Override
	protected Class<? extends AbstractComponentBean> getBeanClass() {
		return TablePagerBean.class;
	}

	@Override
	protected Class<? extends AbstractComponentResourceProvider> getResourceProviderClass() {
		return TablePagerResourceProvider.class;
	}

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pageParameter,
			final Element component) {
		final TablePagerBean tablePager = (TablePagerBean) super.createComponentBean(pageParameter,
				component);
		tablePager.parseElement();
		final IScriptEval scriptEval = pageParameter.getScriptEval();
		final Element columns = component.element("columns");
		if (columns != null) {
			final Iterator<?> it = columns.elementIterator("column");
			final Map<String, TablePagerColumn> columnsMap = tablePager.getColumns();
			while (it.hasNext()) {
				final Element column = (Element) it.next();
				final TablePagerColumn tpColumn = new TablePagerColumn(column);
				tpColumn.parseElement(scriptEval);
				columnsMap.put(tpColumn.getColumnName(), tpColumn);
			}
		}
		return tablePager;
	}
}
