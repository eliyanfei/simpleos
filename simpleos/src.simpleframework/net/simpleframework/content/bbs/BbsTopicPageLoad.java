package net.simpleframework.content.bbs;

import net.simpleframework.content.component.topicpager.ITopicPagerHandle;
import net.simpleframework.content.component.topicpager.TopicBean;
import net.simpleframework.content.component.topicpager.TopicPagerUtils;
import net.simpleframework.core.bean.ITextBeanAware;
import net.simpleframework.web.AbstractTitleAwarePageLoad;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class BbsTopicPageLoad extends AbstractTitleAwarePageLoad {

	@Override
	public Object getBeanProperty(final PageParameter pageParameter, final String beanProperty) {
		if ("jobView".equals(beanProperty)) {
			BbsUtils.initTopicPager(pageParameter);
		} else if ("title".equals(beanProperty)) {
			final ComponentParameter nComponentParameter = TopicPagerUtils
					.getComponentParameter(pageParameter);
			final ITopicPagerHandle tHandle = (ITopicPagerHandle) nComponentParameter
					.getComponentHandle();
			final TopicBean topic = tHandle.getEntityBeanByRequest(nComponentParameter);
			if (topic != null) {
				String topicString = topic.getTopic();
				final ITextBeanAware catalog = tHandle.getCatalogById(nComponentParameter,
						topic.getCatalogId());
				if (catalog != null) {
					topicString += " - " + catalog.getText();
				}
				return wrapApplicationTitle(topicString);
			}
		}
		return super.getBeanProperty(pageParameter, beanProperty);
	}
}
