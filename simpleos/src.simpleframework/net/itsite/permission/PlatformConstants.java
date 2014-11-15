package net.itsite.permission;

import java.util.ArrayList;
import java.util.List;

import net.simpleframework.core.ado.db.Table;

public abstract class PlatformConstants {
	public static Table simpleChartTable = new Table("simple_chart", "id");
	public static Table simpleJobTable = new Table("simple_job", "id");
	public static Table simpleJobMemberTable = new Table("simple_job_member", "id");
	public static Table platformPermissionTable = new Table("platform_permission", "id");
	public static Table simpleDepartmentTable = new Table("simple_department", "id");
	public static String Customer_View = "sys_my";
	
	public static final String DEFAULT_RETURN_VALUE = "ret";
	public static final String DEFAULT_TABLE_ACTION = "action";
	public static final String DEFAULT_RETURN_MESSAGE = "msg";

	public static List<String> views = new ArrayList<String>();
	static {
		views.add(Customer_View);
		views.add("sys_jobchart");
	}
}
