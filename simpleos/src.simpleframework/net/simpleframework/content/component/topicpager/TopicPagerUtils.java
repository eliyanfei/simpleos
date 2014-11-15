package net.simpleframework.content.component.topicpager;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.lucene.AbstractLuceneManager;
import net.simpleframework.ado.lucene.LuceneQuery;
import net.simpleframework.content.ContentUtils;
import net.simpleframework.content.bbs.BbsUser;
import net.simpleframework.content.bbs.BbsUtils;
import net.simpleframework.content.component.vote.IVoteHandle;
import net.simpleframework.content.component.vote.Vote;
import net.simpleframework.content.component.vote.VoteBean;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.organization.account.IGetAccountAware;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.HTMLUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ado.IDbComponentHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class TopicPagerUtils {
	public static final String BEAN_ID = "topic_@bid";

	public static ComponentParameter getComponentParameter(final PageRequestResponse requestResponse) {
		return ComponentParameter.get(requestResponse, BEAN_ID);
	}

	public static ComponentParameter getComponentParameter(final HttpServletRequest request, final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}

	public static String getAllTopicContent(final ComponentParameter compParameter, final TopicBean topicBean) {
		return getAllTopicContent(compParameter, topicBean, null);
	}

	public static String getAllTopicContent(final ComponentParameter compParameter, final TopicBean topicBean, final PostsTextBean excludePostText) {
		final StringBuilder sb = new StringBuilder();
		final ITopicPagerHandle tHandle = (ITopicPagerHandle) compParameter.getComponentHandle();
		final ExpressionValue ev = new ExpressionValue("id in (select id from "
				+ tHandle.getTableEntityManager(compParameter, PostsBean.class).getTablename() + " where topicid=?)",
				new Object[] { topicBean.getId() });
		final IDataObjectQuery<PostsTextBean> doq = tHandle.getTableEntityManager(compParameter, PostsTextBean.class).query(ev, PostsTextBean.class);
		PostsTextBean pt;
		while ((pt = doq.next()) != null) {
			if (excludePostText != null && excludePostText.equals2(pt)) {
				continue;
			}
			sb.append(pt.getContent());
		}
		return sb.toString();
	}

	// topic_view_pager.jsp
	public static String getPostUserHTML(final ComponentParameter compParameter, final PostsBean postsBean) {
		final StringBuilder sb = new StringBuilder();
		final IUser user = OrgUtils.um().queryForObjectById(postsBean.getUserId());
		if (user == null) {
			return sb.toString();
		}
		final ITopicPagerHandle tHandle = (ITopicPagerHandle) compParameter.getComponentHandle();
		final String userIdParameterName = OrgUtils.um().getUserIdParameterName();
		sb.append("<table style=\"width: 100%;\" cellpadding=\"1\" align='center'>");
		// line 2
		sb.append("<tr><td align='center'>");
		sb.append("<img class=\"photo_icon\" src=\"").append(OrgUtils.getPhotoSRC(compParameter.request, user, 96, 96)).append("\" /></td></tr>");
		// line 3
		sb.append("<tr><td align='center'>");
		final Map<String, Object> propertiesEx = tHandle.getUserViewPropertiesEx(compParameter, postsBean);
		final BbsUser bbsUser = BbsUtils.getBbsUser(postsBean.getUserId());

		if (propertiesEx != null) {
			if (bbsUser != null) {
				sb.append("<table class='stat' style=\"\" cellpadding=\"1\">");
				sb.append("<tr>");
				sb.append("<td class=\"fc\" align='center'>").append("<div class='l1'>").append(bbsUser.getTopics()).append("</div>");
				sb.append("<div class=\"l2\">").append("主题").append("</td>");
				sb.append("<td align='center'>").append("<div class='l1'>").append(bbsUser.getMessages()).append("</div>");
				sb.append("<div class=\"l2\">").append("消息").append("</td>");
				sb.append("</tr>");
				sb.append("</table>");
			}
		}
		sb.append("</td></tr>");
		sb.append("<tr><td align='center'>");
		sb.append("<a style=\"font-weight: bold;margin-right: 3px;\" userId=\"").append(user.getId()).append("\">");
		sb.append(user.getText()).append("</a>");
		sb.append("<span class=\"right_down_menu\"></span>");
		final String blogUrl = tHandle.getBlogUrl(compParameter, user);
		if (StringUtils.hasText(blogUrl)) {
			sb.append("<input type=\"hidden\" value=\"");
			sb.append(HTMLUtils.htmlEscape(blogUrl)).append("\" />");
		}
		final String userId = compParameter.getRequestParameter(userIdParameterName);
		if (StringUtils.hasText(userId)) {
			sb.append("<span style=\"margin-left: 10px;\">(&nbsp;");
			sb.append("<a onclick=\"POST_UTILS.alluser(this);\">");
			sb.append("#(topic_view_pager.4)</a></span>&nbsp;)");
		}
		sb.append("</td></tr>");
		sb.append("</table>");
		return sb.toString();
	}

	public static String getPostTextHTML(final ComponentParameter compParameter, final TopicBean topic, final PostsBean postsBean,
			final PostsTextBean postText) {
		final StringBuilder sb = new StringBuilder();
		final ITopicPagerHandle tHandle = (ITopicPagerHandle) compParameter.getComponentHandle();
		final PostsBean quotePost = tHandle.getEntityBeanById(compParameter, postsBean.getQuoteId(), PostsBean.class);
		if (quotePost != null) {
			sb.append("<blockquote>");
			sb.append("<div class=\"inherit_c wrap_text\">").append(tHandle.getPostsText(compParameter, quotePost).getContent()).append("</div>");
			sb.append("<div class=\"gray-color\" style=\"padding-top: 6px;\">").append(quotePost.getUserText()).append(", ")
					.append(ConvertUtils.toDateString(quotePost.getCreateDate())).append("</div>");
			sb.append("</blockquote>");
		}

		sb.append("<div class=\"inherit_c wrap_text\">");
		if (postText != null) {
			sb.append(postText.getContent());
		}
		sb.append("</div>");
		if (postsBean.isFirstPost()) {
			final VoteBean voteBean = getVoteBean(compParameter);
			if (voteBean != null) {
				final ComponentParameter vComponentParameter = ComponentParameter.get(compParameter, voteBean);
				final IVoteHandle vHandle = (IVoteHandle) vComponentParameter.getComponentHandle();
				final Vote vote = vHandle.getVoteByDocumentId(vComponentParameter, topic.getId());
				if (vote != null) {
					sb.append("<div id=\"__topic_voteId\"></div>");
					if (vHandle.getItemGroups(vComponentParameter, vote).getCount() == 0) {
						final IAccount login = AccountSession.getLogin(compParameter.getSession());
						if (login != null && (login.getId().equals2(postsBean.getUserId()) || IDbComponentHandle.Utils.isManager(compParameter))) {
							sb.append("<a onclick=\"$Actions['ajaxPostVoteDelete']();\">#(topic_edit.8)</a>");
						}
					}
				}
			}
		}
		return sb.toString();
	}

	static VoteBean getVoteBean(final PageRequestResponse requestResponse) {
		return (VoteBean) AbstractComponentBean.getComponentBeanByName(requestResponse, getHomePath() + "/jsp/topic_view_pager.xml", "topicVote");
	}

	public static Vote getVote(final ComponentParameter compParameter, final Object topicId) {
		final ComponentParameter nComponentParameter = ComponentParameter.get(compParameter, getVoteBean(compParameter));
		return nComponentParameter.componentBean != null ? ((IVoteHandle) nComponentParameter.getComponentHandle()).getVoteByDocumentId(
				nComponentParameter, topicId) : null;
	}

	public static String getFastReplyLeft(final ComponentParameter compParameter, final TopicBean topic) throws IOException {
		final StringBuilder sb = new StringBuilder();
		final ITopicPagerHandle tHandle = (ITopicPagerHandle) compParameter.getComponentHandle();
		final TopicLuceneManager tlmgr = (TopicLuceneManager) tHandle.createLuceneManager(compParameter);
		sb.append("<div class=\"lbl\">#(topic_view.3)</div>");
		String qt = StringUtils.blank(topic.getTopic());
		if (qt.length() < AbstractLuceneManager.QUERY_MIN_LENGTH) {
			final PostsTextBean postText = tHandle.getPostsText(compParameter, topic);
			if (postText != null) {
				qt += " " + ContentUtils.getShortContent(postText, 50, false);
			}
		}
		final LuceneQuery<?> lq = tlmgr.getLuceneQuery(StringUtils.join(tlmgr.getQueryTokens(qt), " "));
		if (lq == null) {
			return "";
		}
		lq.setCount(8);
		Object obj;

		sb.append("<table class=\"fixed_table\" style=\"width: 100%;\">");
		while ((obj = lq.next()) != null) {
			final TopicBean topic2 = (TopicBean) obj;
			if (topic.equals(topic2)) {
				continue;
			}
			sb.append("<tr>");
			sb.append("<td width=\"18px;\" valign=\"top\"><span class=\"dot1_image\"></span></td>");
			sb.append("<td class=\"wrap_text\">").append("<a href=\"").append(tHandle.getPostViewUrl(compParameter, topic2)).append("\">")
					.append(topic2.getTopic()).append("</a><div style=\"padding-top: 2px;\" class=\"gray-color\">");
			final IGetAccountAware accountAware = tHandle.getAccountAware();
			if (accountAware != null) {
				sb.append(accountAware.wrapAccountHref(compParameter, topic2.getUserId(), topic2.getUserText()));
			} else {
				sb.append(topic2.getUserText());
			}
			sb.append(" , ").append(ConvertUtils.toDateString(topic2.getCreateDate(), "yyyy-MM-dd")).append("</div>").append("</td>");
			sb.append("<td width=\"40px;\" align=\"right\"><span class=\"nnum\">").append(topic2.getReplies()).append("/").append(topic2.getViews())
					.append("</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		return sb.toString();
	}

	public static String getFastReplyRight(final ComponentParameter compParameter, final TopicBean topic) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"te1\">");
		sb.append("<input type=\"text\" id=\"fastReplyTopic\" name=\"fastReplyTopic\" ");
		sb.append("value=\"#(TopicEditPageLoad.0)").append(HTMLUtils.htmlEscape(topic.getTopic())).append("\" />");
		sb.append("</div>");

		sb.append("<div class=\"te2\">");
		sb.append("<textarea id=\"fastReplyContent\" name=\"fastReplyContent\" rows=\"8\">");
		sb.append("</textarea></div>");

		sb.append("<table style=\"width: 100%;\" cellpadding=\"0\" cellspacing=\"0\"><tr>");
		sb.append("<td valign=\"top\"><div class=\"ins\">");
		sb.append("<a class=\"emotion\" onclick=\"$Actions['smileyTopicPagerDict']();\">#(topic_view.4)</a>");
		sb.append("</div></td>");
		sb.append("<td width=\"80\" align=\"right\"><span class=\"btn\" ");
		sb.append("onclick=\"$Actions['ajaxFastReplyTopicSave']();\">#(Button.Ok)</span></td>");
		sb.append("</tr></table>");

		sb.append("<div style=\"padding: 4px 8px;\">");
		sb.append("<table cellpadding=\"1\"><tr>");
		//		if ((Boolean) compParameter.getBeanProperty("showValidateCode")) {
		//			sb.append("<td id=\"__pager_postsValidateCode\"></td>");
		//		}
		//		sb.append("<td align=\"right\" valign=\"middle\"><a ");
		//		sb.append("onclick=\"$Actions['topicPagerReplyWindow']();\">#(topic_view.1)</a></td>");
		sb.append("</tr></table></div>");
		return sb.toString();
	}

	public static String getHomePath() {
		return AbstractComponentRegistry.getRegistry(TopicPagerRegistry.topicPager).getResourceHomePath();
	}

	public static String getCssPath(final PageRequestResponse requestResponse) {
		return AbstractComponentRegistry.getRegistry(TopicPagerRegistry.topicPager).getCssResourceHomePath(requestResponse);
	}
}
