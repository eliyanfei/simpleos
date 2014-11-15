package net.itsite.permission;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.a.ItSiteUtil;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.id.ID;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;

public class PermissionAction extends AbstractAjaxRequestHandle {

	public IForward bindMenuWithJob(final ComponentParameter compParameter) throws Exception {
		final HttpServletRequest request = compParameter.request;
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(final Map<String, Object> json) throws Exception {
				final String menuNames = request.getParameter("menuNames");
				final ITableEntityManager tMgr = ItSiteUtil.getTableEntityManager(ItSiteUtil.applicationModule, PermissionBean.class);
				final String[] checkedMenuArray = menuNames.split(",");
				try {
					tMgr.delete(new ExpressionValue(" job_id = ? ", new Object[] { request.getParameter("jobId") }));
				} catch (final Exception e) {

				}
				for (final String menuName : checkedMenuArray) {
					final PermissionBean permissionBean = new PermissionBean();
					permissionBean.setJob_id(ID.Utils.newID(request.getParameter("jobId")));
					permissionBean.setJob_name(request.getParameter("jobName"));
					permissionBean.setMenu_name(menuName);
					tMgr.insert(permissionBean);
				}
			}

		});
	}
}
