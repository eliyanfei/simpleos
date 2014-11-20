package net.itsite.document.docu;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.itsite.ItSiteUtil;
import net.itsite.impl.AbstractCatalog;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.ado.lucene.AbstractLuceneManager;
import net.simpleframework.applets.attention.AttentionBean;
import net.simpleframework.applets.attention.AttentionUtils;
import net.simpleframework.content.EContentStatus;
import net.simpleframework.content.EContentType;
import net.simpleframework.content.component.filepager.FileBean;
import net.simpleframework.content.component.filepager.FilePagerRegistry;
import net.simpleframework.content.component.filepager.FilePagerUtils;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.id.ID;
import net.simpleframework.my.space.MySpaceUtils;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.Account;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.EAccountStatus;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.organization.account.IGetAccountAware;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.util.HTTPUtils;
import net.simpleframework.util.IoUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.ComponentParameter;

public final class DocuUtils {
	public static String docuPath = "f:\\data";//文件存储路径
	public static String $data$ = "$data$";
	public static String docuId = "docuId";
	public static String deploy = "/app/docu";
	public static IDocuApplicationModule applicationModule;
	public static long docuCounter = 0;// 总的文档数量

	public static void setDocuCounter(int wsCounter) {
		DocuUtils.docuCounter = wsCounter;
	}

	public static InputStream getInputStream(String fileName) {
		File file = new File(fileName);
		if (file.exists()) {
			try {
				return new FileInputStream(file);
			} catch (Exception e) {
			}
		}
		return null;
	}

	public static void doStatRebuild() {
		final ITableEntityManager tMgr = applicationModule.getDataObjectManager(DocuBean.class);
		// 更新评论和分数
		final StringBuffer sql1 = new StringBuffer();
		sql1.append("update ").append(DocuApplicationModule.docu_documentshare.getName()).append(" d set remarks=(select count(id) from ");
		sql1.append(DocuApplicationModule.docu_remark.getName()).append(" where documentid=d.id) where d.status=?");
		tMgr.execute(new SQLValue(sql1.toString(), new Object[] { EContentStatus.publish }));
		sql1.setLength(0);
		// 更新关注
		sql1.append("update ").append(DocuApplicationModule.docu_documentshare.getName()).append(" d set attentions=(select count(id) from ");
		sql1.append(AttentionUtils.getTableEntityManager(AttentionBean.class).getTablename()).append(
				" where attentionId=d.id and vtype=" + EFunctionModule.docu.ordinal() + ")  where d.status=?");
		tMgr.execute(new SQLValue(sql1.toString(), new Object[] { EContentStatus.publish }));
		sql1.setLength(0);
		// 更新发布记录
		sql1.append("update ").append(DocuApplicationModule.docu_user.getName()).append(" d set upFiles=(select count(id) from ");
		sql1.append(DocuApplicationModule.docu_documentshare.getName()).append("  where status=? and userId=d.userId)");
		tMgr.execute(new SQLValue(sql1.toString(), new Object[] { EContentStatus.publish }));
		sql1.setLength(0);
		// 更新目录
		sql1.append("update ").append(DocuApplicationModule.docu_catalog.getName()).append(" osc set osc.counter=(select count(id) from ");
		sql1.append(DocuApplicationModule.docu_documentshare.getName()).append(" where status=? and catalogId=osc.id and osc.parentid<>0)");
		try {
			tMgr.execute(new SQLValue(sql1.toString(), new Object[] { EContentStatus.publish }));
		} catch (Exception e) {
		}
		sql1.setLength(0);
		sql1.append("update ").append(DocuApplicationModule.docu_catalog.getName())
				.append(" osc set counter=(select sum(osc1.counter) from ( SELECT counter,parentid from ");
		sql1.append(DocuApplicationModule.docu_catalog.getName()).append(") osc1 where osc1.parentid=osc.id ) where osc.parentid=0");
		try {
			tMgr.execute(new SQLValue(sql1.toString()));
		} catch (Exception e) {
		}
		tMgr.reset();
	}

	public static void addDocuCounter(int value) {
		synchronized (applicationModule) {
			DocuUtils.docuCounter += value;
		}
	}

	public static final int statWebstite() {
		final ITableEntityManager tMgr = applicationModule.getDataObjectManager();
		return tMgr.getCount(new ExpressionValue("status="));
	}

