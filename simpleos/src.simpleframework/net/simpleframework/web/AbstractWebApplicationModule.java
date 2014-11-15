package net.simpleframework.web;

import java.util.Map;

import net.simpleframework.ado.DataObjectManagerUtils;
import net.simpleframework.core.AbstractApplicationModule;
import net.simpleframework.core.IInitializer;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.organization.IJob;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.SkinUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractWebApplicationModule extends AbstractApplicationModule implements
		IWebApplicationModule {
	@Override
	public String getManager(final Object... params) {
		return IJob.sj_manager;
	}

	@Override
	public IWebApplication getApplication() {
		return (IWebApplication) super.getApplication();
	}

	protected void putTables(final Map<Class<?>, Table> tables) {
	}

	@Override
	public void init(final IInitializer initializer) {
		super.init(initializer);
		putTables(DataObjectManagerUtils.getApplicationModuleTables(this));
	}

	private String applicationUrl;

	@Override
	public String getApplicationUrl(final PageRequestResponse requestResponse) {
		return applicationUrl;
	}

	public void setApplicationUrl(final String applicationUrl) {
		this.applicationUrl = applicationUrl;
	}

	private String skin;

	@Override
	public String getSkin(final PageRequestResponse requestResponse) {
		return SkinUtils.getSkin(requestResponse, skin);
	}

	public void setSkin(final String skin) {
		this.skin = skin;
	}
}
