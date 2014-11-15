package net.simpleframework.content.component.topicpager;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.simpleframework.content.EContentStatus;
import net.simpleframework.content.EContentType;
import net.simpleframework.content.bbs.BbsTopic;
import net.simpleframework.content.bbs.BbsUtils;
import net.simpleframework.my.message.MessageUtils;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.HTMLUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.AbstractUrlForward;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.db.AbstractDbTablePagerAction;
import net.simpleframework.web.page.component.ui.validatecode.DefaultValidateCodeHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TopicPagerAction extends AbstractDbTablePagerAction {
	@Override
	protected ComponentParameter getComponentParameter(final PageRequestResponse requestResponse) {
		return TopicPagerUtils.getComponentParameter(requestResponse);
	}

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("selector".equals(beanProperty)) {
			final String componentName = (String) compParameter.getBeanProperty("name");
			final ComponentParameter nComponentParameter = getComponentParameter(compParameter);
			final String selector = (String) nComponentParameter.getBeanProperty("selector");
			if ("ajaxSaveTopic".equals(componentName)) {
				return selector + ", #topicPropEditor";
			} else if ("ajaxSaveTopic2".equals(componentName)) {
				return selector + ", .tp_edit2";
			}
		} else if ("jobExecute".equals(beanProperty)) {
			final String componentName = (String) compParameter.getBeanProperty("name");
			final ComponentParameter nComponentParameter = getComponentParameter(compParameter);
			if ("ajaxTopicPagerAdd".equals(componentName) || "ajaxSaveTopic".equals(componentName)) {
				return nComponentParameter.getBeanProperty("jobAdd");
			} else if ("topicPagerMove".equals(componentName)) {
				return nComponentParameter.getBeanProperty("jobExchange");
			} else if ("ajaxTopicUpdate".equals(componentName) || "ajaxSaveTopic2".equals(componentName)) {
				return nComponentParameter.getBeanProperty("jobEdit");
			} else {
				final ITopicPagerHandle tHandle = (ITopicPagerHandle) nComponentParameter.getComponentHandle();
				if ("ajaxTopicMove2".equals(componentName)) {
					return tHandle.getManager(nComponentParameter);
				} else {
					final TopicBean topicBean = tHandle.getEntityBeanByRequest(nComponentParameter);
					if (topicBean != null && AccountSession.isAccount(compParameter.getSession(), topicBean.getUserId())) {
						return IJob.sj_account_normal;
					} else {
						if ("ajaxTopicPagerEdit".equals(componentName)) {
							return nComponentParameter.getBeanProperty("jobEdit");
						} else if ("topicPagerDelete".equals(componentName)) {
							return nComponentParameter.getBeanProperty("jobDelete");
						}
					}
				}
			}
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	public IForward saveTopic(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = getComponentParameter(compParameter);
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				if ((Boolean) nComponentParameter.getBeanProperty("showValidateCode")
						&& !DefaultValidateCodeHandle.isValidateCode(compParameter.request, "textTopicEditorValidateCode")) {
					json.put("validateCode", DefaultValidateCodeHandle.getErrorString());
				} else {
					final Map<String, Object> data = new HashMap<String, Object>();
					putDefaultData(data, compParameter.request);
					final ITopicPagerHandle tHandle = (ITopicPagerHandle) nComponentParameter.getComponentHandle();
					final String topicId = nComponentParameter.getRequestParameter(tHandle.getIdParameterName(nComponentParameter));
					final String jsCallback;
					if (StringUtils.hasText(topicId)) {
						tHandle.doEdit(nComponentParameter, topicId, data);
						jsCallback = tHandle.getJavascriptCallback(nComponentParameter, "edit",
								tHandle.getEntityBeanById(nComponentParameter, topicId));
					} else {
						final TopicBean topic = tHandle.doAdd(nComponentParameter, data);
						jsCallback = tHandle.getJavascriptCallback(nComponentParameter, "add", topic);
					}
					if (StringUtils.hasText(jsCallback)) {
						json.put("jsCallback", jsCallback);
					}
				}
			}
		});
	}

	static void putDefaultData(final Map<String, Object> data, final HttpServletRequest request) {
		data.put("topic", HTMLUtils.htmlEscape(request.getParameter("tp_topic")));
		data.put("keywords", HTMLUtils.htmlEscape(request.getParameter("tp_keywords")));
		data.put("content", request.getParameter("tp_content"));
		data.put("attention", request.getParameter("tp_attention"));
		data.put("vote", request.getParameter("tp_vote"));
		data.put("att1", request.getParameter("tp_att1"));
		data.put("att2", request.getParameter("tp_att2"));
		data.put("att3", request.getParameter("tp_att3"));
	}

	public IForward saveTopic2(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = getComponentParameter(compParameter);
		final ITopicPagerHandle tHandle = (ITopicPagerHandle) nComponentParameter.getComponentHandle();
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final Map<String, Object> data = new HashMap<String, Object>();
				if (ConvertUtils.toBoolean(compParameter.getRequestParameter("top"), false)) {
					data.put("top", Boolean.TRUE);
				} else {
					data.put("star", ConvertUtils.toShort(compParameter.getRequestParameter("tp_star"), (short) 0));
					data.put("ttype", ConvertUtils.toEnum(EContentType.class, compParameter.getRequestParameter("tp_type")));
					data.put("status", ConvertUtils.toEnum(EContentStatus.class, compParameter.getRequestParameter("tp_status")));
				}
				final TopicBean topic = tHandle.doEdit(nComponentParameter,
						compParameter.getRequestParameter(tHandle.getIdParameterName(nComponentParameter)), data);

				try {
					if (topic.getTtype() == EContentType.normal
							&& ConvertUtils.toEnum(EContentType.class, compParameter.getRequestParameter("tp_type")) == EContentType.recommended) {
						MessageUtils.createNotifation(compParameter, "你的发布的帖子被推荐",
								BbsUtils.applicationModule.getPostUrl(compParameter, (BbsTopic) topic), OrgUtils.um().getUserByName("admin").getId(),
								topic.getUserId());
					} else if (topic.getStar() == 0 && ConvertUtils.toShort(compParameter.getRequestParameter("tp_star"), (short) 0) > 0) {
						MessageUtils.createNotifation(compParameter, "你的发布的帖子被加精",
								BbsUtils.applicationModule.getPostUrl(compParameter, (BbsTopic) topic), OrgUtils.um().getUserByName("admin").getId(),
								topic.getUserId());
					}
				} catch (Exception e) {
				}
				final String jsCallback = tHandle.getJavascriptCallback(nComponentParameter, "edit", topic);
				if (StringUtils.hasText(jsCallback)) {
					json.put("jsCallback", jsCallback);
				}
			}
		});
	}

	public IForward move2(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = getComponentParameter(compParameter);
		final ITopicPagerHandle tHandle = (ITopicPagerHandle) nComponentParameter.getComponentHandle();
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final String topicId = compParameter.getRequestParameter(tHandle.getIdParameterName(nComponentParameter));
				final Map<String, Object> data = new HashMap<String, Object>();
				final String catalog = compParameter.getRequestParameter("mCatalog");
				if (StringUtils.hasText(catalog)) {
					data.put("catalog", catalog);
				}

				tHandle.doEdit(nComponentParameter, topicId, data);
				final String jsCallback = tHandle.getJavascriptCallback(nComponentParameter, "move2", null);
				if (StringUtils.hasText(jsCallback)) {
					json.put("jsCallback", jsCallback);
				}
			}
		});
	}

	public IForward topicEditUrl(final ComponentParameter compParameter) {
		return AbstractUrlForward.componentUrl(TopicPagerRegistry.topicPager, "/jsp/topic_edit.jsp");
	}

	public IForward topicEdit2Url(final ComponentParameter compParameter) {
		return AbstractUrlForward.componentUrl(TopicPagerRegistry.topicPager, "/jsp/topic_edit2.jsp");
	}
}
