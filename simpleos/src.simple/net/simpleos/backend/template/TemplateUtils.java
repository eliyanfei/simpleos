package net.simpleos.backend.template;

import java.util.HashMap;
import java.util.Map;

import net.simpleos.SimpleosUtil;
import net.simpleos.template.ITemplateBean;
import net.simpleos.template.TemplateBean1;
import net.simpleos.utils.StringsUtils;

/**
 * 
 * @author 李岩飞
 * 2013-3-28下午04:17:22
 */
public class TemplateUtils {
	public static Map<String, ITemplateBean> templateMap = new HashMap<String, ITemplateBean>();

	/**
	 * 获取模板地址
	 * @return
	 */
	public static String getTemplateUrl() {
		final TemplateBean templateBean = TemplateUtils.getTemplateBean();
		final String templateUrl = "/simpleos/template/" + (StringsUtils.u(templateBean.templateId, "/", templateBean.templateId)) + ".jsp";
		return templateUrl;
	}

	public static TemplateBean getTemplateBean() {
		TemplateBean bean = new TemplateBean();
		bean.templateId = new TemplateBean1().getName();
		String template = SimpleosUtil.attrMap.get("template.template");
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
