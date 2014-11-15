package net.simpleframework.web.page.component.ui.tree;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class FolderTreeHandle extends AbstractTreeHandle {

	@Override
	public Collection<AbstractTreeNode> getTreenodes(final ComponentParameter compParameter,
			final AbstractTreeNode treeNode) {
		final FolderTreeBean folderTree = (FolderTreeBean) compParameter.componentBean;
		final Collection<AbstractTreeNode> nodes = new ArrayList<AbstractTreeNode>();
		if (treeNode == null) {
			final File root = new File(folderTree.getRootFolderPath());
			if (folderTree.isShowRoot()) {
				nodes.add(new FolderTreeNode(folderTree, null, root));
			} else {
				addTreeNodes(root, nodes, folderTree, null);
			}
		} else {
			final FolderTreeNode folderNode = (FolderTreeNode) treeNode;
			addTreeNodes(folderNode.getFolder(), nodes, folderTree, folderNode);
		}
		return nodes;
	}

	private void addTreeNodes(final File parent, final Collection<AbstractTreeNode> nodes,
			final FolderTreeBean folderTree, final FolderTreeNode folderNode) {
		final File[] files = parent.listFiles();
		if (files == null) {
			return;
		}
		for (final File file : files) {
			if (file.isFile() && !folderTree.isShowFile()) {
				continue;
			}
			nodes.add(new FolderTreeNode(folderTree, folderNode, file));
		}
	}
}
