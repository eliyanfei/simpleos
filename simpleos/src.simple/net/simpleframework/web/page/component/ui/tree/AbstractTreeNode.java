package net.simpleframework.web.page.component.ui.tree;

import java.util.Collection;

import net.simpleframework.core.bean.IDescriptionBeanAware;
import net.simpleframework.core.bean.IIdBeanAware;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.AbstractTreeNodeUIBean;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractTreeNode extends AbstractTreeNodeUIBean {
	private boolean dynamicLoading;

	private String image, imageClose, imageOpen;

	private boolean draggable, acceptdrop;

	private boolean opened;

	private String tooltip;

	private String postfixText;

	private boolean checkbox;

	private int check; // 1 -1 0

	private String contextMenu;

	// event
	private String jsCheckCallback;

	private final Object data;

	public AbstractTreeNode(final Element element, final AbstractTreeBean treeBean,
			final AbstractTreeNode parent, final Object data) {
		super(element, parent);
		this.treeBean = treeBean;
		this.data = data;
		setText(ConvertUtils.toString(data));
		if (data instanceof IDescriptionBeanAware) {
			setTooltip(((IDescriptionBeanAware) data).getDescription());
		}
		if (data instanceof IIdBeanAware) {
			setId(String.valueOf(((IIdBeanAware) data).getId().getValue()));
		}
	}

	public AbstractTreeNode(final AbstractTreeBean treeBean, final AbstractTreeNode parent,
			final Object data) {
		this(null, treeBean, parent, data);
	}

	public boolean isDynamicLoading() {
		return dynamicLoading;
	}

	public void setDynamicLoading(final boolean dynamicLoading) {
		this.dynamicLoading = dynamicLoading;
	}

	public String getImage() {
		return image;
	}

	public void setImage(final String image) {
		this.image = image;
	}

	public String getImageClose() {
		return StringUtils.text(imageClose, getImage());
	}

	public void setImageClose(final String imageClose) {
		this.imageClose = imageClose;
	}

	public String getImageOpen() {
		return StringUtils.text(imageOpen, getImage());
	}

	public void setImageOpen(final String imageOpen) {
		this.imageOpen = imageOpen;
	}

	public boolean isDraggable() {
		return draggable;
	}

	public void setDraggable(final boolean draggable) {
		this.draggable = draggable;
	}

	public boolean isAcceptdrop() {
		return acceptdrop;
	}

	public void setAcceptdrop(final boolean acceptdrop) {
		this.acceptdrop = acceptdrop;
	}

	public boolean isOpened() {
		return opened;
	}

	public void setOpened(final boolean opened) {
		this.opened = opened;
	}

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(final String tooltip) {
		this.tooltip = tooltip;
	}

	public String getPostfixText() {
		return postfixText;
	}

	public void setPostfixText(final String postfixText) {
		this.postfixText = postfixText;
	}

	public boolean isCheckbox() {
		return checkbox;
	}

	public void setCheckbox(final boolean checkbox) {
		this.checkbox = checkbox;
	}

	public int getCheck() {
		return check;
	}

	public void setCheck(final int check) {
		this.check = check;
	}

	public String getJsCheckCallback() {
		return jsCheckCallback;
	}

	public void setJsCheckCallback(final String jsCheckCallback) {
		this.jsCheckCallback = jsCheckCallback;
	}

	public String getContextMenu() {
		return contextMenu;
	}

	public void setContextMenu(final String contextMenu) {
		this.contextMenu = contextMenu;
	}

	public Object getDataObject() {
		return data;
	}

	private final AbstractTreeBean treeBean;

	public AbstractTreeBean getTreeBean() {
		return treeBean;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<AbstractTreeNode> getChildren() {
		return (Collection<AbstractTreeNode>) super.getChildren();
	}
	
	@Override
	public void parseElement() {
		super.parseElement();
		if (getChildren() != null)
			for (AbstractTreeNode menuItem : getChildren()) {
				menuItem.parseElement();
			}
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "text", "tooltip", "jsClickCallback", "jsDblclickCallback",
				"jsCheckCallback" };
	}
}
