package net.prj.manager.links;

import java.util.Map;

import net.itsite.impl.AItSiteAppclicationModule;
import net.simpleframework.content.component.remark.RemarkItem;
import net.simpleframework.core.IInitializer;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;

public class PrjLinksAppModule extends AItSiteAppclicationModule implements IPrjLinksAppModule {

	public static Table catalog = new Table("prj_links", "id");

	@Override
	protected void putTables(final Map<Class<?>, Table> tables) {
		tables.put(PrjLinksBean.class, catalog);
	}

	@Override
	public String getViewUrl(Object id) {
		final StringBuilder sb = new StringBuilder();
		return sb.toString();
	}

	static final String deployName = "prjlinks";

	@Override
	public void init(final IInitializer initializer) {
		super.init(initializer);
		doInit(PrjLinksUtils.class, deployName);
		PrjLinksUtils.appModule = this;
	}

	@Override
	public String tabs(PageRequestResponse requestResponse) {
		return null;
	}

	@Override
	public Class<?> getEntityBeanClass() {
		return PrjLinksBean.class;
	}

	@Override
	public void doAttentionSent(ComponentParameter compParameter, RemarkItem remark, Class<?> classBean) {
	}

	@Override
	public String getDeployPath() {
		return deployName;
	}

}
