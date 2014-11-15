package net.simpleframework.my.space;

import net.simpleframework.organization.IUser;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.AbstractTitleAwarePageLoad;
import net.simpleframework.web.page.PageParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MySpacePageLoad extends AbstractTitleAwarePageLoad {

	@Override
	public Object getBeanProperty(final PageParameter pageParameter, final String beanProperty) {
		if ("title".equals(beanProperty)) {
			final IAccount account = MySpaceUtils.getAccountAware().getSpecifiedAccount(pageParameter);
			String title = null;
			if (account != null) {
				final IUser user = account.user();
				if (user != null) {
					title = user.getText() + LocaleI18n.getMessage("MySpacePageLoad.0");
				}
			}
			title = wrapApplicationTitle(title);
			if (title != null) {
				return title;
			}
		} else if ("jobView".equals(beanProperty)) {
		} else if ("importCSS".equals(beanProperty)) {
			return new String[] { MySpaceUtils.getCssPath(pageParameter) + "/space.css" };
		} else if ("importPage".equals(beanProperty)) {
			if (pageParameter.getRequestParameter("t") == null) {
				String[] pages;
				if (MySpaceUtils.getAccountAware().isMyAccount(pageParameter)) {
					pages = new String[] { MySpaceUtils.deployPath + "jsp/space_log.xml", MySpaceUtils.deployPath + "jsp/space_log_editor.xml" };
				} else {
					pages = new String[] { MySpaceUtils.deployPath + "jsp/space_log.xml" };
				}
				return addImportPage(pageParameter, pages);
			}
		}
		return super.getBeanProperty(pageParameter, beanProperty);
	}
}
