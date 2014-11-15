package net.prj.manager.datatemp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.content.component.catalog.Catalog;
import net.simpleframework.content.component.catalog.CatalogBean;
import net.simpleframework.content.component.catalog.DefaultCatalogHandle;
import net.simpleframework.core.IApplicationModule;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.core.bean.IIdBeanAware;
import net.simpleframework.sysmgr.dict.DictUtils;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.propeditor.PropEditorBean;
import net.simpleframework.web.page.component.ui.propeditor.PropField;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeBean;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeNode;

/**
 *  
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-3下午01:42:25
 */
public class PrjProductItemHandle extends DefaultCatalogHandle {
	@Override
	public Class<? extends IIdBeanAware> getEntityBeanClass() {
		return PrjProductCatalogBean.class;
	}

	@Override
	public IApplicationModule getApplicationModule() {
		return PrjDataTempUtils.appModule;
	}

	@Override
	public IDataObjectQuery<? extends Catalog> catalogs(final ComponentParameter compParameter, final Object parentId) {
		return beans(compParameter, parentId, "order by oorder asc");
	}

	@Override
	protected boolean hideOwnerMgrMenu() {
		return true;
	}

	@Override
	public Collection<? extends AbstractTreeNode> getCatalogTreenodes(final ComponentParameter compParameter, final AbstractTreeBean treeBean,
			final AbstractTreeNode treeNode, final boolean dictionary) {
		final Collection<? extends AbstractTreeNode> coll = super.getCatalogTreenodes(compParameter, treeBean, treeNode, dictionary);
		for (final AbstractTreeNode treeNode2 : coll) {
			if (treeNode == null) {
				treeNode2.setOpened(true);
			} else {
				final PrjProductCatalogBean sysDict = (PrjProductCatalogBean) treeNode2.getDataObject();
				if (sysDict != null) {
				}
			}
			if (treeNode == null && !dictionary) {
				treeNode2.setImage(DictUtils.getCssPath(compParameter) + "/images/dict.png");
				final PrjProductCatalogBean sysDict = getEntityBeanById(compParameter, getDocumentId(compParameter));
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
	protected ExpressionValue getBeansSQL(final ComponentParameter compParameter, final Object parentId) {
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

	@Override
	public <T extends IDataObjectBean> void doAddCallback(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		super.doAddCallback(compParameter, temgr, t, data, beanClazz);
		PrjDataTempUtils.loadMenu();
	}

	public <T extends IDataObjectBean> void doDeleteCallback(final ComponentParameter compParameter, final IDataObjectValue dataObjectValue,
			final Class<T> beanClazz) {
		super.doDeleteCallback(compParameter, dataObjectValue, beanClazz);
		PrjDataTempUtils.loadMenu();
	}

	@Override
	protected boolean isAllowEdit() {
		return true;
	}

	@Override
	public <T extends IDataObjectBean> void doEditCallback(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		super.doEditCallback(compParameter, temgr, t, data, beanClazz);
		PrjDataTempUtils.loadMenu();
	}

	@Override
	public Collection<PropField> getPropFields(final ComponentParameter compParameter, final PropEditorBean formEditor) {
		return PrjDataTempUtils.appModule.doDictItemPropEditor(compParameter.request, compParameter.response, (CatalogBean) compParameter.componentBean,
				formEditor);
	}

	@Override
	public boolean showHtml() {
		return true;
	}
}
