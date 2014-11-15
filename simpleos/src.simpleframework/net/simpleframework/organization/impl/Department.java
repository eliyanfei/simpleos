package net.simpleframework.organization.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IEntityBeanAware;
import net.simpleframework.content.component.catalog.Catalog;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.ado.db.Column;
import net.simpleframework.organization.IDepartment;
import net.simpleframework.organization.IJobChart;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class Department extends Catalog implements IDepartment, IEntityBeanAware {

	private static Map<String, Column> columns = new HashMap<String, Column>();
	static {
		columns.put("id", new Column("id"));
		columns.put("parentId", new Column("parentId"));
		columns.put("name", new Column("name"));
		columns.put("text", new Column("text"));
		columns.put("description", new Column("description"));
		columns.put("oorder", new Column("oorder"));
	}

	@Override
	public Map<String, Column> getTableColumnDefinition() {
		return columns;
	}

	/*----------------------------------关联操作 --------------------------------*/

	@Override
	public IDepartment parent() {
		return OrgUtils.dm().queryForObjectById(getParentId());
	}

	@Override
	public Collection<? extends IDepartment> children() {
		return OrgUtils.dm().children(this);
	}

	@Override
	public Collection<? extends IUser> users() {
		return IDataObjectQuery.Utils.toList(OrgUtils.um().query(
				new ExpressionValue("departmentid=?", new Object[] { getId() })));
	}

	@Override
	public Collection<? extends IJobChart> charts() {
		return null;
	}

	private static final long serialVersionUID = 2533084440695513574L;
}
