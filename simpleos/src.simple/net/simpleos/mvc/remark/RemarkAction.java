package net.simpleos.mvc.remark;

import java.util.Date;
import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.content.bbs.BbsTopic;
import net.simpleframework.content.bbs.BbsUtils;
import net.simpleframework.content.blog.Blog;
import net.simpleframework.content.blog.BlogRemark;
import net.simpleframework.content.blog.BlogUtils;
import net.simpleframework.content.component.topicpager.PostsBean;
import net.simpleframework.content.component.topicpager.PostsTextBean;
import net.simpleframework.content.news.News;
import net.simpleframework.content.news.NewsRemark;
import net.simpleframework.content.news.NewsUtils;
import net.simpleframework.core.id.LongID;
import net.simpleframework.my.message.EMessageType;
import net.simpleframework.my.message.MessageUtils;
import net.simpleframework.my.message.SimpleMessage;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;
import net.simpleos.SimpleosUtil;

/**
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-16上午09:14:06
 */
public class RemarkAction extends AbstractAjaxRequestHandle {
	/**
	 * 删除选择的消息
	 * @param compParameter
	 * @return
	 */
	public IForward remarkDelete(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				final IAccount account = SimpleosUtil.getLoginAccount(compParameter);
				if (account == null)
					return;
				if (SimpleosUtil.isManage(compParameter)) {
					final String rtype = compParameter.getParameter("rtype");
					final String raction = compParameter.getParameter("raction");
					final String remarkId = compParameter.getParameter("remarkId");
					final String rd_content = compParameter.getParameter("rd_content");
					if ("News".equals(rtype)) {
						final NewsRemark remark = NewsUtils.getTableEntityManager(NewsRemark.class).queryForObjectById(remarkId, NewsRemark.class);
						if (remark != null) {
							final News news = NewsUtils.getTableEntityManager(News.class).queryForObjectById(remark.getDocumentId(), News.class);
							sendSimpleMessage(compParameter, remark.getUserId(), remark.getContent(), rd_content,
									NewsUtils.applicationModule.getViewUrl(compParameter, news), news.getTopic());
							NewsUtils.getTableEntityManager(NewsRemark.class).delete(new ExpressionValue("id=" + remarkId));
						}
					} else if ("Blog".equals(rtype)) {
						final BlogRemark remark = BlogUtils.getTableEntityManager(BlogRemark.class).queryForObjectById(remarkId, BlogRemark.class);
						if (remark != null) {
							final Blog blog = BlogUtils.getTableEntityManager(Blog.class).queryForObjectById(remark.getDocumentId(), Blog.class);
							sendSimpleMessage(compParameter, remark.getUserId(), remark.getContent(), rd_content,
									BlogUtils.applicationModule.getBlogViewUrl(compParameter, blog), blog.getTopic());
							BlogUtils.getTableEntityManager(BlogRemark.class).delete(new ExpressionValue("id=" + remarkId));
						}
					} else if ("Bbs".equals(rtype)) {
						final PostsTextBean postsTextBean = BbsUtils.getTableEntityManager(PostsTextBean.class).queryForObjectById(remarkId,
								PostsTextBean.class);
						final PostsBean postsBean = BbsUtils.getTableEntityManager(PostsBean.class).queryForObjectById(remarkId, PostsBean.class);
						if (postsBean != null) {
							final BbsTopic topic = BbsUtils.getTableEntityManager(BbsTopic.class).queryForObjectById(postsBean.getTopicId(),
									BbsTopic.class);
							sendSimpleMessage(compParameter, postsBean.getUserId(), postsTextBean.getContent(), rd_content,
									BbsUtils.applicationModule.getPostUrl(compParameter, topic), topic.getTopic());
							BbsUtils.getTableEntityManager(PostsBean.class).delete(new ExpressionValue("id=" + remarkId));
							BbsUtils.getTableEntityManager(PostsTextBean.class).delete(new ExpressionValue("id=" + remarkId));
						}
					}
					json.put("action", raction);
				}
			}
		});
	}

	private void sendSimpleMessage(final ComponentParameter compParameter, final Object userId, final String textBody, String rd_content,
			String href, String topic) {
		final ITableEntityManager temgr = MessageUtils.getTableEntityManager(SimpleMessage.class);
		final SimpleMessage sMessage = new SimpleMessage();
		sMessage.setMessageType(EMessageType.notification);
		sMessage.setSentId(OrgUtils.am().getAccountByName("admin").getId());//系统管理员
		sMessage.setSubject("删除评论");
		sMessage.setSentDate(new Date());
		final StringBuffer buf = new StringBuffer();
		buf.append("<a href='").append(href).append("' target='blank'>").append(topic).append("</a><br/>");
		buf.append("<blockquote>评论内容<br/>").append(textBody).append("</blockquote>");
		buf.append("<blockquote>删除原因<br/>").append(rd_content).append("</blockquote>");
		sMessage.setTextBody(buf.toString());
		sMessage.setToId(new LongID(userId));
		temgr.insertTransaction(sMessage, new TableEntityAdapter() {
			@Override
			public void afterInsert(final ITableEntityManager manager, final Object[] objects) {
			}
		});
	}
}
