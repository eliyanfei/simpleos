package net.simpleframework.my.friends;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractPagerHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class FriendsPagerHandle extends AbstractPagerHandle {
	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("title".equals(beanProperty)) {
			final String op = compParameter.getRequestParameter("op");
			if (!"search".equals(op)) {
				final StringBuilder sb = new StringBuilder();
				sb.append("<input style=\"vertical-align: middle; margin-right: 4px;\" ");
				sb.append("type=\"checkbox\" onclick=\"__my_friends_checkAll(this);\" />");
				if ("online".equals(op)) {
					sb.append("<span>#(FriendsGroupHandle.0)</span>");
				} else {
					final String gid = compParameter.getRequestParameter("gid");
					sb.append("<a onclick=\"$Actions['idMyFriendsPager']('gid=');\">#(my_friends_group.0)</a>");
					if (StringUtils.hasText(gid)) {
						final ITableEntityManager temgr = FriendsUtils
								.getTableEntityManager(FriendsGroup.class);
						FriendsGroup fg = temgr.queryForObjectById(gid, FriendsGroup.class);
						final ArrayList<FriendsGroup> al = new ArrayList<FriendsGroup>();
						if (fg != null) {
							al.add(fg);
							while ((fg = (FriendsGroup) fg.parent()) != null) {
								al.add(fg);
							}
							for (int i = al.size() - 1; i >= 0; i--) {
								final FriendsGroup fg2 = al.get(i);
								sb.append(HTMLBuilder.NAV);
								if (i > 0) {
									sb.append("<a onclick=\"$Actions['idMyFriendsPager']('gid=");
									sb.append(fg2.getId()).append("');\">");
								}
								sb.append(fg2.getText());
								if (i > 0) {
									sb.append("</a>");
								}
							}
						}
					}
				}
				return sb.toString();
			}
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		final String op = compParameter.getRequestParameter("op");
		parameters.put("op", op);
		if (!"search".equals(op)) {
			putParameter(compParameter, parameters, "gid");
		}
		return parameters;
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		final IAccount login = AccountSession.getLogin(compParameter.getSession());
		if (login == null) {
			return null;
		}
		final StringBuilder sql = new StringBuilder();
		final ArrayList<Object> al = new ArrayList<Object>();
		final ITableEntityManager temgr = FriendsUtils.getTableEntityManager(Friends.class);
		final String op = compParameter.getRequestParameter("op");
		if ("online".equals(op)) {
			sql.append("select t.* from ").append(temgr.getTablename());
			sql.append(" t left join ").append(OrgUtils.am().tblname());
			sql.append(" a on t.friendid=a.id where a.login=? and t.userid=?");
			return temgr.query(new SQLValue(sql.toString(),
					new Object[] { Boolean.TRUE, login.getId() }), Friends.class);
		} else if ("search".equals(op)) {
			final String fs_name = compParameter.getRequestParameter("fs_name");
			if (StringUtils.hasText(fs_name)) {
				sql.append(" and name like '%").append(fs_name).append("%'");
			}
			final String fs_text = compParameter.getRequestParameter("fs_text");
			if (StringUtils.hasText(fs_text)) {
				sql.append(" and text like '%").append(fs_name).append("%'");
			}
			final String fs_email = compParameter.getRequestParameter("fs_email");
			if (StringUtils.hasText(fs_email)) {
				sql.append(" and email like '%").append(fs_name).append("%'");
			}
			final String fs_mobile = compParameter.getRequestParameter("fs_mobile");
			if (StringUtils.hasText(fs_mobile)) {
				sql.append(" and mobile like '%").append(fs_name).append("%'");
			}
			final String fs_birthday_s = compParameter.getRequestParameter("fs_birthday_s");
			final String fs_birthday_e = compParameter.getRequestParameter("fs_birthday_e");
			if (StringUtils.hasText(fs_birthday_s) || StringUtils.hasText(fs_birthday_e)) {
				sql.append(" and (");
				if (StringUtils.hasText(fs_birthday_s)) {
					final int s = ConvertUtils.toInt(fs_birthday_s, 0);
					final Calendar cal = Calendar.getInstance();
					cal.add(Calendar.YEAR, -s);
					sql.append("birthday<?");
					al.add(cal.getTime());
					if (StringUtils.hasText(fs_birthday_e)) {
						sql.append(" and ");
					}
				}
				if (StringUtils.hasText(fs_birthday_e)) {
					final int e = ConvertUtils.toInt(fs_birthday_e, 0);
					final Calendar cal = Calendar.getInstance();
					cal.add(Calendar.YEAR, -e);
					sql.append("birthday>?");
					al.add(cal.getTime());
				}
				sql.append(")");
			}
			if (sql.length() > 0) {
				sql.insert(0, "1=1");
			} else {
				sql.append("1=2");
			}
			return OrgUtils.um().query(new ExpressionValue(sql.toString(), al.toArray()));
		} else {
			sql.append("userid=?");
			al.add(login.getId());
			final String gid = compParameter.getRequestParameter("gid");
			if (StringUtils.hasText(gid)) {
				sql.append(" and groupid=?");
				al.add(gid);
			}
			return temgr.query(new ExpressionValue(sql.toString(), al.toArray()), Friends.class);
		}
	}
}
