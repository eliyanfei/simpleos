package net.simpleframework.workflow;

import java.util.Date;

import net.simpleframework.core.id.ID;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class WorkitemBean extends AbstractWorkflowBean {
	private EWorkitemStatus status;

	private ID activityId;

	private ID jobId;

	private Date completeDate;

	private boolean readMark;

	public EWorkitemStatus getStatus() {
		return status != null ? status : EWorkitemStatus.running;
	}

	public void setStatus(final EWorkitemStatus status) {
		this.status = status;
	}

	public ID getJobId() {
		return jobId;
	}

	public void setJobId(final ID jobId) {
		this.jobId = jobId;
	}

	public ID getActivityId() {
		return activityId;
	}

	public void setActivityId(final ID activityId) {
		this.activityId = activityId;
	}

	public Date getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(final Date completeDate) {
		this.completeDate = completeDate;
	}

	public boolean isReadMark() {
		return readMark;
	}

	public void setReadMark(final boolean readMark) {
		this.readMark = readMark;
	}

	/*----------------------------------关联操作 --------------------------------*/

	public ActivityBean activity() {
		return WorkflowUtils.am().queryForObjectById(getActivityId());
	}

	private static final long serialVersionUID = 1553478269588195799L;
}
