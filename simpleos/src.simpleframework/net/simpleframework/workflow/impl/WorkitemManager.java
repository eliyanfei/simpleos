package net.simpleframework.workflow.impl;

import java.util.Map;

import net.simpleframework.workflow.ActivityBean;
import net.simpleframework.workflow.IScriptAware;
import net.simpleframework.workflow.IWorkitemManager;
import net.simpleframework.workflow.WorkflowUtils;
import net.simpleframework.workflow.WorkitemBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class WorkitemManager extends AbstractWorkflowManager<WorkitemBean> implements
		IWorkitemManager {
	@Override
	public Map<String, Object> createVariables(final WorkitemBean workitem) {
		final Map<String, Object> variables = ((IScriptAware<ActivityBean>) WorkflowUtils.am())
				.createVariables(workitem.activity());
		variables.put("workitem", workitem);
		return variables;
	}
}
