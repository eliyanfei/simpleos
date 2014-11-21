package net.simpleframework.web.page.component.ui.dictionary;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.simpleframework.web.page.component.ui.htmleditor.HtmlEditorUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class SmileyUtils {
	private static final Pattern smileyPattern = Pattern
			.compile("[\\s\\S]*\\[:em(\\d+)\\][\\s\\S]*");

	public static String replaceSmiley(final String content) {
		final StringBuilder sb = new StringBuilder();
		sb.append(content);
		while (true) {
			final Matcher matcher = smileyPattern.matcher(sb.toString());
			if (matcher.matches()) {
				final MatchResult result = matcher.toMatchResult();
				final String group = result.group(1);
				final String tmp = sb.toString();
				sb.setLength(0);
				sb.append(tmp.substring(0, result.start(1) - 4));
				sb.append("<img style=\"vertical-align: middle;\" src=\"").append(
						HtmlEditorUtils.getHomePath());
				sb.append("/smiley/").append(group).append(".gif\">");
				sb.append(tmp.substring(result.end(1) + 1));
			} else {
				break;
			}
		}
		return sb.toString();
	}
}
