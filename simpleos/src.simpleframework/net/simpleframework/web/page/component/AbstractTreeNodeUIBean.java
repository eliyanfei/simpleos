package net.simpleframework.web.page.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import net.simpleframework.util.StringUtils;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractTreeNodeUIBean extends AbstractNodeUIBean {
	private Collection<AbstractTreeNodeUIBean> children;

	private final AbstractTreeNodeUIBean parent;

	public AbstractTreeNodeUIBean(final Element element, final AbstractTreeNodeUIBean parent) {
		super(element);
		this.parent = parent;
	}

	public AbstractTreeNodeUIBean getParent() {
		return parent;
	}

	public Collection<? extends AbstractTreeNodeUIBean> getChildren() {
		if (children == null) {
			children = new ArrayList<AbstractTreeNodeUIBean>();
		}
		return children;
	}

	public int getLevel() {
		int i = -1;
		AbstractTreeNodeUIBean node = getParent();
		while (node != null) {
			i--;
			node = node.getParent();
		}
		return Math.abs(i);
	}

	public String nodeId() {
		AbstractTreeNodeUIBean parent = getParent();
		if (parent == null) {
			return getId();
		} else {
			final LinkedList<String> ll = new LinkedList<String>();
			ll.addFirst(getId());
			while (parent != null) {
				ll.addFirst(parent.getId());
				parent = parent.getParent();
			}
			return StringUtils.join(ll, "_");
		}
	}
}
