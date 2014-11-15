package net.a;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.itsite.ItSiteHomeApplicationModule;
import net.itsite.ItSiteOrganizationApplicationModule.AccountExt;
import net.itsite.i.IAppModule;
import net.itsite.i.ICommonBeanAware;
import net.itsite.permission.PlatformMenuItem;
import net.itsite.user.CounterBean;
import net.itsite.utils.StringsUtils;
import net.itsite.utils.UID;
import net.prj.core.$VType;
import net.prj.manager.PrjMgrUtils;
import net.simpleframework.ado.DataObjectManagerUtils;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.applets.attention.AttentionUtils;
import net.simpleframework.applets.notification.MailMessageNotification;
import net.simpleframework.applets.notification.NotificationUtils;
import net.simpleframework.content.ContentUtils;
import net.simpleframework.core.IApplicationModule;
import net.simpleframework.core.id.ID;
import net.simpleframework.core.id.LongID;
import net.simpleframework.my.file.component.fileselect.FileSelectUtils;
import net.simpleframework.my.message.MessageUtils;
import net.simpleframework.my.message.SimpleMessage;
import net.simpleframework.my.space.MySpaceUtils;
import net.simpleframework.my.space.SapceLogBean;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountContext;
import net.simpleframework.organization.account.AccountLog;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.Exp;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.organization.component.userpager.UserPagerUtils;
import net.simpleframework.organization.impl.User;
import net.simpleframework.util.AlgorithmUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.DateUtils;
import net.simpleframework.util.HTMLUtils;
import net.simpleframework.util.HTTPUtils;
import net.simpleframework.util.ImageUtils;
import net.simpleframework.util.JavascriptUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.IWebApplicationModule;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.AbstractUrlForward;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.SkinUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.menu.MenuItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public final class ItSiteUtil {
	public static String cacheName = "prj";
	public static String url;//网站URL
	public static String title;//网站标题
	public static ItSiteHomeApplicationModule applicationModule;
	public static int aloneLimit = 10;
	public static Map<String, String> attrMap = new HashMap<String, String>();

	public static String anonymity = "匿名";
	public static Map<String, String> languageMap = new LinkedHashMap<String, String>();
	static {
		languageMap.put("zh", "简体中文");
		languageMap.put("zh-tw", "繁体中文");
		languageMap.put("en", "English");
	}

	public static String getUserText(Object userId) {
		IUser user = OrgUtils.um().queryForObjectById(userId);
		return user == null ? ItSiteUtil.anonymity : user.getText();
	}

	public static void setLanguage(final String language) {
		if ("zh".equals(language)) {
			LocaleI18n.setLocale(Locale.CHINA);
		} else if ("zh-tw".equals(language)) {
			LocaleI18n.setLocale(Locale.TAIWAN);
		} else if ("en".equals(language)) {
			LocaleI18n.setLocale(Locale.ENGLISH);
		}
	}

	public static void setSkin(final String skin, final IAccount ac, final HttpServletRequest request) {
		if (skin != null) {
			final AccountExt account = (AccountExt) ac;
			if (account != null) {
				if (SkinUtils.getSkinList().containsKey(skin)) {
					account.setSkin(skin);
					SkinUtils.setSessionSkin(request.getSession(), skin);
				} else {
					account.setSkin(SkinUtils.DEFAULT_SKIN);
					SkinUtils.setSessionSkin(request.getSession(), SkinUtils.DEFAULT_SKIN);
				}
				OrgUtils.am().update(new String[] { "skin" }, account);
			}
		}
	}

	public static String witchLanguage(final PageRequestResponse requestResponse) {
		final StringBuffer sb = new StringBuffer();
		Map<String, String> map = PrjMgrUtils.loadCustom("sys");
		String language = StringsUtils.trimNull(map.get("sys_language"), "zh,zh-tw,en");
		String[] ls = language.split(",");
		if (ls.length > 1) {
			sb.append("<select onchange=\"$IT.A('doLanguage','language='+this.value);\" style='color:black;'>");
			String locale = Locale.getDefault().getLanguage();
			String country = Locale.getDefault().getCountry();
			if (country.length() != -0) {
				locale += "-" + country.toLowerCase();
			}
			for (String l : ls) {
				sb.append("<option style='color:black;' value=\"" + l + "\" " + (l.equals(locale) ? "selected=\"selected\"" : "") + ">"
						+ languageMap.get(l) + "</option>");
			}
			sb.append("</select>");
		}
		return sb.toString();
	}

	public static String witchSkin(final PageRequestResponse requestResponse) {
		final StringBuffer sb = new StringBuffer();
		Map<String, String> map = PrjMgrUtils.loadCustom("sys");
		String skin = map.get("sys_skin");
		if (StringsUtils.isNotBlank1(skin)) {
			String[] ss = skin.split(",");
			if (ss.length > 1) {
				sb.append("<select onchange=\"$IT.A('doSkin','skin='+this.value);\" style='color:black;'>");
				final String dskin = ItSiteUtil.getLoginAccount(requestResponse).getSkin();
				for (String s : SkinUtils.skinMap.keySet()) {
					sb.append("<option style='color:black;' value=\"" + s + "\" " + (dskin.equals(s) ? "selected=\"selected\"" : "") + ">"
							+ SkinUtils.skinMap.get(s) + "</option>");
				}
				sb.append("</select>");
			}
		}
		return sb.toString();
	}

	/**
	 * 获取分页排行
	 * @return
	 */
	public static String getTabList(final String act, final String param) {
		final StringBuffer sb = new StringBuffer();
		final String id = UID.asString();
		sb.append("<div class=\"tabs\" id=\"" + UID.asString() + "\">");
		sb.append("<span id='" + id + "' class=\"tab active\"");
		sb.append(" onclick=\"$IT.togglePageletV(this,'" + act + "','time=week&" + param + "');\">周</span><span");
		sb.append(" class=\"tab\"");
		sb.append(" onclick=\"$IT.togglePageletV(this,'" + act + "','time=month&" + param + "');\">月</span><span");
		sb.append(" class=\"tab\"");
		sb.append(" onclick=\"$IT.togglePageletV(this,'" + act + "','time=year&" + param + "');\">年</span>");
		sb.append("</div>");
		sb.append(JavascriptUtils.wrapScriptTag(JavascriptUtils.wrapWhenReady("$IT.togglePageletV($('" + id + "'),'" + act + "','time=week&" + param
				+ "');")));
		return sb.toString();
	}

	public static String getSQLNullExpr(final Class<?> type, final String column) {
		final StringBuilder sb = new StringBuilder();
		sb.append(column);
		if (Number.class.isAssignableFrom(type)) {
			sb.append(" = 0");
		} else {
			sb.append(" is null");
		}
		return sb.toString();
	}

	/**
	 * 给管理员发邮件
	 * @param subject
	 * @param body
	 */
	public static void sendMailToManager(final String subject, final String body) {
		final MailMessageNotification mailMessage = new MailMessageNotification();
		mailMessage.setHtmlContent(true);
		mailMessage.getTo().add(ItSiteUtil.getUserById(2824));
		mailMessage.getTo().add(ItSiteUtil.getUserById(3233));
		mailMessage.setSubject(subject);
		mailMessage.setTextBody(body);
		NotificationUtils.sendMessage(mailMessage);
	}

	/**
	 * 给发邮件给某个用户
	 * @param subject
	 * @param body
	 */
	public static void sendMailToUser(final String subject, final String body, final IUser user) {
		final MailMessageNotification mailMessage = new MailMessageNotification();
		mailMessage.setHtmlContent(true);
		mailMessage.getTo().add(user);
		mailMessage.setSubject(subject);
		mailMessage.setTextBody(body);
		NotificationUtils.sendMessage(mailMessage);
	}

	/**
	 * 
	 * @param content
	 * @param length
	 * @return
	 */
	public static String getShortString(final String content, int length, final boolean more) {
		if (content.length() < length)
			return content;
		float temp = length;
		float position = 0;
		char[] cs = content.toCharArray();
		for (int i = 0; i < cs.length; i++) {
			if (cs[i] >= 33 && cs[i] <= 126) {
				temp -= 0.5;
			} else {
				temp -= 1;
			}
			if (temp >= 0) {
				position++;
			} else {
				break;
			}
		}
		if (content.length() <= position)
			return content;
		return content.substring(0, Math.min((int) position, content.length())) + (more ? "..." : "");
	}

	public static String getShortContent(final String content, final int length, final boolean newLine) {
		return getShortContent(content, length, newLine, false);
	}

	public static String getShortContent(final String content, final int length, final boolean newLine, final boolean removeA) {
		final Document document = HTMLUtils.createHtmlDocument(content, false);
		if (removeA) {
			document.select("a").remove();
		}
		return HTMLUtils.truncateHtml(document, length, newLine, true, true);
	}

	public static ITableEntityManager getTableEntityManager(final IApplicationModule applicationModule, final Class<?> beanClazz) {
		return DataObjectManagerUtils.getTableEntityManager(applicationModule, beanClazz);
	}

	public static ITableEntityManager getTableEntityManager(final IApplicationModule applicationModule) {
		return DataObjectManagerUtils.getTableEntityManager(applicationModule);
	}

	public static boolean isManage(final PageRequestResponse requestResponse) {
		try {
			if (IWebApplicationModule.Utils.isManager(requestResponse, applicationModule)) {
				return true;
			}
		} catch (final Exception e) {
		}
		return false;
	}

	public static boolean isManage(final PageRequestResponse requestResponse, final IWebApplicationModule module) {
		try {
			if (IWebApplicationModule.Utils.isManager(requestResponse, module)) {
				return true;
			}
		} catch (final Exception e) {
		}
		return false;
	}

	public static boolean isManageOrSelf(final PageRequestResponse requestResponse, final IWebApplicationModule module, final Object userId) {
		try {
			if (isManage(requestResponse, module)) {
				return true;
			}
			if (ItSiteUtil.getLoginUser(requestResponse).getId().equals(userId)) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 区分大小写
	 */
	public static String replaceString(String source, String oldstring, String newstring, final boolean ignoreCase) {
		if (ignoreCase)
			return source.replaceAll(oldstring, newstring);//大小写敏感
		else
			return source.replaceAll("(?i)" + oldstring, newstring); //大小写不敏感 
	}

	//使用正则表达式实现不区分大小写替换
	public static String replaceStringP(String source, String oldstring, String newstring, final boolean ignoreCase) {
		if (ignoreCase) {
			Matcher m1 = Pattern.compile(oldstring, Pattern.CANON_EQ).matcher(source);
			return m1.replaceAll(newstring);//大小写敏感
		} else {
			Matcher m = Pattern.compile(oldstring, Pattern.CASE_INSENSITIVE).matcher(source);
			return m.replaceAll(newstring); //大小写不敏感 
		}
	}

	public static String markContent(final String mark, final String content) {
		if (StringsUtils.isBlank(mark))
			return content;
		final String[] marks = mark.split(" ");
		String temp = content;
		for (final String m : marks) {
			temp = StringsUtils.replace(temp.toLowerCase(), m.toLowerCase(), StringsUtils.u("<span class=\"_red\">", m, "</span>"));
		}
		return temp;
	}

	/**
	 * 格式化URL
	 * @return
	 */
	public static String formatUrl(final String url) {
		if (StringUtils.hasText(url)) {
			if (url.startsWith("http")) {
				return url;
			}
			return "http://" + url;
		}
		return "#";
	}

	public static String layoutListInfo(PageRequestResponse requestResponse, final ICommonBeanAware commonBean) {
		final StringBuffer sb = new StringBuffer();
		sb.append(ContentUtils.getAccountAware().wrapAccountHref(requestResponse, commonBean.getUserId(), commonBean.getUserText()));
		sb.append(" , 发布于 ").append(DateUtils.getRelativeDate(commonBean.getCreateDate()));
		sb.append(" , (").append(commonBean.getRemarks()).append("条评论");
		sb.append(" , ").append(commonBean.getViews()).append("次阅读)");
		return sb.toString();
	}

	/**
	 * 显示评论的用户和时间，8小时内的评论标新
	 * @return
	 */
	public static String layoutRemarkTime(PageRequestResponse requestResponse, final ICommonBeanAware commonBean) {
		final StringBuilder sb = new StringBuilder();
		Date date = commonBean.getCreateDate();
		boolean isRemark = false;
		if (commonBean.getRemarkDate() != null) {
			date = commonBean.getRemarkDate();
			isRemark = true;
		}
		sb.append("<span title=\"" + ConvertUtils.toDateString(date) + "\">").append(DateUtils.getRelativeDate(date)).append("</span>");
		sb.append(", ");
		final Object userId = commonBean.getRemarkUserId() == null || commonBean.getRemarkUserId().equals2(0) ? commonBean.getUserId() : commonBean
				.getRemarkUserId();
		sb.append("By ");
		sb.append(StringUtils.text(ContentUtils.getAccountAware().wrapAccountHref(requestResponse, userId), "匿名"));
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -12);
		if (date.after(cal.getTime())) {
			if (isRemark) {
				sb.append("<span class=\"remark_new\" style=\"margin-left: 10px;\"></span>");
			} else
				sb.append("<span class=\"new_gif_image\" style=\"margin-left: 10px;\"></span>");
		}
		return sb.toString();
	}

	/**
	 * 显示阅读，评论次数
	 * @param commonBean
	 * @return
	 */
	public static String layoutRemarkAndView(final ICommonBeanAware commonBean, final boolean today) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<span class=\"nnum\" title=\"" + commonBean.getRemarks() + "评论/" + (today ? commonBean.getTodayViews() : commonBean.getViews())
				+ "阅读\">");
		if (commonBean.isNewRemark()) {
			sb.append("<span class=\"nnum _red\">");
			sb.append(commonBean.getRemarks());
			sb.append("</span>");
		} else {
			sb.append(commonBean.getRemarks());
		}
		sb.append("/").append(today ? commonBean.getTodayViews() : commonBean.getViews());
		sb.append("</span>");
		return sb.toString();
	}

	public static String layoutRemarkAndViewAndVote(final ICommonBeanAware commonBean) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<span class=\"nnum\" title=\"" + commonBean.getVotes() + "支持/" + commonBean.getRemarks() + "评论/" + commonBean.getViews() + "阅读\">");
		sb.append(commonBean.getVotes()).append("支持/");
		if (commonBean.isNewRemark()) {
			sb.append("<span class=\"nnum _red\">");
			sb.append(commonBean.getRemarks());
			sb.append("</span>");
		} else {
			sb.append(commonBean.getRemarks());
		}
		sb.append("评论/").append(commonBean.getViews()).append("阅读");
		sb.append("</span>");
		return sb.toString();
	}

	/**
	 * 显示项目的发布者和发布时间
	 * @return
	 */
	public static String layoutTime(PageRequestResponse requestResponse, final ICommonBeanAware commonBean, final String dateFormat,
			final boolean isNew) {
		final StringBuilder sb = new StringBuilder();
		sb.append(ContentUtils.getAccountAware().wrapAccountHref(requestResponse, commonBean.getUserId()));
		sb.append(", ");
		if (dateFormat != null) {
			try {
				sb.append(ConvertUtils.toDateString(commonBean.getModifyDate(), dateFormat));
			} catch (final Exception ex) {
				sb.append(ex.getMessage());
			}
		} else {
			sb.append("<span title=\"" + ConvertUtils.toDateString(commonBean.getModifyDate()) + "\">")
					.append(DateUtils.getRelativeDate(commonBean.getModifyDate())).append("</span>");
		}
		if (isNew) {
			final Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR, -12);
			if (commonBean.getModifyDate().after(cal.getTime())) {
				sb.append("<span class=\"new_gif_image\" style=\"margin-left: 10px;\"></span>");
			}
		}
		return sb.toString();
	}

	public static String layoutTimeHome(PageRequestResponse requestResponse, final ICommonBeanAware commonBean, final String dateFormat,
			final boolean isNew) {
		final StringBuilder sb = new StringBuilder();
		if (dateFormat != null) {
			try {
				sb.append(ConvertUtils.toDateString(commonBean.getModifyDate(), dateFormat));
			} catch (final Exception ex) {
				sb.append(ex.getMessage());
			}
		} else {
			sb.append("<span title=\"" + ConvertUtils.toDateString(commonBean.getModifyDate()) + "\">")
					.append(DateUtils.getRelativeDate(commonBean.getModifyDate())).append("</span>");
		}
		sb.append(", ");
		String remarkUserText = ContentUtils.getAccountAware().wrapAccountHref(requestResponse, commonBean.getRemarkUserId());
		if (!StringUtils.hasText(remarkUserText)) {
			remarkUserText = ContentUtils.getAccountAware().wrapAccountHref(requestResponse, commonBean.getUserId());
		}
		sb.append("By ").append(remarkUserText);
		if (isNew) {
			final Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR, -12);
			if (commonBean.getModifyDate().after(cal.getTime())) {
				sb.append("<span class=\"new_gif_image\" style=\"margin-left: 10px;\"></span>");
			}
		}
		return sb.toString();
	}

	/**
	 * 用户是否登入
	 * @return
	 */
	public static boolean isLogin(final PageRequestResponse requestResponse) {
		final IAccount login = AccountSession.getLogin(requestResponse.getSession());
		if (login == null) {
			return false;
		}
		return true;
	}

	/**
	 * 根据用户ID获得用户对象
	 * @param userId
	 * @return
	 */
	public static IUser getUserById(final Object userId) {
		return OrgUtils.um().queryForObjectById(userId);
	}

	/**
	 * 根据用户ID获得用户账号
	 * @param userId
	 * @return
	 */
	public static IAccount getAccountById(final Object userId) {
		return OrgUtils.am().queryForObjectById(userId);
	}

	/**
	 * 获得登入的用户
	 * @param requestResponse
	 * @return
	 */
	public static IUser getLoginUser(final PageRequestResponse requestResponse) {
		final IAccount account = AccountSession.getLogin(requestResponse.getSession());
		if (account == null) {
			final User u = new User();
			u.setId(LongID.zero);
			u.setText("匿名");
			return u;
		}
		return account.user();
	}

	public static IAccount getLoginAccount(final PageRequestResponse requestResponse) {
		return AccountSession.getLogin(requestResponse.getSession());
	}

	/**
	 * 把图片格式转成可显示的标签
	 * @param compParameter
	 * @param content
	 * @return
	 */
	public static String doDownloadContent(final ComponentParameter compParameter, final String content) {
		final Document doc = Jsoup.parse(content);
		final Elements atts = doc.select("a[href^=" + FileSelectUtils.DOWNLOAD_FLAG + "]");
		if (atts.isEmpty()) {
			return content;
		}
		for (int i = 0; i < atts.size(); i++) {
			final Element att = atts.get(i);
			String dl = att.attr("href").substring(FileSelectUtils.DOWNLOAD_FLAG.length());
			att.removeAttr("href");
			final int p = dl.indexOf("?");
			final Map<String, Object> params = WebUtils.toQueryParams(dl.substring(p + 1));
			final String dl_points = compParameter.getRequestParameter("dl_points");
			if (StringUtils.hasText(dl_points)) {
				params.put("points", 1);
				params.put("posttext", 0);
			}
			final String nDL = AlgorithmUtils.base64Encode((dl.substring(0, p + 1) + WebUtils.toQueryString(params)).getBytes());
			att.attr("onclick", "$Actions['__my_folderfile_ajax_download']('dl=" + nDL + "');");
			if (true) {
				dl = compParameter.wrapContextPath(dl);
				try {
					final URL url = new URL(AbstractUrlForward.getLocalhostUrl(compParameter.request) + dl);
					if (ImageUtils.isImage(url)) {
						final Element img = doc.createElement("img");
						img.attr("src", WebUtils.addParameters(dl, "loc=true")).attr("style",
								"padding: 1px; border: 1px solid #999; max-width: 600px;");
						att.replaceWith(img);
					}
				} catch (final Exception e) {
				}
			}
		}
		return doc.body().html();
	}

	public static final long period = 60 * 60 * 24;// 默认隔一天清理一次
	public static final long weekperiod = 60 * 60 * 24 * 7;// 默认隔一天清理一次
	public static final String yyyyMMdd_HHmm = "yyyy-MM-dd HH:mm";
	public static final String yyyyMMdd = "yyyy-MM-dd";
	public static final String yyyyMMdd_HH = "yyyy-MM-dd HH";

	/**
	 * 当前时间到凌晨0点的时间间隔
	 * @return
	 */
	public static long get0Time() {
		final Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.DATE, 1);
		return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
	}

	/**
	 * 获得周一的凌晨
	 * @return
	 */
	public static long getWeekTime() {
		final Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.DAY_OF_WEEK, cal.getActualMaximum(Calendar.DAY_OF_WEEK));
		cal.add(Calendar.DATE, 2);
		return (cal.getTimeInMillis() - new Date().getTime()) / 1000;
	}

	public static String getHtmlEditorToolbar(final PageRequestResponse requestResponse, final String bean) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<a class=\"simple_btn simple_btn_left\" ");
		sb.append("onclick=\"$Actions['__").append(bean);
		sb.append("_myfileSelect']();\">#(TopicPagerUtils.0)</a>");
		sb.append("<a class=\"simple_btn simple_btn_right\" ");
		sb.append("onclick=\"$Actions['__").append(bean);
		sb.append("_syntaxHighlighter'].editor();\">#(TopicPagerUtils.4)</a>");
		return sb.toString();
	}

	public static String getHtmlEditorToolbar(final PageRequestResponse requestResponse, final String bean, final String content) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<a class=\"simple_btn simple_btn_left\" ");
		sb.append("onclick=\"$Actions['fileUploadWindowAct']('refId=" + content + "');\">#(TopicPagerUtils.0)</a>");
		sb.append("<a class=\"simple_btn simple_btn_right\" ");
		sb.append("onclick=\"$Actions['__").append(bean);
		sb.append("_syntaxHighlighter'].editor();\">#(TopicPagerUtils.4)</a>");
		return sb.toString();
	}

	public static String buildVote(ICommonBeanAware beanAware, final String id) {
		final StringBuffer buf = new StringBuffer();
		buf.append("<div class='voteCommon'>");
		buf.append("<span class='__vote voteUp' title='顶" + beanAware.getVotes() + "次'");
		buf.append("param='voteValue=up&").append(id);
		buf.append("=").append(beanAware.getId()).append("'>").append("顶</span>");
		buf.append("<span class='voteValue' title='顶" + beanAware.getVotes() + "次,踩" + beanAware.getDownVotes()
				+ "次\n点击隐藏' id='voteValue' onclick=\"this.up('div').$toggle();\">");
		buf.append(beanAware.getTotalVotes());
		buf.append("</span>");
		buf.append("<span class='__vote voteDown' title='踩" + beanAware.getDownVotes() + "次'");
		buf.append("param='voteValue=down&").append(id);
		buf.append("=").append(beanAware.getId()).append("'>").append("踩</span>");
		buf.append("</div>");
		return buf.toString();
	}

	/**
	 * 当前点击的是哪个菜单
	 * @return
	 */
	public static String witchDescription(final PageRequestResponse requestResponse) {
		final String url = HTTPUtils.getRequestURI(requestResponse.request);
		final StringBuffer buf = new StringBuffer();
		buf.append("<meta name=\"description\" content=\"");
		boolean description = false;
		if (url != null) {
		}
		buf.append("\">");
		if (description)
			return buf.toString();
		return "";
	}

	public static Map<String, String> menuMap = new HashMap<String, String>();//路径和菜单的对应关系
	static {
		menuMap.put("/manager/home.html", "首页");
		menuMap.put("/manager/message.html", "消息");
		menuMap.put("/manager/remark.html", "评论");
		menuMap.put("/manager/site.html", "站点");
		menuMap.put("/manager/company.html", "企业");
		menuMap.put("/manager/template.html", "模板");
		menuMap.put("/manager/function.html", "功能");
		menuMap.put("/manager/manager", "系统管理");
		menuMap.put("/manager/desktop.html", "界面");
		menuMap.put("/manager/favorite.html", "收藏");
	}

	/**
	 * 当前点击的是哪个菜单
	 * @return
	 */
	public static String witchMenu(final PageRequestResponse requestResponse) {
		final String url = HTTPUtils.getRequestURI(requestResponse.request);
		final String h = requestResponse.getContextPath();
		if (url != null) {
			for (final String menu : menuMap.keySet()) {
				if (url.startsWith(h + menu)) {
					return menuMap.get(menu);
				}
			}
		}
		return "首页";
	}

	/**
	 * 交换积分
	 */
	public static void switchPoint(final PageRequestResponse requestResponse, final int sPoint, final Object userId, final ID id) {
		//交换积分
		final IAccount account = ItSiteUtil.getAccountById(ItSiteUtil.getLoginUser(requestResponse).getId());
		if (account != null) {
			account.setPoints(account.getPoints() - sPoint);
		}
	}

	/**
	 * 更新账号日志
	 */
	public static void addAccountLog(final ID userId, final String eventId, final int point, final int exp, final ID logId) {
		final AccountLog log = new AccountLog();
		log.setAccountId(userId);
		log.setEventId(eventId);
		log.setCreateDate(new Date());
		log.setExp(exp);
		log.setPoints(point);
		log.setLogId(logId);
	}

	public static void update(final PageRequestResponse requestResponse, final ID userId, final ID id, final boolean remark) {
		if (isManage(requestResponse, applicationModule)) {
			if (remark)
				AccountContext.update(ItSiteUtil.getAccountById(userId), "ma_delete_remark", id);
			else
				AccountContext.update(ItSiteUtil.getAccountById(userId), "ma_delete", id);
		} else {
			if (remark)
				AccountContext.update(ItSiteUtil.getAccountById(userId), "my_delete_remark", id);
			else
				AccountContext.update(ItSiteUtil.getAccountById(userId), "my_delete", id);
		}
	}

	/**
	* 添加日志
	* @param account
	* @param content
	*/
	public static void addSpaceLog(final IAccount account, final String content, final EFunctionModule functionModule, final ID refId) {
		final ITableEntityManager log_mgr = MySpaceUtils.getTableEntityManager(SapceLogBean.class);
		final SapceLogBean sapceLog = new SapceLogBean();
		sapceLog.setUserId(account == null ? new LongID(0) : account.getId());
		sapceLog.setCreateDate(new Date());
		sapceLog.setContent(content);
		sapceLog.setRefId(refId);
		sapceLog.setRefModule(functionModule);
		log_mgr.insert(sapceLog);
	}

	/**
	 * 使内容里的URL变成全URL
	 * @return
	 */
	public static String formatContentUrl(final String content) {
		final Document document = Jsoup.parse(content);
		final Elements as = document.getElementsByTag("a");
		for (int i = 0; i < as.size(); i++) {
			final Element a = as.get(i);
			final String href = a.attr("href");
			if (!href.startsWith("http") && href.startsWith("www")) {
				a.attr("href", href.charAt(0) == '/' ? ItSiteUtil.url + href : ItSiteUtil.url + "/" + href);
			}
		}
		return document.body().html();
	}

	/**
	 * 是否已经关注
	 * @return
	 */
	public static String isAttention(final PageRequestResponse requestResponse, final EFunctionModule vtype, final ID attentionId, final String text,
			String text1) {
		return AttentionUtils.get(requestResponse, vtype, attentionId) != null ? text : text1;
	}

	public static String buildComplaint(final PageRequestResponse requestRespons, final EFunctionModule refModule, final Object id) {
		return buildComplaint(requestRespons, refModule, id, "a2");
	}

	public static String buildComplaint(final PageRequestResponse requestRespons, final EFunctionModule refModule, final Object id,
			final String className) {
		final StringBuffer buf = new StringBuffer();
		buf.append("<a dl='false' ");
		if (className != null) {
			buf.append(" class='").append(className).append("'");
		}
		buf.append(" onclick=\"$Actions['complaintWindowAct']('refId=" + id + "&refModule=" + refModule.name() + "');\">举报</a>");
		return buf.toString();
	}

	public static String buildPropEditor(final Enum<?>[] objs) {
		final StringBuffer buf = new StringBuffer();
		int i = 1;
		for (final Enum<?> obj : objs) {
			buf.append(obj.name()).append("=").append(obj.toString());
			if (i++ != objs.length)
				buf.append(";");
		}
		return buf.toString();
	}

	/**
	 * 跳转函数
	 */
	public static String buildActionLoc(final String fun) {
		return JavascriptUtils.wrapScriptTag(JavascriptUtils.wrapWhenReady("$Actions.loc('" + fun + "');"));
	}

	/**
	 * 获得等级
	 * @param account
	 * @return
	 */
	public static String getExpGrade(final IAccount account) {
		Exp exp;
		if (account != null && (exp = AccountContext.getExp(account.getExp())) != null) {
			final StringBuilder sb = new StringBuilder();
			sb.append(exp).append(UserPagerUtils.getExpIcon(exp));
			return sb.toString();
		}
		return "";
	}

	public static int getConstantValue(final String name) {
		final ITableEntityManager temgr = ItSiteUtil.getTableEntityManager(ItSiteUtil.applicationModule, CounterBean.class);
		final CounterBean counterBean = temgr.queryForObject(new ExpressionValue("name=?", new Object[] { name }), CounterBean.class);
		return counterBean == null ? 0 : counterBean.getCounter();
	}

	public static int setConstantValue(final String name) {
		final ITableEntityManager temgr = ItSiteUtil.getTableEntityManager(ItSiteUtil.applicationModule, CounterBean.class);
		CounterBean counterBean = temgr.queryForObject(new ExpressionValue("name=?", new Object[] { name }), CounterBean.class);
		if (counterBean == null) {
			counterBean = new CounterBean();
		}
		counterBean.setName(name);
		counterBean.setCounter(counterBean.getCounter() + 1);
		if (counterBean.getId() == null)
			temgr.insert(counterBean);
		else
			temgr.update(counterBean);
		return counterBean.getCounter();
	}

	public static final Map<String, IAppModule> modules = new HashMap<String, IAppModule>();
	public static final List<MenuItem> menuList = new ArrayList<MenuItem>();

	public static void registerAppModule(IAppModule appModule) {
		modules.put(appModule.getModuleName(), appModule);
		menuList.addAll(appModule.getMenuItems());
		Collections.sort(menuList, new Comparator<MenuItem>() {

			@Override
			public int compare(final MenuItem o1, final MenuItem o2) {
				final PlatformMenuItem menu1 = (PlatformMenuItem) o1;
				final PlatformMenuItem menu2 = (PlatformMenuItem) o2;
				if (menu1.getIndex() > menu2.getIndex()) {
					return 1;
				} else if (menu1.getIndex() < menu2.getIndex()) {
					return -1;
				}
				return 0;
			}
		});
	}

	public static String wrapSpan(long value, String className) {
		final StringBuffer buf = new StringBuffer();
		if (value != 0) {
			buf.append("<span class='").append(className).append("'>");
			buf.append(value).append("</span>");
		} else {
			buf.append(value);
		}
		return buf.toString();
	}

	public static String wrapSpan(long value) {
		return wrapSpan(value, "rrred");
	}

	/**
	 * 添加导航菜单
	 */
	public static void addMenuNav(HttpSession session, final String url, final String title, final boolean init) {
		List<String> navList = (List<String>) session.getAttribute("navList");
		if (navList == null) {
			navList = new ArrayList<String>();
			session.setAttribute("navList", navList);
		}
		if (init) {
			navList.clear();
		}
		final StringBuffer buf = new StringBuffer();
		if (url != null) {
			buf.append("<a href='").append(url).append("'>");
		}
		buf.append(title);
		if (url != null) {
			buf.append("</a>");
		}
		navList.add(buf.toString());
	}

	public static String buildTimeString(Date date) {
		final StringBuffer sb = new StringBuffer();
		final String[] months = { "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月" };
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		sb.append("<div class=\"time\">");
		sb.append("<div class=\"month\">" + months[cal.get(Calendar.MONTH)] + "</div>");
		sb.append("<div class=\"day\">" + cal.get(Calendar.DATE) + "</div>");
		sb.append("<div class=\"year\">" + cal.get(Calendar.YEAR) + "</div>");
		sb.append("</div>");
		return sb.toString();
	}

	/**
	 * 获取消息提醒
	 * @param requestResponse
	 * @return
	 */
	public static String getMessages(final PageRequestResponse requestResponse, int type) {
		IAccount account = getLoginAccount(requestResponse);
		if (account == null) {
			return "";
		}
		final StringBuffer sql = new StringBuffer();
		sql.append("select id from simple_my_message where messageread=0 and toid=" + account.getId());
		if (type != -1) {
			sql.append(" and messageType=" + type);
		}
		IQueryEntitySet<Map<String, Object>> qs = MessageUtils.getTableEntityManager(SimpleMessage.class).query(new SQLValue(sql.toString()));
		int count = qs.getCount();

		if (type == -1) {
			sql.setLength(0);
			sql.append("select id from simple_dialog where (toread=0 and toid=" + account.getId() + ") or (sendread=0 and sentid=" + account.getId()
					+ ")");
			qs = MessageUtils.getTableEntityManager(SimpleMessage.class).query(new SQLValue(sql.toString()));
			count += qs.getCount();
		}
		if (count == 0)
			return "";
		final StringBuffer sb = new StringBuffer();
		sb.append("<sup class=\"highlight \">");
		sb.append(count).append("</sup>");
		return sb.toString();
	}

	/**
	 * 获取消息提醒
	 * @param requestResponse
	 * @return
	 */
	public static String getDialog(final PageRequestResponse requestResponse) {
		IAccount account = getLoginAccount(requestResponse);
		if (account == null) {
			return "";
		}
		final StringBuffer sql = new StringBuffer();
		sql.append("select id from simple_dialog where (toread=0 and toid=" + account.getId() + ") or (sendread=0 and sentid=" + account.getId()
				+ ")");
		final IQueryEntitySet<Map<String, Object>> qs = MessageUtils.getTableEntityManager(SimpleMessage.class).query(new SQLValue(sql.toString()));
		final int count = qs.getCount();
		if (count == 0)
			return "";
		final StringBuffer sb = new StringBuffer();
		sb.append("<sup class=\"highlight \">");
		sb.append(count).append("</sup>");
		return sb.toString();
	}

	public static String getIpAndMac(HttpServletRequest request) {
		final String ip = getIpAddr(request);
		return StringsUtils.u(ip);
	}

	/**  
	* 通过HttpServletRequest返回IP地址  
	* @param request HttpServletRequest  
	* @return ip String  
	* @throws Exception  
	*/
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**  
	 * 通过IP地址获取MAC地址  
	 * @param ip String,127.0.0.1格式  
	 * @return mac String  
	 * @throws Exception  
	 */
	public static String getMACAddress(String ip) {
		String line = "";
		String macAddress = "";
		try {
			final String MAC_ADDRESS_PREFIX = "MAC Address = ";
			final String LOOPBACK_ADDRESS = "127.0.0.1";
			//如果为127.0.0.1,则获取本地MAC地址。    
			if (LOOPBACK_ADDRESS.equals(ip)) {
				InetAddress inetAddress = InetAddress.getLocalHost();
				//貌似此方法需要JDK1.6。    
				byte[] mac = NetworkInterface.getByInetAddress(inetAddress).getHardwareAddress();
				//下面代码是把mac地址拼装成String    
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < mac.length; i++) {
					if (i != 0) {
						sb.append("-");
					}
					//mac[i] & 0xFF 是为了把byte转化为正整数    
					String s = Integer.toHexString(mac[i] & 0xFF);
					sb.append(s.length() == 1 ? 0 + s : s);
				}
				//把字符串所有小写字母改为大写成为正规的mac地址并返回    
				macAddress = sb.toString().trim().toUpperCase();
				return macAddress;
			}
			//获取非本地IP的MAC地址    
			Process p = Runtime.getRuntime().exec("nbtstat -A " + ip);
			InputStreamReader isr = new InputStreamReader(p.getInputStream());
			BufferedReader br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
				if (line != null) {
					int index = line.indexOf(MAC_ADDRESS_PREFIX);
					if (index != -1) {
						macAddress = line.substring(index + MAC_ADDRESS_PREFIX.length()).trim().toUpperCase();
					}
				}
			}
			br.close();
		} catch (IOException e) {
		}
		return macAddress;
	}

	public static String getAboutHtml(int type) {
		final StringBuffer buf = new StringBuffer();
		buf.append("<div class='v_about" + type + "'>");
		buf.append("<a style='color:#3393c9;' onclick=\"$Actions['aboutWinAct']();\">智汇</a>");
		buf.append("(").append("<span style='color: red;'>");
		buf.append($VType.getNowVType().ver.toString()).append("</span>)</div>");
		return buf.toString();
	}
}
