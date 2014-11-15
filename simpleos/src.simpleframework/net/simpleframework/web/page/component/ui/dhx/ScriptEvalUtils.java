package net.simpleframework.web.page.component.ui.dhx;

import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.simpleframework.util.StringUtils;
import net.simpleframework.util.script.IScriptEval;
import net.simpleframework.util.script.ScriptEvalFactory;

/**
 * @Description
 * @Date 2012-12-6
 * @author lxy
 */

public class ScriptEvalUtils {
	private static final Pattern EXPR_PATTERN = Pattern.compile("[\\s\\S]*(\\@\\{[\\s\\S]*\\})[\\s\\S]*");

	public static String replaceExpr(final Map<String, Object> variables, final String expr) {
		return replaceExpr(ScriptEvalFactory.createDefaultScriptEval(variables), expr);
	}

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
					expr = expr.substring(0, result.start(1)) + StringUtils.blank(scriptEval.eval(group)) + expr.substring(result.end(1));
				} else {
					break;
				}
			}
			return expr;
		}
		return "";
	}

}
