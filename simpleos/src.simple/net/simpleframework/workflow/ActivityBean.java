package net.simpleframework.workflow;

import java.util.Date;

import net.simpleframework.core.id.ID;
import net.simpleframework.workflow.schema.AbstractTaskNode;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ActivityBean extends AbstractWorkflowBean {
	private ID processId;

	private ID previousId;

	private String tasknodeId;

	private EActivityStatus status;

	private Date completeDate;

	public ID getProcessId() {
		return processId;
	}

	public void setProcessId(final ID processId) {
		this.processId = processId;
	}

	public ID getPreviousId() {
		return previousId;
	}

	public void setPreviousId(final ID previousId) {
		this.previousId = previousId;
	}

	public String getTasknodeId() {
		return tasknodeId;
	}

	public void setTasknodeId(final String tasknodeId) {
		this.tasknodeId = tasknodeId;
	}

	public EActivityStatus getStatus() {
		return status != null ? status : EActivityStatus.running;
	}

	public void setStatus(final EActivityStatus status) {
		this.status = status;
	}

	public Date getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(final Date completeDate) {
		this.completeDate = completeDate;
	}

	/*----------------------------------关联操作 --------------------------------*/

	public ProcessBean process() {
		return WorkflowUtils.pm().queryForObjectById(getProcessId());
	}

	private AbstractTaskNode tasknode;

	public AbstractTaskNode tasknode() {
		if (tasknode == null) {
			tasknode = (AbstractTaskNode) process().model().document().getProcessNode()
					.getNodeById(getTasknodeId());
		}
		return tasknode;
	}

	private static final long serialVersionUID = 5146309554672912773L;
}
