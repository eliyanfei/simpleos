package net.prj.manager;

import java.util.Map;

import net.itsite.ItSiteUtil;
import net.itsite.docu.DocuUtils;
import net.itsite.impl.AItSiteAppclicationModule;
import net.itsite.utils.StringsUtils;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.core.IInitializer;
import net.simpleframework.core.ado.db.Table;

/**
 * 
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-3下午12:29:55
 */
public class PrjMgrAppModule extends AItSiteAppclicationModule implements IPrjMgrAppModule {

	public static Table custom_ = new Table("prj_custom", "id");
	public static Table nav_ = new Table("prj_nav", "id");

	@Override
	protected void putTables(final Map<Class<?>, Table> tables) {
		tables.put(PrjCustomBean.class, custom_);
		tables.put(PrjNavBean.class, nav_);
	}

	static final String deployName = "prjmgr";

	@Override
	public void init(final IInitializer initializer) {
		super.init(initializer);
		doInit(PrjMgrUtils.class, deployName);
		PrjMgrUtils.appModule = this;
		loadCustom();
		DocuUtils.docuPath = StringsUtils.trimNull(ItSiteUtil.attrMap.get("docu.docuPath"), "c:\\");
		ItSiteUtil.url = StringsUtils.trimNull(ItSiteUtil.attrMap.get("site.site_url"), "");
		ItSiteUtil.title = StringsUtils.trimNull(ItSiteUtil.attrMap.get("site.site_name"), "");
	}

	/**
	 * 加载所有的常量值
	 * @return
	 */
	public void loadCustom() {
		try {
			IQueryEntitySet<PrjCustomBean> qs = PrjMgrUtils.appModule.queryBean("1=1", PrjCustomBean.class);
			PrjCustomBean customBean = null;
			while ((customBean = qs.next()) != null) {
				ItSiteUtil.attrMap.put(customBean.getType() + "." + customBean.getName(), customBean.getValue());
			}
		} catch (Exception e) {
		}
	}

	@Override
	public Class<?> getEntityBeanClass() {
		return PrjCustomBean.class;
	}

	@Override
	public String getDeployPath() {
		return deployName;
	}

}
