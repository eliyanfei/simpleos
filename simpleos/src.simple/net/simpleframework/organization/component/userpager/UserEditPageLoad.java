package net.simpleframework.organization.component.userpager;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.simpleframework.organization.IDepartment;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.DefaultPageHandle;
import net.simpleframework.web.page.PageParameter;
import net.simpleos.SimpleosOrganizationApplicationModule.AccountExt;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class UserEditPageLoad extends DefaultPageHandle {

	public void addUserLoaded(final PageParameter pageParameter, final Map<String, Object> dataBinding, final List<String> visibleToggleSelector,
			final List<String> readonlySelector, final List<String> disabledSelector) {
		final HttpServletRequest request = pageParameter.request;
		final IDepartment dept = OrgUtils.dm().queryForObjectById(request.getParameter(OrgUtils.dm().getDepartmentIdParameterName()));
		if (dept != null) {
			dataBinding.put("user_departmentId", dept.getId());
			dataBinding.put("user_departmentText", dept.getText());
		}
	}

	public void userExtLoaded(final PageParameter pageParameter, final Map<String, Object> dataBinding, final List<String> visibleToggleSelector,
			final List<String> readonlySelector, final List<String> disabledSelector) {
		final HttpServletRequest request = pageParameter.request;
		Object userId = request.getParameter(OrgUtils.um().getUserIdParameterName());
		if (userId == null) {
			userId = AccountSession.getLogin(pageParameter.getSession()).getId().getValue();
		}
	}

	public void userSpaceLoaded(final PageParameter pageParameter, final Map<String, Object> dataBinding, final List<String> visibleToggleSelector,
			final List<String> readonlySelector, final List<String> disabledSelector) {
		final HttpServletRequest request = pageParameter.request;
		Object userId = request.getParameter(OrgUtils.um().getUserIdParameterName());
		if (userId == null) {
			userId = AccountSession.getLogin(pageParameter.getSession()).getId().getValue();
		}
		final IUser user = OrgUtils.um().queryForObjectById(userId);
		if (user != null) {
			final AccountExt account = (AccountExt) user.account();
			dataBinding.put("user_space_skin", account.getSkin());
		}
	}

	public void userBaseLoaded(final PageParameter pageParameter, final Map<String, Object> dataBinding, final List<String> visibleToggleSelector,
			final List<String> readonlySelector, final List<String> disabledSelector) {
		final HttpServletRequest request = pageParameter.request;
		final String userId = request.getParameter(OrgUtils.um().getUserIdParameterName());
		final IUser user = OrgUtils.um().queryForObjectById(userId);
		if (user != null) {
			for (final String prop : UserPagerUtils.userProperties) {
				dataBinding.put("user_" + prop, BeanUtils.getProperty(user, prop));
			}
			dataBinding.put("user_hometown_text", user.getHometownText());
			dataBinding.put("user_birthday", ConvertUtils.toDateString(user.getBirthday(), IUser.birthdayDateFormat));
			final String email = user.getEmail();
			if (StringUtils.hasText(email)) {// &&
				// user.getAccount().isMailbinding()
				readonlySelector.add("#user_email");
			}
		}
	}

	/**
	* 高级信息修改
	*/
	public void userAttrLoaded(final PageParameter pageParameter, final Map<String, Object> dataBinding, final List<String> visibleToggleSelector,
			final List<String> readonlySelector, final List<String> disabledSelector) {
		final HttpServletRequest request = pageParameter.request;
		final String userId = request.getParameter(OrgUtils.um().getUserIdParameterName());
		final IAccount account = OrgUtils.am().queryForObjectById(userId);
		if (account != null) {
			dataBinding.put("user_points", BeanUtils.getProperty(account, "points"));
		}
	}
}
