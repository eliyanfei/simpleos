package net.simpleframework.workflow.web.component.modellist;

import java.io.IOException;
import java.io.InputStreamReader;

import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.web.page.AbstractUrlForward;
import net.simpleframework.web.page.IMultipartFile;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.HandleException;
import net.simpleframework.web.page.component.base.submit.AbstractSubmitHandle;
import net.simpleframework.workflow.WorkflowUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ModelUploadAction extends AbstractSubmitHandle {

	@Override
	public AbstractUrlForward submit(final ComponentParameter compParameter) {
		final IMultipartFile multipartFile = getMultipartFile(compParameter, "ml_upload");
		final IAccount account = AccountSession.getLogin(compParameter.getSession());
		try {
			WorkflowUtils.pmm().add(account.user(),
					new InputStreamReader(multipartFile.getInputStream()));
			return AbstractUrlForward.componentUrl(ModelListRegistry.modellist,
					"/jsp/model_upload_result.jsp");
		} catch (final IOException e) {
			throw HandleException.wrapException(e);
		}
	}
}
