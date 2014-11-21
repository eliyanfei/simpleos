package net.simpleframework.workflow;

import java.util.Collection;

import net.simpleframework.workflow.impl.Participant;
import net.simpleframework.workflow.schema.TransitionNode;
import net.simpleframework.workflow.schema.UserNode;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IActivityCallback {
	ActivityBean getActivityBean();

	Collection<TransitionNode> getTransitions();

	Collection<Participant> participantsByUsernode(UserNode usernode);

	void next();
}