	public static String buildTitle(final PageRequestResponse requestResponse, final String catalogId) {
		final int t = ConvertUtils.toInt(requestResponse.getRequestParameter("t"), 0);
		final int s = ConvertUtils.toInt(requestResponse.getRequestParameter("s"), 0);
		final int od = ConvertUtils.toInt(requestResponse.getRequestParameter("od"), 0);
		final StringBuffer param = new StringBuffer();
		param.append(t).append("-").append(s).append("-").append(od).append("-0");
		final String c = requestResponse.getRequestParameter("c");
		final StringBuffer buf = new StringBuffer();
		buf.append("<a href=\"/docu.html\">" + LocaleI18n.getMessage("Docu.util.6") + "</a>");
		buf.append(HTMLBuilder.NAV);
		final DocuCatalog catalog = applicationModule.getBean(DocuCatalog.class, catalogId);
		if (catalog != null) {
			buildTitle(requestResponse, buf, catalog, param.toString());
			buf.append("<a href='/docu/").append(param).append("-").append(catalog.getId()).append(".html").append("'>");
			buf.append(catalog.getText());
			buf.append("</a>");
		}
		if (StringUtils.hasText(c)) {
			buf.append(HTMLBuilder.NAV);
			buf.append(LocaleI18n.getMessage("Docu.util.7"));
		}
		return buf.toString();
	}

	public static void buildTitle(final PageRequestResponse requestResponse, final StringBuffer buf, final DocuCatalog catalog, final String param) {
		final DocuCatalog pCatalog = (DocuCatalog) catalog.parent(applicationModule);
		if (pCatalog != null) {
			buildTitle(requestResponse, buf, pCatalog, param);
			buf.append("<a href='/docu/").append(param).append("-").append(pCatalog.getId()).append(".html").append("'>");
			buf.append(pCatalog.getText());
			buf.append("</a>");
			buf.append(HTMLBuilder.NAV);
		}
	}

	public static final IDataObjectQuery<?> queryDocu(final PageRequestResponse requestResponse, final DocuBean docuBean, final String docu,
			final String type, final int order, final int _tab_param) {
		if ("lucene".equals(type)) {
			final String title = WebUtils.toLocaleString(requestResponse.getRequestParameter("title"));
			try {
				return queryRelatedDocu(requestResponse, title);
			} catch (IOException e) {
			}
		}
		final int catalogId = ConvertUtils.toInt(requestResponse.getRequestParameter("catalogId"), 0);
		final ITableEntityManager tMgr = applicationModule.getDataObjectManager();
		final List<Object> ol = new ArrayList<Object>();
		final StringBuffer sql = new StringBuffer();
		final StringBuffer where = new StringBuffer();
		final StringBuffer orderBuf = new StringBuffer();
		sql.append("select * from it_document where ");
		if (catalogId != 0) {
			if (where.length() != 0)
				where.append(" and ");
			where.append("  catalogId in(").append(AbstractCatalog.Utils.getJoinCatalog(catalogId, DocuUtils.applicationModule, DocuCatalog.class))
					.append(")");
		}
		if ("userPoint".equals(type)) {
			return OrgUtils.getTableEntityManager(Account.class).query(
					new ExpressionValue("status=? order by points desc", new Object[] { EAccountStatus.normal }), Account.class);
		} else if (_tab_param == 0) {
			orderBuf.append("createDate desc");
		} else if (_tab_param == 1) {
			orderBuf.append("views desc");
		} else if (_tab_param == 2) {
			orderBuf.append("downCounter desc");
		} else if ("new".equals(type) || order == 0) {
			orderBuf.append("createDate desc");
		} else if ("download".equals(type) || order == 2) {
			orderBuf.append("downCounter desc");
		} else if ("grade".equals(type)) {
			orderBuf.append("totalGrade desc");
		} else if ("view".equals(type) || order == 1) {
			orderBuf.append("views desc");
		} else if ("recommended".equals(type)) {
			if (where.length() != 0)
				where.append(" and ");
			where.append("  ttype=?");
			ol.add(EContentType.recommended);
			orderBuf.append("createDate desc");
		} else if ("same".equals(type)) {
			if (where.length() != 0)
				where.append(" and ");
			where.append("  sameId=? and id<>? and sameId<>0");
			ol.add(docuBean.getSameId());
			ol.add(docuBean.getId());
		} else {
			orderBuf.append("createDate desc");
		}
		if (where.length() != 0)
			where.append(" and ");
		where.append(" status=?");
		sql.append(where);
		ol.add(EContentStatus.publish);
		if (orderBuf.length() != 0)
			sql.append(" order by ").append(orderBuf);
		return tMgr.query(new SQLValue(sql.toString(), ol.toArray(new Object[] {})), DocuBean.class);
	}

