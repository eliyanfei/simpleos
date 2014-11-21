package net.itsite.permission;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.id.ID;

@SuppressWarnings("serial")
public class PermissionBean extends AbstractIdDataObjectBean {

	private ID id;
	private ID job_id;
	private String job_name;
	private String menu_name;

	public PermissionBean() {
		super();
	}

	public PermissionBean(final String menu_name) {
		super();
		this.menu_name = menu_name;
	}

	@Override
	public ID getId() {
		return id;
	}

	@Override
	public void setId(final ID id) {
		this.id = id;
	}

	public ID getJob_id() {
		return job_id;
	}

	public void setJob_id(final ID job_id) {
		this.job_id = job_id;
	}

	public String getMenu_name() {
		return menu_name;
	}

	public void setMenu_name(final String menu_name) {
		this.menu_name = menu_name;
	}

	public String getJob_name() {
		return job_name;
	}

	public void setJob_name(final String job_name) {
		this.job_name = job_name;
	}

}
