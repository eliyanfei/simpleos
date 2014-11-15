package net.simpleframework.organization.account;

import java.util.Date;

import net.itsite.utils.MD5;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.organization.AbstractOrganizationManager;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.IAccount.InsertCallback;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class AccountManager extends AbstractOrganizationManager<IAccount> {

	public AccountManager() {
		// 加入admin
		final IAccount account = getAccountByName(IUser.admin);
		if (account == null) {
			insertAccount(IUser.admin, IUser.admin, null, new IAccount.InsertCallback() {
				@Override
				public void insert(final IUser user) {
					user.setText(IUser.admin);
				}
			});
		}
	}

	@Override
	public Class<? extends IAccount> getBeanClass() {
		return Account.class;
	}

	public IAccount getAccountByName(final String name) {
		final IUser user = OrgUtils.um().getUserByName(name);
		if (user == null) {
			return null;
		}
		return queryForObjectById(user.getId());
	}

	public void updatePoint(final Object userId, final int points) {
		final IAccount account = queryForObjectById(userId);
		if (account != null) {
			account.setPoints(account.getPoints() + points);
			if (account.getPoints() < 0) {
				account.setPoints(0);
			}
			update(new String[] { "points" }, account);
		}
	}

	public static void main(String[] args) {
		System.out.println(MD5.getHashString("demo"));
	}
	
	public IAccount insertAccount(final String name, final String password, final String ip, final InsertCallback callback) {
		final Account account = new Account();
		account.setPassword(MD5.getHashString(password));
		account.setStatus(EAccountStatus.normal);
		account.setCreateDate(new Date());
		account.setLastLoginDate(account.getCreateDate());
		account.setLastLoginIP(ip);
		if (callback != null) {
			callback.init(account);
		}
		getEntityManager().insertTransaction(account, new TableEntityAdapter() {
			@Override
			public void afterInsert(final ITableEntityManager manager, final Object[] objects) {
				final IUser user = OrgUtils.um().createUser(account.getId());
				user.setName(name);
				if (callback != null) {
					callback.insert(user);
				}
				OrgUtils.um().insert(user);
			}
		});
		return account;
	}
}
