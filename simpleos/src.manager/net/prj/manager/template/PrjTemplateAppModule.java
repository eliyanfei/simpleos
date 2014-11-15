package net.prj.manager.template;

import java.beans.Beans;
import java.util.Collection;
import java.util.Map;

import net.itsite.impl.AItSiteAppclicationModule;
import net.itsite.utils.ReflectUtils;
import net.prj.core.i.IModelBean;
import net.prj.core.impl.frame.ITemplateBean;
import net.prj.manager.menu.PrjMenuBean;
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
public class PrjTemplateAppModule extends AItSiteAppclicationModule implements IPrjTemplateAppModule {

	@Override
	protected void putTables(final Map<Class<?>, Table> tables) {
	}

	static final String deployName = "template";

	@Override
	public void init(final IInitializer initializer) {
		super.init(initializer);
		doInit(PrjTemplateUtils.class, deployName);
		PrjTemplateUtils.appModule = this;

		try {
			Beans.setDesignTime(true);
			ReflectUtils.createSharedReflections("classes", "bin", "app.");
			try {
				final Collection<String> subTypes = ReflectUtils.listSubClass(ITemplateBean.class);//
				for (final String subType : subTypes) {
					final ITemplateBean impl = ReflectUtils.initClass(subType, ITemplateBean.class);
					if (null == impl)
						continue;
					PrjTemplateUtils.templateMap.put(impl.getName(), impl);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			Beans.setDesignTime(true);
			ReflectUtils.createSharedReflections("classes", "bin", "app.");
			try {
				final Collection<String> subTypes = ReflectUtils.listSubClass(IModelBean.class);//
				for (final String subType : subTypes) {
					final IModelBean impl = ReflectUtils.initClass(subType, IModelBean.class);
					if (null == impl)
						continue;
					PrjTemplateUtils.modelMap.put(impl.getName(), impl);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public String tabs(PageRequestResponse requestResponse) {
		return null;
	}

	@Override
	public Class<?> getEntityBeanClass() {
		return PrjMenuBean.class;
	}

	@Override
	public void doAttentionSent(ComponentParameter compParameter, RemarkItem remark, Class<?> classBean) {
	}

	@Override
	public String getDeployPath() {
		return deployName;
	}

}
