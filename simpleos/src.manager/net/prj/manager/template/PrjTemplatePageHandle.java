package net.prj.manager.template;

import java.util.List;
import java.util.Map;

import net.simpleframework.web.page.DefaultPageHandle;
import net.simpleframework.web.page.PageParameter;

/**
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-5下午03:57:10
 */
public class PrjTemplatePageHandle extends DefaultPageHandle {

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
		PrjTemplateBean templateBean = PrjTemplateUtils.getTemplateBean();
		if (templateBean == null)
			return pages;
		importPages[i++] = "/simpleos/template/" + templateBean.templateId + "/" + templateBean.templateId + ".xml";
		return importPages;
	}

	@Override
	public void pageLoad(PageParameter pageParameter, Map<String, Object> dataBinding, List<String> visibleToggleSelector,
			List<String> readonlySelector, List<String> disabledSelector) {
		super.pageLoad(pageParameter, dataBinding, visibleToggleSelector, readonlySelector, disabledSelector);
	}
}
