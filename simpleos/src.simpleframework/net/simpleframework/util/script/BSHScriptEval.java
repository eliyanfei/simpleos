package net.simpleframework.util.script;

import java.util.Map;

import net.simpleframework.core.SimpleException;
import net.simpleframework.util.script.ScriptEvalUtils.ScriptEvalException;
import bsh.EvalError;
import bsh.Interpreter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public final class BSHScriptEval implements IScriptEval {
	private final Interpreter bsh = new Interpreter();

	public BSHScriptEval() {
		this(null);
	}

	public BSHScriptEval(final Map<String, Object> variables) {
		if (variables != null) {
			for (final Map.Entry<String, Object> entry : variables.entrySet()) {
				putVariable(entry.getKey(), entry.getValue());
			}
		}
	}

	@Override
	public void putVariable(final String key, final Object value) {
		try {
			bsh.set(key, value);
		} catch (final EvalError e) {
			throw SimpleException.wrapException(ScriptEvalException.class, null, e);
		}
	}

	@Override
	public Object eval(final String script) {
		try {
			return bsh.eval(ScriptEvalUtils.trimScript(script));
		} catch (final Exception e) {
			throw SimpleException.wrapException(ScriptEvalException.class, null, e);
		}
	}
}
