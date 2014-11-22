package net.simpleos.backend.links;

import java.util.Map;

import net.simpleframework.content.component.remark.RemarkItem;
import net.simpleframework.core.IInitializer;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleos.impl.ASimpleosAppclicationModule;

public class LinksAppModule extends ASimpleosAppclicationModule implements ILinksAppModule {

	public static Table catalog = new Table("simpleos_links", "id");

	@Override
	protected void putTables(final Map<Class<?>, Table> tables) {
		tables.put(LinksBean.class, catalog);
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
		doInit(LinksUtils.class, deployName);
		LinksUtils.appModule = this;
	}

	@Override
	public String tabs(PageRequestResponse requestResponse) {
		return null;
	}

	@Override
	public Class<?> getEntityBeanClass() {
		return LinksBean.class;
	}

	@Override
	public void doAttentionSent(ComponentParameter compParameter, RemarkItem remark, Class<?> classBean) {
	}

	@Override
	public String getDeployPath() {
		return deployName;
	}

}
