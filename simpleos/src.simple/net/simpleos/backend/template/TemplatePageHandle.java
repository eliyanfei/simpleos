package net.simpleos.backend.template;

import net.simpleframework.web.page.DefaultPageHandle;
import net.simpleframework.web.page.PageParameter;

/**
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-5下午03:57:10
 */
public class TemplatePageHandle extends DefaultPageHandle {

	@Override
	public Object getBeanProperty(final PageParameter pageParameter, final String beanProperty) {
		if ("importPage".equals(beanProperty)) {
			return importPage((String[]) super.getBeanProperty(pageParameter, beanProperty), pageParameter);
		}
		return super.getBeanProperty(pageParameter, beanProperty);
	}

	public String[] importPage(final String[] pages, final PageParameter pageParameter) {
		final String[] importPages = new String[(pages == null ? 0 : pages.length) + 1];
		int i = 0;
		if (pages != null)
			for (final String p : pages) {
				importPages[i++] = p;
			}
		TemplateBean templateBean = TemplateUtils.getTemplateBean();
		if (templateBean == null)
			return pages;
		importPages[i++] = "/simpleos/template/" + templateBean.templateId + "/" + templateBean.templateId + ".xml";
		return importPages;
	}

}
