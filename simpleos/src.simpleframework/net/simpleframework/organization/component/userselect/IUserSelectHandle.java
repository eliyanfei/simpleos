package net.simpleframework.organization.component.userselect;

import java.util.Collection;

import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.organization.IDepartment;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.component.deptselect.DeptSelectBean;
import net.simpleframework.organization.component.userpager.UserPagerBean;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.dictionary.IDictionaryHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IUserSelectHandle extends IDictionaryHandle {

	IDataObjectQuery<? extends IUser> getUsers(ComponentParameter compParameter,
			UserPagerBean userPager);

	Collection<? extends IDepartment> getSelectedDepartments(ComponentParameter compParameter,
			DeptSelectBean deptSelect, IDepartment parent);
}
