package net.simpleframework.content.component.topicpager;

import net.simpleframework.web.page.DefaultPageHandle;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PostsEditPageLoad extends DefaultPageHandle {

	@Override
	public Object getBeanProperty(final PageParameter pageParameter, final String beanProperty) {
		if ("importPage".equals(beanProperty)) {
			final ComponentParameter nComponentParameter = TopicPagerUtils
					.getComponentParameter(pageParameter);
			if (nComponentParameter.componentBean != null) {
				final ITopicPagerHandle tHandle = (ITopicPagerHandle) nComponentParameter
						.getComponentHandle();
				return tHandle.getPostImportPages(nComponentParameter);
			}
		} else if ("importCSS".equals(beanProperty)) {
			return new String[] { TopicPagerUtils.getCssPath(pageParameter) + "/topicpager.css" };
		} else if ("importJavascript".equals(beanProperty)) {
			return new String[] { TopicPagerUtils.getHomePath() + "/js/post_utils.js" };
		}
		return super.getBeanProperty(pageParameter, beanProperty);
	}
}
