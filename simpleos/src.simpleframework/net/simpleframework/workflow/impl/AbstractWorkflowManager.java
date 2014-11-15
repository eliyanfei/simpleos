package net.simpleframework.workflow.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.simpleframework.ado.db.AbstractBeanManager;
import net.simpleframework.util.script.IScriptEval;
import net.simpleframework.util.script.ScriptEvalFactory;
import net.simpleframework.workflow.AbstractWorkflowBean;
import net.simpleframework.workflow.WorkflowUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractWorkflowManager<T extends AbstractWorkflowBean> extends
		AbstractBeanManager<T> {
	@Override
	public WorkflowEngine getApplicationModule() {
		return WorkflowUtils.applicationModule;
	}

	public IScriptEval createScriptEval(final T bean) {
		final Map<String, Object> variables = new HashMap<String, Object>();
		variables.putAll(createVariables(bean));
		final IScriptEval script = ScriptEvalFactory.createDefaultScriptEval(variables);
		script.eval("import net.simpleframework.workflow.*;");
		return script;
	}

	public Map<String, Object> createVariables(final T bean) {
		final Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("engine", getApplicationModule());
		return variables;
	}

	public Collection<String> getEventListeners(final T bean) {
		return null;
	}

	public void addEventListener(final T bean, final String listenerClass) {
	}

	public boolean removeEventListener(final T bean, final String listenerClass) {
		return false;
	}
}
