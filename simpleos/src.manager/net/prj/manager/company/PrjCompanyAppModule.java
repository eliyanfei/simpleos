package net.prj.manager.company;

import java.util.Map;

import net.itsite.impl.AItSiteAppclicationModule;
import net.simpleframework.content.component.remark.RemarkItem;
import net.simpleframework.core.IInitializer;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 站点设置
 * 
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-3下午12:29:55
 */
public class PrjCompanyAppModule extends AItSiteAppclicationModule implements IPrjCompanyAppModule {

	@Override
	protected void putTables(final Map<Class<?>, Table> tables) {
	}

	static final String deployName = "site";

	@Override
	public void init(final IInitializer initializer) {
		super.init(initializer);
		doInit(PrjCompanyUtils.class, deployName);
		PrjCompanyUtils.appModule = this;
	}

	@Override
	public String tabs(PageRequestResponse requestResponse) {
		return null;
	}

	@Override
	public void doAttentionSent(ComponentParameter compParameter, RemarkItem remark, Class<?> classBean) {
	}

	@Override
	public String getDeployPath() {
		return deployName;
	}

}
