package net.simpleframework.organization;

import java.util.HashMap;
import java.util.Map;

import net.a.ItSiteUtil;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.organization.impl.JobMember;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractTablePagerData;
import net.simpleframework.web.page.component.ui.pager.db.AbstractDbTablePagerHandle;

public class JobTableHandle extends AbstractDbTablePagerHandle {
	@Override
	public IDataObjectQuery<?> createDataObjectQuery(ComponentParameter compParameter) {
		final IJob job = OrgUtils.jm().queryForObjectById(compParameter.getParameter(OrgUtils.jm().getJobIdParameterName()));
		return OrgUtils.getTableEntityManager(JobMember.class).query(new ExpressionValue("jobid=?", new Object[] { job.getId() }), JobMember.class);
	}

	@Override
	public AbstractTablePagerData createTablePagerData(final ComponentParameter compParameter) {
		return new AbstractTablePagerData(compParameter) {

			@Override
			protected Map<Object, Object> getRowData(Object dataObject) {
				Map<Object, Object> data = new HashMap<Object, Object>();
				JobMember jobMember = (JobMember) dataObject;
				data.put("memberType", jobMember.getMemberType() == EMemberType.user ? "#(job_detail_m.1)" : "#(job_detail_m.2)");
				data.put("memberId", ItSiteUtil.getUserText(jobMember.getMemberId()));
				data.put(
						"primaryJob",
						"<div>"
								+ (jobMember.isPrimaryJob() ? "是" : ("否&nbsp;&nbsp;"
										+ "<input type='button' value='更改' onclick=\"$Actions['ajaxJobmemberUpdate']('memberType="
										+ jobMember.getMemberType() + "&memberId=" + jobMember.getMemberId() + "');\">")) + "</div>");
				data.put("description", jobMember.getDescription());
				final StringBuilder buf = new StringBuilder();
				buf.append("<div class=\"delete_image\" onclick=\"$Actions['ajaxJobmemberDelete']('memberType=" + jobMember.getMemberType()
						+ "&memberId=" + jobMember.getMemberId() + "');\"></div>");
				data.put("act", buf.toString());
				return data;
			}
		};
	}

	@Override
	public Map<String, Object> getFormParameters(ComponentParameter compParameter) {
		Map<String, Object> map = super.getFormParameters(compParameter);
		putParameter(compParameter, map, OrgUtils.jm().getJobIdParameterName());
		return map;
	}

}
