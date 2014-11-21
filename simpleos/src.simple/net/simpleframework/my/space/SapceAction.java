package net.simpleframework.my.space;

import java.io.File;
import java.util.Map;

import net.simpleframework.my.message.MessageUtils;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.FilePathWrapper;
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
public class SapceAction extends AbstractAjaxRequestHandle {

	@Override
	public Object getBeanProperty(ComponentParameter compParameter, String beanProperty) {
		if ("jobExecute".equals(beanProperty)) {
			Object obj = super.getBeanProperty(compParameter, beanProperty);
			if (obj == null)
				return IJob.sj_account_normal;
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	public IForward userAttention(final ComponentParameter compParameter) {
		final IAccount login = AccountSession.getLogin(compParameter.getSession());
		if (login == null) {
			return null;
		}
		final String attentionId = compParameter.getRequestParameter("attentionId");
		if (!StringUtils.hasText(attentionId)) {
			return null;
		}

		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				if (login.getId().equals2(attentionId)) {
					json.put("error", LocaleI18n.getMessage("SapceAction.0"));
				} else {
					if (ConvertUtils.toBoolean(compParameter.getRequestParameter("delete"), false)) {
						MySpaceUtils.deleteUserAttention(compParameter, attentionId);
					} else {
						MySpaceUtils.addUserAttention(compParameter, attentionId);
					}
					json.put("attention", MySpaceUtils.buildUserAttention(compParameter, attentionId));
				}
			}
		});
	}

	public IForward logSave(final ComponentParameter compParameter) {
		final String content = compParameter.getRequestParameter("ta_space_log_editor");
		if (!StringUtils.hasText(content)) {
			return null;
		}
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				if (MySpaceUtils.addSapceLog(compParameter, content) != null) {
					json.put("result", Boolean.TRUE);
				}
			}
		});
	}

	public IForward logDelete(final ComponentParameter compParameter) {
		final String logid = compParameter.getRequestParameter("logid");
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				if (StringUtils.hasText(logid) && MySpaceUtils.deleteSapceLog(compParameter, logid) > 0) {
					json.put("result", Boolean.TRUE);
				}
			}
		});
	}

	public IForward logRemarkSave(final ComponentParameter compParameter) {
		final SapceLogBean sapceLog = MySpaceUtils.getTableEntityManager(SapceLogBean.class).queryForObjectById(
				compParameter.getRequestParameter("logid"), SapceLogBean.class);
		if (sapceLog == null) {
			return null;
		}
		final String content = compParameter.getRequestParameter("ta_space_log_remark");
		if (!StringUtils.hasText(content)) {
			return null;
		}
		final IAccount account = AccountSession.getLogin(compParameter.getSession());
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				SapceRemarkBean remarkBean = MySpaceUtils.addSapceLogRemark(compParameter, sapceLog, content,
						ConvertUtils.toBoolean(compParameter.getRequestParameter("cb_space_log_opt1"), false));
				if (remarkBean != null) {
					if (account != null && !sapceLog.getUserId().equals2(remarkBean.getUserId())) {
						final StringBuffer textBody = new StringBuffer();
						textBody.append(content).append("<br/>");
						textBody.append("<a href='/space.html?logId=");
						textBody.append(sapceLog.getId()).append("&logRemarkId=").append(remarkBean.getId()).append("'>");
						textBody.append("查看评论").append("</a>");
						//						NotificationUtils.createSystemMessageNotification(account.getId(), sapceLog.getUserId(), account.user().getText()
						//								+ "评论你的动态...", textBody.toString(), remarkBean.getId());
						MessageUtils.createNotifation(compParameter, account.user().getText() + "评论你的动态...", textBody.toString(), account.getId(),
								sapceLog.getUserId());
					}
					json.put("result", Boolean.TRUE);
				}
			}
		});
	}

	public IForward logRemarkDelete(final ComponentParameter compParameter) {
		final String remarkid = compParameter.getRequestParameter("remarkid");
		if (!StringUtils.hasText(remarkid)) {
			return null;
		}
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				json.put("result", MySpaceUtils.deleteSapceLogRemark(compParameter, remarkid) > 0);
			}
		});
	}

	public IForward imgDelete(final ComponentParameter compParameter) {
		final int code = ConvertUtils.toInt(compParameter.getRequestParameter("code"), 0);
		if (code == 0) {
			return null;
		}
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final File[] files = MySpaceUtils.getUploadDir(compParameter.getSession()).listFiles();
				if (files != null) {
					for (final File file : files) {
						if (file.hashCode() == code) {
							if (file.delete()) {
								json.put("result", Boolean.TRUE);
							}
							break;
						}
					}
				}
			}
		});
	}

	public IForward logImageLoaded(final ComponentParameter compParameter) {
		final String resourceId = compParameter.getRequestParameter("resourceId");
		if (!StringUtils.hasText(resourceId)) {
			return null;
		}
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final FilePathWrapper fp = MySpaceUtils.getFilePathWrapper(compParameter);
				final SapceResourceBean resourceBean = MySpaceUtils.getBeanById(resourceId, SapceResourceBean.class);
				if (resourceBean != null) {
					final ELogResource lr = resourceBean.getLogResource();
					if (lr == ELogResource.img_upload) {
						json.put("src", fp.getImagePath(compParameter, MySpaceUtils.createInputStreamAware(resourceBean), 0, 0));
					} else if (lr == ELogResource.img_url) {
						json.put("src", resourceBean.getResourceUrl());
					}
				}
			}
		});
	}

	public IForward imgUrlAdd(final ComponentParameter compParameter) {
		final String url = compParameter.getRequestParameter("url");
		if (!StringUtils.hasText(url)) {
			return null;
		}
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				MySpaceUtils.getSessionUrls(compParameter.getSession()).add(url);
				json.put("url", url);
			}
		});
	}

	public IForward imgUrlDelete(final ComponentParameter compParameter) {
		final String url = compParameter.getRequestParameter("url");
		if (!StringUtils.hasText(url)) {
			return null;
		}
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				if (MySpaceUtils.getSessionUrls(compParameter.getSession()).remove(new String(StringUtils.decodeHex(url)))) {
					json.put("result", Boolean.TRUE);
				}
			}
		});
	}
}
