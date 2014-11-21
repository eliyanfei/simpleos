package net.simpleframework.organization.component.userselect;

import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.organization.EUserStatus;
import net.simpleframework.organization.IDepartment;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.component.deptselect.DeptSelectBean;
import net.simpleframework.organization.component.userpager.UserPagerBean;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.dictionary.AbstractDictionaryHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultUserSelectHandle extends AbstractDictionaryHandle implements IUserSelectHandle {

	@Override
	public Collection<? extends IDepartment> getSelectedDepartments(final ComponentParameter compParameter, final DeptSelectBean deptSelect,
			final IDepartment parent) {
		return OrgUtils.dm().children(parent);
	}

	protected ExpressionValue getFilterSQL(final PageRequestResponse requestResponse) {
		final ArrayList<Object> al = new ArrayList<Object>();
		final StringBuilder sql = new StringBuilder();
		sql.append("status=?");
		al.add(EUserStatus.normal);
		final String deptId = requestResponse.getRequestParameter(OrgUtils.dm().getDepartmentIdParameterName());
		if (StringUtils.hasText(deptId)) {
			sql.append(" and departmentid=?");
			al.add(deptId);
		}
		final String user_name = requestResponse.getRequestParameter(OrgUtils.um().getUserTextParameterName());
		if (StringUtils.hasText(user_name) && !user_name.equals(LocaleI18n.getMessage("user_select.3"))) {
			sql.append(" and (name like '%").append(user_name).append("%' or text like '%").append(user_name).append("%')");
		}
		sql.append(" order by oorder desc");
		return new ExpressionValue(sql.toString(), al.toArray());
	}

	@Override
	public IDataObjectQuery<? extends IUser> getUsers(final ComponentParameter compParameter, final UserPagerBean userPager) {
		return OrgUtils.um().query(getFilterSQL(compParameter));
	}
}
