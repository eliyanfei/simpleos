package net.simpleframework.sysmgr.dict;

import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.content.component.catalog.CatalogBean;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.propeditor.PropEditorBean;
import net.simpleframework.web.page.component.ui.propeditor.PropField;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeBean;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeNode;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DictItemHandle extends AbstractDictHandle {
	@Override
	public Collection<? extends AbstractTreeNode> getCatalogTreenodes(
			final ComponentParameter compParameter, final AbstractTreeBean treeBean,
			final AbstractTreeNode treeNode, final boolean dictionary) {
		final Collection<? extends AbstractTreeNode> coll = super.getCatalogTreenodes(compParameter,
				treeBean, treeNode, dictionary);
		for (final AbstractTreeNode treeNode2 : coll) {
			if (treeNode == null) {
				treeNode2.setOpened(true);
			} else {
				final SysDict sysDict = (SysDict) treeNode2.getDataObject();
				if (sysDict != null) {
					treeNode2.setJsClickCallback("$Actions['ajax_sys_dict_item_c']('itemId="
							+ sysDict.getId() + "');");
				}
			}
			if (treeNode == null && !dictionary) {
				treeNode2.setImage(DictUtils.getCssPath(compParameter) + "/images/dict.png");
				final SysDict sysDict = getEntityBeanById(compParameter, getDocumentId(compParameter));
				if (sysDict != null) {
					treeNode2.setPostfixText(WebUtils.enclose(sysDict.getText()));
				}
			} else {
				treeNode2.setImage(DictUtils.getCssPath(compParameter) + "/images/dict_item.png");
			}
		}
		return coll;
	}

	@Override
	protected ExpressionValue getBeansSQL(final ComponentParameter compParameter,
			final Object parentId) {
		final ArrayList<Object> al = new ArrayList<Object>();
		final StringBuilder sql = new StringBuilder();
		final Object documentId = getDocumentId(compParameter);
		if (documentId == null) {
			sql.append("1=2");
		} else {
			sql.append("documentid=?");
			al.add(documentId);
			sql.append(" and ");
			if (parentId == null) {
				sql.append(Table.nullExpr(getTableEntityManager(compParameter).getTable(), "parentid"));
			} else {
				sql.append("parentid=?");
				al.add(parentId);
			}
		}
		return new ExpressionValue(sql.toString(), al.toArray());
	}

	@Override
	public Collection<PropField> getPropFields(final ComponentParameter compParameter,
			final PropEditorBean formEditor) {
		return DictUtils.applicationModule.doDictItemPropEditor(compParameter.request,
				compParameter.response, (CatalogBean) compParameter.componentBean, formEditor);
	}
}
