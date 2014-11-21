package net.simpleframework.sysmgr.dict;

import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.HandleException;
import net.simpleframework.web.page.component.ui.propeditor.EComponentType;
import net.simpleframework.web.page.component.ui.propeditor.FieldComponent;
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
public class DictTypeHandle extends AbstractDictHandle {
	@Override
	public Collection<? extends AbstractTreeNode> getCatalogTreenodes(
			final ComponentParameter compParameter, final AbstractTreeBean treeBean,
			final AbstractTreeNode treeNode, final boolean dictionary) {
		final Collection<? extends AbstractTreeNode> coll = super.getCatalogTreenodes(compParameter,
				treeBean, treeNode, dictionary);
		for (final AbstractTreeNode treeNode2 : coll) {
			final SysDict sysDict = (SysDict) treeNode2.getDataObject();
			if (sysDict != null && sysDict.getMark() == SysDict.MARK_SELECTED) {
				if (!dictionary) {
					final StringBuilder sb = new StringBuilder();
					sb.append("__sys_dict_items('");
					sb.append(getDocumentIdParameterName(compParameter));
					sb.append("=").append(sysDict.getId());
					sb.append("');");
					treeNode2.setJsClickCallback(sb.toString());
				}
				treeNode2.setImage(DictUtils.getCssPath(compParameter) + "/images/dict.png");
			} else {
				treeNode2.setImage(DictUtils.getCssPath(compParameter) + "/images/dict_open.png");
			}

			if (treeNode == null) {
				treeNode2.setOpened(true);
				if (!dictionary) {
					treeNode2.setImage(DictUtils.getCssPath(compParameter) + "/images/dict_root.png");
				}
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
			sql.append(Table.nullExpr(getTableEntityManager(compParameter).getTable(), "documentid"));
		} else {
			sql.append("documentid=?");
			al.add(documentId);
		}
		sql.append(" and ");
		if (parentId == null) {
			sql.append(Table.nullExpr(getTableEntityManager(compParameter).getTable(), "parentid"));
		} else {
			sql.append("parentid=?");
			al.add(parentId);
		}
		return new ExpressionValue(sql.toString(), al.toArray());
	}

	public static final short MARK_NORMAL = 0;

	public static final short MARK_FOLDER = 1;

	@Override
	public Collection<PropField> getPropFields(final ComponentParameter compParameter,
			final PropEditorBean formEditor) {
		final ArrayList<PropField> al = new ArrayList<PropField>(formEditor.getFormFields());
		final PropField propField = new PropField();
		propField.setLabel(LocaleI18n.getMessage("DictTypeHandle.0"));

		final FieldComponent fieldComponent = new FieldComponent();
		propField.getFieldComponents().add(fieldComponent);

		fieldComponent.setName("catalog_mark");
		fieldComponent.setType(EComponentType.select);

		final StringBuilder sb = new StringBuilder();
		sb.append(MARK_NORMAL).append("=");
		sb.append(LocaleI18n.getMessage("DictTypeHandle.1")).append(";");
		sb.append(MARK_FOLDER).append("=");
		sb.append(LocaleI18n.getMessage("DictTypeHandle.2"));
		fieldComponent.setDefaultValue(sb.toString());

		al.add(3, propField);
		return al;
	}

	@Override
	public <T extends IDataObjectBean> void doDelete(final ComponentParameter compParameter,
			final IDataObjectValue ev, final Class<T> beanClazz) {
		final int count = getTableEntityManager(compParameter).getCount(
				new ExpressionValue("documentId=?", ev.getValues()));
		if (count > 0) {
			throw HandleException.wrapException(LocaleI18n.getMessage("DictTypeHandle.3"));
		}
		super.doDelete(compParameter, ev, beanClazz);
	}
}
