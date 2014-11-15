package net.simpleframework.organization.account;

import java.util.Date;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.content.ContentUtils;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.page.PageRequestResponse;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AccountViewLogUtils {

	static int LOG_SIZE = 12;

	public static void updateLog(final IAccount account, final IAccount login,
			final EFunctionModule vtype) {
		if (account == null || login == null) {
			return;
		}
		final ITableEntityManager log_mgr = OrgUtils.getTableEntityManager(AccountViewLog.class);
		AccountViewLog log = log_mgr.queryForObject(
				new ExpressionValue("vtype=? and accountId=? and viewId=?", new Object[] { vtype,
						account.getId(), login.getId() }), AccountViewLog.class);
		if (log == null) {
			log = new AccountViewLog();
			log.setVtype(vtype);
			log.setAccountId(account.getId());
			log.setViewId(login.getId());
			log.setCreateDate(new Date());
			log.setLastUpdate(log.getCreateDate());
			log_mgr.insert(log);

			// strip logs
			final IQueryEntitySet<AccountViewLog> qs = log_mgr.query(
					new ExpressionValue("vtype=? and accountId=? order by lastupdate desc",
							new Object[] { vtype, account.getId() }), AccountViewLog.class);
			if (qs.getCount() > LOG_SIZE) {
				qs.move(LOG_SIZE - 2);
				final AccountViewLog log2 = qs.next();
				if (log2 != null) {
					log_mgr.delete(new ExpressionValue("vtype=? and accountId=? and lastupdate<?",
							new Object[] { vtype, account.getId(), log2.getLastUpdate() }));
				}
			}
		} else {
			log.setLastUpdate(new Date());
			log_mgr.update(new Object[] { "lastUpdate" }, log);
		}
	}

	public static void updateLog(final PageRequestResponse requestResponse,
			final EFunctionModule vtype) {
		final IAccount account = ContentUtils.getAccountAware().getSpecifiedAccount(requestResponse);
		final IAccount login = AccountSession.getLogin(requestResponse.getSession());
		if (login != null && login.equals(account)) {
			return;
		}
		updateLog(account, login, vtype);
	}

	public static IQueryEntitySet<AccountViewLog> queryLogs(
			final PageRequestResponse requestResponse, final EFunctionModule vtype) {
		return queryLogs(ContentUtils.getAccountAware().getAccount(requestResponse), vtype);
	}

	public static IQueryEntitySet<AccountViewLog> queryLogs(final IAccount account,
			final EFunctionModule vtype) {
		if (account == null) {
			return null;
		}
		final ITableEntityManager temgr = OrgUtils.getTableEntityManager(AccountViewLog.class);
		return temgr.query(new ExpressionValue("accountId=? and vtype=? order by lastUpdate desc",
				new Object[] { account.getId(), vtype }), AccountViewLog.class);
	}
}
