package net.simpleframework.workflow;

import java.util.Collection;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IVariableAware<T extends AbstractWorkflowBean> {

	Object getVariable(T bean, String name);

	Collection<String> getVariableNames();

	void setVariable(T bean, String name, Object value);

	void setVariable(T bean, String[] names, Object[] values);
}
