package net.simpleframework.web.page.component.ui.pager;

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
public abstract class AbstractTablePagerBean extends PagerBean {
	private boolean showCheckbox = true;

	private boolean showVerticalLine;

	private boolean showLineNo;

	private int rowMargin = 0;

	private String jsRowClick, jsRowDblclick;

	public AbstractTablePagerBean(final IComponentRegistry componentRegistry,
			final PageDocument pageDocument, final Element element) {
		super(componentRegistry, pageDocument, element);
	}

	public boolean isShowCheckbox() {
		return showCheckbox;
	}

	public void setShowCheckbox(final boolean showCheckbox) {
		this.showCheckbox = showCheckbox;
	}

	public boolean isShowVerticalLine() {
		return showVerticalLine;
	}

	public void setShowVerticalLine(final boolean showVerticalLine) {
		this.showVerticalLine = showVerticalLine;
	}

	public int getRowMargin() {
		return rowMargin;
	}

	public void setRowMargin(final int rowMargin) {
		this.rowMargin = rowMargin;
	}

	public boolean isShowLineNo() {
		return showLineNo;
	}

	public void setShowLineNo(final boolean showLineNo) {
		this.showLineNo = showLineNo;
	}

	public String getJsRowClick() {
		return jsRowClick;
	}

	public void setJsRowClick(final String jsRowClick) {
		this.jsRowClick = jsRowClick;
	}

	public String getJsRowDblclick() {
		return jsRowDblclick;
	}

	public void setJsRowDblclick(final String jsRowDblclick) {
		this.jsRowDblclick = jsRowDblclick;
	}
}
