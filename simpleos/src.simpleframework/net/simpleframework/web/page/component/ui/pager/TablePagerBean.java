package net.simpleframework.web.page.component.ui.pager;

import java.util.LinkedHashMap;
import java.util.Map;

import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.IComponentRegistry;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TablePagerBean extends AbstractTablePagerBean {

	private Map<String, TablePagerColumn> columns;

	public TablePagerBean(final IComponentRegistry componentRegistry, final PageDocument pageDocument, final Element element) {
		super(componentRegistry, pageDocument, element);
	}

	public Map<String, TablePagerColumn> getColumns() {
		if (columns == null) {
			columns = new LinkedHashMap<String, TablePagerColumn>();
		}
		return columns;
	}

	public void parseElement() {
		super.parseElement();
		if (columns != null)
			for (TablePagerColumn menuItem : columns.values()) {
				menuItem.parseElement();
			}
	}

	@Override
	public String getDataPath() {
		return getResourceHomePath() + "/jsp/tablepager.jsp";
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsRowClick", "jsRowDblclick", "jsLoadedCallback" };
	}
}