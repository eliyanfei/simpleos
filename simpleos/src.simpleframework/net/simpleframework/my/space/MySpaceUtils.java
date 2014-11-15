package net.simpleframework.my.space;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import net.a.ItSiteUtil;
import net.simpleframework.ado.DataObjectManagerUtils;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.content.ContentUtils;
import net.simpleframework.content.IContentApplicationModule;
import net.simpleframework.content.ResourceLobBean;
import net.simpleframework.content.bbs.BbsTopic;
import net.simpleframework.content.bbs.BbsUtils;
import net.simpleframework.content.blog.Blog;
import net.simpleframework.content.blog.BlogRemark;
import net.simpleframework.content.blog.BlogUtils;
import net.simpleframework.content.component.newspager.INewsPagerHandle;
import net.simpleframework.content.component.newspager.NewsBean;
import net.simpleframework.content.component.topicpager.ETopicQuery;
import net.simpleframework.content.component.topicpager.ITopicPagerHandle;
import net.simpleframework.content.component.topicpager.PostsBean;
import net.simpleframework.content.news.News;
import net.simpleframework.content.news.NewsRemark;
import net.simpleframework.content.news.NewsUtils;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.organization.account.IGetAccountAware;
import net.simpleframework.util.DateUtils;
import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.util.HTMLUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.FilePathWrapper;
import net.simpleframework.web.IWebApplicationModule;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.dictionary.SmileyUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class MySpaceUtils {
	public static ISpaceApplicationModule applicationModule;

	public static String deployPath;

	public static String getCssPath(final PageRequestResponse requestResponse) {
		final StringBuilder sb = new StringBuilder();
		sb.append(deployPath).append("css/").append(applicationModule.getSkin(requestResponse));
		return sb.toString();
	}

	public static String getSexText(PageRequestResponse requestResponse, String sex) {
		final String userid = requestResponse.getRequestParameter("userId");
		final IGetAccountAware accountAware = MySpaceUtils.getAccountAware();
		final boolean isMe = !StringUtils.hasText(userid) && accountAware.isMyAccount(requestResponse);
		final String text = (isMe ? "我" : ("男".equals(sex) ? "他" : "她"));
		return text;
	}

	public static ITableEntityManager getTableEntityManager(final Class<?> beanClazz) {
		return DataObjectManagerUtils.getTableEntityManager(applicationModule, beanClazz);
	}

	public static ITableEntityManager getTableEntityManager() {
		return DataObjectManagerUtils.getTableEntityManager(applicationModule);
	}

	public static IGetAccountAware getAccountAware() {
		return (IGetAccountAware) applicationModule;
	}

	public static <T extends IDataObjectBean> T getBeanById(final Object id, final Class<T> beanClazz) {
		return applicationModule.getBeanById(id, beanClazz);
	}

	public static int deleteSapceLog(final PageRequestResponse requestResponse, final Object id) {
		return applicationModule.deleteSapceLog(requestResponse, id);
	}

	public static SapceLogBean addSapceLog(final PageRequestResponse requestResponse, final String content) {
		return applicationModule.addSapceLog(requestResponse, content, null, null, true);
	}

	public static SapceLogBean addSapceLog(final PageRequestResponse requestResponse, final String content, final EFunctionModule refModule,
			final ID refId) {
		return applicationModule.addSapceLog(requestResponse, content, refModule, refId, false);
	}

	public static SapceRemarkBean addSapceLogRemark(final PageRequestResponse requestResponse, final SapceLogBean sapceLog, final String content,
			final boolean opt1) {
		return applicationModule.addSapceLogRemark(requestResponse, sapceLog, content, opt1);
	}

	public static int deleteSapceLogRemark(final PageRequestResponse requestResponse, final Object remarkid) {
		return applicationModule.deleteSapceLogRemark(requestResponse, remarkid);
	}

	public static UserAttentionBean getUserAttentionById(final PageRequestResponse requestResponse, final Object attentionId) {
		return applicationModule.getUserAttentionById(requestResponse, attentionId);
	}

	public static void addUserAttention(final PageRequestResponse requestResponse, final Object attentionId) {
		applicationModule.addUserAttention(requestResponse, attentionId);
	}

	public static void deleteUserAttention(final PageRequestResponse requestResponse, final Object attentionId) {
		applicationModule.deleteUserAttention(requestResponse, attentionId);
	}

	public static SpaceStatBean getSpaceStat(final PageRequestResponse requestResponse) {
		final IAccount account = getAccountAware().getAccount(requestResponse);
		if (account == null) {
			return null;
		}
		return getSpaceStatById(account.getId());
	}

	public static SpaceStatBean getSpaceStatById(final ID userId) {
		final ITableEntityManager stat_mgr = getTableEntityManager(SpaceStatBean.class);
		SpaceStatBean stat = stat_mgr.queryForObjectById(userId, SpaceStatBean.class);
		if (stat == null) {
			stat = new SpaceStatBean();
			stat.setId(userId);
			stat_mgr.insert(stat);
		}
		return stat;
	}

	public static void updateViews(final PageRequestResponse requestResponse) {
		WebUtils.updateViews(requestResponse.getSession(), getTableEntityManager(SpaceStatBean.class), getSpaceStat(requestResponse));
	}

	static void doStatRebuild() {
		final ITableEntityManager stat_mgr = getTableEntityManager(SpaceStatBean.class);
		final ITableEntityManager attention_mgr = getTableEntityManager(UserAttentionBean.class);
		final StringBuilder sql = new StringBuilder();
		sql.append("update ").append(stat_mgr.getTablename()).append(" t set attentions=(select count(*) from ").append(attention_mgr.getTablename())
				.append(" where userid=t.id)");
		attention_mgr.execute(new SQLValue(sql.toString()));

		sql.setLength(0);
		sql.append("update ").append(stat_mgr.getTablename()).append(" t set fans=(select count(*) from ").append(attention_mgr.getTablename())
				.append(" where attentionid=t.id)");
		attention_mgr.execute(new SQLValue(sql.toString()));

		final ITableEntityManager remark_mgr = getTableEntityManager(SapceRemarkBean.class);
		final ITableEntityManager log_mgr = getTableEntityManager(SapceLogBean.class);
		sql.setLength(0);
		sql.append("update ").append(log_mgr.getTablename()).append(" t set remarks=(select count(*) from ").append(remark_mgr.getTablename())
				.append(" a where t.id=a.logid)");
		log_mgr.execute(new SQLValue(sql.toString()));
	}

	public static File getUploadDir(final HttpSession httpSession) {
		return new File(WebUtils.getTempPath(httpSession.getServletContext()) + File.separator + httpSession.getId() + File.separator);
	}

	/***************************** utils for jsp ****************************/

	public static String buildUserAttention(final PageRequestResponse requestResponse, final Object attentionId) {
		final StringBuilder sb = new StringBuilder();
		final UserAttentionBean attentionBean = getUserAttentionById(requestResponse, attentionId);
		sb.append("<a class=\"a2\" style=\"padding: 0;\" onclick=\"$Actions['ajaxUserAttention']('attentionId=");
		sb.append(attentionId);
		if (attentionBean != null) {
			sb.append("&delete=true");
		}
		sb.append("');\">");
		sb.append(LocaleI18n.getMessage(attentionBean == null ? "user_nav_tooltip.5" : "user_nav_tooltip.6"));
		sb.append("</a>");
		return sb.toString();
	}

	public static String buildSpaceLink(final PageRequestResponse requestResponse, final String filename, final String href, final String text) {
		final StringBuilder sb = new StringBuilder();
		final String cp = requestResponse.request.getContextPath();
		sb.append("<a style=\"background: url(").append(cp).append(deployPath);
		sb.append("images/").append(filename).append(") left center no-repeat;\" ");
		if (href.toLowerCase().startsWith("javascript:")) {
			sb.append("onclick=\"").append(href.substring(11));
		} else {
			sb.append("href=\"").append(cp).append(href);
		}
		sb.append("\">").append(text).append("</a>");
		return sb.toString();
	}

	public static String buildSpaceLink(final PageRequestResponse requestResponse, final String filename,
			final IWebApplicationModule applicationModule) {
		return buildSpaceLink(requestResponse, filename, applicationModule.getApplicationUrl(requestResponse), applicationModule.getApplicationText());
	}

	public static ETopicQuery bbsTopicQuery(final PageRequestResponse requestResponse) {
		return "posts".equals(requestResponse.getRequestParameter("tq")) ? ETopicQuery.postsAndTopic : ETopicQuery.onlyTopic;
	}

	public static String bbsNav(final PageRequestResponse requestResponse) {
		final StringBuilder sb = new StringBuilder();
		final IAccount account = getAccountAware().getAccount(requestResponse);
		if (account != null) {
			sb.append("<a class=\"a2");
			final ETopicQuery topicQuery = bbsTopicQuery(requestResponse);
			if (topicQuery == ETopicQuery.onlyTopic) {
				sb.append(" nav_arrow");
			}
			final String spaceUrl = WebUtils.addParameters(applicationModule.getSpaceUrl(requestResponse, account.user()), "t=bbs");
			sb.append("\" href=\"").append(spaceUrl).append("\">#(space_bbs.0)</a>");
			sb.append(HTMLBuilder.SEP);
			sb.append("<a class=\"a2");
			if (topicQuery == ETopicQuery.postsAndTopic) {
				sb.append(" nav_arrow");
			}
			sb.append("\" href=\"").append(WebUtils.addParameters(spaceUrl, "tq=posts")).append("\">#(space_bbs.1)</a>");
		}
		return sb.toString();
	}

	public static String getContent(final String content, final boolean newLine) {
		String c = StringUtils.blank(content);
		c = HTMLUtils.htmlEscape(c);
		c = HTMLUtils.stripScripts(c);
		c = SmileyUtils.replaceSmiley(c);
		c = HTMLUtils.autoLink(c);
		if (newLine) {
			c = HTMLUtils.convertHtmlLines(c);
		}
		return c;
	}

	public static String getContent(final String content) {
		return getContent(content, true);
	}

	static String getContent(final PageRequestResponse requestResponse, final SapceLogBean sapceLog) {
		final EFunctionModule refModule = sapceLog.getRefModule();
		if (refModule == EFunctionModule.space_log) {
			return SmileyUtils.replaceSmiley(ItSiteUtil.getShortContent(sapceLog.getContent(), 500, false));
		} else if (refModule == EFunctionModule.blog) {
			final Blog blog = BlogUtils.getTableEntityManager().queryForObjectById(sapceLog.getRefId(), Blog.class);
			if (blog != null) {
				return ContentUtils.getShortContent(blog, 140, true);
			}
		} else if (refModule == EFunctionModule.blog_remark) {
			final BlogRemark remark = BlogUtils.getTableEntityManager(BlogRemark.class).queryForObjectById(sapceLog.getRefId(), BlogRemark.class);
			if (remark != null) {
				return ContentUtils.getShortContent(remark, 140, true);
			}
		} else if (refModule == EFunctionModule.news) {
			final News news = NewsUtils.getTableEntityManager().queryForObjectById(sapceLog.getRefId(), News.class);
			if (news != null) {
				return ContentUtils.getShortContent(news, 140, true);
			}
		} else if (refModule == EFunctionModule.news_remark) {
			final NewsRemark remark = NewsUtils.getTableEntityManager(NewsRemark.class).queryForObjectById(sapceLog.getRefId(), NewsRemark.class);
			if (remark != null) {
				return ContentUtils.getShortContent(remark, 140, true);
			}
		} else if (refModule == EFunctionModule.bbs || refModule == EFunctionModule.bbs_posts) {
			final ComponentParameter nComponentParameter = ComponentParameter.get(requestResponse,
					BbsUtils.applicationModule.getComponentBean(requestResponse));
			final ITopicPagerHandle tHandle = (ITopicPagerHandle) nComponentParameter.getComponentHandle();
			if (refModule == EFunctionModule.bbs) {
				final BbsTopic topic = BbsUtils.getTableEntityManager().queryForObjectById(sapceLog.getRefId(), BbsTopic.class);
				if (topic != null) {
					return ContentUtils.getShortContent(tHandle.getPostsText(nComponentParameter, topic), 140, true);
				}
			} else {
				final PostsBean posts = BbsUtils.getTableEntityManager(PostsBean.class).queryForObjectById(sapceLog.getRefId(), PostsBean.class);
				if (posts != null) {
					return ContentUtils.getShortContent(tHandle.getPostsText(nComponentParameter, posts), 140, true);
				}
			}
		}
		return "";
	}

	public static String spaceLogContent(final PageRequestResponse requestResponse, final SapceLogBean sapceLog) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<div>");
		sb.append(getAccountAware().wrapAccountHref(requestResponse, sapceLog.getUserId()));
		sb.append("<span style=\"margin-left: 5px;\" class=\"gray-color\">");
		final EFunctionModule refModule = sapceLog.getRefModule();
		if (refModule == EFunctionModule.space_log) {
			sb.append("#(DefaultSpaceApplicationModule.6)");
		} else if (refModule == EFunctionModule.blog) {
			final Blog blog = BlogUtils.getTableEntityManager().queryForObjectById(sapceLog.getRefId(), Blog.class);
			if (blog != null) {
				sb.append("#(space_log_list.2)");
				sb.append("<span style=\"margin-left: 10px;\">");
				sb.append(newsOpenLink(requestResponse, BlogUtils.applicationModule, blog));
				sb.append("</span>");
			} else {
				return null;
			}
		} else if (refModule == EFunctionModule.blog_remark) {
			final BlogRemark remark = BlogUtils.getTableEntityManager(BlogRemark.class).queryForObjectById(sapceLog.getRefId(), BlogRemark.class);
			if (remark != null) {
				sb.append("#(space_log_list.6)");
				final Blog blog = BlogUtils.getTableEntityManager().queryForObjectById(remark.getDocumentId(), Blog.class);
				if (blog != null) {
					sb.append("<span style=\"margin-left: 10px;\">");
					sb.append(newsOpenLink(requestResponse, BlogUtils.applicationModule, blog));
					sb.append("</span>");
				}
			} else {
				return null;
			}
		} else if (refModule == EFunctionModule.news) {
			final News news = NewsUtils.getTableEntityManager().queryForObjectById(sapceLog.getRefId(), News.class);
			if (news != null) {
				sb.append("#(space_log_list.3)");
				sb.append("<span style=\"margin-left: 10px;\">");
				sb.append(newsOpenLink(requestResponse, NewsUtils.applicationModule, news));
				sb.append("</span>");
			} else {
				return null;
			}
		} else if (refModule == EFunctionModule.news_remark) {
			final NewsRemark remark = NewsUtils.getTableEntityManager(NewsRemark.class).queryForObjectById(sapceLog.getRefId(), NewsRemark.class);
			if (remark != null) {
				sb.append("#(space_log_list.7)");
				final News news = NewsUtils.getTableEntityManager().queryForObjectById(remark.getDocumentId(), News.class);
				if (news != null) {
					sb.append("<span style=\"margin-left: 10px;\">");
					sb.append(newsOpenLink(requestResponse, NewsUtils.applicationModule, news));
					sb.append("</span>");
				}
			} else {
				return null;
			}
		} else if (refModule == EFunctionModule.bbs || refModule == EFunctionModule.bbs_posts) {
			final ComponentParameter nComponentParameter = ComponentParameter.get(requestResponse,
					BbsUtils.applicationModule.getComponentBean(requestResponse));
			final ITopicPagerHandle tHandle = (ITopicPagerHandle) nComponentParameter.getComponentHandle();
			if (refModule == EFunctionModule.bbs) {
				final BbsTopic topic = BbsUtils.getTableEntityManager().queryForObjectById(sapceLog.getRefId(), BbsTopic.class);
				if (topic != null) {
					sb.append("#(space_log_list.4)");
					sb.append("<span style=\"margin-left: 10px;\">");
					sb.append(tHandle.wrapOpenLink(nComponentParameter, topic));
					sb.append("</span>");
				} else {
					return null;
				}
			} else {
				final PostsBean posts = BbsUtils.getTableEntityManager(PostsBean.class).queryForObjectById(sapceLog.getRefId(), PostsBean.class);
				if (posts != null) {
					sb.append("#(space_log_list.5)");
					final BbsTopic topic = BbsUtils.getTableEntityManager().queryForObjectById(posts.getTopicId(), BbsTopic.class);
					if (topic != null) {
						sb.append("<span style=\"margin-left: 10px;\">");
						sb.append(tHandle.wrapOpenLink(nComponentParameter, topic));
						sb.append("</span>");
					}
				}
			}
		}

		sb.append("</span></div>");
		sb.append("<div class=\"c\" style=\"padding: 6px 0 3px;\">");
		sb.append(getContent(requestResponse, sapceLog));
		if (refModule == EFunctionModule.space_log) {
			final IQueryEntitySet<SapceResourceBean> qs = applicationModule.resourceList(sapceLog);
			SapceResourceBean resource;
			final FilePathWrapper fp = getFilePathWrapper(requestResponse);
			while ((resource = qs.next()) != null) {
				sb.append("<div class=\"image\" style=\"width: 210px;\">");
				final ELogResource elr = resource.getLogResource();
				String src = null;
				if (elr == ELogResource.img_upload) {
					src = fp.getImagePath(requestResponse, createInputStreamAware(resource), 200, 150);
				} else if (elr == ELogResource.img_url) {
					src = fp.getImagePath(requestResponse, resource.getResourceUrl(), 200, 150);
				}
				if (src != null) {
					sb.append("<img src=\"").append(src).append("\" onclick=\"space_log_image_click(this, 'resourceId=").append(resource.getId())
							.append("');\"/>");
					sapceLog.setAttribute("hasImg", true);
				}
				sb.append("</div>");
			}
		}
		sb.append("</div>");
		final SapceLogBean replyFrom = getBeanById(sapceLog.getReplyFrom(), SapceLogBean.class);
		IUser user;
		if (replyFrom != null && (user = OrgUtils.um().queryForObjectById(replyFrom.getUserId())) != null) {
			sb.append("<div class=\"space_log_remark_top\" style=\"background-position: 12px center;\"></div>");
			sb.append("<div class=\"simple_toolbar\">");
			sb.append("<table style=\"width: 100%;\" cellpadding=\"0\" cellspacing=\"0\"><tr>");
			sb.append("<td width=\"40\" valign=\"top\">").append("<img class=\"photo_icon\" style=\"width: 24px; height: 24px;\" src=\"")
					.append(OrgUtils.getPhotoSRC(requestResponse.request, user, 64, 64)).append("\"></td>");
			sb.append("<td>");
			sb.append("<div>").append(getAccountAware().wrapAccountHref(requestResponse, user))
					.append("<span style=\"margin-left: 10px;\" class=\"gray-color\">").append(DateUtils.getRelativeDate(replyFrom.getCreateDate()))
					.append("</span></div>");
			sb.append("<div class=\"c\">").append(getContent(requestResponse, replyFrom));
			sb.append("</div></td></tr></table></div>");
		}
		return sb.toString();
	}

	static FilePathWrapper getFilePathWrapper(final PageRequestResponse requestResponse) {
		return new FilePathWrapper(requestResponse.getServletContext(), deployPath + "/file-cache/");
	}

	static FilePathWrapper.IInputStreamAware createInputStreamAware(final SapceResourceBean resource) {
		return new FilePathWrapper.AbstractPathInputStreamAware(resource.getId() + "_" + resource.getResourceUrl()) {
			@Override
			public InputStream getInputStream() throws IOException {
				final ResourceLobBean lob = getBeanById(resource.getId(), ResourceLobBean.class);
				return lob != null ? lob.getLob() : null;
			}
		};
	}

	private static final String my_space_images_url = "my_space_images_url";

	public static Set<String> getSessionUrls(final HttpSession session) {
		@SuppressWarnings("unchecked")
		Set<String> urls = (HashSet<String>) session.getAttribute(my_space_images_url);
		if (urls == null) {
			session.setAttribute(my_space_images_url, urls = new LinkedHashSet<String>());
		}
		return urls;
	}

	public static List<ID> getReplyFrom(final ID spaceId) {
		IQueryEntitySet<SapceLogBean> qs = getTableEntityManager(SapceLogBean.class).query(new ExpressionValue("id=" + spaceId),
				SapceLogBean.class);
		final List<ID> list = new ArrayList<ID>();
		if (qs != null) {
			SapceLogBean logBean;
			while ((logBean = qs.next()) != null) {
				list.add(logBean.getUserId());
			}
		}
		return list;
	}

	private static String newsOpenLink(final PageRequestResponse requestResponse, final IContentApplicationModule module, final NewsBean news) {
		final ComponentParameter nComponentParameter = ComponentParameter.get(requestResponse, module.getComponentBean(requestResponse));
		final INewsPagerHandle nHandle = (INewsPagerHandle) nComponentParameter.getComponentHandle();
		return nHandle.wrapOpenLink(nComponentParameter, news);
	}
}
