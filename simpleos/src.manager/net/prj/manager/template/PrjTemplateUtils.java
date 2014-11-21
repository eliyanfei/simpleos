package net.prj.manager.template;

import java.util.HashMap;
import java.util.Map;

import net.itsite.ItSiteUtil;
import net.itsite.utils.StringsUtils;
import net.simpleos.module.IModuleBean;
import net.simpleos.template.ITemplateBean;
import net.simpleos.template.TemplateBean1;

/**
 * 
 * @author 李岩飞
 * 2013-3-28下午04:17:22
 */
public class PrjTemplateUtils {
	public static IPrjTemplateAppModule appModule = null;
	public static Map<String, IModuleBean> modelMap = new HashMap<String, IModuleBean>();
	public static Map<String, ITemplateBean> templateMap = new HashMap<String, ITemplateBean>();

	/**
	 * 获取模板地址
	 * @return
	 */
	public static String getTemplateUrl() {
		final PrjTemplateBean templateBean = PrjTemplateUtils.getTemplateBean();
		final String templateUrl = "/simpleos/template/" + (StringsUtils.u(templateBean.templateId, "/", templateBean.templateId)) + ".jsp";
		return templateUrl;
	}

	public static PrjTemplateBean getTemplateBean() {
		PrjTemplateBean bean = new PrjTemplateBean();
		bean.templateId = new TemplateBean1().getName();
		String template = ItSiteUtil.attrMap.get("template.template");
		if (StringsUtils.isNotBlank(template)) {
			String[] attrs = template.split(";");
			for (String attr : attrs) {
				String[] vs = attr.split("=");
				if (vs.length == 2)
					bean.attrMap.put(vs[0], vs[1]);
			}
			bean.templateId = bean.attrMap.get("name");
		}
		return bean;
	}

}
