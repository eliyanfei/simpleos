package net.simpleframework.desktop;

import java.util.Map;

import net.itsite.impl.AItSiteAppclicationModule;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.IInitializer;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.core.bean.IIdBeanAware;
import net.simpleframework.core.id.ID;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.AbstractComponentBean;

public class DesktopUIApplicationModule extends AItSiteAppclicationModule {

	public static Table desktop_ui = new Table("simple_desktop", "id");

	@Override
	protected void putTables(Map<Class<?>, Table> tables) {
		tables.put(DesktopUIBean.class, desktop_ui);
	}

	@Override
	public void init(final IInitializer initializer) {
		super.init(initializer);
		doInit(getClass(), deployName);
		DesktopUIUtils.application = this;
		DesktopMgr.getDesktopMgr().init();
	}

	@Override
	public String tabs(PageRequestResponse requestResponse) {
		return null;
	}

	@Override
	public ITableEntityManager getDataObjectManager(Class<?> beanClazz) {
		return super.getDataObjectManager(beanClazz);
	}

	@Override
	public ITableEntityManager getDataObjectManager() {
		return super.getDataObjectManager(getEntityBeanClass());
	}

	@Override
	public Class<? extends IIdBeanAware> getEntityBeanClass() {
		return DesktopUIBean.class;
	}

	public void doUpdate(DesktopUIBean bean) {
		ITableEntityManager tMgr = getDataObjectManager();
		if (tMgr != null) {
			if (bean.getId() == null) {
				bean.setId(ID.Utils.newID(tMgr.nextIncValue("id")));
				tMgr.insert(bean);
			} else {
				tMgr.update(bean);
			}
		}
	}

	static final String deployName = "desktop";

	@Override
	public AbstractComponentBean getComponentBean(PageRequestResponse requestResponse) {
		return null;
	}

	@Override
	public String getDeployPath() {
		return deployName;
	}

}
