package net.simpleframework.workflow.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import net.simpleframework.core.ALoggerAware;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.IUser;
import net.simpleframework.workflow.ActivityBean;
import net.simpleframework.workflow.IActivityCallback;
import net.simpleframework.workflow.WorkflowUtils;
import net.simpleframework.workflow.schema.AbstractUserNodeParticipantType;
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
public class ActivityCallback extends ALoggerAware implements IActivityCallback {
	private final ActivityBean activity;

	private final Map<String, TransitionNode> transitions;

	private final Map<String, Collection<Participant>> participants;
	{
		transitions = new LinkedHashMap<String, TransitionNode>();
		participants = new LinkedHashMap<String, Collection<Participant>>();
	}

	public ActivityCallback(final ActivityBean activity) {
		this.activity = activity;
	}

	@Override
	public Collection<TransitionNode> getTransitions() {
		return transitions.values();
	}

	public void putTransition(final TransitionNode transition) {
		transitions.put(transition.getId(), transition);
	}

	public TransitionNode getTransitionById(final String id) {
		return transitions.get(id);
	}

	public TransitionNode removeTransitionById(final String id) {
		return transitions.remove(id);
	}

	public void putParticipant(final UserNode usernode) {
		final AbstractUserNodeParticipantType pt = usernode.getParticipantType();
		if (pt == null) {
			return;
		}
		final ArrayList<Participant> list = new ArrayList<Participant>();
		final String participant = pt.getParticipant();
		if (pt instanceof AbstractUserNodeParticipantType.User) {
			final IUser user = WorkflowUtils.org().user(participant);
			if (user != null) {
				list.add(new Participant(user));
			}
		} else if (pt instanceof AbstractUserNodeParticipantType.Job) {
			final IJob job = WorkflowUtils.org().job(participant);
			Collection<? extends IUser> users;
			if (job != null && (users = job.users()) != null) {
				for (final IUser user : users) {
					list.add(new Participant(user, job));
				}
			}
		} else if (pt instanceof AbstractUserNodeParticipantType.RelativeJob) {
		}
		if (list.size() > 0) {
			participants.put(usernode.getId(), list);
		}
	}

	@Override
	public Collection<Participant> participantsByUsernode(final UserNode usernode) {
		return participants.get(usernode);
	}

	@Override
	public ActivityBean getActivityBean() {
		return activity;
	}

	@Override
	public void next() {
		WorkflowUtils.am().next(this);
	}

	public boolean isTransitionManual() {
		for (final TransitionNode transition : getTransitions()) {
			if (transition.getTransitionType().isManual()) {
				return true;
			}
		}
		return false;
	}

	public boolean isParticipantManual() {
		final ProcessNode processNode = (ProcessNode) activity.tasknode().getParent();
		for (final String id : participants.keySet()) {
			final UserNode usernode = (UserNode) processNode.getNodeById(id);
			if (usernode == null) {
				continue;
			}
			final AbstractUserNodeParticipantType pt = usernode.getParticipantType();
			if (pt instanceof AbstractUserNodeParticipantType.Job
					&& ((AbstractUserNodeParticipantType.Job) pt).isManual()) {
				return true;
			}
		}
		return false;
	}
}
