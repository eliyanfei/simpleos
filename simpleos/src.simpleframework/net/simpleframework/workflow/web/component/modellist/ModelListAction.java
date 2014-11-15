package net.simpleframework.workflow.web.component.modellist;

import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;
import net.simpleframework.web.page.component.ui.pager.PagerUtils;
import net.simpleframework.workflow.EProcessModelStatus;
import net.simpleframework.workflow.IProcessModelManager;
import net.simpleframework.workflow.InitiateItem;
import net.simpleframework.workflow.ProcessModelBean;
import net.simpleframework.workflow.WorkflowUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ModelListAction extends AbstractAjaxRequestHandle {

	public IForward deleteModel(final ComponentParameter compParameter) {
		final IProcessModelManager pmm = WorkflowUtils.pmm();
		final ProcessModelBean model = pmm.queryForObjectById(compParameter
				.getRequestParameter("modelId"));
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) throws Exception {
				if (model != null) {
					pmm.delete(new ExpressionValue("id=?", new Object[] { model.getId() }));
					json.put("models",
							PagerUtils.getComponentParameter(compParameter).getBeanProperty("name"));
				}
			}
		});
	}

	public IForward optSave(final ComponentParameter compParameter) {
		final IProcessModelManager pmm = WorkflowUtils.pmm();
		final ProcessModelBean model = pmm.queryForObjectById(compParameter
				.getRequestParameter("modelId"));
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) throws Exception {
				model.setStatus(ConvertUtils.toEnum(EProcessModelStatus.class,
						compParameter.getRequestParameter("model_status")));
				pmm.update(model);
				json.put("models",
						PagerUtils.getComponentParameter(compParameter).getBeanProperty("name"));
			}
		});
	}

	public IForward createProcess(final ComponentParameter compParameter) {
		final IAccount account = AccountSession.getLogin(compParameter.getSession());
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) throws Exception {
				if (account != null) {
					final IProcessModelManager pmm = WorkflowUtils.pmm();
					final IUser user = account.user();
					final InitiateItem item = pmm.items(user).get(
							pmm.id(compParameter.getRequestParameter("modelId")));
					WorkflowUtils.pm().createProcess(item, user, null);
				}
			}
		});
	}
}
