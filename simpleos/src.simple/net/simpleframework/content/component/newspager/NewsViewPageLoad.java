package net.simpleframework.content.component.newspager;

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
public class NewsViewPageLoad extends AbstractTitleAwarePageLoad {

	@Override
	public Object getBeanProperty(final PageParameter pageParameter, final String beanProperty) {
		if ("title".equals(beanProperty)) {
			final ComponentParameter nComponentParameter = NewsPagerUtils
					.getComponentParameter(pageParameter);
			if (nComponentParameter.componentBean != null) {
				final INewsPagerHandle nHandle = (INewsPagerHandle) nComponentParameter
						.getComponentHandle();
				final NewsBean news = nHandle.getEntityBeanByRequest(nComponentParameter);
				if (news != null) {
					String topicString = news.getTopic();
					final ITextBeanAware catalog = nHandle.getCatalogById(nComponentParameter,
							news.getCatalogId());
					if (catalog != null) {
						topicString += " - " + catalog.getText();
					}
					return wrapApplicationTitle(topicString);
				}
			}
		}
		return super.getBeanProperty(pageParameter, beanProperty);
	}
}
