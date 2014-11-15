package net.simpleframework.my.space;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.UniqueValue;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.content.ResourceLobBean;
import net.simpleframework.content.bbs.BbsUtils;
import net.simpleframework.content.blog.BlogUtils;
import net.simpleframework.content.component.topicpager.ETopicQuery;
import net.simpleframework.content.component.vote.Vote;
import net.simpleframework.content.component.vote.VoteUtils;
import net.simpleframework.core.ExecutorRunnable;
import net.simpleframework.core.IInitializer;
import net.simpleframework.core.ITaskExecutorAware;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.core.id.ID;
import net.simpleframework.core.id.LongID;
import net.simpleframework.my.dialog.SimpleDialog;
import net.simpleframework.my.dialog.SimpleDialogItem;
import net.simpleframework.my.file.MyFileUtils;
import net.simpleframework.my.friends.FriendsUtils;
import net.simpleframework.my.home.MyHomeUtils;
import net.simpleframework.my.message.MessageUtils;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.organization.account.IGetAccountAware;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.DateUtils;
import net.simpleframework.util.IoUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.AbstractWebApplicationModule;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.IWebApplicationModule;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.PageRequestResponse;
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
public class DefaultSpaceApplicationModule extends AbstractWebApplicationModule implements ISpaceApplicationModule, IGetAccountAware {
	static final String deployName = "my/space";

	@Override
	protected void putTables(final Map<Class<?>, Table> tables) {
		tables.put(SpaceStatBean.class, new Table("simple_my_space_stat"));
		tables.put(SimpleDialog.class, new Table("simple_dialog"));
		tables.put(SimpleDialogItem.class, new Table("simple_dialog_item"));
		tables.put(SapceLogBean.class, new Table("simple_my_space_log"));
		tables.put(UserAttentionBean.class, new Table("simple_user_attention", new String[] { "userid", "attentionid" }));
		tables.put(SapceRemarkBean.class, new Table("simple_my_space_remark"));
		tables.put(SapceResourceBean.class, new Table("simple_my_space_resource"));
		tables.put(ResourceLobBean.class, new Table("simple_resource_lob", true));
		VoteUtils.putTables(tables);
	}

	@Override
	public void init(final IInitializer initializer) {
		super.init(initializer);
		doInit(MySpaceUtils.class, deployName);
		((ITaskExecutorAware) getApplication()).getTaskExecutor().addScheduledTask(60 * 5, DateUtils.DAY_PERIOD, new ExecutorRunnable() {
			@Override
			public void task() {
				MySpaceUtils.doStatRebuild();
			}
		});

		PortalModuleRegistryFactory.regist(SpacePortalModule.class, "space_log", "最新动态", "信息", MySpaceUtils.deployPath + "/images/space.png", "最新动态");
	}

	@Override
	public String getSpaceUrl(final PageRequestResponse requestResponse, final IUser user) {
		return null;
	}

	@Override
	public String getApplicationText() {
		return StringUtils.text(super.getApplicationText(), LocaleI18n.getMessage("DefaultSpaceApplicationModule.0"));
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
		final String blogUrl = BlogUtils.applicationModule.getBlogUrl(requestResponse, user);
		sb.append("<a class=\"a2\" href=\"").append(requestResponse.wrapContextPath(WebUtils.addParameters(blogUrl, "op_act=add")))
				.append("\" style=\"float:right\">").append(LocaleI18n.getMessage("DefaultSpaceApplicationModule.5")).append("</a>");
		sb.append(MySpaceUtils.buildSpaceLink(requestResponse, "blog.gif", blogUrl, BlogUtils.applicationModule.getApplicationText()));
		sb.append("</div><div>");
		sb.append(MySpaceUtils.buildSpaceLink(requestResponse, "forum.gif",
				BbsUtils.applicationModule.getTopicUrl2(requestResponse, user, ETopicQuery.onlyTopic),
				BbsUtils.applicationModule.getApplicationText()));
		sb.append("</div><div>");
		sb.append(MySpaceUtils.buildSpaceLink(requestResponse, "file.png", MyFileUtils.applicationModule));
		sb.append("</div></td>");
		// col 2
		sb.append("<td width=\"1px\" style=\"border-left: 3px double #aaa;\"></td>");
		// col 3
		sb.append("<td valign=\"top\"><div>");
		sb.append(MySpaceUtils.buildSpaceLink(requestResponse, "friends.png", FriendsUtils.applicationModule));
		sb.append("</div><div>");
		sb.append(MySpaceUtils.buildSpaceLink(requestResponse, "message.png", MessageUtils.applicationModule));
		sb.append("</div></td></tr></table>");
		return sb.toString();
	}

