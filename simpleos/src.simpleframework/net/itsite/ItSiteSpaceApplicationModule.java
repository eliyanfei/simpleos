package net.itsite;

import net.simpleframework.content.blog.BlogUtils;
import net.simpleframework.my.friends.FriendsUtils;
import net.simpleframework.my.home.MyHomeUtils;
import net.simpleframework.my.message.MessageUtils;
import net.simpleframework.my.space.DefaultSpaceApplicationModule;
import net.simpleframework.my.space.MySpaceUtils;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.PageRequestResponse;

public class ItSiteSpaceApplicationModule extends DefaultSpaceApplicationModule {
	@Override
	public String getSpaceUrl(final PageRequestResponse requestResponse, final IUser user) {
		final StringBuilder sb = new StringBuilder();
		sb.append("/space/").append(user.getId()).append(".html");
		return sb.toString();
	}

	@Override
	public String getMySpaceNavigationHtml(final PageRequestResponse requestResponse) {
		final IAccount login = AccountSession.getLogin(requestResponse.getSession());
		final IUser user = login != null ? login.user() : null;
		if (user == null) {
			return "";
		}
		final StringBuilder sb = new StringBuilder();
		sb.append("<table style=\"width: 100%;\" cellspacing=\"5\" class=\"space_navigation\">");
		// col 1
		sb.append("<tr><td valign=\"top\" width=\"49%\"><div>");
		sb.append(MySpaceUtils.buildSpaceLink(requestResponse, "portal.png", MyHomeUtils.applicationModule));
		sb.append("</div><div>");
		sb.append(MySpaceUtils.buildSpaceLink(requestResponse, "os.png", "/os/3-0-1-0-0-0-0.html", "我的软件"));
		//		sb.append("</div><div>");
		//		sb.append(MySpaceUtils.buildSpaceLink(requestResponse, "cs.png", "/cs/2-0-0-0-0.html", "我的代码"));
		sb.append("</div><div>");
		sb.append(MySpaceUtils.buildSpaceLink(requestResponse, "docu.png", "/docu/4-0-0-0-0.html", "我的文档"));
		sb.append("</div><div>");
		sb.append(MySpaceUtils.buildSpaceLink(requestResponse, "question.png", "/question/4-0-0-0.html", "我的讨论"));
		//		sb.append("</div><div>");
		//		sb.append(MySpaceUtils.buildSpaceLink(requestResponse, "forum.gif",
		//				BbsUtils.applicationModule.getTopicUrl2(requestResponse, user, ETopicQuery.onlyTopic),
		//				"我的" + BbsUtils.applicationModule.getApplicationText()));
		sb.append("</div>");
		sb.append("</td>");
		// col 2
		sb.append("<td width=\"1px\" style=\"border-left: 3px double #aaa;\"></td>");
		// col 3
		sb.append("<td valign=\"top\"><div>");
		final String blogUrl = BlogUtils.applicationModule.getBlogUrl(requestResponse, user);
		sb.append("<a class=\"a2\" href=\"").append(requestResponse.wrapContextPath(WebUtils.addParameters(blogUrl, "op_act=add")))
				.append("\" style=\"float:right\">").append(LocaleI18n.getMessage("DefaultSpaceApplicationModule.5")).append("</a>");
		sb.append(MySpaceUtils.buildSpaceLink(requestResponse, "blog.gif", blogUrl, BlogUtils.applicationModule.getApplicationText()));
		sb.append("</div><div>");
		sb.append(MySpaceUtils.buildSpaceLink(requestResponse, "friends.png", FriendsUtils.applicationModule));
		sb.append("</div><div>");
		sb.append(MySpaceUtils.buildSpaceLink(requestResponse, "message.png", MessageUtils.applicationModule));
		sb.append("</div></td></tr></table>");
		return sb.toString();
	}
}
