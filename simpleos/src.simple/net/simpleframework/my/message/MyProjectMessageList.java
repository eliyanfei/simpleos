package net.simpleframework.my.message;

import java.util.ArrayList;
import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.applets.notification.NotificationUtils;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractPagerHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MyProjectMessageList extends AbstractPagerHandle {

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, "box");
		return parameters;
	}

	static final String SESSION_DELETE_MESSAGE = "deleteMessageNotification";

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		final IAccount login = AccountSession.getLogin(compParameter.getSession());
		if (login == null) {
			return null;
		}

		if (compParameter.getSessionAttribute(SESSION_DELETE_MESSAGE) == null
				&& NotificationUtils.deleteMessageNotificationByMessageId(login.getId()) > 0) {
			compParameter.setSessionAttribute(SESSION_DELETE_MESSAGE, Boolean.TRUE);
		}

		final ITableEntityManager temgr = MessageUtils.getTableEntityManager(SimpleMessage.class);
		final StringBuilder sql = new StringBuilder();
		final ArrayList<Object> al = new ArrayList<Object>();
		sql.append("toId=?");
		al.add(login.getId());
		sql.append(" and messageType=?");
		al.add(EMessageType.project);
		sql.append(" order by sentdate desc");
		return temgr.query(new ExpressionValue(sql.toString(), al.toArray()), SimpleMessage.class);
	}
}
