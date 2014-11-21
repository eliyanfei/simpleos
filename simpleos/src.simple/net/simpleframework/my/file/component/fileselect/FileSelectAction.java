package net.simpleframework.my.file.component.fileselect;

import java.util.Map;

import net.simpleframework.content.component.filepager.FileBean;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.account.AbstractAccountRule;
import net.simpleframework.organization.account.AccountContext;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.HandleException;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class FileSelectAction extends AbstractAjaxRequestHandle {
	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("jobExecute".equals(beanProperty)) {
			return WebUtils.toQueryParams(
					FileSelectUtils.decodeDownloadParameter(compParameter.request)).get("job");
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	public IForward download(final ComponentParameter compParameter) {
		final String dl = FileSelectUtils.decodeDownloadParameter(compParameter.request);
		final Map<String, Object> params = WebUtils.toQueryParams(dl);
		final FileBean fileBean = FileSelectUtils.getFileBean(compParameter, params);
		if (fileBean == null) {
			throw HandleException.wrapException(LocaleI18n.getMessage("FileSelectAction.1"));
		}
		params.remove("job");
		final int points = ConvertUtils.toInt(params.get("points"), 0);
		if (points > 0) {
			final IAccount login = AccountSession.getLogin(compParameter.getSession());
			final int p2 = login.getPoints();
			if (p2 - points < 0) {
				throw HandleException.wrapException(LocaleI18n.getMessage("FileSelectAction.0", points,
						p2)
						+ "<a onclick=\"$Actions['userAccountRuleWindow']();\">"
						+ LocaleI18n.getMessage("user_utils.0") + "</a>");
			}
		}
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				if (points > 0) {
					json.put("confirm", LocaleI18n.getMessage("FileSelectAction.2", points));
					json.put("updateAccount", "points=" + points + "&posttext=" + params.get("posttext"));
					params.remove("points");
					params.remove("posttext");
				}
				json.put("dl", dl.substring(0, dl.indexOf("?") + 1) + WebUtils.toQueryString(params));
			}
		});
	}

	public IForward updateAccount(final ComponentParameter compParameter) {
		final int points = ConvertUtils.toInt(compParameter.getRequestParameter("points"), 0);
		if (points > 0) {
			final AbstractAccountRule ar = (AbstractAccountRule) AccountContext
					.getAccountRule("bbs_download");
			ar.setPoints(-points);
			AccountContext.update(AccountSession.getLogin(compParameter.getSession()), ar,
					ID.Utils.newID(compParameter.getRequestParameter("posttext")));
		}
		return null;
	}
}
