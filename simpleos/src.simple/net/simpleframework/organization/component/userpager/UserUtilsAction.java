package net.simpleframework.organization.component.userpager;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.itsite.ItSiteCache;
import net.itsite.ItSiteOrganizationApplicationModule.AccountExt;
import net.itsite.ItSiteUtil;
import net.itsite.utils.MD5;
import net.itsite.utils.StringsUtils;
import net.simpleframework.applets.notification.MailMessageNotification;
import net.simpleframework.applets.notification.NotificationUtils;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountContext;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.HTMLUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.util.script.ScriptEvalUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.SkinUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.HandleException;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;
import net.simpleframework.web.page.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.web.page.component.ui.validatecode.DefaultValidateCodeHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class UserUtilsAction extends AbstractAjaxRequestHandle {
	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("selector".equals(beanProperty)) {
			final String handleMethod = ((AjaxRequestBean) compParameter.componentBean).getHandleMethod();
			if ("bindingMail".equals(handleMethod) || "sendBindingMail".equals(handleMethod)) {
				return "#bindingMail_form";
			} else if ("editPassword".equals(handleMethod)) {
				return "#_userpwd_form";
			} else if ("editUserBase".equals(handleMethod)) {
				return "#_userbase_form";
			}
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	public IForward bindingMail(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final String bindingMailCode = compParameter.getRequestParameter("textBindingMailCode");
				if (!StringUtils.hash(compParameter.getSession()).equals(bindingMailCode)) {
					json.put("bindingMailCode", LocaleI18n.getMessage("UserPagerAction.5"));
				} else {
					final IAccount account = OrgUtils.am().queryForObjectById(
							compParameter.getRequestParameter(OrgUtils.um().getUserIdParameterName()));
					account.setMailbinding(true);
					OrgUtils.am().update(account);
				}
			}
		});
	}

	public IForward editUserAttr(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final IAccount account = OrgUtils.am().queryForObjectById(compParameter.getRequestParameter(OrgUtils.um().getUserIdParameterName()));
				if (account.user().isBuildIn())
					throw HandleException.wrapException(LocaleI18n.getMessage("buildin.1"));
				if (account != null) {
					BeanUtils.setProperty(account, "points",
							ConvertUtils.toInt(compParameter.getRequestParameter("user_points"), account.getPoints()));
					OrgUtils.am().update(account);
				}
			}
		});
	}

	/**
	 * 修改签名
	 * @param compParameter
	 * @return
	 */
	public IForward editUserSignature(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final IUser user = ItSiteUtil.getLoginUser(compParameter);
				final String signature_text = compParameter.getRequestParameter("user_signature");
				user.setSignature(HTMLUtils.htmlEscape(signature_text));
				OrgUtils.um().update(new String[] { "signature" }, user);
				json.put("signature", user.getSignature());
			}
		});
	}

	public IForward sendBindingMail(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				if (!DefaultValidateCodeHandle.isValidateCode(compParameter.request, "textBindingMailValidateCode")) {
					json.put("validateCode", DefaultValidateCodeHandle.getErrorString());
				} else {
					final IUser user = OrgUtils.um().queryForObjectById(compParameter.getRequestParameter(OrgUtils.um().getUserIdParameterName()));
					final String email = compParameter.getRequestParameter("textSendBindingMail");
					if (!email.equals(user.getEmail())) {
						user.setEmail(email);
						OrgUtils.um().update(user);
					}

					final MailMessageNotification mailMessage = new MailMessageNotification();
					mailMessage.getTo().add(user);
					mailMessage.setSubject(LocaleI18n.getMessage("UserPagerAction.3",
							StringsUtils.trimNull(ItSiteUtil.attrMap.get("site.site_name"), "")));
					final Map<String, Object> variable = new HashMap<String, Object>();
					variable.put("username", user);
					variable.put("bindingcode", StringUtils.hash(compParameter.getSession()));
					mailMessage.setTextBody(ScriptEvalUtils.replaceExprFromResource(DefaultUserPagerHandle.class, "mail_binding.txt", variable));
					NotificationUtils.sendMessage(mailMessage);
					json.put("send", LocaleI18n.getMessage("UserPagerAction.4"));
				}
			}
		});
	}

	public IForward unBindingMail(final ComponentParameter compParameter) {
		final IAccount account = OrgUtils.am().queryForObjectById(compParameter.getRequestParameter(OrgUtils.um().getUserIdParameterName()));
		account.setMailbinding(false);
		OrgUtils.am().update(account);
		return null;
	}

	public IForward editUserSpace(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(final Map<String, Object> json) throws Exception {
				Object userId = compParameter.getRequestParameter(OrgUtils.um().getUserIdParameterName());
				if (userId == null) {
					userId = AccountSession.getLogin(compParameter.getSession()).getId().getValue();
				}
				final AccountExt account = (AccountExt) ItSiteUtil.getAccountById(userId);
				if (account.user().isBuildIn())
					throw HandleException.wrapException(LocaleI18n.getMessage("buildin.1"));
				if (account != null) {
					final String user_space_skin = compParameter.getRequestParameter("user_space_skin");
					if (SkinUtils.getSkinList().containsKey(user_space_skin)) {
						account.setSkin(user_space_skin);
					} else {
						account.setSkin(SkinUtils.DEFAULT_SKIN);
						SkinUtils.setSessionSkin(compParameter.getSession(), SkinUtils.DEFAULT_SKIN);
					}
					OrgUtils.am().update(new String[] { "skin" }, account);
				}
			}
		});
	}

	public IForward editUserExt(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				Object userId = compParameter.getRequestParameter(OrgUtils.um().getUserIdParameterName());
				if (userId == null) {
					userId = AccountSession.getLogin(compParameter.getSession()).getId().getValue();
				}
			}
		});
	}

	public IForward editUserBase(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final IUser user = OrgUtils.um().queryForObjectById(compParameter.getRequestParameter(OrgUtils.um().getUserIdParameterName()));
				if (user.isBuildIn())
					throw HandleException.wrapException(LocaleI18n.getMessage("buildin.1"));
				PageUtils.setObjectFromRequest(user, compParameter.request, "user_", UserPagerUtils.userProperties);
				user.setBirthday(ConvertUtils.toDate(compParameter.getRequestParameter("user_birthday"), IUser.birthdayDateFormat));
				OrgUtils.um().update(user);

				if (StringUtils.hasText(user.getMobile()) && StringUtils.hasText(user.getHometown()) && StringUtils.hasText(user.getDescription())) {
					AccountContext.update(user.account(), "org_edituser", user.getId());
				}
			}
		});
	}

	/**
	 * 重新设置秘密
	 * @param compParameter
	 * @return
	 */
	public IForward findPassword(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(final Map<String, Object> json) throws Exception {
				final String sid = compParameter.getRequestParameter("sid");
				final String email = ItSiteCache.getEmail(sid);
				if (!"null".equals(sid) && StringUtils.hasText(sid) && email != null) {
					final IUser user = OrgUtils.um().getUserByEmail(email);
					if (user != null) {
						final IAccount account = user.account();
						account.setPassword(MD5.getHashString(compParameter.getRequestParameter("user_password")));
						OrgUtils.am().update(new String[] { "password" }, account);
					}
					json.put("act", "true");
				} else {
					json.put("act", "false");
				}
			}
		});
	}

	public IForward editPassword(final ComponentParameter compParameter) {
		// final ComponentParameter nComponentParameter =
		// getComponentParameter(compParameter);
		// final IUserPagerHandle uHandle = (IUserPagerHandle)
		// nComponentParameter.getComponentHandle();
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				if (!DefaultValidateCodeHandle.isValidateCode(compParameter.request, "user_validateCode")) {
					json.put("validateCode", DefaultValidateCodeHandle.getErrorString());
				} else {
					final IAccount account = OrgUtils.am().queryForObjectById(
							compParameter.getRequestParameter(OrgUtils.um().getUserIdParameterName()));
					if (account.user().isBuildIn())
						throw HandleException.wrapException(LocaleI18n.getMessage("buildin.1"));

					final String oldpassword = compParameter.getRequestParameter("user_old_password");
					if (!account.getPassword().equals(MD5.getHashString(oldpassword))) {
						json.put("oldPassword", LocaleI18n.getMessage("UserPagerAction.1"));
					} else {
						account.setPassword(MD5.getHashString(compParameter.getRequestParameter("user_password")));
						OrgUtils.am().update(account);
						if (ConvertUtils.toBoolean(compParameter.getRequestParameter("user_SendMail"), false)) {
							final MailMessageNotification mailMessage = new MailMessageNotification();
							mailMessage.getTo().add(account.user());
							mailMessage.setSubject(LocaleI18n.getMessage("UserPagerAction.2", PageUtils.pageContext.getApplication()
									.getApplicationConfig().getTitle()));
							final Map<String, Object> variable = new HashMap<String, Object>();
							variable.put("username", account.user());
							variable.put("date", ConvertUtils.toDateString(new Date()));
							mailMessage.setTextBody(ScriptEvalUtils.replaceExprFromResource(DefaultUserPagerHandle.class, "mail_edit_password.txt",
									variable));
							NotificationUtils.sendMessage(mailMessage);
						}
					}
				}
			}
		});
	}
}
