package net.simpleframework.organization.impl;

import net.simpleframework.core.id.ID;
import net.simpleframework.organization.IDepartment;
import net.simpleframework.organization.IJobChart;
import net.simpleframework.organization.OrgUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class JobChart extends AbstractOrderBean implements IJobChart {
	private static final long serialVersionUID = 5374197712027096861L;

	private ID departmentId;

	@Override
	public ID getDepartmentId() {
		return departmentId;
	}

	@Override
	public void setDepartmentId(final ID departmentId) {
		this.departmentId = departmentId;
	}

	@Override
	public IDepartment department() {
		return OrgUtils.dm().queryForObjectById(getDepartmentId());
	}
}