package net.simpleframework.content.component.vote;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.simpleframework.ado.db.UniqueValue;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.IoUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.AbstractUrlForward;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class VoteAction extends AbstractAjaxRequestHandle {

	public IForward voteSubmit(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = VoteUtils.getComponentParameter(compParameter);
		final IVoteHandle vHandle = (IVoteHandle) nComponentParameter.getComponentHandle();
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final Vote vote = vHandle.getEntityBeanByRequest(nComponentParameter);
				final Date expiredDate = vote.getExpiredDate();
				if (expiredDate != null && expiredDate.before(new Date())) {
					json.put("error", LocaleI18n.getMessage("VoteAction.1"));
				} else {
					final HttpSession httpSession = compParameter.getSession();
					final String sessionKey = "vote_" + vote.getId();
					if (ConvertUtils.toBoolean(httpSession.getAttribute(sessionKey), false)) {
						json.put("error", LocaleI18n.getMessage("VoteAction.0"));
					} else {
						int count = 0;
						final Enumeration<?> en = compParameter.request.getParameterNames();
						while (en.hasMoreElements()) {
							final String key = (String) en.nextElement();
							if (key.startsWith("__vg_")) {
								final VoteResult[] results = vHandle.submitVote(nComponentParameter,
										compParameter.request.getParameterValues(key));
								if (results != null) {
									count += results.length;
								}
							}
						}
						if (count > 0) {
							httpSession.setAttribute(sessionKey, Boolean.TRUE);
						}

						final String jsCallback = vHandle.getJavascriptCallback(nComponentParameter,
								"submit", vote);
						if (StringUtils.hasText(jsCallback)) {
							json.put("jsCallback", jsCallback);
						}
					}
				}
			}
		});
	}

	public IForward voteDelete(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = VoteUtils.getComponentParameter(compParameter);
		final IVoteHandle vHandle = (IVoteHandle) nComponentParameter.getComponentHandle();
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				vHandle.doDelete(
						nComponentParameter,
						new UniqueValue(compParameter.getRequestParameter(vHandle
								.getIdParameterName(nComponentParameter))));
				final String jsCallback = vHandle.getJavascriptCallback(nComponentParameter, "delete",
						null);
				if (StringUtils.hasText(jsCallback)) {
					json.put("jsCallback", jsCallback);
				}
			}
		});
	}

	public IForward voteCreate(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = VoteUtils.getComponentParameter(compParameter);
		final IVoteHandle vHandle = (IVoteHandle) nComponentParameter.getComponentHandle();
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final Map<String, Object> data = new HashMap<String, Object>();
				final Vote vote = vHandle.doAdd(nComponentParameter, data);
				final String jsCallback = vHandle.getJavascriptCallback(nComponentParameter, "add",
						vote);
				if (StringUtils.hasText(jsCallback)) {
					json.put("jsCallback", jsCallback);
				}
			}
		});
	}

	public IForward voteSave(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = VoteUtils.getComponentParameter(compParameter);
		final IVoteHandle vHandle = (IVoteHandle) nComponentParameter.getComponentHandle();
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final Map<String, Object> data = new HashMap<String, Object>();
				for (final String property : new String[] { "text", "status", "description" }) {
					data.put(property, compParameter.getRequestParameter("ve1_" + property));
				}
				data.put("expiredDate", ConvertUtils.toDate(
						compParameter.getRequestParameter("ve1_expiredDate"), Vote.expiredDateFormat));
				vHandle.doEdit(nComponentParameter, compParameter.getRequestParameter(vHandle
						.getIdParameterName(nComponentParameter)), data);
			}
		});
	}

	public IForward groupSave(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = VoteUtils.getComponentParameter(compParameter);
		final IVoteHandle vHandle = (IVoteHandle) nComponentParameter.getComponentHandle();
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final Map<String, Object> data = new HashMap<String, Object>();
				data.put("text", compParameter.getRequestParameter("ve2_text"));
				data.put("multiple", compParameter.getRequestParameter("ve2_multiple"));
				final String groupId = compParameter.getRequestParameter(VoteUtils.VOTE_GROUP_ID);
				if (StringUtils.hasText(groupId)) {
					vHandle.doEdit(nComponentParameter, groupId, data, VoteItemGroup.class);
				} else {
					data.put("voteId", compParameter.getRequestParameter(vHandle
							.getIdParameterName(nComponentParameter)));
					vHandle.doAdd(nComponentParameter, data, VoteItemGroup.class);
				}
			}
		});
	}

	public IForward groupDelete(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = VoteUtils.getComponentParameter(compParameter);
		final IVoteHandle vHandle = (IVoteHandle) nComponentParameter.getComponentHandle();
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				vHandle.doDelete(nComponentParameter,
						new UniqueValue(compParameter.getRequestParameter(VoteUtils.VOTE_GROUP_ID)),
						VoteItemGroup.class);
			}
		});
	}

	public IForward itemSave(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = VoteUtils.getComponentParameter(compParameter);
		final IVoteHandle vHandle = (IVoteHandle) nComponentParameter.getComponentHandle();
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) throws IOException {
				final Map<String, Object> data = new HashMap<String, Object>();
				final String text = compParameter.getRequestParameter("ve3_text");
				final String itemId = compParameter.getRequestParameter(VoteUtils.VOTE_ITEM_ID);
				if (StringUtils.hasText(itemId)) {
					data.put("text", text);
					vHandle.doEdit(nComponentParameter, itemId, data, VoteItem.class);
				} else {
					data.put(VoteUtils.VOTE_GROUP_ID,
							compParameter.getRequestParameter(VoteUtils.VOTE_GROUP_ID));
					for (final String sText : IoUtils.getStringsFromReader(new StringReader(text))) {
						if (StringUtils.hasText(sText)) {
							data.put("text", sText);
							vHandle.doAdd(nComponentParameter, data, VoteItem.class);
						}
					}
				}
			}
		});
	}

	public IForward itemDelete(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = VoteUtils.getComponentParameter(compParameter);
		final IVoteHandle vHandle = (IVoteHandle) nComponentParameter.getComponentHandle();
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				vHandle.doDelete(nComponentParameter,
						new UniqueValue(compParameter.getRequestParameter(VoteUtils.VOTE_ITEM_ID)),
						VoteItem.class);
			}
		});
	}

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("jobExecute".equals(beanProperty)) {
			final ComponentParameter nComponentParameter = VoteUtils
					.getComponentParameter(compParameter);
			if (nComponentParameter.componentBean != null) {
				final String componentName = (String) compParameter.getBeanProperty("name");
				if ("ajaxVoteCreate".equals(componentName)) {
					return nComponentParameter.getBeanProperty("jobAdd");
				} else if ("ajaxVoteSubmit".equals(componentName)) {
					return IJob.sj_anonymous;
				} else {
					final Vote vote = ((IVoteHandle) nComponentParameter.getComponentHandle())
							.getEntityBeanByRequest(nComponentParameter);
					if (vote != null
							&& AccountSession.isAccount(compParameter.getSession(), vote.getUserId())) {
						return IJob.sj_account_normal;
					} else {
						if ("ajaxVoteEditPage".equals(componentName)) {
							return nComponentParameter.getBeanProperty("jobEdit");
						} else if ("ajaxVoteDelete".equals(componentName)) {
							return nComponentParameter.getBeanProperty("jobDelete");
						}
					}
				}
			}
		} else if ("selector".equals(beanProperty)) {
			final ComponentParameter nComponentParameter = VoteUtils
					.getComponentParameter(compParameter);
			if (nComponentParameter.componentBean != null) {
				final String componentName = (String) compParameter.getBeanProperty("name");
				if ("ajaxVoteUsersViewPage".equals(componentName)
						|| "ajaxVoteUserDelete".equals(componentName)) {
					return nComponentParameter.getBeanProperty(beanProperty);
				}
			}
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	public IForward editUrl(final ComponentParameter compParameter) {
		return AbstractUrlForward.componentUrl(VoteRegistry.vote, "/jsp/vote_edit.jsp");
	}

	public IForward usersViewUrl(final ComponentParameter compParameter) {
		return AbstractUrlForward.componentUrl(VoteRegistry.vote, "/jsp/vote_users_view.jsp");
	}

	public IForward userDelete(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = VoteUtils.getComponentParameter(compParameter);
		final IVoteHandle vHandle = (IVoteHandle) nComponentParameter.getComponentHandle();
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) throws Exception {
				vHandle.doDelete(nComponentParameter,
						new UniqueValue(compParameter.getRequestParameter("resultId")), VoteResult.class);
			}
		});
	}
}