	public static final IDataObjectQuery<?> queryDocu(final PageRequestResponse requestResponse, final DocuBean docuBean, final String docu,
			final String type) throws Exception {
		return queryDocu(requestResponse, docuBean, docu, type, -1, -1);
	}

	public static String wrapOpenLink(final PageRequestResponse requestResponse, final DocuBean docuBean) {
		final StringBuffer buf = new StringBuffer();
		if (docuBean != null) {
			buf.append("<a target=\"_blank\"");
			buf.append(" href=\"" + applicationModule.getViewUrl(docuBean.getId()) + "\"");
			if (requestResponse != null && ConvertUtils.toBoolean(requestResponse.getRequestAttribute("home"), false)) {
				buf.append(" title=\"").append(docuBean.getTitle()).append("\"");
				buf.append(">");
				buf.append(docuBean.getTitle());
			} else {
				buf.append(">");
				buf.append(docuBean.getTitle());
			}
			buf.append("</a>");
		}
		return buf.toString();
	}

	/**
	 * 获得该用户当前存储目录
	 * 
	 * @param userId
	 * @return
	 */
	public static String getDatabase(final Object userId) {
		String path = docuPath + "/" + userId + "/";
		try {
			new File(path).mkdirs();
		} catch (Exception e) {
		}
		return path;
	}

	public static int allowDownloadDocus = 5;//一天最大支持免费下载5个文档

	/**
	 * 下载文件
	 * 
	 * @param requestResponse
	 * @throws Exception
	 */
	public static void doDownload(final PageRequestResponse requestResponse) throws Exception {
		synchronized (AccountSession.getLoginObject(requestResponse.getSession())) {
			final String s = requestResponse.getRequestParameter("s");
			final DocuBean docuBean = DocuUtils.applicationModule.getBean(DocuBean.class, requestResponse.getRequestParameter(DocuUtils.docuId));
			if (docuBean != null) {
				if (ConvertUtils.toLong(s, 0) != docuBean.getFileSize()) {
					return;
				}
				final IAccount account = ItSiteUtil.getLoginAccount(requestResponse);
				final String path1 = DocuUtils.getDatabase(docuBean.getUserId());
				final InputStream is = new FileInputStream(path1 + docuBean.getFileName());
				if (is != null) {
					final OutputStream outputStream = HTTPUtils.getFileOutputStream(requestResponse.request, requestResponse.response,
							docuBean.getFileName(), docuBean.getFileSize());
					try {
						IoUtils.copyStream(is, outputStream);
						docuBean.setDownCounter(docuBean.getDownCounter() + 1);
						DocuUtils.applicationModule.doUpdate(new Object[] { "downCounter" }, docuBean, new TableEntityAdapter() {
							@Override
							public void afterUpdate(ITableEntityManager manager, Object[] objects) {
								//记录改用户下载文档的数量

								final DocuLogBean logBean = new DocuLogBean();
								logBean.setDocuId(docuBean.getId());
								logBean.setIp(ItSiteUtil.getIpAddr(requestResponse.request));
								if (account != null)
									logBean.setUserId(account.getId());
								DocuUtils.applicationModule.doUpdate(logBean);
								// 使总下载文档数+1
								DocuUserBean userBean = DocuUtils.applicationModule.getBeanByExp(DocuUserBean.class, "userId=?",
										new Object[] { docuBean.getUserId() });
								if (userBean == null) {
									userBean = new DocuUserBean();
								}
								userBean.setDownFiles(userBean.getDownFiles() + 1);
								DocuUtils.applicationModule.doUpdate(userBean);
								// 添加全文索引
								DocuUtils.applicationModule.createLuceneManager(
										new ComponentParameter(requestResponse.request, requestResponse.response, null)).objects2DocumentsBackground(
										docuBean);
							}
						});
					} catch (Exception e) {
					} finally {
						is.close();
						outputStream.close();
					}
				}
			}
		}
	}

	/**
	 * 获得文件路径
	 * 
	 * @return
	 */
	public static String getFileImage(final PageRequestResponse requestResponse, final DocuBean docu) {
		final FileBean fileBean = new FileBean();
		fileBean.setFilename(docu.getFileName());
		fileBean.setFiletype(docu.getExtension());
		return AbstractComponentRegistry.getRegistry(FilePagerRegistry.filePager).getResourceHomePath(requestResponse) + "/images/"
				+ FilePagerUtils.getIconW(fileBean).icon;
	}

