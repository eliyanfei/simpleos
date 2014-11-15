package net.simpleframework.content.bbs;

import net.simpleframework.web.AbstractTitleAwarePageLoad;
import net.simpleframework.web.page.PageParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class BbsForumPageLoad extends AbstractTitleAwarePageLoad {
	@Override
	public Object getBeanProperty(final PageParameter pageParameter, final String beanProperty) {
		if ("title".equals(beanProperty)) {
			final BbsForum forum = BbsUtils.getForum(pageParameter);
			if (forum != null) {
				final String title = wrapApplicationTitle(forum.getText());
				if (title != null) {
					return title;
				}
			}
		}
		return super.getBeanProperty(pageParameter, beanProperty);
	}
}
