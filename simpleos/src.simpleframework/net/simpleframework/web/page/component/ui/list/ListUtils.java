package net.simpleframework.web.page.component.ui.list;

import java.util.Collection;

import net.simpleframework.core.ALoggerAware;
import net.simpleframework.core.Logger;
import net.simpleframework.web.page.component.ComponentParameter;

public abstract class ListUtils {
	static Logger logger = ALoggerAware.getLogger(ListUtils.class);

	public static final String BEAN_ID = "list_@bid";

	public static Collection<? extends ListNode> getListnodes(final ComponentParameter compParameter) {
		final IListHandle listHandle = (IListHandle) compParameter.getComponentHandle();
		Collection<? extends ListNode> nodes = null;
		if (listHandle != null) {
			nodes = listHandle.getListnodes(compParameter, null);
		}
		if (nodes == null) {
			nodes = ((ListBean) compParameter.componentBean).getListNodes();
		}
		return nodes;
	}

	public static Collection<? extends ListNode> getListnodes(final ComponentParameter compParameter, final ListNode treeNode) {
		Collection<? extends ListNode> nodes = null;
		final IListHandle listHandle = (IListHandle) compParameter.getComponentHandle();
		if (listHandle != null) {
			nodes = listHandle.getListnodes(compParameter, treeNode);
		}
		if (nodes == null) {
			nodes = treeNode.getChildren();
		}
		return nodes;
	}
}
