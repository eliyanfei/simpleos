package net.simpleframework.desktop;

import java.io.File;
import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.desktop.DesktopMgr.DesktopBean;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;

/**
 */
public class DesktopUIActionHandle extends AbstractAjaxRequestHandle {

	public IForward addUI(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				final DesktopUIBean desktopBean = new DesktopUIBean();
				final String tabId = compParameter.getRequestParameter("tabId");
				final String uiName = compParameter.getRequestParameter("uiName");
				desktopBean.setUserId(AccountSession.getLogin(compParameter.getSession()).getId());
				desktopBean.setTabId(tabId);
				desktopBean.setUiName(uiName);
				DesktopBean desBean = DesktopMgr.getDesktopMgr().getDesktopBean(uiName);
				try {
					if (desBean != null) {
						desktopBean.setOorder(desBean.idx);
						DesktopUIUtils.application.doUpdate(desktopBean);
						json.put("rs", "ok");
					}
				} catch (Exception e) {
				}
			}
		});
	}

	public IForward delUI(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				final String tabId = compParameter.getRequestParameter("tabId");
				final String uiName = compParameter.getRequestParameter("uiName");
				ITableEntityManager tMgr = DesktopUIUtils.application.getDataObjectManager();
				try {
					tMgr.delete(new ExpressionValue("userId=? and tabId=? and uiName=?", new Object[] {
							AccountSession.getLogin(compParameter.getSession()).getId(), tabId, uiName }));
				} catch (Exception e) {
				}
			}
		});
	}

	public IForward delDesktopTheme(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				final String fileName = compParameter.getRequestParameter("fileName");
				IAccount account = AccountSession.getLogin(compParameter.getSession());
				String path = compParameter.request.getContextPath() + compParameter.request.getRealPath("/desktop/themes/theme") + "/"
						+ account.getId() + "/" + fileName;
				new File(path).delete();
			}
		});
	}
}
