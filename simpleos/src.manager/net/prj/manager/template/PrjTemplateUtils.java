package net.prj.manager.template;

import java.util.HashMap;
import java.util.Map;

import net.itsite.utils.StringsUtils;
import net.prj.core.i.IModelBean;
import net.prj.core.impl.frame.ITemplateBean;
import net.prj.core.impl.frame.TemplateBean1;
import net.prj.manager.PrjMgrUtils;

/**
 * 
 * @author 李岩飞
 * 2013-3-28下午04:17:22
 */
public class PrjTemplateUtils {
	public static IPrjTemplateAppModule appModule = null;
	public static Map<String, IModelBean> modelMap = new HashMap<String, IModelBean>();
	public static Map<String, ITemplateBean> templateMap = new HashMap<String, ITemplateBean>();

	/**
	 * 获取模板地址
	 * @return
	 */
	public static String getTemplateUrl() {
		final PrjTemplateBean templateBean = PrjTemplateUtils.getTemplateBean();
		final String templateUrl = "/frame/template/" + (StringsUtils.u(templateBean.templateId, "/", templateBean.templateId)) + ".jsp";
		return templateUrl;
	}

	public static PrjTemplateBean getTemplateBean() {
		Map<String, String> map = PrjMgrUtils.loadCustom("template");
		PrjTemplateBean bean = new PrjTemplateBean();
		bean.templateId = new TemplateBean1().getName();
		String template = map.get("template");
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
