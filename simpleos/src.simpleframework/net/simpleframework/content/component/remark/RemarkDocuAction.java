package net.simpleframework.content.component.remark;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.a.ItSiteUtil;
import net.itsite.document.docu.DocuRemark;
import net.itsite.document.docu.DocuUtils;
import net.simpleframework.content.ContentUtils;
import net.simpleframework.content.EContentStatus;
import net.simpleframework.core.id.LongID;
import net.simpleframework.organization.IUser;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.HandleException;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;
import net.simpleframework.web.page.component.ui.validatecode.DefaultValidateCodeHandle;

public class RemarkDocuAction extends AbstractAjaxRequestHandle {

	public IForward saveRemark(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final String itemId = compParameter.getRequestParameter("itemId");
				final IUser user = ItSiteUtil.getLoginUser(compParameter);
				if (StringUtils.hasText(itemId)) {
					final DocuRemark docuRemark = DocuUtils.applicationModule.getBean(DocuRemark.class, itemId);
					if (docuRemark != null) {
						docuRemark.setContent(compParameter.getRequestParameter("textareaRemarkHtmlEditor"));
						DocuUtils.applicationModule.doUpdate(docuRemark);
					}
				} else {
					final DocuRemark docuRemark = new DocuRemark();
					docuRemark.setContent(compParameter.getRequestParameter("textareaRemarkHtmlEditor"));
					docuRemark.setIp(compParameter.request.getRemoteAddr());
					docuRemark.setLastUpdate(new Date());
					docuRemark.setLastUserId(user.getId());
					docuRemark.setStatus(EContentStatus.publish);
					docuRemark.setParentId(new LongID(compParameter.getRequestParameter("parentId")));
					docuRemark.setUserId(user.getId());
					docuRemark.setDocumentId(new LongID(compParameter.getRequestParameter("documentId")));
					DocuUtils.applicationModule.doUpdate(docuRemark);
				}
			}
		});
	}

	public IForward save2Remark(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = RemarkUtils.getComponentParameter(compParameter);
		final IRemarkHandle rHandle = (IRemarkHandle) nComponentParameter.getComponentHandle();
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				if ((Boolean) nComponentParameter.getBeanProperty("showValidateCode")
						&& !DefaultValidateCodeHandle.isValidateCode(compParameter.request, "textRemarkEditorValidateCode")) {
					json.put("validateCode", DefaultValidateCodeHandle.getErrorString());
				} else {
					final Map<String, Object> data = new HashMap<String, Object>();
					data.put("content", ContentUtils.doTextContent(compParameter.getRequestParameter("textareaRemarkEditor")));
					rHandle.doAdd(nComponentParameter, data);
				}
			}
		});
	}

	public IForward doSupport(final ComponentParameter compParameter) {
		return doSupportOpposition(compParameter, true);
	}

	public IForward doOpposition(final ComponentParameter compParameter) {
		return doSupportOpposition(compParameter, false);
	}

	private IForward doSupportOpposition(final ComponentParameter compParameter, final boolean support) {
		final String itemId = compParameter.getRequestParameter("itemId");
		final String sessionKey = "remark_support_opposition_" + itemId;
		final HttpSession httpSession = compParameter.getSession();
		if (Boolean.TRUE.equals(httpSession.getAttribute(sessionKey))) {
			throw HandleException.wrapException(LocaleI18n.getMessage("RemarkAction.0"));
		}
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final RemarkItem remark = DocuUtils.applicationModule.getBean(DocuRemark.class, itemId);
				if (remark != null) {
					if (support) {
						remark.setSupport(remark.getSupport() + 1);
					} else {
						remark.setOpposition(remark.getOpposition() + 1);
					}
				}
				DocuUtils.applicationModule.doUpdate(remark);
				httpSession.setAttribute(sessionKey, Boolean.TRUE);
				json.put("k", "remark_" + (support ? "support" : "opposition") + "_" + remark.getId());
				json.put("v", "(" + (support ? remark.getSupport() : remark.getOpposition()) + ")");
			}
		});
	}

	public IForward doDelete(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				DocuUtils.applicationModule.doDelete(DocuRemark.class, compParameter.getRequestParameter("itemId"));
			}
		});
	}

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		return super.getBeanProperty(compParameter, beanProperty);
	}
}
