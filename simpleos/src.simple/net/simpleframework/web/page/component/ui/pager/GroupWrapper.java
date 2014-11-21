package net.simpleframework.web.page.component.ui.pager;

import java.util.List;

import net.simpleframework.util.ANamedObject;
import net.simpleframework.util.JavascriptUtils;
import net.simpleframework.util.StringUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class GroupWrapper extends ANamedObject {

	public GroupWrapper(final String name) {
		super(name);
	}

	public String getLeftTitle(final List<Object> data) {
		final StringBuilder sb = new StringBuilder();
		final String desc = getDescription();
		boolean title;
		if (title = StringUtils.hasText(desc)) {
			sb.append("<div title=\"").append(JavascriptUtils.escape(desc)).append("\">");
		}
		sb.append(getName());
		if (title) {
			sb.append("</div>");
		}
		return sb.toString();
	}

	public String getRightTitle(final List<Object> data) {
		final StringBuilder sb = new StringBuilder();
		sb.append("( ");
		sb.append(data.size()).append(" )");
		return sb.toString();
	}

	private static final long serialVersionUID = 1080241521644844455L;
}
