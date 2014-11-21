package net.simpleos.backend;

import java.beans.Beans;
import java.util.Collection;
import java.util.Map;

import net.itsite.ItSiteUtil;
import net.itsite.docu.DocuUtils;
import net.itsite.impl.AItSiteAppclicationModule;
import net.itsite.utils.ReflectUtils;
import net.itsite.utils.StringsUtils;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.core.IInitializer;
import net.simpleframework.core.ado.db.Table;
import net.simpleos.backend.slide.IndexSlideBean;
import net.simpleos.backend.template.TemplateUtils;
import net.simpleos.template.ITemplateBean;

/**
 * 
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-3下午12:29:55
 */
public class BackendAppModule extends AItSiteAppclicationModule {

	public static Table custom_ = new Table("simpleos_custom", "id");
	public static Table nav_ = new Table("simpleos_nav", "id");

	@Override
	protected void putTables(final Map<Class<?>, Table> tables) {
		tables.put(CustomBean.class, custom_);
		tables.put(IndexSlideBean.class, nav_);
	}

	static final String deployName = "backend";

	@Override
	public void init(final IInitializer initializer) {
		super.init(initializer);
		doInit(BackendUtils.class, deployName);
		BackendUtils.applicationModule = this;
		loadCustom();
		loadTempate();
		DocuUtils.docuPath = StringsUtils.trimNull(ItSiteUtil.attrMap.get("docu.docuPath"), "c:\\");
		ItSiteUtil.url = StringsUtils.trimNull(ItSiteUtil.attrMap.get("site.site_url"), "");
		ItSiteUtil.title = StringsUtils.trimNull(ItSiteUtil.attrMap.get("site.site_name"), "");
	}

	public void loadTempate() {
		try {
			Beans.setDesignTime(true);
			ReflectUtils.createSharedReflections("classes", "bin", "simpleos.");
			try {
				final Collection<String> subTypes = ReflectUtils.listSubClass(ITemplateBean.class);//
				for (final String subType : subTypes) {
					final ITemplateBean impl = ReflectUtils.initClass(subType, ITemplateBean.class);
					if (null == impl)
						continue;
					TemplateUtils.templateMap.put(impl.getName(), impl);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 加载所有的常量值
	 * @return
	 */
	public void loadCustom() {
		try {
			IQueryEntitySet<CustomBean> qs = BackendUtils.applicationModule.queryBean("1=1", CustomBean.class);
			CustomBean customBean = null;
			while ((customBean = qs.next()) != null) {
				ItSiteUtil.attrMap.put(customBean.getType() + "." + customBean.getName(), customBean.getValue());
			}
		} catch (Exception e) {
		}
	}

	@Override
	public Class<?> getEntityBeanClass() {
		return CustomBean.class;
	}

	@Override
	public String getDeployPath() {
		return deployName;
	}

}
