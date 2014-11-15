package net.simpleframework.content.component.newspager;

import java.util.HashMap;
import java.util.Map;

import net.a.ItSiteUtil;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.content.EContentStatus;
import net.simpleframework.content.EContentType;
import net.simpleframework.content.news.NewsPagerHandle;
import net.simpleframework.content.news.NewsUtils;
import net.simpleframework.core.id.ID;
import net.simpleframework.core.id.LongID;
import net.simpleframework.my.message.EMessageType;
import net.simpleframework.my.message.MessageUtils;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.HTMLUtils;
import net.simpleframework.util.JavascriptUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.AbstractUrlForward;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.web.page.component.ui.pager.db.AbstractDbTablePagerAction;
import net.simpleframework.web.page.component.ui.pager.db.IDbTablePagerHandle;
import net.simpleframework.web.page.component.ui.validatecode.DefaultValidateCodeHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class NewsPagerAction extends AbstractDbTablePagerAction {
	@Override
	protected ComponentParameter getComponentParameter(final PageRequestResponse requestResponse) {
		return NewsPagerUtils.getComponentParameter(requestResponse);
	}

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		final ComponentParameter nComponentParameter = getComponentParameter(compParameter);
		if (nComponentParameter.componentBean != null) {
			if ("selector".equals(beanProperty)) {
				final String handleMethod = ((AjaxRequestBean) compParameter.componentBean).getHandleMethod();
				final String selector = (String) nComponentParameter.getBeanProperty("selector");
				if ("npSave".equals(handleMethod)) {
					return selector + ", #__np__newsAddForm";
				} else if ("npSave2".equals(handleMethod)) {
					return selector + ", #__np__newsAddForm2";
				} else {
					return selector;
				}
			} else if ("jobExecute".equals(beanProperty)) {
				final String componentName = (String) compParameter.getBeanProperty("name");
				if ("ajaxAddNewspagerPage".equals(componentName)) {
					return nComponentParameter.getBeanProperty("jobAdd");
				} else if ("newspagerDelete".equals(componentName)) {
					return nComponentParameter.getBeanProperty("jobDelete");
				} else {
					return IJob.sj_account_normal;
				}
			}
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	public IForward npSave(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = getComponentParameter(compParameter);
		final INewsPagerHandle nHandle = (INewsPagerHandle) nComponentParameter.getComponentHandle();
		final NewsBean news = nHandle.getEntityBeanByRequest(compParameter);
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				if (((NewsPagerBean) nComponentParameter.componentBean).isShowValidateCode()
						&& !DefaultValidateCodeHandle.isValidateCode(compParameter.request, "textNewsEditorValidateCode")) {
					json.put("validateCode", DefaultValidateCodeHandle.getErrorString());
				} else {
					final Map<String, Object> data = new HashMap<String, Object>();
					for (final String attri : new String[] { "topic", "author", "source", "keywords", "description", "content" }) {
						data.put(attri, nComponentParameter.getRequestParameter("np_" + attri));
					}
					data.put("allowComments", ConvertUtils.toBoolean(compParameter.getRequestParameter("np_allowComments"), false));
					final String catalogId = compParameter.getRequestParameter("np_catalog");
					if (StringUtils.hasText(catalogId)) {
						data.put("catalogId", ID.Utils.newID(ConvertUtils.toInt(catalogId, 0)));
					}
					data.put("topic", HTMLUtils.htmlEscape(nComponentParameter.getRequestParameter("np_topic")));
					data.put("keywords", HTMLUtils.htmlEscape(nComponentParameter.getRequestParameter("np_keywords")));
					data.put("source", HTMLUtils.htmlEscape(nComponentParameter.getRequestParameter("np_source")));
					data.put("description", HTMLUtils.htmlEscape(nComponentParameter.getRequestParameter("np_description")));
					data.put("att2", true);
					data.put("mark", ConvertUtils.toBoolean(compParameter.getRequestParameter("np_vote"), false) ? 1 : 0);
					data.put("attention", compParameter.getRequestParameter("np_attention"));
					String jsCallback = null;
					if (news != null) {
						nHandle.doEdit(nComponentParameter, news.getId(), data);
						jsCallback = nHandle.getJavascriptCallback(nComponentParameter, "edit", news);
					} else {
						data.put("status", EContentStatus.audit);
						jsCallback = nHandle.getJavascriptCallback(nComponentParameter, "add", nHandle.doAdd(nComponentParameter, data));
						if (!ItSiteUtil.isManage(compParameter) && nHandle instanceof NewsPagerHandle) {//只有新闻才发邮件通知
							MessageUtils.createNotifation(compParameter, "资讯审核", (String) data.get("topic"), LongID.zero, OrgUtils.am()
									.getAccountByName("admin").getId(), EMessageType.notification);
						}
					}
					if (StringUtils.hasText(jsCallback)) {
						json.put("jsCallback", jsCallback);
					}
				}
			}
		});
	}

	public IForward publish(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = getComponentParameter(compParameter);
		final IDbTablePagerHandle tHandle = (IDbTablePagerHandle) nComponentParameter.getComponentHandle();
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				final String[] idArray = StringUtils.split(nComponentParameter.request.getParameter(tHandle.getIdParameterName(nComponentParameter)));
				if (idArray != null) {
					final ID userId = OrgUtils.um().getUserByName("admin").getId();
					for (String id : idArray) {
						final ITableEntityManager tMgr = tHandle.getTableEntityManager(nComponentParameter);
						NewsBean newsBean = (NewsBean) tMgr.queryForObjectById(id, tHandle.getEntityBeanClass());
						if (newsBean != null) {
							newsBean.setStatus(EContentStatus.publish);
							try {
								tMgr.update(new Object[] { "status" }, newsBean);
								MessageUtils.createNotifation(compParameter, "审核成功", NewsUtils.applicationModule.getViewUrl(compParameter, newsBean),
										userId, newsBean.getUserId());
							} catch (Exception e) {
							}
						}
					}
					final String jsCallback = tHandle.getJavascriptCallback(nComponentParameter, "delete", null);
					if (StringUtils.hasText(jsCallback)) {
						json.put("jsCallback", jsCallback);
					}
				}
			}
		});
	}

	public IForward npSave2(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = getComponentParameter(compParameter);
		final INewsPagerHandle nHandle = (INewsPagerHandle) nComponentParameter.getComponentHandle();
		final NewsBean news = nHandle.getEntityBeanByRequest(nComponentParameter);
		if (news == null) {
			return null;
		}
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final Map<String, Object> data = new HashMap<String, Object>();
				final String nv_template = compParameter.getRequestParameter("nv_template");
				data.put("viewTemplate", ConvertUtils.toEnum(ENewsTemplate.class, nv_template));
				final String nv_status = compParameter.getRequestParameter("nv_status");
				data.put("status", ConvertUtils.toEnum(EContentStatus.class, nv_status));
				final String nv_type = compParameter.getRequestParameter("nv_type");
				data.put("ttype", ConvertUtils.toEnum(EContentType.class, nv_type));
				data.put("ttop", ConvertUtils.toBoolean(compParameter.getRequestParameter("nv_ttop"), false));
				nHandle.doEdit(nComponentParameter, news.getId(), data);
				try {
					if (news.getStatus() == EContentStatus.audit && ConvertUtils.toEnum(EContentStatus.class, nv_status) == EContentStatus.publish) {
						MessageUtils.createNotifation(compParameter, "审核成功", NewsUtils.applicationModule.getViewUrl(compParameter, news), OrgUtils
								.um().getUserByName("admin").getId(), news.getUserId());
					} else if (news.getTtype() == EContentType.normal && ConvertUtils.toEnum(EContentType.class, nv_type) == EContentType.recommended) {
						MessageUtils.createNotifation(compParameter, "你的发布的内容被推荐", NewsUtils.applicationModule.getViewUrl(compParameter, news),
								OrgUtils.um().getUserByName("admin").getId(), news.getUserId());
					}
				} catch (Exception e) {
				}
				final String jsCallback = nHandle.getJavascriptCallback(nComponentParameter, "edit", news);
				if (StringUtils.hasText(jsCallback)) {
					json.put("jsCallback", jsCallback);
				}
			}
		});
	}

	public IForward editUrl(final ComponentParameter compParameter) {
		return AbstractUrlForward.componentUrl(NewsPagerRegistry.newsPager, "/jsp/np_edit.jsp");
	}

	public IForward edit2Url(final ComponentParameter compParameter) {
		return AbstractUrlForward.componentUrl(NewsPagerRegistry.newsPager, "/jsp/news_edit.jsp");
	}

	public IForward newsAttention(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = getComponentParameter(compParameter);
		final INewsPagerHandle nHandle = (INewsPagerHandle) nComponentParameter.getComponentHandle();
		final NewsBean news = nHandle.getEntityBeanByRequest(nComponentParameter);
		if (news == null) {
			return null;
		}
		nHandle.contentAttention(nComponentParameter, news, ConvertUtils.toBoolean(compParameter.getRequestParameter("delete"), false));
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final StringBuilder js = new StringBuilder();
				js.append("$(\"act_").append(news.getId()).append("\").update(\"");
				js.append(JavascriptUtils.escape(nHandle.getActionsHTML(nComponentParameter, news))).append("\");");
				json.put("jsCallback", js.toString());
			}
		});
	}
}
