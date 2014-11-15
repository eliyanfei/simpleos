package net.simpleframework.content.component.vote;

import net.simpleframework.web.page.DefaultPageHandle;
import net.simpleframework.web.page.PageParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class VotePageLoad extends DefaultPageHandle {

	@Override
	public Object getBeanProperty(final PageParameter pageParameter, final String beanProperty) {
		if ("importPage".equals(beanProperty)) {
			return new String[] { VoteUtils.getHomePath() + "/jsp/vote_admin.xml" };
		}
		return super.getBeanProperty(pageParameter, beanProperty);
	}
}