	@Override
	public String getUserNavigationPath(final PageRequestResponse requestResponse) {
		return "user_nav_tooltip.jsp";
	}

	private TabHref createTabHref(final PageRequestResponse requestResponse, final IWebApplicationModule applicationModule) {
		return new TabHref(applicationModule.getApplicationText(), applicationModule.getApplicationUrl(requestResponse));
	}

	@Override
	public String tabs(final PageRequestResponse requestResponse) {
		final ArrayList<TabHref> al = new ArrayList<TabHref>();
		final IAccount account = getAccount(requestResponse);
		IUser user = null;
		if (account != null) {
			user = account.user();
		}
		if (user != null) {
			final String spaceUrl = getSpaceUrl(requestResponse, user);
			al.add(new TabHref("#(DefaultSpaceApplicationModule.1)", spaceUrl));
			final TabHref bbsTab = new TabHref(BbsUtils.applicationModule.getApplicationText(), WebUtils.addParameters(spaceUrl, "t=bbs"));
			bbsTab.setMatchMethod(EMatchMethod.startsWith);
			al.add(bbsTab);
		}
		if (isMyAccount(requestResponse)) {
			//			al.add(createTabHref(requestResponse, MyHomeUtils.applicationModule));
			al.add(createTabHref(requestResponse, FriendsUtils.applicationModule));
			al.add(createTabHref(requestResponse, MessageUtils.applicationModule));
		}
		return TabsUtils.tabs(requestResponse, al.toArray(new TabHref[al.size()]));
	}

	@Override
	public String logTabs(final PageRequestResponse requestResponse) {
		final IUser user = getAccount(requestResponse).user();
		final String spaceUrl = getSpaceUrl(requestResponse, user);
		return TabsUtils.tabs(requestResponse, new TabHref("#(DefaultSpaceApplicationModule.2)", WebUtils.addParameters(spaceUrl, "lt=attention")),
				new TabHref("#(DefaultSpaceApplicationModule.3)", WebUtils.addParameters(spaceUrl, "lt=friends")), new TabHref(
						"#(DefaultSpaceApplicationModule.4)", WebUtils.addParameters(spaceUrl, "lt=my")));
	}

	@Override
	public String attentionTabs(PageRequestResponse requestResponse) {
		final ArrayList<TabHref> al = new ArrayList<TabHref>();
		final IUser user = getAccount(requestResponse).user();
		String spaceUrl = "/attention.html";
		if (user != null) {
			spaceUrl = "/attention/" + user.getId() + ".html";
		}
		final String text = MySpaceUtils.getSexText(requestResponse, user.getSex());
		if (user != null) {
			al.add(new TabHref(text + "的关注", WebUtils.addParameters(spaceUrl, "t=attentions")));
			al.add(new TabHref(text + "的粉丝", WebUtils.addParameters(spaceUrl, "t=fans")));
		}
		return TabsUtils.tabs(requestResponse, al.toArray(new TabHref[al.size()]));
	}

	/************************** IGetAccountAware **************************/

