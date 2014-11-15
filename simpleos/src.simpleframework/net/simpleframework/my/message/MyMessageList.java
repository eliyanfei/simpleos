package net.simpleframework.my.message;

import java.util.ArrayList;
import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.applets.notification.NotificationUtils;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractPagerHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MyMessageList extends AbstractPagerHandle {

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("title".equals(beanProperty)) {
			final StringBuilder sb = new StringBuilder();
			final String box = compParameter.getRequestParameter("box");
			final boolean r = "r".equals(box);
			final boolean s = "s".equals(box);
			final String act = "\" onclick=\"$Actions['__my_message_pager']('box=";
			sb.append("<a class=\"a2");
			if (!r && !s) {
				sb.append(" nav_arrow");
			}
			sb.append(act).append("');\">#(MyMessageList.2)</a>").append(HTMLBuilder.SEP);
			sb.append("<a class=\"a2");
			if (r) {
				sb.append(" nav_arrow");
			}
			sb.append(act).append("r');\">#(MyMessageList.0)</a>").append(HTMLBuilder.SEP);
			sb.append("<a class=\"a2");
			if (s) {
				sb.append(" nav_arrow");
			}
			sb.append(act).append("s');\">#(MyMessageList.1)</a>");
			return sb.toString();
		} else if ("jsLoadedCallback".equals(beanProperty)) {
			if (!compParameter.isHttpRequest()) {
				return "$Actions['user_navTooltip'].createTip('.my_message_list .user_navTooltip_class');";
			}
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, "box");
		return parameters;
	}

	public static final String SESSION_DELETE_MESSAGE = "deleteMessageNotification";

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
		final String box = compParameter.getRequestParameter("box");
		final StringBuilder sql = new StringBuilder();
		final ArrayList<Object> al = new ArrayList<Object>();
		if ("r".equals(box)) {
			sql.append("toId=?");
			al.add(login.getId());
		} else if ("s".equals(box)) {
			sql.append("sentId=?");
			al.add(login.getId());
		} else {
			sql.append("(sentId=? or toId=?)");
			al.add(login.getId());
			al.add(login.getId());
		}
		sql.append(" and messageType=?");
		al.add(EMessageType.user);
		sql.append(" order by sentdate desc");
		return temgr.query(new ExpressionValue(sql.toString(), al.toArray()), SimpleMessage.class);
	}
}
