package net.simpleframework.organization.component.register;

import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.content.AbstractContentLayoutModuleHandle;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.EAccountStatus;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.UrlForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.portal.PageletBean;

/**
 * 
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2014-1-5下午02:09:01
 */
public class UserLastLoginPortalModule extends AbstractContentLayoutModuleHandle {
	public UserLastLoginPortalModule(final PageletBean pagelet) {
		super(pagelet);
	}

	private static String[] defaultOptions = new String[] { "_user_l_rows=6", "_user_l_size=64" };

	@Override
	protected String[] getDefaultOptions() {
		return defaultOptions;
	}

	@Override
	public IForward getPageletOptionContent(final ComponentParameter compParameter) {
		return new UrlForward(UserRegisterUtils.getHomePath() + "/jsp/user_last_login_option.jsp");
	}

	@Override
	public void optionLoaded(final PageParameter pageParameter, final Map<String, Object> dataBinding) {
		super.optionLoaded(pageParameter, dataBinding);
	}

	@Override
	public IForward getPageletContent(final ComponentParameter compParameter) {
		compParameter.setRequestAttribute("__qs",
				OrgUtils.am().query(new ExpressionValue("status=? order by lastLoginDate desc", new Object[] { EAccountStatus.normal })));
		String forward = OrgUtils.deployPath + "jsp/account_layout.jsp?";
		forward = WebUtils.addParameters(forward, "showCreateDate=false");
		forward = WebUtils.addParameters(forward, "size=" + ConvertUtils.toInt(getPagelet().getOptionProperty("_user_l_size"), 64));
		forward = WebUtils.addParameters(forward, "rows=" + ConvertUtils.toInt(getPagelet().getOptionProperty("_user_l_rows"), 6));
		return new UrlForward(forward, "pa");
	}
}
