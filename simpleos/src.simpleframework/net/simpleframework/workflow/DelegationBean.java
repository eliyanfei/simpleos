package net.simpleframework.workflow;

import java.util.Date;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.id.ID;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DelegationBean extends AbstractIdDataObjectBean {

	private EDelegationSource delegationSource;

	private ID sourceId;

	private EDelegationStatus status;

	private ID jobId;

	private ID userId;

	private Date createDate;

	private Date runningDate, completeDate;

	private String startRule, endRule;

	private String ruleHandleClass;

	public EDelegationSource getDelegationSource() {
		return delegationSource;
	}

	public void setDelegationSource(final EDelegationSource delegationSource) {
		this.delegationSource = delegationSource;
	}

	public ID getSourceId() {
		return sourceId;
	}

	public void setSourceId(final ID sourceId) {
		this.sourceId = sourceId;
	}

	public EDelegationStatus getStatus() {
		return status;
	}

	public void setStatus(final EDelegationStatus status) {
		this.status = status;
	}

	public ID getJobId() {
		return jobId;
	}

	public void setJobId(final ID jobId) {
		this.jobId = jobId;
	}

	public ID getUserId() {
		return userId;
	}

	public void setUserId(final ID userId) {
		this.userId = userId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(final Date createDate) {
		this.createDate = createDate;
	}

	public Date getRunningDate() {
		return runningDate;
	}

	public void setRunningDate(final Date runningDate) {
		this.runningDate = runningDate;
	}

	public Date getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(final Date completeDate) {
		this.completeDate = completeDate;
	}

	public String getStartRule() {
		return startRule;
	}

	public void setStartRule(final String startRule) {
		this.startRule = startRule;
	}

	public String getEndRule() {
		return endRule;
	}

	public void setEndRule(final String endRule) {
		this.endRule = endRule;
	}

	public String getRuleHandleClass() {
		return ruleHandleClass;
	}

	public void setRuleHandleClass(final String ruleHandleClass) {
		this.ruleHandleClass = ruleHandleClass;
	}

	private static final long serialVersionUID = -642924978376103383L;
}
