package net.simpleframework.workflow.impl;

import java.util.Collection;
import java.util.Date;

import net.simpleframework.workflow.ActivityBean;
import net.simpleframework.workflow.ProcessBean;
import net.simpleframework.workflow.WorkflowUtils;
import net.simpleframework.workflow.WorkitemBean;
import net.simpleframework.workflow.schema.AbstractTaskNode;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class ActivityUtils {

	static ActivityBean createActivity(final ProcessBean process, final AbstractTaskNode tasknode,
			final ActivityBean preActivity, final Collection<Participant> participants) {
		final ActivityBean activity = new ActivityBean();
		activity.setProcessId(process.getId());
		activity.setTasknodeId(tasknode.getId());
		activity.setUserId(process.getUserId());
		activity.setCreateDate(new Date());
		WorkflowUtils.am().insert(activity);
		if (participants != null) {
			for (final Participant participant : participants) {
				final WorkitemBean workitem = new WorkitemBean();
				workitem.setUserId(participant.user.getId());
			}
		}
		return activity;
	}
}
