package net.simpleframework.applets.attention;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.applets.notification.MailMessageNotification;
import net.simpleframework.applets.notification.NotificationUtils;
import net.simpleframework.core.IInitializer;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.util.db.DbUtils;
import net.simpleframework.web.AbstractWebApplicationModule;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.page.PageRequestResponse;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultAttentionApplicationModule extends AbstractWebApplicationModule implements IAttentionApplicationModule {
	static final String deployName = "applets/attention";

	@Override
	protected void putTables(final Map<Class<?>, Table> tables) {
		tables.put(AttentionBean.class, new Table("simple_attention"));
	}

	@Override
	public Class<?> getEntityBeanClass() {
		return AttentionBean.class;
	}

	@Override
	public void init(final IInitializer initializer) {
		super.init(initializer);
		doInit(AttentionUtils.class, deployName);

		// 删除用户关联数据
		OrgUtils.um().addListener(new TableEntityAdapter() {
			@Override
			public void afterDelete(final ITableEntityManager manager, final IDataObjectValue dataObjectValue) {
				final Object[] params = dataObjectValue.getValues();
				AttentionUtils.getTableEntityManager().delete(new ExpressionValue(DbUtils.getIdsSQLParam("accountid", params.length), params));
			}
		});
	}

	@Override
	public void sentMessage(final PageRequestResponse requestResponse, final EFunctionModule vtype, final ISentCallback callback) {
		if (callback == null) {
			return;
		}
		final IQueryEntitySet<AttentionBean> qs = AttentionUtils.getTableEntityManager().query(
				new ExpressionValue("vtype=? and attentionid=?", new Object[] { vtype, callback.getId() }), AttentionBean.class);
		AttentionBean attention;
		while ((attention = qs.next()) != null) {
			try {
				final IUser toUser = OrgUtils.um().queryForObjectById(attention.getAccountId());
				if (toUser != null && callback.isSent(toUser)) {
					final MailMessageNotification mailMessage = new MailMessageNotification();
					mailMessage.setHtmlContent(true);
					mailMessage.getTo().add(toUser);
					mailMessage.setSubject(callback.getSubject(toUser));
					mailMessage.setTextBody(callback.getBody(toUser));
					NotificationUtils.sendMessage(mailMessage);
				}
			} catch (final Exception e) {
				logger.warn(e);
			}
		}
	}

	@Override
	public AttentionBean get(final PageRequestResponse requestResponse, final EFunctionModule vtype, final Object attentionId) {
		final IAccount login = AccountSession.getLogin(requestResponse.getSession());
		if (login == null) {
			return null;
		}
		return AttentionUtils.getTableEntityManager().queryForObject(
				new ExpressionValue("vtype=? and accountId=? and attentionId=?", new Object[] { vtype, login.getId(), attentionId }),
				AttentionBean.class);
	}

	@Override
	public IQueryEntitySet<AttentionBean> queryAttentions(EFunctionModule vtype, ID attentionId) {
		return AttentionUtils.getTableEntityManager().query(new ExpressionValue("vtype=?  and attentionId=?", new Object[] { vtype, attentionId }),
				AttentionBean.class);
	}

	@Override
	public void delete(final PageRequestResponse requestResponse, final EFunctionModule vtype, final Object attentionId) {
		final IAccount login = AccountSession.getLogin(requestResponse.getSession());
		if (login == null) {
			return;
		}
		AttentionUtils.getTableEntityManager().delete(
				new ExpressionValue("vtype=? and accountId=? and attentionId=?", new Object[] { vtype, login.getId(), attentionId }));
	}

	@Override
	public AttentionBean insert(final PageRequestResponse requestResponse, final EFunctionModule vtype, final ID attentionId) {
		final IAccount login = AccountSession.getLogin(requestResponse.getSession());
		if (login == null) {
			return null;
		}
		final AttentionBean attention = new AttentionBean();
		attention.setCreateDate(new Date());
		attention.setAccountId(login.getId());
		attention.setVtype(vtype);
		attention.setAttentionId(attentionId);
		AttentionUtils.getTableEntityManager().insert(attention);
		return attention;
	}

	@Override
	public AttentionBean insert(ID userId, EFunctionModule vtype, ID attentionId) {
		if (userId == null) {
			return null;
		}
		AttentionBean attention = null;
		try {
			attention = new AttentionBean();
			attention.setCreateDate(new Date());
			attention.setAccountId(userId);
			attention.setVtype(vtype);
			attention.setAttentionId(attentionId);
			AttentionUtils.getTableEntityManager().insert(attention);
		} catch (Exception e) {
		}
		return attention;
	}

	@Override
	public void deleteByAttentionId(final PageRequestResponse requestResponse, final EFunctionModule vtype, final Object[] attentionIds) {
		final ArrayList<Object> al = new ArrayList<Object>(Arrays.asList(attentionIds));
		al.add(0, vtype);
		AttentionUtils.getTableEntityManager().delete(
				new ExpressionValue("vtype=? and " + DbUtils.getIdsSQLParam("attentionid", attentionIds.length), al.toArray()));
	}

	@Override
	public String getApplicationText() {
		return StringUtils.text(super.getApplicationText(), LocaleI18n.getMessage("DefaultAttentionApplicationModule.0"));
	}
}
