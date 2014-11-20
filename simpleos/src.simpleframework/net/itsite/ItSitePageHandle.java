package net.itsite;

import net.simpleframework.organization.IJob;
import net.simpleframework.web.page.DefaultPageHandle;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.SkinUtils;
/**
 * 
 * @author 李岩飞 
 * @date 2014年11月19日 下午4:57:33 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public class ItSitePageHandle extends DefaultPageHandle {
	public String deploy = "/simple/include/css/";

	@Override
	public Object getBeanProperty(final PageParameter pageParameter, final String beanProperty) {
		if ("importCSS".equals(beanProperty)) {
			return getImportCSS((String[]) super.getBeanProperty(pageParameter, beanProperty), pageParameter);
		} else if ("jobView".equals(beanProperty)) {
			return IJob.sj_manager;
		}
		return super.getBeanProperty(pageParameter, beanProperty);
	}

	public String[] getImportCSS(final String[] pages, final PageParameter pageParameter) {
		final String[] importCss = new String[(pages == null ? 0 : pages.length) + csss.length + 1];
		int i = 0;
		final String skin = SkinUtils.getSkin(pageParameter, SkinUtils.DEFAULT_SKIN);
		if (pages != null)
			for (final String p : pages) {
				importCss[i++] = p;
			}
		for (final String css : csss) {
			importCss[i++] = deploy + skin + "/" + css;
		}
		importCss[i++] = "/default/simple.css";
		return importCss;
	}

	public static String[] csss = { "itsite.css", "category.css" };

}
