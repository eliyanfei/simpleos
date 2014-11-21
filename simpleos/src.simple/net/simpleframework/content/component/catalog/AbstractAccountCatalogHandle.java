package net.simpleframework.content.component.catalog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.propeditor.PropEditorBean;
import net.simpleframework.web.page.component.ui.propeditor.PropField;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractAccountCatalogHandle extends DefaultCatalogHandle {
	@Override
	protected boolean hideOwnerMgrMenu() {
		return true;
	}

	protected IAccount getAccount(final ComponentParameter compParameter) {
		return AccountSession.getLogin(compParameter.getSession());
	}

	@Override
	protected ExpressionValue getBeansSQL(final ComponentParameter compParameter,
			final Object parentId) {
		final ArrayList<Object> al = new ArrayList<Object>();
		final StringBuilder sql = new StringBuilder();
		final IAccount account = getAccount(compParameter);
		if (account == null) {
			sql.append("1=2");
		} else {
			sql.append("userid=?");
			sql.append(" and ");
			al.add(account.getId());
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
		final List<PropField> al = new ArrayList<PropField>(formEditor.getFormFields());
		al.remove(1);
		return al;
	}
}
