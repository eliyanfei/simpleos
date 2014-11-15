package net.simpleframework.web.page;

import net.simpleframework.util.StringUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TextForward implements IForward {
	private final String responseText;

	public TextForward(final String responseText) {
		this.responseText = responseText;
	}

	@Override
	public String getResponseText(final PageRequestResponse requestResponse) {
		return StringUtils.blank(responseText);
	}
}
