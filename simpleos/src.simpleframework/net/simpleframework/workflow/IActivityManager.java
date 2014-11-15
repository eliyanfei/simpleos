package net.simpleframework.workflow;

import net.simpleframework.ado.db.IBeanManagerAware;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IActivityManager extends IVariableAware<ActivityBean>,
		IBeanManagerAware<ActivityBean>, IListenerAware<ActivityBean>, IScriptAware<ActivityBean> {

	IActivityCallback getActivityCallback(ActivityBean activity);

	void next(IActivityCallback activityCallback);
}
