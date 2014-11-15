package net.simpleframework.my.friends;

import java.util.ArrayList;
import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.ConvertUtils;
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
public class FriendsRequestPagerHandle extends AbstractPagerHandle {

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("title".equals(beanProperty)) {
			if (FriendsUtils.isMyFriendsRequest(compParameter.request)) {
				final StringBuilder sb = new StringBuilder();
				final int status = ConvertUtils.toInt(compParameter.getRequestParameter("status"), -1);
				final int request = ERequestStatus.request.ordinal();
				final int yes = ERequestStatus.yes.ordinal();
				final int no = ERequestStatus.no.ordinal();
				final String act = "\" onclick=\"$Actions['idMyFriendsRequestPager']('status=";
				sb.append("<a class=\"a2");
				if (status != request && status != yes && status != no) {
					sb.append(" nav_arrow");
				}
				sb.append(act).append("');\">#(FriendsRequestPagerHandle.0)</a>")
						.append(HTMLBuilder.SEP);
				sb.append("<a class=\"a2");
				if (status == request) {
					sb.append(" nav_arrow");
				}
				sb.append(act).append(request).append("');\">").append(ERequestStatus.request)
						.append("</a>").append(HTMLBuilder.SEP);
				sb.append("<a class=\"a2");
				if (status == yes) {
					sb.append(" nav_arrow");
				}
				sb.append(act).append(yes).append("');\">").append(ERequestStatus.yes).append("</a>")
						.append(HTMLBuilder.SEP);
				sb.append("<a class=\"a2");
				if (status == no) {
					sb.append(" nav_arrow");
				}
				sb.append(act).append(no).append("');\">").append(ERequestStatus.no).append("</a>");
				return sb.toString();
			}
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, "op");
		return parameters;
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		final IAccount login = AccountSession.getLogin(compParameter.getSession());
		if (login == null) {
			return null;
		}

		final ITableEntityManager temgr = FriendsUtils.getTableEntityManager(FriendsRequest.class);
		if (FriendsUtils.isMyFriendsRequest(compParameter.request)) {
			String sql = "sentid=?";
			final ArrayList<Object> al = new ArrayList<Object>();
			al.add(login.getId());
			final int status = ConvertUtils.toInt(compParameter.getRequestParameter("status"), -1);
			if (status > -1) {
				sql += " and requeststatus=?";
				al.add(status);
			}
			return temgr.query(new ExpressionValue(sql, al.toArray()), FriendsRequest.class);
		} else {
			return temgr.query(
					new ExpressionValue("toid=? and requeststatus=?", new Object[] { login.getId(),
							ERequestStatus.request }), FriendsRequest.class);
		}
	}
}
