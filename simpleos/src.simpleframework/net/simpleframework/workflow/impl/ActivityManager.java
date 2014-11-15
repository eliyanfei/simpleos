package net.simpleframework.workflow.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.util.script.IScriptEval;
import net.simpleframework.workflow.ActivityBean;
import net.simpleframework.workflow.IActivityCallback;
import net.simpleframework.workflow.IActivityManager;
import net.simpleframework.workflow.IScriptAware;
import net.simpleframework.workflow.ProcessBean;
import net.simpleframework.workflow.WorkflowUtils;
import net.simpleframework.workflow.schema.AbstractTaskNode;
import net.simpleframework.workflow.schema.AbstractTransitionType;
import net.simpleframework.workflow.schema.AbstractTransitionType.Conditional;
import net.simpleframework.workflow.schema.AbstractTransitionType.LogicConditional;
import net.simpleframework.workflow.schema.ETransitionLogic;
import net.simpleframework.workflow.schema.ProcessNode;
import net.simpleframework.workflow.schema.TransitionNode;
import net.simpleframework.workflow.schema.UserNode;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ActivityManager extends AbstractWorkflowManager<ActivityBean> implements
		IActivityManager {
	@Override
	public ActivityCallback getActivityCallback(final ActivityBean activity) {
		final AbstractTaskNode tasknode = activity.tasknode();
		if (tasknode == null) {
		}
		final ActivityCallback activityCallback = new ActivityCallback(activity);
		final IScriptEval script = createScriptEval(activity);

		// 解析条件正确的transition
		final LinkedHashMap<String, TransitionNode> logicTransitions = new LinkedHashMap<String, TransitionNode>();
		for (final TransitionNode transition : tasknode.toTransitions()) {
			final AbstractTransitionType tt = transition.getTransitionType();
			if (tt instanceof Conditional) {
				final String expr = ((Conditional) tt).getExpression();
				if (StringUtils.hasText(expr)) {
					script.putVariable("transition", transition);
					if (ConvertUtils.toBoolean(script.eval(expr), false)) {
						activityCallback.putTransition(transition);
					}
				} else {
					activityCallback.putTransition(transition);
				}
			} else if (tt instanceof AbstractTransitionType.Interface) {
				//
			} else if (tt instanceof LogicConditional) {
				logicTransitions.put(transition.getId(), transition);
			}
		}
		while (logicTransitions.size() > 0) {
			final TransitionNode transition = logicTransitions.remove(0);
			final LogicConditional lc = (LogicConditional) transition.getTransitionType();
			final String id2 = lc.getTransitionId();
			final ETransitionLogic logic = lc.getLogic();
			final TransitionNode transition2 = activityCallback.getTransitionById(id2);
			if (transition2 == null && logicTransitions.get(id2) != null) {
				logicTransitions.put(transition.getId(), transition);
				continue;
			}
			if ((logic == ETransitionLogic.and && transition2 != null)
					|| (logic == ETransitionLogic.not && transition2 == null)) {
				activityCallback.putTransition(transition);
			}
		}

		// 解析jobs
		final ProcessNode processNode = (ProcessNode) tasknode.getParent();
		for (final TransitionNode transition : activityCallback.getTransitions()) {
			final AbstractTaskNode toTask = (AbstractTaskNode) processNode.getNodeById(transition
					.getTo());
			if (toTask == null) {
				continue;
			}
			if (toTask instanceof UserNode) {
				activityCallback.putParticipant((UserNode) toTask);
			}
		}
		return activityCallback;
	}

	@Override
	public void next(final IActivityCallback activityCallback) {
		final ActivityBean activity = activityCallback.getActivityBean();
		final AbstractTaskNode tasknode = activity.tasknode();
		final ProcessNode processNode = (ProcessNode) tasknode.getParent();
		for (final TransitionNode transition : activityCallback.getTransitions()) {
			final AbstractTaskNode tasknode2 = (AbstractTaskNode) processNode.getNodeById(transition
					.getTo());
			if (tasknode2 == null) {
				continue;
			}
			ActivityUtils.createActivity(
					activity.process(),
					tasknode2,
					activity,
					tasknode2 instanceof UserNode ? activityCallback
							.participantsByUsernode((UserNode) tasknode2) : null);
		}
	}

	@Override
	public Map<String, Object> createVariables(final ActivityBean activity) {
		final Map<String, Object> variables = ((IScriptAware<ProcessBean>) WorkflowUtils.pm())
				.createVariables(activity.process());
		variables.put("activity", activity);
		for (final String variable : getVariableNames()) {
			variables.put(variable, getVariable(activity, variable));
		}
		return variables;
	}

	@Override
	public Object getVariable(final ActivityBean activity, final String name) {
		return null;
	}

	@Override
	public void setVariable(final ActivityBean activity, final String[] names, final Object[] values) {
	}

	@Override
	public void setVariable(final ActivityBean activity, final String name, final Object value) {
		setVariable(activity, new String[] { name }, new Object[] { value });
	}

	@Override
	public Collection<String> getVariableNames() {
		return new ArrayList<String>();
	}
}
