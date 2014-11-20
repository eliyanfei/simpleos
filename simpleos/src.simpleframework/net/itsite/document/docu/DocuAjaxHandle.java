package net.itsite.document.docu;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import net.itsite.ItSiteUtil;
import net.itsite.impl.AbstractCommonAjaxAction;
import net.itsite.utils.IOUtils;
import net.itsite.utils.NumberUtils;
import net.prj.manager.PrjCustomBean;
import net.prj.manager.PrjMgrUtils;
import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.content.IContentApplicationModule;
import net.simpleframework.core.id.ID;
import net.simpleframework.core.id.LongID;
import net.simpleframework.my.message.EMessageType;
import net.simpleframework.my.message.MessageUtils;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;

public class DocuAjaxHandle extends AbstractCommonAjaxAction {

	@Override
	public Object getBeanProperty(ComponentParameter compParameter, String beanProperty) {
		if ("jobExecute".equals(beanProperty)) {
			Object obj = super.getBeanProperty(compParameter, beanProperty);
			if (obj == null)
				return IJob.sj_account_normal;
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	protected EFunctionModule getFunctionModule() {
		return EFunctionModule.docu;
	}

	@Override
	public IForward indexRebuild(final ComponentParameter compParameter) {
		DocuUtils.applicationModule.createLuceneManager(compParameter).rebuildAll(true);
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				json.put("info", LocaleI18n.getMessage("manager_tools.6"));
			}
		});
	}

	@Override
	protected String getAttentionParameter() {
		return DocuUtils.docuId;
	}

	/**
	 * 下载
	 * 
	 * @param compParameter
	 * @return
	 */
	public IForward docuDownload(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(final Map<String, Object> json) throws Exception {
				final DocuBean docuBean = DocuUtils.applicationModule.getBean(DocuBean.class, compParameter.getRequestParameter(DocuUtils.docuId));
				if (docuBean != null) {
					final IUser user = ItSiteUtil.getLoginUser(compParameter);
					//					final IAccount account = ItSiteUtil.getLoginAccount(compParameter);
					json.put("point", docuBean.getPoint());
					json.put("self", docuBean.getUserId().equals2(user.getId()));
					json.put("docuId", compParameter.getRequestParameter("docuId"));
					json.put("s", compParameter.getRequestParameter("s"));
					// 表示该用户已经下载过该文档，不再扣去积分
					if (DocuUtils.applicationModule.queryBean("userId=? and docuId=?", new Object[] { user.getId(), docuBean.getId() },
							DocuLogBean.class).getCount() == 0) {
						final String path1 = DocuUtils.getDatabase(docuBean.getUserId());
						final InputStream is = DocuUtils.getInputStream(path1 + docuBean.getFileName());
						if (is == null) {
							json.put("act", "文件已被删除，等待管理员核查!");
							return;
						} else {
							IOUtils.closeIO(is);
						}
					} else {
						final String path1 = DocuUtils.getDatabase(docuBean.getUserId());
						final InputStream is = DocuUtils.getInputStream(path1 + docuBean.getFileName());
						if (is == null) {
							json.put("act", "文件已被删除，等待管理员核查!");
							return;
						}
						json.put("self", true);
					}
				}
			}
		});
	}

	/**
	 * 删除文档
	 * @param compParameter
	 * @return
	 */
	public IForward docuDelete(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(final Map<String, Object> json) throws Exception {
				final ID userId = ItSiteUtil.getLoginUser(compParameter).getId();
				final String[] docuIds = compParameter.getRequestParameter("docuIds").split(",");
				final IAccount account = OrgUtils.am().getAccountByName("admin");
				final String delReason = compParameter.getParameter("delReason");
				for (final String docuId : docuIds) {
					final DocuBean docuBean = DocuUtils.applicationModule.getBean(DocuBean.class, docuId);
					if (docuBean != null && ItSiteUtil.isManageOrSelf(compParameter, DocuUtils.applicationModule, userId)) {
						//不是管理员,不能删除发布状态和用户申请删除状态
						if (docuBean.getStatus() == EDocuStatus.publish || docuBean.getStatus() == EDocuStatus.delete_user) {
							if (!ItSiteUtil.isManage(compParameter, DocuUtils.applicationModule)) {
								json.put("act", "你没有权限操作。");
								return;
							}
						}
						DocuUtils.applicationModule.doDelete(docuBean, new TableEntityAdapter() {
							@Override
							public void afterDelete(ITableEntityManager manager, IDataObjectValue dataObjectValue) {
								DocuUtils.applicationModule.deleteRemarks(docuBean.getId());
								ItSiteUtil.update(compParameter, docuBean.getUserId(), docuBean.getId(), false);
								//总数加-1
								DocuUtils.addDocuCounter(-1);
								//删除lucence
								DocuUtils.applicationModule.createLuceneManager(compParameter).deleteDocumentBackground(dataObjectValue.getValues());
								IOUtils.delete(new File(DocuUtils.getDatabase(docuBean.getUserId()), docuBean.getFileName()));
								IOUtils.delete(new File(DocuUtils.getDatabase(docuBean.getUserId()), docuBean.getId().toString()));
								final StringBuffer textBody = new StringBuffer();
								textBody.append("<blockquote>名称<br/>").append(docuBean.getTitle()).append("</blockquote>");
								textBody.append("<blockquote>原因<br/>").append(delReason).append("</blockquote>");
								MessageUtils.createNotifation(compParameter, "你的文档被删除", textBody.toString(), account.getId(), docuBean.getUserId(),
										EMessageType.notification);
							}
						});
						json.put("act", "删除成功!");
					} else {
						json.put("act", "你没有权限操作。");
						break;
					}
				}
			}
		});
	}

	/**
	 * 编辑文档
	 * @param compParameter
	 * @return
	 */
	public IForward docuEditSave(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(final Map<String, Object> json) throws Exception {
				final ID userId = ItSiteUtil.getLoginUser(compParameter).getId();
				final DocuBean docuBean = DocuUtils.applicationModule.getBean(DocuBean.class, compParameter.getRequestParameter(DocuUtils.docuId));
				if (docuBean != null && ItSiteUtil.isManageOrSelf(compParameter, DocuUtils.applicationModule, userId)) {
					final String docu_title = compParameter.getRequestParameter("docu_title");
					final String docu_content = compParameter.getRequestParameter("docu_content");
					final String docu_keyworks = compParameter.getRequestParameter("docu_keyworks");
					final String docu_catalog = compParameter.getRequestParameter("docu_catalog");
					final String docu_point = compParameter.getRequestParameter("docu_point");
					final String docu_free_page = compParameter.getRequestParameter("docu_free_page");
					docuBean.setTitle(docu_title);
					docuBean.setLastUserId(userId);
					docuBean.setContent(docu_content);
					docuBean.setKeyworks(docu_keyworks);
					if (StringUtils.hasText(docu_catalog)) {
						docuBean.setCatalogId(new LongID(docu_catalog));
					}
					docuBean.setDocuFunction(EDocuFunction.data);
					docuBean.setPoint(ConvertUtils.toInt(docu_point, 0));
					docuBean.setAllowRead(NumberUtils.toDouble(docu_free_page));
					//当文档不是发布状态时
					if (docuBean.getStatus() != EDocuStatus.publish) {
						docuBean.setStatus(EDocuStatus.audit);//文档编辑后改为审核状态
					}
					DocuUtils.applicationModule.doUpdate(docuBean);
				}
			}
		});
	}

	/**
	 * 高级属性
	 * @param compParameter
	 * @return
	 */
	public IForward docuAudit(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(final Map<String, Object> json) throws Exception {
				final String[] docuIds = compParameter.getRequestParameter("docuIds").split(",");
				for (final String docuId : docuIds) {
					final DocuBean docuBean = DocuUtils.applicationModule.getBean(DocuBean.class, docuId);
					docuBean.setStatus(EDocuStatus.publish);
					DocuUtils.applicationModule.doUpdate(new Object[] { "status" }, docuBean);
					MessageUtils.createNotifation(compParameter, "审核成功", DocuUtils.wrapOpenLink(compParameter, docuBean), OrgUtils.um()
							.getUserByName("admin").getId(), docuBean.getUserId());
				}
			}
		});
	}

	/**
	 * 保存路径
	 * @param compParameter
	 * @return
	 */
	public IForward docuPath(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(final Map<String, Object> json) throws Exception {
				final String docuPath = compParameter.getParameter("docuPath");
				PrjCustomBean customBean = PrjMgrUtils.appModule.getBeanByExp(PrjCustomBean.class, "name=? and type=?", new Object[] { "docuPath",
						"docu" });
				if (customBean == null) {
					customBean = new PrjCustomBean();
					customBean.setType("docu");
					customBean.setName("docuPath");
				}
				customBean.setValue(docuPath);
				DocuUtils.docuPath = docuPath;
				PrjMgrUtils.appModule.doUpdate(customBean);
			}
		});
	}

	/**
	 * 保存文档
	 */
	public IForward docuSave(final ComponentParameter compParameter) throws Exception {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(final Map<String, Object> json) throws Exception {
				final ID userId = ItSiteUtil.getLoginUser(compParameter).getId();
				try {
					final String docuId = compParameter.getRequestParameter("docuId");
					final String docu_title = compParameter.getRequestParameter("docu_title");
					final String docu_content = compParameter.getRequestParameter("docu_content");
					final String docu_keyworks = compParameter.getRequestParameter("docu_keyworks");
					final String docu_catalog = compParameter.getRequestParameter("docu_catalog");
					final String docu_point = compParameter.getRequestParameter("docu_point");
					final DocuBean docuBean = DocuUtils.applicationModule.getBean(DocuBean.class, docuId);
					if (docuBean != null) {
						docuBean.setSameId(docuBean.getId());
						docuBean.setUserId(userId);
						docuBean.setLastUserId(userId);
						//表示是单个文件
						BeanUtils.setProperty(docuBean, "title", docu_title);
						BeanUtils.setProperty(docuBean, "content", docu_content);
						BeanUtils.setProperty(docuBean, "keyworks", docu_keyworks);
						if (StringUtils.hasText(docu_catalog)) {
							docuBean.setCatalogId(new LongID(docu_catalog));
						}
						docuBean.setDocuFunction(EDocuFunction.data);
						docuBean.setPoint(ConvertUtils.toInt(docu_point, 0));
						docuBean.setAttentions(1);
						docuBean.setAllowRead(100);
						docuBean.setSuccess(1);
						DocuUtils.applicationModule.doUpdate(docuBean);
					}
				} catch (Exception e) {
				}
			}
		});
	}

	@Override
	public IContentApplicationModule getApplicationModule() {
		return DocuUtils.applicationModule;
	}

	@Override
	protected void doStatRebuild() {
		DocuUtils.doStatRebuild();
	}
}