	@Override
	public IAccount getSpecifiedAccount(final PageRequestResponse requestResponse) {
		return OrgUtils.am().queryForObjectById(requestResponse.getRequestParameter(OrgUtils.um().getUserIdParameterName()));
	}

	@Override
	public boolean isMyAccount(final PageRequestResponse requestResponse) {
		final IAccount login = AccountSession.getLogin(requestResponse.getSession());
		if (login == null) {
			return false;
		}
		final IAccount account = getSpecifiedAccount(requestResponse);
		return account == null || account.equals2(login);
	}

	@Override
	public IAccount getAccount(final PageRequestResponse requestResponse) {
		IAccount account = getSpecifiedAccount(requestResponse);
		if (account == null) {
			account = AccountSession.getLogin(requestResponse.getSession());
		}
		return account;
	}

	@Override
	public String wrapAccountHref(final PageRequestResponse requestResponse, final Object userObject, final String text) {
		final StringBuilder sb = new StringBuilder();
		final boolean linkSpace = ConvertUtils.toBoolean(requestResponse.getRequestParameter("linkSpace"), true);
		final boolean showTip = ConvertUtils.toBoolean(requestResponse.getRequestParameter("showTip"), true);
		final IUser user = OrgUtils.getUserByObject(userObject);
		if (linkSpace && user != null) {
			sb.append("<a title='" + user.getText() + "(" + user.getId() + ")" + "' tipParam=\"userId=").append(user.getId());
			sb.append("\" class=\"a2");
			if (text == null || !text.startsWith("<img") || showTip) {
				sb.append(" user_navTooltip_class");
			}
			sb.append("\" href=\"");
			sb.append(getSpaceUrl(requestResponse, user));
			sb.append("\">");
		}
		sb.append(StringUtils.text(text, StringUtils.blank(user == null ? "匿名" : user)));
		if (linkSpace && user != null) {
			sb.append("</a>");
		}
		return sb.toString();
	}

	@Override
	public String wrapAccountHref(final PageRequestResponse requestResponse, final Object userObject) {
		return wrapAccountHref(requestResponse, userObject, null);
	}

