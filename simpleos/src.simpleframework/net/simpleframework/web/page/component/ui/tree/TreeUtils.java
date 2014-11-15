package net.simpleframework.web.page.component.ui.tree;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import net.simpleframework.core.ALoggerAware;
import net.simpleframework.core.Logger;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.JavascriptUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.JsonForward;
import net.simpleframework.web.page.ReflectUtils;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class TreeUtils {
	static Logger logger = ALoggerAware.getLogger(TreeUtils.class);

	public static final String BEAN_ID = "tree_@bid";

	public static IForward dropHandle(final ComponentParameter compParameter) {
		final HashMap<String, Object> json = new HashMap<String, Object>();
		final String treeName = (String) compParameter.getBeanProperty("name");
		IForward forward = null;
		try {
			forward = (IForward) ReflectUtils.methodAccessForward.invoke(null, compParameter,
					compParameter.getBeanProperty("jobDrop"), treeName);
		} catch (final Exception e) {
			logger.warn(e);
		}
		if (forward != null) {
			json.put("responseText", forward.getResponseText(compParameter));
			json.put("ajaxRequestId", treeName);
			json.put("dropOk", false);
		} else {
			final ITreeHandle tHandle = (ITreeHandle) compParameter.getComponentHandle();
			if (tHandle != null) {
				final AbstractTreeNode drag = getTreenodeById(compParameter,
						compParameter.getRequestParameter("drag_id"));
				final AbstractTreeNode drop = getTreenodeById(compParameter,
						compParameter.getRequestParameter("drop_id"));
				json.put("dropOk", tHandle.drop(compParameter, drag, drop));
			} else {
				json.put("dropOk", false);
			}
		}
		return new JsonForward(json);
	}

	public static String nodeHandle(final ComponentParameter compParameter) {
		final AbstractTreeBean treeBean = (AbstractTreeBean) compParameter.componentBean;
		final TreeRender render = (TreeRender) treeBean.getComponentRender();
		final AbstractTreeNode node = getTreenodeById(compParameter,
				compParameter.getRequestParameter("nodeId"));
		if (node == null) {
			return "[]";
		} else {
			return render.jsonData(compParameter, getTreenodes(compParameter, node));
		}
	}

	public static Collection<? extends AbstractTreeNode> getTreenodes(
			final ComponentParameter compParameter) {
		final ITreeHandle treeHandle = (ITreeHandle) compParameter.getComponentHandle();
		Collection<? extends AbstractTreeNode> nodes = null;
		if (treeHandle != null) {
			nodes = treeHandle.getTreenodes(compParameter, null);
		}
		if (nodes == null) {
			nodes = ((AbstractTreeBean) compParameter.componentBean).getTreeNodes();
		}
		return nodes;
	}

	public static Collection<? extends AbstractTreeNode> getTreenodes(
			final ComponentParameter compParameter, final AbstractTreeNode treeNode) {
		Collection<? extends AbstractTreeNode> nodes = null;
		final ITreeHandle treeHandle = (ITreeHandle) compParameter.getComponentHandle();
		if (treeHandle != null) {
			nodes = treeHandle.getTreenodes(compParameter, treeNode);
		}
		if (nodes == null) {
			nodes = treeNode.getChildren();
		}
		return nodes;
	}

	public static AbstractTreeNode getTreenodeById(final ComponentParameter compParameter,
			final String id) {
		final String[] idArray = StringUtils.split(id, "_");
		final Collection<? extends AbstractTreeNode> treeNodes = getTreenodes(compParameter);
		return idArray != null ? findTreenode(compParameter,
				new LinkedList<String>(Arrays.asList(idArray)), treeNodes) : null;
	}

	private static AbstractTreeNode findTreenode(final ComponentParameter compParameter,
			final LinkedList<String> ll, final Collection<? extends AbstractTreeNode> coll) {
		if (ll.size() == 0 || coll.size() == 0) {
			return null;
		}
		final String id = ll.get(0);
		for (final AbstractTreeNode node : coll) {
			if (id.equals(node.getId())) {
				ll.removeFirst();
				if (ll.size() == 0) {
					return node;
				}
				final Collection<? extends AbstractTreeNode> treeNodes = getTreenodes(compParameter,
						node);
				final AbstractTreeNode find = findTreenode(compParameter, ll, treeNodes);
				if (find != null) {
					return find;
				}
			}
		}
		return null;
	}

	public static boolean isDynamicLoading(final ComponentParameter compParameter,
			final AbstractTreeNode treeNode) {
		return ConvertUtils.toBoolean(compParameter.getBeanProperty("dynamicLoading"), false);
	}

	static String genEvent2(final Object bean, final String prefix, final String[] properties) {
		if (properties == null) {
			return "";
		}
		final StringBuilder sb = new StringBuilder();
		for (final String property : properties) {
			final String event = (String) BeanUtils.getProperty(bean, property);
			if (StringUtils.hasText(event)) {
				sb.append(StringUtils.blank(prefix)).append(property).append(" = \"");
				sb.append(JavascriptUtils.escape(event)).append("\";");
			}
		}
		return sb.toString();
	}
}
