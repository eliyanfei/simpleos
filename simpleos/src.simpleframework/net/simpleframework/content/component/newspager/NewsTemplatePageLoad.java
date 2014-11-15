package net.simpleframework.content.component.newspager;

import net.simpleframework.util.LangUtils;
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
public class NewsTemplatePageLoad extends AbstractTitleAwarePageLoad {

	@Override
	public Object getBeanProperty(final PageParameter pageParameter, final String beanProperty) {
		if ("importPage".equals(beanProperty)) {
			String[] importPage = new String[] { "/app/common/complaint.xml", "/app/myfavorite/myfavorite_utils.xml" };
			final ComponentParameter nComponentParameter = NewsPagerUtils.getComponentParameter(pageParameter);
			if (NewsPagerUtils.isNewsEdit(nComponentParameter, null)) {
				importPage = (String[]) LangUtils.add(importPage, NewsPagerUtils.getHomePath() + "/jsp/__newspager_window.xml");
			}
			return importPage;
		}
		return super.getBeanProperty(pageParameter, beanProperty);
	}
}
