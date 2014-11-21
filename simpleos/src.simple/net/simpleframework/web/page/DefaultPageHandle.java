package net.simpleframework.web.page;

import java.util.List;
import java.util.Map;

import net.simpleframework.core.IApplication;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.LangUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultPageHandle implements IPageHandle {
	@Override
	public IApplication getApplication() {
		return PageUtils.pageContext.getApplication();
	}

	@Override
	public void pageLoad(final PageParameter pageParameter, final Map<String, Object> dataBinding,
			final List<String> visibleToggleSelector, final List<String> readonlySelector,
			final List<String> disabledSelector) {
	}

	@Override
	public Object getBeanProperty(final PageParameter pageParameter, final String beanProperty) {
		final PageBean pageBean = pageParameter.getPageDocument().getPageBean();
		if ("jobView".equals(beanProperty)) {
			return pageBean.getJobView();
		} else if ("importPage".equals(beanProperty)) {
			return pageBean.getImportPage();
		} else if ("title".equals(beanProperty)) {
			return pageBean.getTitle();
		} else if ("importCSS".equals(beanProperty)) {
			return pageBean.getImportCSS();
		} else if ("importJavascript".equals(beanProperty)) {
			return pageBean.getImportJavascript();
		} else if ("scriptInit".equals(beanProperty)) {
			return pageBean.getScriptInit();
		}
		return BeanUtils.getProperty(pageBean, beanProperty);
	}

	@Override
	public void documentInit(final PageParameter pageParameter) {
	}

	protected String[] addImportPage(final PageParameter pageParameter, final String[] importPage) {
		final String[] importPage2 = pageParameter.getPageDocument().getPageBean().getImportPage();
		if (importPage2 == null || importPage2.length == 0) {
			return importPage;
		}
		return (String[]) LangUtils.addAll(importPage2, importPage);
	}
}
