package net.simpleframework.web.page.component.ui.dhx;

import java.util.ArrayList;
import java.util.Collection;

import org.dom4j.Element;

import net.simpleframework.core.AbstractElementBean;
import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.AbstractContainerBean;
import net.simpleframework.web.page.component.IComponentRegistry;

/**
 * @Description
 * @Date 2012-11-28
 * @author lxy
 */
public class DhxLayoutBean extends AbstractContainerBean {
	// 继承之containerId
	// 或者
	// layout要求之 parent，诸如嵌套在某一cell中的layout
	private DhxLayoutBean parentLayout = null;

	private String parentCellName = null;

	private boolean fullScreen = false;// true时全屏：document.body

	private EDhxLayoutSkin skin = EDhxLayoutSkin.skyblue;// skin type: optional

	private String imagePath;

	private EDhxLayoutPattern pattern;// layout type: optional

	private Margins _margins;

	private Collection<Cell> cells;
	private String jsDock;
	private String jsUndock;
	private String jsResizeFinish;
	private String jsPanelResizeFinish;
	private String jsExpand;
	private String jsCollapse;
	private String jsDblClick;

	public String getJsDock() {
		return jsDock;
	}

	public void setJsDock(String jsDock) {
		this.jsDock = jsDock;
	}

	public String getJsUndock() {
		return jsUndock;
	}

	public void setJsUndock(String jsUndock) {
		this.jsUndock = jsUndock;
	}

	public String getJsResizeFinish() {
		return jsResizeFinish;
	}

	public void setJsResizeFinish(String jsResizeFinish) {
		this.jsResizeFinish = jsResizeFinish;
	}

	public String getJsPanelResizeFinish() {
		return jsPanelResizeFinish;
	}

	public void setJsPanelResizeFinish(String jsPanelResizeFinish) {
		this.jsPanelResizeFinish = jsPanelResizeFinish;
	}

	public String getJsExpand() {
		return jsExpand;
	}

	public void setJsExpand(String jsExpand) {
		this.jsExpand = jsExpand;
	}

	public String getJsCollapse() {
		return jsCollapse;
	}

	public void setJsCollapse(String jsCollapse) {
		this.jsCollapse = jsCollapse;
	}

	public String getJsDblClick() {
		return jsDblClick;
	}

	public void setJsDblClick(String jsDblClick) {
		this.jsDblClick = jsDblClick;
	}

	/*
	 * @see net.simpleframework.core.AbstractElementBean#elementAttributes()
	 * 由于在xsd中由xsd:sequence,有别与其他属性，在此做补充属性处理，详见AbstractElementBean
	 */
	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsDock", "jsUndock", "jsResizeFinish", "jsPanelResizeFinish", "jsExpand", "jsCollapse", "jsDblClick" };
	}

	public DhxLayoutBean(final IComponentRegistry componentRegistry, final PageDocument pageDocument, final Element element) {
		super(componentRegistry, pageDocument, element);
	}

	/*---------------------*/
	public static class Cell extends AbstractElementBean {

		DhxLayoutBean parentLayout;

		private String cellName;

		// Title：
		private boolean showHeader = false;// 显示标题与否
		private String headerText;// 标题

		// Attach Content
		private String url;// 关联的URL
		// or：
		private String targetId;// 关联的HTML Object
		// or:
		private String refLayout;// 嵌套引用的Layout

		// Sizing
		private boolean fixWidth = true;// 宽固定与否
		private boolean fixHeight = true;// 高度固定与否

		// Width and height
		private int height = 0;
		private int width = 0;

		public Cell(Element element, DhxLayoutBean parent) {
			super(element);
			this.parentLayout = parent;
		}

		public Cell(DhxLayoutBean parent) {
			this(null, parent);
		}

		public String getCellName() {
			return cellName;
		}

		public void setCellName(String cellName) {
			this.cellName = cellName;
		}

		public boolean isShowHeader() {
			return showHeader;
		}

		public void setShowHeader(boolean showHeader) {
			this.showHeader = showHeader;
		}

		public String getHeaderText() {
			return headerText;
		}

		public void setHeaderText(String headerText) {
			this.headerText = headerText;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getTargetId() {
			return targetId;
		}

		public void setTargetId(String targetId) {
			this.targetId = targetId;
		}

		public String getRefLayout() {
			return refLayout;
		}

		public void setRefLayout(String refLayout) {
			this.refLayout = refLayout;
		}

		public boolean isFixWidth() {
			return fixWidth;
		}

		public void setFixWidth(boolean fixWidth) {
			this.fixWidth = fixWidth;
		}

		public boolean isFixHeight() {
			return fixHeight;
		}

		public void setFixHeight(boolean fixHeight) {
			this.fixHeight = fixHeight;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

	}

	/*
	 * DHTML Layout Margins
	 */
	public static class Margins extends AbstractElementBean {
		DhxLayoutBean parentLayout;

		private int top = 2;// top margin

		private int left = 2;// left margin

		private int bottom = -4;// bottom margin

		private int right = -4;// right margin

		public Margins(Element element, DhxLayoutBean parent) {
			super(element);
			this.parentLayout = parent;
		}

		public int getTop() {
			return top;
		}

		public void setTop(int top) {
			this.top = top;
		}

		public int getLeft() {
			return left;
		}

		public void setLeft(int left) {
			this.left = left;
		}

		public int getBottom() {
			return bottom;
		}

		public void setBottom(int bottom) {
			this.bottom = bottom;
		}

		public int getRight() {
			return right;
		}

		public void setRight(int right) {
			this.right = right;
		}

	}

	public DhxLayoutBean getParentLayout() {
		return parentLayout;
	}

	public void setParentLayout(DhxLayoutBean parentLayout) {
		this.parentLayout = parentLayout;
	}

	public String getParentCellName() {
		return parentCellName;
	}

	public void setParentCellName(String parentCellName) {
		this.parentCellName = parentCellName;
	}

	public boolean isFullScreen() {
		return fullScreen;
	}

	public void setFullScreen(boolean fullScreen) {
		this.fullScreen = fullScreen;
	}

	public EDhxLayoutSkin getSkin() {
		return skin;
	}

	public void setSkin(EDhxLayoutSkin skin) {
		this.skin = skin;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public EDhxLayoutPattern getPattern() {
		return pattern;
	}

	public void setPattern(EDhxLayoutPattern pattern) {
		this.pattern = pattern;
	}

	public Margins get_margins() {
		return _margins;
	}

	public void set_margins(Margins _margins) {
		this._margins = _margins;
	}

	public Collection<Cell> getCells() {
		if (cells == null)
			cells = new ArrayList<Cell>();
		return cells;
	}

	public void setCells(Collection<Cell> cells) {
		this.cells = cells;
	}

}
