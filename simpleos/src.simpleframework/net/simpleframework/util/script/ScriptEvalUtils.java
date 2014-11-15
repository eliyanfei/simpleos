package net.simpleframework.util.script;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.simpleframework.core.SimpleException;
import net.simpleframework.util.AlgorithmUtils;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.IoUtils;
import net.simpleframework.util.StringUtils;

import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class ScriptEvalUtils {

	static String trimScript(String script) {
		if (script == null) {
			return null;
		}
		script = script.trim();
		final char[] charArray = script.toCharArray();
		final int length = charArray.length;
		if (length > 3 && charArray[0] == '$' && charArray[1] == '{' && charArray[length - 1] == '}') {
			script = script.substring(2, script.length() - 1);
		}
		return script;
	}

	private static final Pattern EXPR_PATTERN = Pattern.compile("[\\s\\S]*(\\$\\{.+\\})[\\s\\S]*");

	public static String replaceExpr(final IScriptEval scriptEval, String expr) {
		if (StringUtils.hasText(expr)) {
			if (scriptEval == null) {
				return expr;
			}
			while (true) {
				final Matcher matcher = EXPR_PATTERN.matcher(expr);
				if (matcher.matches()) {
					final MatchResult result = matcher.toMatchResult();
					final String group = result.group(1);
					expr = expr.substring(0, result.start(1))
							+ StringUtils.blank(scriptEval.eval(group)) + expr.substring(result.end(1));
				} else {
					break;
				}
			}
			return expr;
		}
		return "";
	}

	public static String replaceExpr(final Map<String, Object> variables, final String expr) {
		return replaceExpr(ScriptEvalFactory.createDefaultScriptEval(variables), expr);
	}

	private final static Map<String, CompiledTemplate> templateCompilerCache = new HashMap<String, CompiledTemplate>();

	public static String replaceExprByMVEL(final Map<String, Object> variables, final String expr) {
		final String digest = AlgorithmUtils.md5Hex(expr);
		CompiledTemplate compiledTemplate = templateCompilerCache.get(digest);
		if (compiledTemplate == null) {
			templateCompilerCache.put(digest,
					compiledTemplate = TemplateCompiler.compileTemplate(expr));
		}
		return TemplateRuntime.execute(compiledTemplate, variables).toString();
	}

	public static String replaceExprFromResource(final Class<?> resourceClazz,
			final String filename, final Map<String, Object> variables) {
		String fileString;
		try {
			fileString = IoUtils.getStringFromInputStream(resourceClazz.getClassLoader()
					.getResourceAsStream(BeanUtils.getResourceClasspath(resourceClazz, filename)),
					"utf-8");
		} catch (final IOException e) {
			throw ScriptEvalException.wrapException(e);
		}
		return replaceExpr(variables, fileString);
	}

	public static class ScriptEvalException extends SimpleException {
		private static final long serialVersionUID = -4496578242304451585L;

		public ScriptEvalException(final String msg, final Throwable cause) {
			super(msg, cause);
		}

		public static RuntimeException wrapException(final Throwable throwable) {
			return wrapException(ScriptEvalException.class, null, throwable);
		}
	}
}
