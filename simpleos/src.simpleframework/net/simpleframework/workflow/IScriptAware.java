package net.simpleframework.workflow;

import java.util.Map;

import net.simpleframework.util.script.IScriptEval;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IScriptAware<T extends AbstractWorkflowBean> {

	IScriptEval createScriptEval(final T bean);

	Map<String, Object> createVariables(final T bean);
}
