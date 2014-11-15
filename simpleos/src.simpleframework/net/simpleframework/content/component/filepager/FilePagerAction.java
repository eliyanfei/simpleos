package net.simpleframework.content.component.filepager;

import java.util.HashMap;
import java.util.Map;

import net.simpleframework.organization.IJob;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.AbstractUrlForward;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.web.page.component.ui.pager.db.AbstractDbTablePagerAction;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class FilePagerAction extends AbstractDbTablePagerAction {
	@Override
	protected ComponentParameter getComponentParameter(final PageRequestResponse requestResponse) {
		return FilePagerUtils.getComponentParameter(requestResponse);
	}

	public IForward fileSave(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = getComponentParameter(compParameter);
		final IFilePagerHandle fHandle = (IFilePagerHandle) nComponentParameter.getComponentHandle();
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final Map<String, Object> data = new HashMap<String, Object>();
				data.put("topic", compParameter.getRequestParameter("file_topic"));
				data.put("description", compParameter.getRequestParameter("file_description"));
				fHandle.doEdit(nComponentParameter, compParameter.getRequestParameter(fHandle.getIdParameterName(nComponentParameter)), data);
				final String jsCallback = fHandle.getJavascriptCallback(nComponentParameter, "edit", null);
				if (StringUtils.hasText(jsCallback)) {
					json.put("jsCallback", jsCallback);
				}
			}
		});
	}

	public IForward topSave(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = getComponentParameter(compParameter);
		final IFilePagerHandle fHandle = (IFilePagerHandle) nComponentParameter.getComponentHandle();
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final Map<String, Object> data = new HashMap<String, Object>();
				data.put("top", Boolean.TRUE);
				fHandle.doEdit(nComponentParameter, compParameter.getRequestParameter(fHandle.getIdParameterName(nComponentParameter)), data);
				final String jsCallback = fHandle.getJavascriptCallback(nComponentParameter, "edit", null);
				if (StringUtils.hasText(jsCallback)) {
					json.put("jsCallback", jsCallback);
				}
			}
		});
	}

	public IForward downloadFile(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = getComponentParameter(compParameter);
		final IFilePagerHandle fHandle = (IFilePagerHandle) nComponentParameter.getComponentHandle();
		final FileBean file = fHandle.getEntityBeanByRequest(nComponentParameter);
		final String dl = fHandle.getDownloadPath(nComponentParameter, file);
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				if (StringUtils.hasText(dl)) {
					json.put("dl", dl);
				}
			}
		});
	}

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("selector".equals(beanProperty)) {
			final ComponentParameter nComponentParameter = getComponentParameter(compParameter);
			if (nComponentParameter.componentBean != null) {
				final String selector = (String) nComponentParameter.getBeanProperty("selector");
				final String handleMethod = ((AjaxRequestBean) compParameter.componentBean).getHandleMethod();
				if ("fileSave".equals(handleMethod)) {
					return selector + ", #__fp_fileForm";
				} else if ("downloadFile".equals(handleMethod)) {
					return selector;
				}
			}
		} else if ("jobExecute".equals(beanProperty)) {
			final ComponentParameter nComponentParameter = getComponentParameter(compParameter);
			if (nComponentParameter.componentBean != null) {
				final String componentName = (String) compParameter.getBeanProperty("name");
				if ("filePagerMove".equals(componentName)) {
					return nComponentParameter.getBeanProperty("jobExchange");
				} else if ("ajaxFilepagerTop".equals(componentName)) {
					return nComponentParameter.getBeanProperty("jobEdit");
				} else {
					final FileBean fileBean = ((IFilePagerHandle) nComponentParameter.getComponentHandle())
							.getEntityBeanByRequest(nComponentParameter);
					if (fileBean != null && AccountSession.isAccount(compParameter.getSession(), fileBean.getUserId())) {
						return IJob.sj_account_normal;
					} else {
						if ("ajaxEditFilePage".equals(componentName)) {
							return nComponentParameter.getBeanProperty("jobEdit");
						} else if ("ajaxFilepagerDownload".equals(componentName)) {
							return nComponentParameter.getBeanProperty("jobDownload");
						} else if ("filePagerDelete".equals(componentName)) {
							return nComponentParameter.getBeanProperty("jobDelete");
						}
					}
				}
			}
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	public IForward fileUrl(final ComponentParameter compParameter) {
		return AbstractUrlForward.componentUrl(FilePagerRegistry.filePager, "/jsp/edit_file.jsp");
	}

	public IForward downloadUrl(final ComponentParameter compParameter) {
		return AbstractUrlForward.componentUrl(FilePagerRegistry.filePager, "/jsp/file_download.jsp");
	}
}