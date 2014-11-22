package net.simpleframework.content.news;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.content.component.catalog.Catalog;
import net.simpleframework.content.component.catalog.DefaultCatalogHandle;
import net.simpleframework.core.IApplicationModule;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.core.bean.IIdBeanAware;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.propeditor.PropEditorBean;
import net.simpleframework.web.page.component.ui.propeditor.PropField;
import net.simpleos.SimpleosUtil;

public class NewsCatalogHandle extends DefaultCatalogHandle {

	@Override
	public Class<? extends IIdBeanAware> getEntityBeanClass() {
		return NewsExtCatalog.class;
	}

	@Override
	public IApplicationModule getApplicationModule() {
		return NewsUtils.applicationModule;
	}

	@Override
	public Collection<PropField> getPropFields(ComponentParameter compParameter, PropEditorBean formEditor) {
		final ArrayList<PropField> al = new ArrayList<PropField>(formEditor.getFormFields());
		return al;
	}

	protected ExpressionValue getBeansSQL(final ComponentParameter compParameter, final Object parentId) {
		final ArrayList<Object> al = new ArrayList<Object>();
		final StringBuilder sql = new StringBuilder();
		if (parentId == null) {
			sql.append(SimpleosUtil.getSQLNullExpr(Long.class, "parentid"));
		} else {
			sql.append("parentid=?");
			al.add(parentId);
		}
		return new ExpressionValue(sql.toString(), al.toArray());
	}

	@Override
	public <T extends IDataObjectBean> void doBeforeAdd(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		super.doBeforeAdd(compParameter, temgr, t, data, beanClazz);
		if (t instanceof Catalog) {
			final Catalog catalog = (Catalog) t;
			catalog.setName(catalog.getName() + catalog.getParentId());
		}
	}

	@Override
	public <T extends IDataObjectBean> void doAddCallback(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		super.doAddCallback(compParameter, temgr, t, data, beanClazz);
		NewsUtils.initNewsCatalog();
	}

	public <T extends IDataObjectBean> void doDeleteCallback(final ComponentParameter compParameter, final IDataObjectValue dataObjectValue,
			final Class<T> beanClazz) {
		super.doDeleteCallback(compParameter, dataObjectValue, beanClazz);
		NewsUtils.initNewsCatalog();
	}

	@Override
	public <T extends IDataObjectBean> void doEditCallback(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		super.doEditCallback(compParameter, temgr, t, data, beanClazz);
		NewsUtils.initNewsCatalog();
	}

	@Override
	public ITableEntityManager getTableEntityManager(final ComponentParameter compParameter, final Class<?> beanClazz) {
		final ITableEntityManager temgr = NewsUtils.getTableEntityManager(beanClazz);
		if (temgr != null) {
			return temgr;
		}
		return super.getTableEntityManager(compParameter, beanClazz);
	}

}
