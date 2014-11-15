package net.simpleframework.workflow.web.component.modellist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.ado.ListDataObjectQuery;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.menu.MenuBean;
import net.simpleframework.web.page.component.ui.menu.MenuItem;
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
public class MyModelListHandle extends AbstractModelListHandle {

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		final IAccount account = AccountSession.getLogin(compParameter.getSession());
		IUser user;
		if (account == null || (user = account.user()) == null) {
			return null;
		}
		return new ListDataObjectQuery<InitiateItem>(WorkflowUtils.pmm().items(user).values());
	}

	@Override
	protected Map<Object, Object> getTableRow(final ComponentParameter compParameter,
			final Object dataObject) {
		final InitiateItem item = (InitiateItem) dataObject;
		final Map<Object, Object> rowData = new HashMap<Object, Object>();
		rowData.put("title", getTitle(item.model()));
		return rowData;
	}

	protected String getTitle(final ProcessModelBean processModel) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<a onclick=\"$Actions['ml_create_process']('modelId=")
				.append(processModel.getId()).append("');\">").append(processModel.getTitle())
				.append("</a>");
		return sb.toString();
	}

	@Override
	public List<MenuItem> getHeaderMenu(final ComponentParameter compParameter,
			final MenuBean menuBean) {
		return null;
	}

	@Override
	public List<MenuItem> getContextMenu(final ComponentParameter compParameter,
			final MenuBean menuBean) {
		return null;
	}
}
