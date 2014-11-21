package net.simpleframework.organization.web;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.EMemberType;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.IJobMember;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.impl.JobMember;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.UrlForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class JobAction extends AbstractAjaxRequestHandle {

	private IJob getJobByRequest(final HttpServletRequest request) {
		return OrgUtils.jm().queryForObjectById(request.getParameter(OrgUtils.jm().getJobIdParameterName()));
	}

	public IForward jobTypeSave(final ComponentParameter compParameter) {
		final HttpServletRequest request = compParameter.request;
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final IJob job = getJobByRequest(request);
				if (job != null) {
					final String ruleValue = request.getParameter("job_ruleValue");
					job.setRuleHandle(ruleValue);
					OrgUtils.jm().update(job);
				}
			}
		});
	}

	public IForward memberEditUrl(final ComponentParameter compParameter) {
		return new UrlForward(OrgUtils.deployPath + "jsp/job/job_member_edit.jsp");
	}

	public IForward jobMemberSave(final ComponentParameter compParameter) {
		final HttpServletRequest request = compParameter.request;
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final ID jobId = getJobByRequest(request).getId();
				final EMemberType mt = EMemberType.valueOf(request.getParameter("jm_memberType"));
				final String[] memberIds = StringUtils.split(request.getParameter("jm_memberId"), ",");
				ArrayList<IJobMember> al = null;
				if (memberIds != null) {
					al = new ArrayList<IJobMember>();
					for (final String memberId : memberIds) {
						final IJobMember jm = OrgUtils.jmm().createJobMember(jobId, mt, JobMember.memberBean(mt, memberId).getId());
						jm.setPrimaryJob(ConvertUtils.toBoolean(request.getParameter("jm_primaryJob"), false));
						jm.setDescription(request.getParameter("jm_description"));
						al.add(jm);
					}
				}
				if (al != null) {
					OrgUtils.jmm().getEntityManager().insert(al.toArray());
				}
				json.put("next", ConvertUtils.toBoolean(request.getParameter("next"), false));
			}
		});
	}

	public IForward jobMemberUpdate(final ComponentParameter compParameter) {
		final HttpServletRequest request = compParameter.request;
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final IJob job = getJobByRequest(request);
				if (job != null) {
					final ExpressionValue ev = new ExpressionValue("jobid=? and membertype=? and memberid=?", new Object[] { job.getId(),
							EMemberType.valueOf(request.getParameter("memberType")), request.getParameter("memberId") });
					ITableEntityManager tMgr = OrgUtils.getTableEntityManager(JobMember.class);
					JobMember jobMember = tMgr.queryForObject(ev, JobMember.class);
					if (jobMember != null) {
						jobMember.setPrimaryJob(true);
						tMgr.update(new Object[] { "primaryJob" }, jobMember);
					}
				}
			}
		});
	}

	public IForward jobMemberDelete(final ComponentParameter compParameter) {
		final HttpServletRequest request = compParameter.request;
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final IJob job = getJobByRequest(request);
				if (job != null) {

					final ExpressionValue ev = new ExpressionValue("jobid=? and membertype=? and memberid=?", new Object[] { job.getId(),
							EMemberType.valueOf(request.getParameter("memberType")), request.getParameter("memberId") });
					OrgUtils.jmm().delete(ev);
				}
			}
		});
	}

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("jobExecute".equals(beanProperty)) {
			final String componentName = (String) compParameter.getBeanProperty("name");
			if ("ajaxJobTypeSave".equals(componentName) || "ajaxJobMemberEdit".equals(componentName) || "ajaxJobmemberDelete".equals(componentName)) {
				return OrgUtils.applicationModule.getManager(compParameter);
			}
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}
}