	@Override
	public String wrapImgAccountHref(final PageRequestResponse requestResponse, final Object userObject, final int width, final int height) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<img class=\"photo_icon\" style=\"width: ").append(width).append("px; height: ").append(height).append("px;\" src=\"");
		sb.append(OrgUtils.getPhotoSRC(requestResponse.request, userObject));
		sb.append("\"></img>");
		return wrapAccountHref(requestResponse, userObject, sb.toString());
	}

	@Override
	public String wrapImgAccountHref(final PageRequestResponse requestResponse, final Object userObject) {
		return wrapImgAccountHref(requestResponse, userObject, 64, 64);
	}

	// do method

	@Override
	public <T extends IDataObjectBean> T getBeanById(final Object id, final Class<T> beanClazz) {
		return MySpaceUtils.getTableEntityManager(beanClazz).queryForObjectById(id, beanClazz);
	}

	@Override
	public SapceLogBean addSapceLog(final PageRequestResponse requestResponse, final String content, final EFunctionModule refModule, final ID refId,
			final boolean transaction) {
		final HttpSession httpSession = requestResponse.getSession();
		final IAccount account = AccountSession.getLogin(httpSession);
		//		if (account == null) {
		//			return null;
		//		}
		final ITableEntityManager log_mgr = MySpaceUtils.getTableEntityManager(SapceLogBean.class);
		final SapceLogBean sapceLog = new SapceLogBean();

		if (refModule == EFunctionModule.space_log) {
			// vote
			final ID voteId = (ID) requestResponse.getSessionAttribute(SpaceVoteHandle.SESSION_VOTE);
			if (voteId != null) {
				final Vote vote = getBeanById(voteId, Vote.class);
				if (vote != null) {
					sapceLog.setId(vote.getDocumentId());
				}
			}
		}

		sapceLog.setUserId(account == null ? LongID.zero : account.getId());
		sapceLog.setCreateDate(new Date());
		String newLine = content, ss;
		final Set<Character> set = new HashSet<Character>();
		final List<IUser> userList = new ArrayList<IUser>();
		set.add('@');
		set.add(',');
		set.add(';');
		set.add(' ');
		int t = 0;
		boolean flag = false;
		try {
			for (int i = 0; i < content.length(); i++) {
				if (set.contains(content.charAt(i))) {
					if (t != i && flag) {
						ss = content.substring(t + 1, i);
						IUser user = OrgUtils.um().queryForObjectById(ss);
						if (user == null)
							user = OrgUtils.um().getUserByName(ss);
						if (user.getId().equals2(sapceLog.getUserId())) {
							continue;
						}
						flag = false;
						if (user != null) {
							if (!userList.contains(user)) {
								userList.add(user);
								newLine = newLine.replaceAll(ss, MySpaceUtils.getAccountAware().wrapAccountHref(requestResponse, user));
							}
						}
					}
				}
				if (content.charAt(i) == '@') {
					t = i;
					flag = true;
				}
			}
			if (flag) {
				ss = content.substring(t + 1, content.length());
				IUser user = OrgUtils.um().queryForObjectById(ss);
				if (user == null)
					user = OrgUtils.um().getUserByName(ss);
				if (!user.getId().equals2(sapceLog.getUserId())) {
					flag = false;
					if (user != null) {
						if (!userList.contains(user)) {
							userList.add(user);
							newLine = newLine.replaceAll(ss, MySpaceUtils.getAccountAware().wrapAccountHref(requestResponse, user));
						}
					}
				}
			}
		} catch (Exception e) {
		}
		sapceLog.setContent(newLine);
		sapceLog.setRefModule(refModule);
		sapceLog.setRefId(refId);
		if (transaction) {
			log_mgr.insertTransaction(sapceLog, new TableEntityAdapter() {
				@Override
				public void afterInsert(final ITableEntityManager manager, final Object[] objects) {
					insertResource(requestResponse, sapceLog);
				}
			});
		} else {
			log_mgr.insert(sapceLog);
			insertResource(requestResponse, sapceLog);
		}
		for (final IUser user : userList) {
			try {
				final StringBuffer textBody = new StringBuffer();
				textBody.append(newLine).append("<br/>");
				final String subject = "用户:" + user.getText() + "@提到我,(" + ConvertUtils.toDateString(new Date(), "yyyy-MM-dd HH:mm") + ")";
				//			NotificationUtils.createSystemMessageNotification(sapceLog.getUserId(), user.getId(), subject, textBody.toString(), sapceLog.getId());
				MessageUtils.createNotifation(new ComponentParameter(requestResponse.request, requestResponse.response, null), subject,
						textBody.toString(), sapceLog.getUserId(), user.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sapceLog;
	}

	private void insertResource(final PageRequestResponse requestResponse, final SapceLogBean sapceLog) {
		final HttpSession httpSession = requestResponse.getSession();
		final File uploadDir = MySpaceUtils.getUploadDir(httpSession);
		final File[] files = uploadDir.listFiles();
		if (files != null) {
			for (final File file : files) {
				FileInputStream inputStream = null;
				try {
					inputStream = new FileInputStream(file);
					final SapceResourceBean resource = new SapceResourceBean();
					resource.setLogResource(ELogResource.img_upload);
					resource.setLogId(sapceLog.getId());
					resource.setResourceUrl(file.getName());
					MySpaceUtils.getTableEntityManager(SapceResourceBean.class).insert(resource);

					final ResourceLobBean lob = new ResourceLobBean();
					lob.setId(resource.getId());
					lob.setLob(inputStream);
					MySpaceUtils.getTableEntityManager(ResourceLobBean.class).insert(lob);
				} catch (final FileNotFoundException e) {
					continue;
				} finally {
					if (inputStream != null) {
						try {
							inputStream.close();
						} catch (final IOException e) {
						}
					}
				}
			}
			try {
				IoUtils.deleteAll(uploadDir);
			} catch (final IOException e) {
			}
		}

		final Set<String> urls = MySpaceUtils.getSessionUrls(httpSession);
		for (final String url : urls) {
			final SapceResourceBean resource = new SapceResourceBean();
			resource.setLogResource(ELogResource.img_url);
			resource.setLogId(sapceLog.getId());
			resource.setResourceUrl(url);
			MySpaceUtils.getTableEntityManager(SapceResourceBean.class).insert(resource);
		}
		urls.clear();
	}

	@Override
	public int deleteSapceLog(final PageRequestResponse requestResponse, final Object id) {
		final ITableEntityManager log_mgr = MySpaceUtils.getTableEntityManager(SapceLogBean.class);
		final ITableEntityManager remark_mgr = MySpaceUtils.getTableEntityManager(SapceRemarkBean.class);
		return log_mgr.deleteTransaction(new UniqueValue(id), new TableEntityAdapter() {
			@Override
			public void afterDelete(final ITableEntityManager manager, final IDataObjectValue dataObjectValue) {
				remark_mgr.delete(new ExpressionValue("logid=?", dataObjectValue.getValues()));
			}
		});
	}

	@Override
	public IQueryEntitySet<SapceRemarkBean> remarkList(final SapceLogBean sapceLog) {
		final ITableEntityManager remark_mgr = MySpaceUtils.getTableEntityManager(SapceRemarkBean.class);
		return remark_mgr.query(new ExpressionValue("logid=? order by createdate desc", new Object[] { sapceLog.getId() }), SapceRemarkBean.class);
	}

	@Override
	public SapceRemarkBean addSapceLogRemark(final PageRequestResponse requestResponse, final SapceLogBean sapceLog, final String content,
			final boolean opt1) {
		final IAccount account = AccountSession.getLogin(requestResponse.getSession());
		if (account == null) {
			return null;
		}
		final ITableEntityManager log_mgr = MySpaceUtils.getTableEntityManager(SapceLogBean.class);
		final ITableEntityManager remark_mgr = MySpaceUtils.getTableEntityManager(SapceRemarkBean.class);
		final SapceRemarkBean remark = new SapceRemarkBean();
		remark.setLogId(sapceLog.getId());
		remark.setContent(content);
		remark.setUserId(account.getId());
		remark.setCreateDate(new Date());
		return remark_mgr.insertTransaction(remark, new TableEntityAdapter() {
			@Override
			public void afterInsert(final ITableEntityManager manager, final Object[] objects) {
				sapceLog.setRemarks(sapceLog.getRemarks() + 1);
				log_mgr.update(new Object[] { "remarks" }, sapceLog);
				if (opt1) {
					final SapceLogBean sapceLog2 = new SapceLogBean();
					sapceLog2.setContent(content);
					sapceLog2.setUserId(account.getId());
					sapceLog2.setCreateDate(new Date());
					sapceLog2.setRefModule(EFunctionModule.space_log);
					sapceLog2.setReplyFrom(sapceLog.getId());
					log_mgr.insert(sapceLog2);
				}
			}
		}) > 0 ? remark : null;
	}

	@Override
	public int deleteSapceLogRemark(final PageRequestResponse requestResponse, final Object remarkid) {
		final ITableEntityManager remark_mgr = MySpaceUtils.getTableEntityManager(SapceRemarkBean.class);
		final SapceRemarkBean remark = remark_mgr.queryForObjectById(remarkid, SapceRemarkBean.class);
		if (remark == null) {
			return 0;
		}
		final ITableEntityManager log_mgr = MySpaceUtils.getTableEntityManager(SapceLogBean.class);
		return remark_mgr.deleteTransaction(new UniqueValue(remarkid), new TableEntityAdapter() {
			@Override
			public void afterDelete(final ITableEntityManager manager, final IDataObjectValue dataObjectValue) {
				final SapceLogBean sapceLog = log_mgr.queryForObjectById(remark.getLogId(), SapceLogBean.class);
				if (sapceLog != null) {
					sapceLog.setRemarks(sapceLog.getRemarks() - 1);
					log_mgr.update(new Object[] { "remarks" }, sapceLog);
				}
			}
		});
	}

	@Override
	public IQueryEntitySet<SapceResourceBean> resourceList(final SapceLogBean sapceLog) {
		return MySpaceUtils.getTableEntityManager(SapceResourceBean.class).query(new ExpressionValue("logId=?", new Object[] { sapceLog.getId() }),
				SapceResourceBean.class);
	}

	@Override
	public UserAttentionBean getUserAttentionById(final PageRequestResponse requestResponse, final Object attentionId) {
		final IAccount account = AccountSession.getLogin(requestResponse.getSession());
		if (account == null) {
			return null;
		}
		final ITableEntityManager attention_mgr = MySpaceUtils.getTableEntityManager(UserAttentionBean.class);
		return attention_mgr.queryForObject(new UniqueValue(new Object[] { account.getId(), attentionId }), UserAttentionBean.class);
	}

	@Override
	public void addUserAttention(final PageRequestResponse requestResponse, final Object attentionId) {
		UserAttentionBean attentionBean = getUserAttentionById(requestResponse, attentionId);
		if (attentionBean == null) {
			final IAccount account = AccountSession.getLogin(requestResponse.getSession());
			final ITableEntityManager attention_mgr = MySpaceUtils.getTableEntityManager(UserAttentionBean.class);
			final ITableEntityManager stat_mgr = MySpaceUtils.getTableEntityManager(SpaceStatBean.class);
			attentionBean = new UserAttentionBean();
			attentionBean.setCreateDate(new Date());
			attentionBean.setUserId(account.getId());
			final ID _attentionId = attention_mgr.getTable().newID(attentionId);
			attentionBean.setAttentionId(_attentionId);
			attention_mgr.insertTransaction(attentionBean, new TableEntityAdapter() {
				@Override
				public void afterInsert(final ITableEntityManager manager, final Object[] objects) {
					final SpaceStatBean stat = MySpaceUtils.getSpaceStatById(account.getId());
					stat.setAttentions(stat.getAttentions() + 1);
					stat_mgr.update(stat);
					final SpaceStatBean stat2 = MySpaceUtils.getSpaceStatById(_attentionId);
					stat2.setFans(stat2.getFans() + 1);
					stat_mgr.update(stat2);
				}
			});
		}
	}

	@Override
	public void deleteUserAttention(final PageRequestResponse requestResponse, final Object attentionId) {
		final UserAttentionBean attentionBean = getUserAttentionById(requestResponse, attentionId);
		if (attentionBean != null) {
			final IAccount account = AccountSession.getLogin(requestResponse.getSession());
			final ITableEntityManager attention_mgr = MySpaceUtils.getTableEntityManager(UserAttentionBean.class);
			final ITableEntityManager stat_mgr = MySpaceUtils.getTableEntityManager(SpaceStatBean.class);
			final ID _attentionId = attentionBean.getAttentionId();
			attention_mgr.deleteTransaction(new UniqueValue(new Object[] { account.getId(), attentionId }), new TableEntityAdapter() {
				@Override
				public void afterDelete(final ITableEntityManager manager, final IDataObjectValue dataObjectValue) {
					final SpaceStatBean stat = MySpaceUtils.getSpaceStatById(account.getId());
					stat.setAttentions(stat.getAttentions() - 1);
					stat_mgr.update(stat);
					final SpaceStatBean stat2 = MySpaceUtils.getSpaceStatById(_attentionId);
					stat2.setFans(stat2.getFans() - 1);
					stat_mgr.update(stat2);
				}
			});
		}
	}
}
