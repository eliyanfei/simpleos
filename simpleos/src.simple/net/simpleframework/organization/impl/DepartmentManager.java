package net.simpleframework.organization.impl;

import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.organization.AbstractOrganizationManager;
import net.simpleframework.organization.IDepartment;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DepartmentManager extends AbstractOrganizationManager<IDepartment> {

	public DepartmentManager() {
		addListener(new TableEntityAdapter() {
			@Override
			public void afterDelete(final ITableEntityManager manager,
					final IDataObjectValue dataObjectValue) {
				// 取消用户与部门的关联
				final IQueryEntitySet<? extends IUser> qs = OrgUtils.um().query(
						new ExpressionValue("departmentid=?", dataObjectValue.getValues()));
				final ArrayList<IUser> al = new ArrayList<IUser>();
				IUser user;
				while ((user = qs.next()) != null) {
					user.setDepartmentId(null);
					al.add(user);
				}
				OrgUtils.um().getEntityManager().update(al.toArray());
			}
		});
	}

	@Override
	public Class<? extends IDepartment> getBeanClass() {
		return Department.class;
	}

	public IDepartment createDepartment(final IDepartment parent, final String name,
			final String text) {
		final Department dept = new Department();
		if (parent != null) {
			dept.setParentId(parent.getId());
		}
		dept.setName(name);
		dept.setText(text);
		return dept;
	}

	public Collection<? extends IDepartment> root() {
		return children(null);
	}

	public Collection<? extends IDepartment> children(final IDepartment parent) {
		final String expr = "parentId=? order by oorder desc";
		final ExpressionValue ev;
		if (parent == null) {
			ev = new ExpressionValue(expr, new Object[] { 0 });
		} else {
			ev = new ExpressionValue(expr, new Object[] { parent.getId() });
		}
		final IDataObjectQuery<?> qs = query(ev);
		final ArrayList<IDepartment> coll = new ArrayList<IDepartment>();
		IDepartment department;
		while ((department = (IDepartment) qs.next()) != null) {
			coll.add(department);
		}
		return coll;
	}

	static final String DEPARTMENT_ID = "__department_Id";

	public String getDepartmentIdParameterName() {
		return DEPARTMENT_ID;
	}
}
