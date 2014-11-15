package net.simpleframework.content.blog;

import net.simpleframework.content.ContentUtils;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.AbstractTitleAwarePageLoad;
import net.simpleframework.web.page.PageParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MyBlogPageLoad extends AbstractTitleAwarePageLoad {

	@Override
	public Object getBeanProperty(final PageParameter pageParameter, final String beanProperty) {
		if ("title".equals(beanProperty)) {
			final IAccount account = ContentUtils.getAccountAware().getAccount(pageParameter);
			IUser user;
			if (account != null && (user = account.user()) != null) {
				String title = user.getText() + LocaleI18n.getMessage("my_blog_bar.0");
				final String applicationTitle = getApplication().getApplicationConfig().getTitle();
				if (StringUtils.hasText(applicationTitle)) {
					title += " - " + applicationTitle;
				}
				return title;
			}
		}
		return super.getBeanProperty(pageParameter, beanProperty);
	}
}
