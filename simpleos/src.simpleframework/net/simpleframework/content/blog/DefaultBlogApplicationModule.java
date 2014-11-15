package net.simpleframework.content.blog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.a.ItSiteUtil;
import net.itsite.i.ISendMail;
import net.itsite.impl.AItSiteAppclicationModule;
import net.simpleframework.applets.attention.AttentionUtils;
import net.simpleframework.applets.attention.ISentCallback;
import net.simpleframework.applets.notification.MailMessageNotification;
import net.simpleframework.applets.notification.NotificationUtils;
import net.simpleframework.content.AbstractContentApplicationModule;
import net.simpleframework.content.ContentUtils;
import net.simpleframework.content.EContentType;
import net.simpleframework.content.component.newspager.INewsPagerHandle;
import net.simpleframework.content.component.newspager.NewsPagerBean;
import net.simpleframework.content.component.newspager.NewsPagerUtils;
import net.simpleframework.content.component.remark.RemarkItem;
import net.simpleframework.core.ExecutorRunnable;
import net.simpleframework.core.IInitializer;
import net.simpleframework.core.ITaskExecutorAware;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.organization.component.login.LoginUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.portal.module.PortalModuleRegistryFactory;
import net.simpleframework.web.page.component.ui.tabs.EMatchMethod;
import net.simpleframework.web.page.component.ui.tabs.TabHref;
import net.simpleframework.web.page.component.ui.tabs.TabsUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultBlogApplicationModule extends AbstractContentApplicationModule implements IBlogApplicationModule {
	static final String deployName = "content/blog";

	@Override
	protected void putTables(final Map<Class<?>, Table> tables) {
		tables.put(Blog.class, new Table("simple_blog"));
		tables.put(BlogCatalog.class, new Table("simple_blog_catalog"));
		tables.put(BlogRemark.class, new Table("simple_blog_remark"));
	}

	@Override
	public void init(final IInitializer initializer) {
		super.init(initializer);
		doInit(BlogUtils.class, deployName);

		PortalModuleRegistryFactory.regist(BlogPortalModule.class, "blog", getApplicationText(), LocaleI18n.getMessage("BlogUtils.0"),
				BlogUtils.deployPath + "images/blog.png", LocaleI18n.getMessage("BlogUtils.1"));

	}

	@Override
	public String getApplicationText() {
		return StringUtils.text(super.getApplicationText(), LocaleI18n.getMessage("DefaultBlogApplicationModule.0"));
	}

	@Override
	public String getBlogUrl(final PageRequestResponse requestResponse, final IUser user) {
		return null;
	}

	@Override
	public String getBlogViewUrl(final ComponentParameter compParameter, final Blog blog) {
		return null;
	}

	@Override
	public String getTemplatePage(final ComponentParameter compParameter) {
		final StringBuilder sb = new StringBuilder();
		sb.append(NewsPagerUtils.getHomePath()).append("/jsp/news_template_");
		final INewsPagerHandle nHandle = (INewsPagerHandle) compParameter.getComponentHandle();
		final Blog blog = nHandle.getEntityBeanByRequest(compParameter);
		sb.append(blog != null ? blog.getViewTemplate().ordinal() : 0).append(".jsp");
		return sb.toString();
	}

	@Override
	public NewsPagerBean getComponentBean(final PageRequestResponse requestResponse) {
		return (NewsPagerBean) AbstractComponentBean.getComponentBeanByName(requestResponse, BlogUtils.deployPath + "jsp/blog_home.xml",
				"__g_blog_pager");
	}

	@Override
	public NewsPagerBean getMyBlogPagerBean(final PageRequestResponse requestResponse) {
		return (NewsPagerBean) AbstractComponentBean.getComponentBeanByName(requestResponse, BlogUtils.deployPath + "jsp/my_blog_c.xml",
				"__my_blog_pager");
	}

	@Override
	public Class<?> getEntityBeanClass() {
		return Blog.class;
	}

	@Override
	public String getPagerHandleClass() {
		return StringUtils.text(super.getPagerHandleClass(), BlogPagerHandle.class.getName());
	}

	@Override
	public String tabs(final PageRequestResponse requestResponse) {
		final String applicationUrl = getApplicationUrl(requestResponse);

		final List<TabHref> tabHrefs = new ArrayList<TabHref>();
		tabHrefs.add(new TabHref("#(ContentUtils.all)", applicationUrl));

		TabHref tabHref = new TabHref(EContentType.recommended.toString(), WebUtils.addParameters(applicationUrl, "t=recommended"));
		tabHrefs.add(tabHref);
		if (ItSiteUtil.isManage(requestResponse)) {
//			tabHref = new TabHref("审核", WebUtils.addParameters(applicationUrl, "t=audit"));
//			tabHrefs.add(tabHref);
		}
		final IAccount login = AccountSession.getLogin(requestResponse.getSession());
		tabHref = new TabHref("#(DefaultAttentionApplicationModule.0)", login == null ? LoginUtils.getLocationPath() : WebUtils.addParameters(
				applicationUrl, "t=attention"));
		tabHrefs.add(tabHref);

		tabHref = new TabHref("#(BlogUtils.2)", login == null ? LoginUtils.getLocationPath() : getBlogUrl(requestResponse, login.user()));
		tabHref.setMatchMethod(EMatchMethod.startsWith);
		tabHrefs.add(tabHref);

		final IAccount sAccount = ContentUtils.getAccountAware().getSpecifiedAccount(requestResponse);
		if (sAccount != null && login != null && !login.equals(sAccount)) {
//			tabHref = new TabHref(StringUtils.substring(sAccount.user().getText(), 8), getBlogUrl(requestResponse, sAccount.user()));
//			tabHrefs.add(tabHref);
		}
		return TabsUtils.tabs(requestResponse, tabHrefs.toArray(new TabHref[tabHrefs.size()]));
	}

	protected void doAttentionMail(final ComponentParameter compParameter, final ISentCallback attentionMail) {
		((ITaskExecutorAware) PageUtils.pageContext.getApplication()).getTaskExecutor().execute(new ExecutorRunnable() {
			@Override
			public void task() {
				AttentionUtils.sentMessage(compParameter, EFunctionModule.blog, attentionMail);
			}
		});
	}

	protected void doAttentionMail(final ComponentParameter compParameter, final ISendMail sendMail) {
		((ITaskExecutorAware) getApplication()).getTaskExecutor().execute(new ExecutorRunnable() {
			@Override
			public void task() {
				final MailMessageNotification mailMessage = new MailMessageNotification();
				mailMessage.setHtmlContent(true);
				final IUser user = sendMail.getUser();
				if (sendMail.isSent(user)) {
					mailMessage.getTo().add(user);
					mailMessage.setSubject(sendMail.getSubject(user));
					mailMessage.setTextBody(sendMail.getBody(user));
					NotificationUtils.sendMessage(mailMessage);
				}
			}
		});
	}

	@Override
	public void doAttentionSent(final ComponentParameter compParameter, final RemarkItem remark, Class<?> classBean) {
		final Blog blog = BlogUtils.getTableEntityManager(Blog.class).queryForObjectById(remark.getDocumentId(), Blog.class);

		doAttentionMail(compParameter, new AttentionContentMail(compParameter, blog) {
			@Override
			public boolean isSent(final IUser toUser) {
				return !toUser.getId().equals2(remark.getUserId());
			}

			@Override
			public String getSubject(final IUser toUser) {
				final StringBuilder sb = new StringBuilder();
				sb.append("\"").append(remark.getUserText()).append("\"").append(LocaleI18n.getMessage("DefaultNewsPagerHandle.1"));
				return sb.toString();
			}

			@Override
			public String getBody(final IUser toUser) {
				final Map<String, Object> variable = new HashMap<String, Object>();
				variable.put("topicHref", linkUrl(getBlogViewUrl(compParameter, blog)));
				variable.put("toUser", toUser);
				variable.put("createUser", newsBean.getUserText());
				variable.put("createDate", ConvertUtils.toDateString(newsBean.getCreateDate()));
				variable.put("fromUser", remark.getUserText());
				variable.put("topic", newsBean.getTopic());
				variable.put("content", remark.getContent());
				return getFromTemplate(variable, AItSiteAppclicationModule.class);
			}
		});
	}

}
