package net.simpleframework.content.component.topicpager;

import java.util.HashMap;
import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.content.ContentUtils;
import net.simpleframework.content.component.vote.Vote;
import net.simpleframework.content.component.vote.VoteUtils;
import net.simpleframework.organization.IJob;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.JavascriptUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.AbstractUrlForward;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;
import net.simpleframework.web.page.component.ui.pager.db.AbstractDbTablePagerAction;
import net.simpleframework.web.page.component.ui.validatecode.DefaultValidateCodeHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PostsAction extends AbstractAjaxRequestHandle {

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("selector".equals(beanProperty)) {
			final String componentName = (String) compParameter.getBeanProperty("name");
			if ("ajaxFastReplyTopicSave".equals(componentName)) {
				return "#__pager_postsId form, .tp_view .fr";
			} else if ("ajaxReplyTopicSave".equals(componentName)) {
				return "#__pager_postsId form, #topicPropEditor";
			} else {
				return "#__pager_postsId form";
			}
		} else if ("jobExecute".equals(beanProperty)) {
			final String componentName = (String) compParameter.getBeanProperty("name");
			final ComponentParameter nComponentParameter = TopicPagerUtils
					.getComponentParameter(compParameter);
			if ("ajaxTopicPagerReply".equals(componentName)
					|| "ajaxReplyTopicSave".equals(componentName)
					|| "ajaxFastReplyTopicSave".equals(componentName)) {
				return nComponentParameter.getBeanProperty("jobAdd");
			} else if ("ajaxPostMove".equals(componentName)) {
				return nComponentParameter.getBeanProperty("jobExchange");
			} else if ("ajaxPostDelete".equals(componentName)) {
				return nComponentParameter.getBeanProperty("jobDelete");
			} else if ("ajaxTopicAttention".equals(componentName)) {
				return IJob.sj_account_normal;
			}
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	public IForward replyTopic(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = TopicPagerUtils
				.getComponentParameter(compParameter);
		final ITopicPagerHandle tHandle = (ITopicPagerHandle) nComponentParameter
				.getComponentHandle();
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				if ((Boolean) nComponentParameter.getBeanProperty("showValidateCode")
						&& !DefaultValidateCodeHandle.isValidateCode(compParameter.request,
								"textTopicEditorValidateCode")) {
					json.put("validateCode", DefaultValidateCodeHandle.getErrorString());
				} else {
					final Map<String, Object> data = new HashMap<String, Object>();
					TopicPagerAction.putDefaultData(data, compParameter.request);
					data.put("topicId", compParameter.getRequestParameter(tHandle
							.getIdParameterName(nComponentParameter)));
					final String postId = compParameter.getRequestParameter("postId");
					if (StringUtils.hasText(postId)) {
						tHandle.doEdit(nComponentParameter, postId, data, PostsBean.class);
					} else {
						data.put("quoteId", compParameter.getRequestParameter("quoteId"));
						tHandle.doAdd(nComponentParameter, data, PostsBean.class);
					}
				}
			}
		});
	}

	public IForward fastReplyTopic(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = TopicPagerUtils
				.getComponentParameter(compParameter);
		final ITopicPagerHandle tHandle = (ITopicPagerHandle) nComponentParameter
				.getComponentHandle();
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				if ((Boolean) nComponentParameter.getBeanProperty("showValidateCode")
						&& !DefaultValidateCodeHandle.isValidateCode(compParameter.request,
								"textFastReplyValidateCode")) {
					json.put("validateCode", DefaultValidateCodeHandle.getErrorString());
				} else {
					final Map<String, Object> data = new HashMap<String, Object>();
					data.put("topic", compParameter.getRequestParameter("fastReplyTopic"));
					data.put("content", ContentUtils.doTextContent(compParameter
							.getRequestParameter("fastReplyContent")));
					data.put("topicId", compParameter.getRequestParameter(tHandle
							.getIdParameterName(nComponentParameter)));
					tHandle.doAdd(nComponentParameter, data, PostsBean.class);
				}
			}
		});
	}

	public IForward postDelete(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = TopicPagerUtils
				.getComponentParameter(compParameter);
		final ITopicPagerHandle tHandle = (ITopicPagerHandle) nComponentParameter
				.getComponentHandle();
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final ExpressionValue ev = AbstractDbTablePagerAction.getDeleteExpressionValue(
						nComponentParameter, "postId");
				if (ev != null) {
					tHandle.doDelete(nComponentParameter, ev, PostsBean.class);
				}
			}
		});
	}

	public IForward postExchange(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = TopicPagerUtils
				.getComponentParameter(compParameter);
		final ITopicPagerHandle tHandle = (ITopicPagerHandle) nComponentParameter
				.getComponentHandle();
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final Object postId = compParameter.getRequestParameter("postId");
				final Object postId2 = compParameter.getRequestParameter("postId2");
				final boolean up = ConvertUtils.toBoolean(compParameter.getRequestParameter("up"),
						false);
				tHandle.doExchange(nComponentParameter, postId, postId2, !up, PostsBean.class);
			}
		});
	}

	public IForward topicAttention(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = TopicPagerUtils
				.getComponentParameter(compParameter);
		final ITopicPagerHandle tHandle = (ITopicPagerHandle) nComponentParameter
				.getComponentHandle();
		final TopicBean topic = tHandle.getEntityBeanByRequest(compParameter);
		if (topic == null) {
			return null;
		}
		tHandle.contentAttention(nComponentParameter, topic,
				ConvertUtils.toBoolean(compParameter.getRequestParameter("delete"), false));
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final StringBuilder js = new StringBuilder();
				js.append("$(\"act_").append(topic.getId()).append("\").update(\"");
				js.append(JavascriptUtils.escape(tHandle.getActionsHTML(compParameter, topic))).append(
						"\");");
				json.put("jsCallback", js.toString());
			}
		});
	}

	public IForward voteDelete(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = TopicPagerUtils
				.getComponentParameter(compParameter);
		final ITopicPagerHandle tHandle = (ITopicPagerHandle) nComponentParameter
				.getComponentHandle();
		final TopicBean topic = tHandle.getEntityBeanByRequest(compParameter);
		if (topic == null) {
			return null;
		}
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final Vote vote = TopicPagerUtils.getVote(nComponentParameter, topic.getId());
				if (vote != null) {
					final ITableEntityManager temgr = tHandle.getTableEntityManager(nComponentParameter,
							Vote.class);
					final SQLValue[] sqlValues = VoteUtils.getDeleteSQLs(nComponentParameter,
							Vote.class, new Object[] { vote.getId() });
					for (final SQLValue sqlValue : sqlValues) {
						temgr.execute(sqlValue);
					}
					topic.setMark((short) (topic.getMark() & (TopicBean.MARK_VOTE ^ 0xFF)));
					tHandle.getTableEntityManager(nComponentParameter).update(new String[] { "mark" },
							topic);
				}
			}
		});
	}

	public IForward replyTopicUrl(final ComponentParameter compParameter) {
		return AbstractUrlForward.componentUrl(TopicPagerRegistry.topicPager, "/jsp/topic_reply.jsp");
	}
}
