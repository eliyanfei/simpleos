package net.simpleos;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.core.ApplicationModuleException;
import net.simpleframework.core.ExecutorRunnable;
import net.simpleframework.core.IInitializer;
import net.simpleframework.core.ITaskExecutorAware;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.OrganizationApplicationModule;
import net.simpleframework.organization.account.Account;
import net.simpleframework.organization.account.AccountManager;
import net.simpleframework.organization.account.EAccountStatus;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.organization.account.IAccount.InsertCallback;
import net.simpleframework.organization.component.register.DefaultUserRegisterHandle;
import net.simpleframework.organization.component.register.UserRegisterUtils;
import net.simpleframework.organization.impl.User;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.HTMLUtils;
import net.simpleframework.util.IoUtils;
import net.simpleframework.util.script.ScriptEvalUtils;
import net.simpleos.utils.MD5;
/**
 * 
 * @author 李岩飞 
 * @date 2014年11月19日 下午4:57:44 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public class SimpleosOrganizationApplicationModule extends OrganizationApplicationModule {
	@Override
	public void init(IInitializer initializer) {
		super.init(initializer);
		//保存
		((ITaskExecutorAware) getApplication()).getTaskExecutor().addScheduledTask(SimpleosUtil.getWeekTime() + 1, SimpleosUtil.weekperiod + 1,
				new ExecutorRunnable() {
					@Override
					protected void task() throws IOException {
						doReviewAccountMail();
					}
				});
	}

	@Override
	protected void putTables(Map<Class<?>, Table> tables) {
		super.putTables(tables);
		tables.put(AccountExt.class, new Table("simple_account"));
	}

	private AccountManager accountMgr;

	@Override
	public AccountManager getAccountMgr() {
		if (accountMgr == null) {
			accountMgr = new AccountManager() {
				@Override
				public Class<? extends IAccount> getBeanClass() {
					return AccountExt.class;
				}

				//TODO MD5 加密密码
				public IAccount insertAccount(final String name, final String password, final String ip, final InsertCallback callback) {
					final AccountExt account = new AccountExt();
					account.setPassword(MD5.getHashString(password));
					if ("true".equals(SimpleosUtil.attrMap.get("sys_mail"))) {
						account.setStatus(EAccountStatus.register);
					} else {
						account.setStatus(EAccountStatus.normal);
					}
					account.setCreateDate(new Date());
					account.setLastLoginDate(account.getCreateDate());
					account.setLastLoginIP(ip);
					account.setPoints(10);
					if (callback != null) {
						callback.init(account);
					}
					getEntityManager().insertTransaction(account, new TableEntityAdapter() {
						@Override
						public void afterInsert(final ITableEntityManager manager, final Object[] objects) {
							final User user = (User) OrgUtils.um().createUser(account.getId());
							user.setName(HTMLUtils.stripScripts(name));
							if (callback != null) {
								callback.insert(user);
							}
							OrgUtils.um().insert(user);
						}
					});
					return account;
				};
			};
		}
		return accountMgr;
	}

	public static class AccountExt extends Account {
		private String password;

		@Override
		public void setPassword(String password) {
			this.password = password;
		}

		@Override
		public String getPassword() {
			return password;
		}

	}

	/**
	 * 每周回顾
	 */
	protected void doReviewAccountMail() {
		IQueryEntitySet<?> qs = OrgUtils.um().query(new ExpressionValue("1=1", null));
		IUser user;
		final Map<String, Object> variable = new HashMap<String, Object>();
		variable.put("title", getApplication().getApplicationConfig().getTitle());
		try {
			while ((user = (IUser) qs.next()) != null) {
				variable.put("email", user.getEmail());
				variable.put("uId", user.getId().toString());
				final String textBody = ScriptEvalUtils.replaceExpr(
						variable,
						IoUtils.getStringFromInputStream(SimpleosOrganizationApplicationModule.class.getClassLoader().getResourceAsStream(
								BeanUtils.getResourceClasspath(SimpleosOrganizationApplicationModule.class, "review.html"))));
				//每周通知一次没有激活账号的用户
				final AccountExt account = (AccountExt) user.account();
				if (account.getStatus() == EAccountStatus.register) {
					UserRegisterUtils.sentMailActivation(account, DefaultUserRegisterHandle.class, "account_active.html");
				}
			}
		} catch (final IOException e) {
			throw ApplicationModuleException.wrapException(e);
		}
	}
}
