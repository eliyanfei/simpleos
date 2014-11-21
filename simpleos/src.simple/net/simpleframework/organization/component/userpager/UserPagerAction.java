package net.simpleframework.organization.component.userpager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.itsite.ItSiteUtil;
import net.itsite.utils.StringsUtils;
import net.prj.manager.PrjMgrUtils;
import net.simpleframework.applets.notification.MailMessageNotification;
import net.simpleframework.applets.notification.NotificationUtils;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.Account;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.EAccountStatus;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.HTTPUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.WebApplicationConfig;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.HandleException;
import net.simpleframework.web.page.component.base.ajaxrequest.AjaxRequestBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class UserPagerAction extends UserPagerUrlAction {
	@Override
	public Object getBeanProperty(final ComponentParameter compParameter,
			final String beanProperty) {
		if ("selector".equals(beanProperty)) {
			String selector = null;
			final ComponentParameter nComponentParameter = getComponentParameter(compParameter);
			if (nComponentParameter.componentBean != null) {
				selector = (String) nComponentParameter
						.getBeanProperty("selector");
			}
			selector = StringUtils.hasText(selector) ? selector + ", " : "";
			final String handleMethod = ((AjaxRequestBean) compParameter.componentBean)
					.getHandleMethod();
			if ("addUser".equals(handleMethod)) {
				return selector + "#userFormEditor";
			} else if ("tabUrl".equals(handleMethod)) {
				return selector + "#__user_edit_form";
			} else if ("sentMail".equals(handleMethod)) {
				return selector + "#__sent_mail_form";
			}
		} else if ("jobExecute".equals(beanProperty)) {
			final ComponentParameter nComponentParameter = getComponentParameter(compParameter);
			if (nComponentParameter.componentBean != null) {
				final String componentName = (String) compParameter
						.getBeanProperty("name");
				if ("ajaxAddUserPage".equals(componentName)) {
					return nComponentParameter.getBeanProperty("jobAdd");
				} else if ("userPagerMove".equals(componentName)) {
					return nComponentParameter.getBeanProperty("jobExchange");
				} else {
					if (AccountSession.isAccount(compParameter.getSession(),
							ConvertUtils.toLong(compParameter
									.getRequestParameter(OrgUtils.um()
											.getUserIdParameterName())))) {
						return IJob.sj_account_normal;
					} else {
						if ("ajaxEditUserPage".equals(componentName)) {
							return nComponentParameter
									.getBeanProperty("jobEdit");
						} else if ("userPagerDelete".equals(componentName)) {
							return nComponentParameter
									.getBeanProperty("jobDelete");
						}
					}
				}
			}
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	public IForward addUser(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final String user_account = compParameter
						.getRequestParameter("user_account");
				Account account = (Account) OrgUtils.am().getAccountByName(
						user_account);
				if (account != null) {
					json.put("exist",
							LocaleI18n.getMessage("UserPagerAction.0"));
				} else {
					final String user_password = compParameter
							.getRequestParameter("user_password");
					account = (Account) OrgUtils.am().insertAccount(
							user_account, user_password,
							HTTPUtils.getRemoteAddr(compParameter.request),
							new IAccount.InsertCallback() {
								@Override
								public void insert(final IUser user) {
									PageUtils
											.setObjectFromRequest(user,
													compParameter.request,
													"user_", new String[] {
															"text", "sex",
															"departmentId",
															"birthday" });
								}
							});
					json.put("next", ConvertUtils.toBoolean(
							compParameter.getRequestParameter("next"), false));

					final ComponentParameter nComponentParameter = getComponentParameter(compParameter);
					final IUserPagerHandle uHandle = (IUserPagerHandle) nComponentParameter
							.getComponentHandle();
					final String jsCallback = uHandle.getJavascriptCallback(
							nComponentParameter, "add", null);
					if (StringUtils.hasText(jsCallback)) {
						json.put("jsCallback", jsCallback);
					}
				}
			}
		});
	}

	/**
	 * 激活用户
	 * 
	 * @param compParameter
	 * @return
	 */
	public IForward active(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				final ComponentParameter nComponentParameter = getComponentParameter(compParameter);
				final IUserPagerHandle uHandle = (IUserPagerHandle) nComponentParameter
						.getComponentHandle();
				final String userId = compParameter
						.getRequestParameter(OrgUtils.um()
								.getUserIdParameterName());
				final IAccount account = OrgUtils.am().queryForObjectById(
						userId);
				if (account != null) {
					account.setStatus(EAccountStatus.normal);
					OrgUtils.am().update(new String[] { "status" }, account);
					MailMessageNotification mailMessage;
					try {
						mailMessage = new MailMessageNotification();
						mailMessage.setHtmlContent(true);
						mailMessage.getTo().add(account.user().getEmail());
						final WebApplicationConfig applicationConfig = (WebApplicationConfig) uHandle
								.getApplicationModule().getApplication()
								.getApplicationConfig();
						mailMessage.setSubject(StringsUtils.trimNull(
								ItSiteUtil.attrMap.get("site.site_name"), "")
								+ "激活通知");
						mailMessage
								.setTextBody("你的账号已经被激活，现在你可以登入站点！<br/><a href='"
										+ applicationConfig.getServerUrl()
										+ "'>"
										+ applicationConfig.getTitle()
										+ "</a>");
						NotificationUtils.sendMessage(mailMessage);
					} catch (Exception e) {
					}
				}
				final String jsCallback = uHandle.getJavascriptCallback(
						nComponentParameter, "delete", null);
				if (StringUtils.hasText(jsCallback)) {
					json.put("jsCallback", jsCallback);
				}
			}
		});
	}

	public IForward unlock(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = getComponentParameter(compParameter);
		return jsonForward(compParameter, new JsonCallback() {
			public void doAction(Map<String, Object> json) {
				IAccount account = (IAccount) OrgUtils.am().queryForObjectById(
						nComponentParameter.getRequestParameter(OrgUtils.um()
								.getUserIdParameterName()));
				if (account != null) {
					account.setStatus(EAccountStatus.normal);
					AccountSession.logout(account);
					OrgUtils.am().update(account);
					IUserPagerHandle uHandle = (IUserPagerHandle) nComponentParameter
							.getComponentHandle();
					String jsCallback = uHandle.getJavascriptCallback(
							nComponentParameter, "add", null);
					if (StringUtils.hasText(jsCallback))
						json.put("jsCallback", jsCallback);
				}
			}
		});
	}

	public IForward lock(ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = getComponentParameter(compParameter);
		return jsonForward(compParameter, new JsonCallback() {
			public void doAction(Map<String, Object> json) {
				IAccount account = (IAccount) OrgUtils.am().queryForObjectById(
						nComponentParameter.getRequestParameter(OrgUtils.um()
								.getUserIdParameterName()));
				if (account != null) {
					if (((IUser) account.user()).isBuildIn())
						throw HandleException.wrapException(LocaleI18n
								.getMessage("buildin.1"));

					account.setStatus(EAccountStatus.locked);
					AccountSession.logout(account);
					OrgUtils.am().update(account);
					IUserPagerHandle uHandle = (IUserPagerHandle) nComponentParameter
							.getComponentHandle();
					String jsCallback = uHandle.getJavascriptCallback(
							nComponentParameter, "add", null);
					if (StringUtils.hasText(jsCallback))
						json.put("jsCallback", jsCallback);
				}
			}
		});
	}

	/**
	 * 用户加入或取消黑名单
	 * 
	 * @param compParameter
	 * @return
	 */
	public IForward blacklist(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				final ComponentParameter nComponentParameter = getComponentParameter(compParameter);
				final IUserPagerHandle uHandle = (IUserPagerHandle) nComponentParameter
						.getComponentHandle();
				final String userId = compParameter
						.getRequestParameter(OrgUtils.um()
								.getUserIdParameterName());
				final IAccount account = OrgUtils.am().queryForObjectById(
						userId);
				if (account.user().isBuildIn())
					throw HandleException.wrapException(LocaleI18n
							.getMessage("buildin.1"));
				if (account != null) {
					account.setBlacklist(!account.isBlacklist());
					OrgUtils.am().update(new String[] { "blacklist" }, account);
					if (account.isBlacklist()) {
						json.put("rs", "已加入黑名单");
					} else {
						json.put("rs", "已经取消黑名单");
					}
				}
				final String jsCallback = uHandle.getJavascriptCallback(
						nComponentParameter, "delete", null);
				if (StringUtils.hasText(jsCallback)) {
					json.put("jsCallback", jsCallback);
				}
			}
		});
	}

	public IForward move2Dept(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = getComponentParameter(compParameter);
		final IUserPagerHandle uHandle = (IUserPagerHandle) nComponentParameter
				.getComponentHandle();
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final String userId = nComponentParameter
						.getRequestParameter(OrgUtils.um()
								.getUserIdParameterName());
				final Map<String, Object> data = new HashMap<String, Object>();
				final String deptId = compParameter
						.getRequestParameter("mDepartment");
				if (StringUtils.hasText(deptId)) {
					data.put("department", deptId);
				}

				uHandle.doEdit(nComponentParameter, userId, data);
				final String jsCallback = uHandle.getJavascriptCallback(
						nComponentParameter, "move2", null);
				if (StringUtils.hasText(jsCallback)) {
					json.put("jsCallback", jsCallback);
				}
			}
		});
	}

	public IForward sentMail(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = getComponentParameter(compParameter);
		final IUserPagerHandle uHandle = (IUserPagerHandle) nComponentParameter
				.getComponentHandle();
		final String[] userArr = StringUtils.split(compParameter
				.getRequestParameter(OrgUtils.um().getUserIdParameterName()));
		final ArrayList<IUser> users = new ArrayList<IUser>();
		if (userArr != null) {
			for (final String userId : userArr) {
				final IUser user = OrgUtils.um().queryForObjectById(userId);
				if (user != null) {
					users.add(user);
				}
			}
			uHandle.doSentMail(nComponentParameter, users, compParameter
					.getRequestParameter("sentMailTopic"), compParameter
					.getRequestParameter("textareaSentMailHtmlEditor"));
		}
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				json.put("info", LocaleI18n.getMessage("UserPagerAction.6",
						users.size()));
			}
		});
	}
}
