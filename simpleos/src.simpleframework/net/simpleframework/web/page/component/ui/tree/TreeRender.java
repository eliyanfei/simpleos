package net.simpleframework.web.page.component.ui.tree;

import java.util.Collection;
import java.util.Map;

import net.itsite.utils.UID;
import net.simpleframework.util.HTMLUtils;
import net.simpleframework.util.JSONUtils;
import net.simpleframework.util.JavascriptUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.IPageConstants;
import net.simpleframework.web.page.component.AbstractComponentJavascriptRender;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ComponentRenderUtils;
import net.simpleframework.web.page.component.IComponentRegistry;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TreeRender extends AbstractComponentJavascriptRender {
	public TreeRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getJavascriptCode(final ComponentParameter compParameter) {
		final StringBuilder sb = new StringBuilder();
		final AbstractTreeBean treeBean = (AbstractTreeBean) compParameter.componentBean;
		treeBean.parseElement();
		final String actionFunc = treeBean.getActionFunction();
		sb.append(actionFunc).append(".treeUrl = function(jsp, p) {");
		sb.append("var url = \"").append(getResourceHomePath(compParameter));
		sb.append("/jsp/\" + jsp + \"?").append(TreeUtils.BEAN_ID).append("=");
		sb.append(treeBean.hashId()).append("\";");
		appendParameters(sb, compParameter, "url");
		sb.append("if (p) url = url.addParameter(p);");
		sb.append("return url;");
		sb.append("};");

		sb.append(actionFunc).append(".default_options.onDropAjax.push(").append(actionFunc).append(".treeUrl(\"ajax_drop.jsp\"));");
		sb.append(actionFunc).append(".createTree = function(nodes) {");
		sb.append(ComponentRenderUtils.initContainerVar(compParameter));
		sb.append(ComponentRenderUtils.VAR_CONTAINER).append(".update(\"\");");
		sb.append(actionFunc).append(".tree = new TafelTree(").append(ComponentRenderUtils.VAR_CONTAINER);
		sb.append(", nodes, Object.extend(").append(actionFunc).append(".default_options, {");
		sb.append("\"imgBase\": \"").append(getCssResourceHomePath(compParameter)).append("/images/\",");
		sb.append("\"lineStyle\" : \"").append(compParameter.getBeanProperty("lineStyle")).append("\",");
		if ((Boolean) compParameter.getBeanProperty("checkboxes")) {
			sb.append("\"checkboxes\": true,");
		}
		if ((Boolean) compParameter.getBeanProperty("checkboxesThreeState")) {
			sb.append("\"checkboxesThreeState\": true,");
		}
		final String loadedCallback = (String) compParameter.getBeanProperty("jsLoadedCallback");
		if (StringUtils.hasText(loadedCallback)) {
			sb.append("\"onLoad\": function() {");
			sb.append(loadedCallback);
			sb.append("$Loading.hide();");
			sb.append("},");
		}
		sb.append(ComponentRenderUtils.jsonHeightWidth(compParameter));
		sb.append("\"cookies\": ").append(compParameter.getBeanProperty("cookies"));
		sb.append("}));");

		// event
		sb.append(TreeUtils.genEvent2(treeBean, (actionFunc + ".tree."), new String[] { "jsClickCallback", "jsDblclickCallback", "jsCheckCallback" }));

		// insert form
		final String params = ComponentRenderUtils.genParameters(compParameter);
		if (StringUtils.hasText(params)) {
			sb.append(ComponentRenderUtils.VAR_CONTAINER).append(".insert(\"");
			sb.append(JavascriptUtils.escape(params));
			sb.append("\");");
		}

		sb.append("};");
		sb.append("try { ").append(actionFunc).append(".createTree(");
		sb.append(jsonData(compParameter)).append("); }");
		sb.append("catch(e) { }");
		return ComponentRenderUtils.wrapActionFunction(compParameter, sb.toString(), "__tree_actions_init(" + actionFunc + ");");
	}

	public String jsonData(final ComponentParameter compParameter) {
		return jsonData(compParameter, TreeUtils.getTreenodes(compParameter));
	}

	public String jsonData(final ComponentParameter compParameter, final Collection<? extends AbstractTreeNode> children) {
		if (children == null) {
			return "[]";
		}
		final StringBuilder sb = new StringBuilder();
		sb.append("[");
		int i = 0;
		final AbstractTreeBean treeBean = (AbstractTreeBean) compParameter.componentBean;
		for (final AbstractTreeNode child : children) {
			String id = UID.asString();
			// items
			final Collection<? extends AbstractTreeNode> items = TreeUtils.getTreenodes(compParameter, child);
			if (i++ > 0) {
				sb.append(",");
			}
			sb.append("{");
			sb.append("\"id\": \"").append(child.getId()).append("\",");
			String str = child.getText();
			sb.append("\"txtId\": \"").append(id).append("\",");
			sb.append("\"txt\": \"").append(JavascriptUtils.escape(str)).append("\",");
			str = child.getImage();
			if (StringUtils.hasText(str)) {
				sb.append("\"img\": \"").append(str).append("\",");
			}
			str = child.getImageClose();
			if (StringUtils.hasText(str)) {
				sb.append("\"imgclose\": \"").append(str).append("\",");
			}
			str = child.getImageOpen();
			if (StringUtils.hasText(str)) {
				sb.append("\"imgopen\": \"").append(str).append("\",");
			}
			sb.append("\"open\": ").append(child.isOpened()).append(",");
			sb.append("\"check\": ").append(child.getCheck()).append(",");
			sb.append("\"draggable\": ").append(child.isDraggable()).append(",");
			sb.append("\"acceptdrop\": ").append(child.isAcceptdrop()).append(",");
			str = child.getTooltip();
			if (StringUtils.hasText(str)) {
				sb.append("\"tooltip\": \"").append(JavascriptUtils.escape(HTMLUtils.convertHtmlLines(str))).append("\",");
			}

			str = child.getPostfixText();
			if (StringUtils.hasText(str)) {
				sb.append("\"postfix\": \"").append(JavascriptUtils.escape(str)).append("\",");
			}

			AbstractTreeNode node = child;
			if (!IPageConstants.NONE.equalsIgnoreCase(node.getContextMenu())) {
				String contextMenu;
				while (!StringUtils.hasText(contextMenu = node.getContextMenu())) {
					node = (AbstractTreeNode) node.getParent();
					if (node == null) {
						break;
					}
				}
				if (!StringUtils.hasText(contextMenu)) {
					contextMenu = treeBean.getContextMenu();
				}
				if (StringUtils.hasText(contextMenu)) {
					sb.append("\"contextMenu\": \"").append(contextMenu).append("\",");
				}
			}

			str = child.getJsClickCallback();
			if (StringUtils.hasText(str)) {
				sb.append("\"jsClickCallback\": \"$IT.stree('" + id + "');").append(JavascriptUtils.escape(str)).append("\",");
			}
			str = child.getJsDblclickCallback();
			if (StringUtils.hasText(str)) {
				sb.append("\"jsDblclickCallback\": \"").append(JavascriptUtils.escape(str)).append("\",");
			}
			str = child.getJsCheckCallback();
			if (StringUtils.hasText(str)) {
				sb.append("\"jsCheckCallback\": \"").append(JavascriptUtils.escape(str)).append("\",");
			}

			final ITreeHandle tHandle = (ITreeHandle) compParameter.getComponentHandle();
			if (tHandle != null) {
				final Map<String, Object> attributes = tHandle.getTreenodeAttributes(compParameter, child);
				if (attributes != null && attributes.size() > 0) {
					sb.append("\"attributes\": ").append(JSONUtils.toJSON(attributes)).append(",");
				}
			}

			// child.get
			if (TreeUtils.isDynamicLoading(compParameter, child) && items != null && items.size() > 0) {
				sb.append("\"canhavechildren\": true,");
				sb.append("\"openlink\": ($Actions[\"").append(compParameter.getBeanProperty("name")).append("\"] || ")
						.append(treeBean.getActionFunction()).append(").treeUrl(\"ajax_node.jsp\", \"nodeId=" + child.nodeId() + "\"),");
				sb.append("\"onopenpopulate\": ").append("function(branch, response) { return response; }");
			} else {
				sb.append("\"items\": ").append(jsonData(compParameter, items));
			}
			sb.append("}");
		}
		sb.append("]");
		return sb.toString();
	}
}