	public static String documentNav(final PageRequestResponse requestResponse) {
		final StringBuilder buf = new StringBuilder();
		final IAccount account = ItSiteUtil.getLoginAccount(requestResponse);
		String param = StringUtils.text(requestResponse.getRequestParameter(OrgUtils.um().getUserIdParameterName()));
		final IGetAccountAware accountAware = MySpaceUtils.getAccountAware();
		final boolean isMe = accountAware.isMyAccount(requestResponse);
		if (StringUtils.hasText(param)) {
			param = OrgUtils.um().getUserIdParameterName() + "=" + param;
		}
		buildDocumentNav(buf, true, "myDocuTableAct", param,
				(isMe ? LocaleI18n.getMessage("Docu.util.4") : "Ta") + LocaleI18n.getMessage("Docu.util.5"), "myAll", true);
		buildDocumentNav(buf, true, "myUploadAct", param, LocaleI18n.getMessage("Docu.util.2"), "myUpload", true);
		if (account == null) {
			return buf.toString();
		}
		if (isMe) {
			buildDocumentNav(buf, false, "myDownloadAct", "t=4", LocaleI18n.getMessage("Docu.util.3"), "myDownload", false);
		}
		return buf.toString();
	}

	public static String documentManagerNav(final PageRequestResponse requestResponse) {
		final StringBuilder buf = new StringBuilder();
		final IAccount account = ItSiteUtil.getLoginAccount(requestResponse);
		String param = StringUtils.text(requestResponse.getRequestParameter(OrgUtils.um().getUserIdParameterName()));
		if (StringUtils.hasText(param)) {
			param = OrgUtils.um().getUserIdParameterName() + "=" + param;
		}
		buildDocumentNav(buf, true, "allDocuTableAct", "docu_type=all" + param, LocaleI18n.getMessage("Docu.util.1"), "myAll", false);
		if (account == null) {
			return buf.toString();
		}
		final IGetAccountAware accountAware = MySpaceUtils.getAccountAware();
		if (accountAware.isMyAccount(requestResponse)) {
			buf.append("<span style=\"margin: 0px 4px;\">|</span>");
			buildDocumentNav(buf, false, "docuDownAct", param, LocaleI18n.getMessage("Docu.util.0"), "allNotDown", false);
		}
		return buf.toString();
	}

	private static void buildDocumentNav(final StringBuilder buf, final boolean arrow, final String action, final String param, final String text,
			final String name, final boolean nav) {
		buf.append("<a id='").append(name).append("'");
		if (arrow) {
			buf.append("class='a2 nav_arrow'");
		}
		buf.append(" onclick=\"$IT.active(this,'#documentNav a','nav_arrow');$Actions['" + action + "']('" + param + "');\"");
		buf.append(">").append(text).append("</a>");
		if (nav)
			buf.append("<span style=\"margin: 0px 4px;\">|</span>");
	}

	public static int pageCount(String path1, String path2, String docuId) {
		final DocuBean docuBean = DocuUtils.applicationModule.getBean(DocuBean.class, docuId);
		if (docuBean != null)
			return docuBean.getFileNum();
		return 0;
	}

	//重复发布文档信息的情况， 用缓存处理
	static final List<ID> cacheSet = new ArrayList<ID>(10);

	public static IDataObjectQuery<?> queryRelatedDocu(final PageRequestResponse requestResponse, String title) throws IOException {
		final AbstractLuceneManager lm = DocuUtils.applicationModule.createLuceneManager(new ComponentParameter(requestResponse.request,
				requestResponse.response, null));
		return lm.getLuceneQuery(StringUtils.join(lm.getQueryTokens(title), " "));
	}

	/**
	 * 获取消息提醒
	 * @param requestResponse
	 * @return
	 */
	public static String getAuditDocus(final PageRequestResponse requestResponse, int type) {
		final IQueryEntitySet<DocuBean> qs = DocuUtils.applicationModule.queryBean(
				new ExpressionValue("status=?", new Object[] { EDocuStatus.audit }), DocuBean.class);
		final int count = qs.getCount();
		if (count == 0)
			return "";
		final StringBuffer sb = new StringBuffer();
		sb.append("<sup class=\"highlight \">");
		sb.append(count).append("</sup>");
		return sb.toString();
	}

}
