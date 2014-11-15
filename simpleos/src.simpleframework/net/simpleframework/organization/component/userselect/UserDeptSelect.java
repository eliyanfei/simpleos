package net.simpleframework.organization.component.userselect;

import java.util.Collection;
import java.util.Map;

import net.simpleframework.organization.IDepartment;
import net.simpleframework.organization.component.deptselect.DefaultDeptSelectHandle;
import net.simpleframework.organization.component.deptselect.DeptSelectBean;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class UserDeptSelect extends DefaultDeptSelectHandle {
	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, UserSelectUtils.BEAN_ID);
		return parameters;
	}

	@Override
	protected Collection<? extends IDepartment> getDepartmentChildren(
			final ComponentParameter compParameter, final IDepartment parent) {
		final ComponentParameter nComponentParameter = UserSelectUtils
				.getComponentParameter(compParameter);
		return ((IUserSelectHandle) nComponentParameter.getComponentHandle()).getSelectedDepartments(
				nComponentParameter, (DeptSelectBean) compParameter.componentBean, parent);
	}
}
