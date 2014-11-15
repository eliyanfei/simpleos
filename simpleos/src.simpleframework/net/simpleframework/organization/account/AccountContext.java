package net.simpleframework.organization.account;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.core.ALoggerAware;
import net.simpleframework.core.ApplicationModuleException;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.IAccountRule.TimeUnit;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.StringUtils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public final class AccountContext extends ALoggerAware {
	private static ApplicationContext applicationContext;

	public static void init() throws IOException {
		final File pointsFile = new File(OrgUtils.applicationModule.getApplication()
				.getApplicationAbsolutePath("/WEB-INF/account_rule.xml"));
		if (pointsFile.exists()) {
			applicationContext = new FileSystemXmlApplicationContext(pointsFile.getAbsolutePath());
		} else {
			applicationContext = new ClassPathXmlApplicationContext(BeanUtils.getResourceClasspath(
					AccountContext.class, "account_rule.xml"));
		}
	}

	public static AccountContext getAccountContext() {
		return (AccountContext) applicationContext.getBean("idAccountContext");
	}

	public static void updateTransaction(final IAccount account, final String eventId, final ID logId) {
		update(account, eventId, logId, true);
	}

	public static void update(final HttpSession httpSession, final String eventId, final ID logId) {
		final IAccount account = AccountSession.getLogin(httpSession);
		if (account != null) {
			update(account, eventId, logId);
		}
	}

	public static void update(final IAccount account, final String eventId, final ID logId) {
		update(account, eventId, logId, false);

	}

	public static void update(final IAccount account, final IAccountRule accountRule, final ID logId) {
		update(account, accountRule, logId, false);
	}

	public static void updateTransaction(final IAccount account, final IAccountRule accountRule,
			final ID logId) {
		update(account, accountRule, logId, true);
	}

	private static void update(final IAccount account, final String eventId, final ID logId,
			final boolean transaction) {
		update(account, getAccountRule(eventId), logId, transaction);
	}

	// 必须synchronized，有可能该IAccountRule的值，IAccountRule是单实例
	public static void update(final IAccount account, final IAccountRule accountRule,
			final ID logId, final boolean transaction) {
		if (account == null || accountRule == null) {
			return;
		}
		final AccountContext ctx = getAccountContext();
		if (!ctx.isEnable()) {
			return;
		}
		synchronized (accountRule) {
			final ITableEntityManager logmgr = OrgUtils.getTableEntityManager(AccountLog.class);
			int exp;
			int points;
			try {
				exp = getNum(logmgr, account, accountRule, "exp");
				points = getNum(logmgr, account, accountRule, "points");
			} catch (final Exception e) {
				throw ApplicationModuleException.wrapException(e);
			}
			if (exp != 0 || points != 0) {
				if (accountRule.isLogOnlyonce()
						&& !ID.Utils.isNone(logId)
						&& logmgr.getCount(new ExpressionValue("accountid=? and eventid=? and logid=?",
								new Object[] { account.getId(), accountRule.getEventId(), logId })) > 0) {
					return;
				}

				account.setExp(account.getExp() + exp);
				account.setPoints(account.getPoints() + points);

				final AccountLog log = new AccountLog();
				log.setAccountId(account.getId());
				log.setEventId(accountRule.getEventId());
				log.setCreateDate(new Date());
				log.setExp(exp);
				log.setPoints(points);
				log.setLogId(logId);

				if (transaction) {
					OrgUtils.am().getEntityManager()
							.updateTransaction(account, new TableEntityAdapter() {
								@Override
								public void afterUpdate(final ITableEntityManager manager,
										final Object[] objects) {
									logmgr.insert(log);
								}
							});
				} else {
					OrgUtils.am().update(account);
					logmgr.insert(log);
				}
			}
		}
	}

	private static int getNum(final ITableEntityManager logmgr, final IAccount account,
			final IAccountRule accountRule, final String type) throws Exception {
		final Class<? extends IAccountRule> clazz = accountRule.getClass();
		final String _type = Character.toUpperCase(type.charAt(0)) + type.substring(1);
		int num = (Integer) clazz.getMethod("get" + _type, IAccount.class).invoke(accountRule,
				account);
		if (num != 0) {
			final int maxValue = (Integer) clazz.getMethod("get" + _type + "MaxValue").invoke(
					accountRule);
			if (maxValue > 0) {
				Object s = null, e = null;
				final TimeUnit timeUnit = (TimeUnit) clazz.getMethod("get" + _type + "TimeUnit")
						.invoke(accountRule);
				final Calendar cal = Calendar.getInstance();
				cal.set(Calendar.MILLISECOND, 0);
				if (timeUnit == TimeUnit.day) {
					cal.set(Calendar.HOUR_OF_DAY, 0);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 0);
					s = cal.getTime();
					cal.add(Calendar.DATE, 1);
					e = cal.getTime();
				} else if (timeUnit == TimeUnit.hour) {
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 0);
					s = cal.getTime();
					cal.add(Calendar.HOUR, 1);
					e = cal.getTime();
				}
				if (s != null && e != null) {
					final int sum = logmgr.getSum(type, new ExpressionValue(
							"accountid=? and eventid=? and (createdate>? and createdate<?)", new Object[] {
									account.getId(), accountRule.getEventId(), s, e }));
					num = Math.min(Math.max(maxValue - sum, 0), num);
				}
			}
		}
		return num;
	}

	public static boolean isRuleEnable() {
		return getAccountContext().isEnable();
	}

	public static IAccountRule getAccountRule(final String eventId) {
		final IAccountRule accountRule = getAccountContext().getAccountRuleMap().get(eventId);
		if (accountRule == null) {
			return null;
		}
		if (!StringUtils.hasText(accountRule.getEventId())) {
			accountRule.setEventId(eventId);
		}
		return accountRule;
	}

	private static List<Exp> exps;

	public static Exp getExp(final int exp) {
		if (exps == null) {
			exps = new ArrayList<Exp>();
			final BufferedReader reader = new BufferedReader(new StringReader(getAccountContext()
					.getExpText()));
			String l;
			try {
				while ((l = reader.readLine()) != null) {
					l = l.trim();
					if (l.startsWith("#")) {
						continue;
					}
					final String[] arr = StringUtils.split(l, ";");
					if (arr.length >= 3) {
						final Exp bean = new Exp();
						bean.setExp(Integer.valueOf(arr[0]));
						bean.setLevel(Integer.valueOf(arr[1]));
						bean.setText(arr[2]);
						if (arr.length >= 4) {
							bean.setIcon(arr[3]);
						}
						exps.add(bean);
					}
				}
				Collections.sort(exps, new Comparator<Exp>() {
					@Override
					public int compare(final Exp o1, final Exp o2) {
						return o1.getLevel() > o2.getLevel() ? 1 : -1;
					}
				});
			} catch (final Exception e) {
				getAccountContext().logger.warn(e);
			}
		}
		for (final Exp bean : exps) {
			if (exp >= bean.getExp()) {
				continue;
			}
			return bean;
		}
		return null;
	}

	/* 是否启用账户积分规则 */
	private boolean enable;

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(final boolean enable) {
		this.enable = enable;
	}

	private Map<String, IAccountRule> accountRuleMap;

	public Map<String, IAccountRule> getAccountRuleMap() {
		return accountRuleMap;
	}

	public void setAccountRuleMap(final Map<String, IAccountRule> accountRuleMap) {
		this.accountRuleMap = accountRuleMap;
	}

	/* 经验与等级 */
	private String expText;

	public String getExpText() {
		return expText;
	}

	public void setExpText(final String expText) {
		this.expText = expText;
	}
}
