package net.itsite.docu.my;

import java.util.ArrayList;
import java.util.Collection;

import net.itsite.docu.DocuUtils;
import net.itsite.docu.EDocuFunction;
import net.simpleframework.content.EContentStatus;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeBean;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeHandle;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeNode;
import net.simpleframework.web.page.component.ui.tree.TreeNode;

public class MyDocuTreeHandle extends AbstractTreeHandle {
	@Override
	public Collection<? extends AbstractTreeNode> getTreenodes(final ComponentParameter compParameter, final AbstractTreeNode treeNode) {
		final Collection<AbstractTreeNode> nodeList = new ArrayList<AbstractTreeNode>();
		final AbstractTreeBean treeBean = (AbstractTreeBean) compParameter.componentBean;
		if (treeNode != null) {
			if ("myManager".equals(treeNode.getId())) {
				treeNode.setImage(DocuUtils.deploy + "/images/cog.png");
				treeNode.setOpened(true);
				TreeNode treeNode2;
				for (final EDocuFunction docu : EDocuFunction.values()) {
					treeNode2 = new TreeNode(treeBean, treeNode, docu);
					treeNode2.setText("<span id='my" + docu.name() + "'>" + LocaleI18n.getMessage("Docu.my.0") + docu.toString() + "</span>");
					treeNode2.setId(docu.name());
					treeNode2.setJsClickCallback("refreshMyDocu('my" + docu.name() + "');$Actions['myDocuTableAct']('docu_type=" + docu.name()
							+ "');");
					treeNode2.setImage(DocuUtils.deploy + "/images/" + docu.name() + ".png");
					nodeList.add(treeNode2);
				}
				//
				treeNode2 = new TreeNode(treeBean, treeNode, LocaleI18n.getMessage("Docu.my.1"));
				treeNode2.setText("<span id='myNotEdit'>" + LocaleI18n.getMessage("Docu.my.1") + "</span>");
				treeNode2.setId("myNotEdit");
				treeNode2.setJsClickCallback("refreshMyDocu('myNotEdit');$Actions['myDocuTableNonAct']('docu_status=" + EContentStatus.edit.name()
						+ "');");
				treeNode2.setImage(DocuUtils.deploy + "/images/edit.png");
				nodeList.add(treeNode2);
				//
				treeNode2 = new TreeNode(treeBean, treeNode, LocaleI18n.getMessage("Docu.my.2"));
				treeNode2.setText("<span id='myNotAudit'>" + LocaleI18n.getMessage("Docu.my.2") + "</span>");
				treeNode2.setId("myNotAudit");
				treeNode2.setJsClickCallback("refreshMyDocu('myNotAudit');$Actions['myDocuTableNonAct']('docu_status=" + EContentStatus.audit.name()
						+ "');");
				treeNode2.setImage(DocuUtils.deploy + "/images/audit.png");
				nodeList.add(treeNode2);
				//
				treeNode2 = new TreeNode(treeBean, treeNode, LocaleI18n.getMessage("Docu.my.3"));
				treeNode2.setText("<span id='myFavorite'>" + LocaleI18n.getMessage("Docu.my.3") + "</span>");
				treeNode2.setId("myFavorite");
				treeNode2.setJsClickCallback("refreshMyDocu('myFavorite');$Actions['myFavoriteAct']();");
				treeNode2.setImage(DocuUtils.deploy + "/images/star.png");
				nodeList.add(treeNode2);
			}
		} else {
			return super.getTreenodes(compParameter, treeNode);
		}
		return nodeList;
	}
}
