package net.prj.manager;

import java.util.Map;

import net.a.ItSiteUtil;
import net.itsite.document.docu.DocuUtils;
import net.itsite.impl.AItSiteAppclicationModule;
import net.itsite.utils.StringsUtils;
import net.simpleframework.content.component.remark.RemarkItem;
import net.simpleframework.core.IInitializer;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;

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
		PrjMgrUtils.loadCustom("sys");

		Map<String, String> map = PrjMgrUtils.loadCustom("docu");
		DocuUtils.docuPath = StringsUtils.trimNull(map.get("docuPath"), "c:\\");
		
		map = PrjMgrUtils.loadCustom("site");
		ItSiteUtil.url = StringsUtils.trimNull(map.get("site_url"), "");
		ItSiteUtil.title = StringsUtils.trimNull(map.get("site_name"), "");
	}

	@Override
	public String tabs(PageRequestResponse requestResponse) {
		return null;
	}

	@Override
	public Class<?> getEntityBeanClass() {
		return PrjCustomBean.class;
	}

	@Override
	public void doAttentionSent(ComponentParameter compParameter, RemarkItem remark, Class<?> classBean) {
	}

	@Override
	public String getDeployPath() {
		return deployName;
	}

}
