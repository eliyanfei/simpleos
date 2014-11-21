package net.itsite.user;

import java.util.ArrayList;
import java.util.Collection;

import net.itsite.ItSiteUtil;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.content.component.catalog.AbstractAccountCatalogHandle;
import net.simpleframework.core.IApplicationModule;
import net.simpleframework.core.bean.IIdBeanAware;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.propeditor.PropEditorBean;
import net.simpleframework.web.page.component.ui.propeditor.PropField;

public class UserSearchCatalogHandle extends AbstractAccountCatalogHandle {

	@Override
	public Class<? extends IIdBeanAware> getEntityBeanClass() {
		return UserSearchCatalog.class;
	}

	@Override
	public IApplicationModule getApplicationModule() {
		return ItSiteUtil.applicationModule;
	}

	@Override
	public Collection<PropField> getPropFields(ComponentParameter compParameter, PropEditorBean formEditor) {
		return formEditor.getFormFields();
	}

	protected ExpressionValue getBeansSQL(final ComponentParameter compParameter, final Object parentId) {
		final ArrayList<Object> al = new ArrayList<Object>();
		final StringBuilder sql = new StringBuilder();
		if (parentId == null) {
			sql.append(ItSiteUtil.getSQLNullExpr(Long.class, "parentid"));
		} else {
			sql.append("parentid=?");
			al.add(parentId);
		}
		return new ExpressionValue(sql.toString(), al.toArray());
	}

	@Override
	public ITableEntityManager getTableEntityManager(final ComponentParameter compParameter, final Class<?> beanClazz) {
		final ITableEntityManager temgr = ItSiteUtil.getTableEntityManager(ItSiteUtil.applicationModule, beanClazz);
		if (temgr != null) {
			return temgr;
		}
		return super.getTableEntityManager(compParameter, beanClazz);
	}

}
