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
public class ProcessBean extends AbstractWorkflowBean {
	private ID modelId;

	private Date completeDate;

	private String title;

	private EProcessStatus status;

	private ID jobId;

	public ID getModelId() {
		return modelId;
	}

	public void setModelId(final ID modelId) {
		this.modelId = modelId;
	}

	public Date getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(final Date completeDate) {
		this.completeDate = completeDate;
	}

	public EProcessStatus getStatus() {
		return status != null ? status : EProcessStatus.running;
	}

	public void setStatus(final EProcessStatus status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public ID getJobId() {
		return jobId;
	}

	public void setJobId(final ID jobId) {
		this.jobId = jobId;
	}

	/*----------------------------------关联操作 --------------------------------*/

	public ProcessModelBean model() {
		return WorkflowUtils.pmm().queryForObjectById(getModelId());
	}

	private static final long serialVersionUID = -4249661933122865392L;
}
