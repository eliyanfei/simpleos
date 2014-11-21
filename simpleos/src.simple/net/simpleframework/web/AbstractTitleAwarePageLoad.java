package net.simpleframework.web;

import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.DefaultPageHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class AbstractTitleAwarePageLoad extends DefaultPageHandle {

	protected String wrapApplicationTitle(String title) {
		if (StringUtils.hasText(title)) {
			final String applicationTitle = getApplication().getApplicationConfig().getTitle();
			if (StringUtils.hasText(applicationTitle)) {
				title += " - " + applicationTitle;
			}
			return title;
		} else {
			return null;
		}
	}
}
