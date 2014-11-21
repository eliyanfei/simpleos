package net.simpleos.openapi;

import java.util.Map;

import net.itsite.impl.AItSiteAppclicationModule;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount.InsertCallback;
import net.simpleframework.organization.account.LoginObject;
import net.simpleframework.organization.account.LoginObject.EAccountType;
import net.simpleframework.util.HTTPUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.LastUrlFilterListener;
import net.simpleframework.web.page.PageRequestResponse;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月20日 上午11:49:57 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public class OpenapiAppModule extends AItSiteAppclicationModule implements IOpenapiAppModule {
	private String deployName = "openapi";
	public static IOpenapiAppModule applicationModule;

	static final Table openapi = new Table("simpleos_openapi");

	@Override
	public Class<?> getEntityBeanClass() {
		return OpenapiBean.class;
	}

	@Override
	protected void putTables(Map<Class<?>, Table> tables) {
		super.putTables(tables);
		tables.put(OpenapiBean.class, openapi);
	}

	@Override
	public OpenapiBean getOpenapiBean(final EOpenapi openapi, final Object openId) {
		final ITableEntityManager tMgr = getDataObjectManager();
		return tMgr.queryForObject(new ExpressionValue("openapi=? and openId=?", new Object[] { openapi, openId }), OpenapiBean.class);
	}

	@Override
	public String login(final PageRequestResponse requestResponse, final OpenapiBean openapiBean) {
		if (openapiBean == null) {
			return null;
		}
		IUser user = OrgUtils.um().getUserByName(openapiBean.getName());
		if (user == null) {
			user = OrgUtils.um().getUserByEmail(openapiBean.getEmail());
		}
		if (user != null && user.account() != null) {
			AccountSession.setLogin(requestResponse.request, new LoginObject(user.getName(), EAccountType.normal));
			if (AccountSession.isLogin(requestResponse.getSession())) {
				return StringUtils.text((String) requestResponse.getSessionAttribute(LastUrlFilterListener.SESSION_LAST_URL), "/");
			}
		}
		return null;
	}

	@Override
	public String login1(final PageRequestResponse requestResponse, final OpenapiBean openapiBean) {
		if (openapiBean == null) {
			return null;
		}
		final String email = openapiBean.getEmail();
		IUser user = OrgUtils.um().getUserByName(openapiBean.getName());
		if (user != null) {
			return "user:该用户名已经被注册！";
		} else {
			user = OrgUtils.um().getUserByEmail(email);
			if (user != null) {
				return "email:该邮箱已经被注册！";
			}
		}
		if (user == null) {
			final String name = openapiBean.getName();
			final String text = openapiBean.getText();
			final String password = openapiBean.getPass();
			if (StringUtils.hasText(name) && StringUtils.hasText(text) && StringUtils.hasText(password)) {
				user = OrgUtils.am().insertAccount(name, password, HTTPUtils.getRemoteAddr(requestResponse.request), new InsertCallback() {
					@Override
					public void insert(final IUser user) {
						user.setText(text);
						user.setName(name);
						user.setEmail(email);
					}
				}).user();
			}
		}
		if (user != null && user.account() != null) {
			AccountSession.setLogin(requestResponse.request, new LoginObject(user.getName(), EAccountType.normal));
			if (AccountSession.isLogin(requestResponse.getSession())) {
				return StringUtils.text((String) requestResponse.getSessionAttribute(LastUrlFilterListener.SESSION_LAST_URL), "/");
			}
		}
		return null;
	}

	@Override
	public String getDeployPath() {
		return deployName;
	}
}
