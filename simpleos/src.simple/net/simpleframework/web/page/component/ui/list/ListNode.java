package net.simpleframework.web.page.component.ui.list;

import java.util.Collection;

import net.simpleframework.core.bean.IDescriptionBeanAware;
import net.simpleframework.core.bean.IIdBeanAware;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.AbstractTreeNodeUIBean;

import org.dom4j.Element;

public class ListNode extends AbstractTreeNodeUIBean {

	private String image, imageOpen;

	private boolean opened, header, footer;

	private String tooltip;

	private String postfixText;

	private int check; // 1 -1 0

	// event
	private String jsCheckCallback;

	private final Object data;

	public ListNode(final Element element, final ListBean listBean, final ListNode parent, final Object data) {
		super(element, parent);
		this.treeBean = listBean;
		this.data = data;
		setText(ConvertUtils.toString(data));
		if (data instanceof IDescriptionBeanAware) {
			setTooltip(((IDescriptionBeanAware) data).getDescription());
		}
		if (data instanceof IIdBeanAware) {
			setId(String.valueOf(((IIdBeanAware) data).getId().getValue()));
		}
	}

	public ListNode(final ListBean treeBean, final ListNode parent, final Object data) {
		this(null, treeBean, parent, data);
	}

	public String getImage() {
		return image;
	}

	public void setImage(final String image) {
		this.image = image;
	}

	public String getImageOpen() {
		return StringUtils.text(imageOpen, getImage());
	}

	public void setImageOpen(final String imageOpen) {
		this.imageOpen = imageOpen;
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

	public Object getDataObject() {
		return data;
	}

	private final ListBean treeBean;

	public ListBean getTreeBean() {
		return treeBean;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<ListNode> getChildren() {
		return (Collection<ListNode>) super.getChildren();
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "text", "tooltip", "jsClickCallback", "jsDblclickCallback", "jsCheckCallback" };
	}
}
