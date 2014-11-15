package net.simpleframework.content.blog;

import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.content.AbstractContentLayoutModuleHandle;
import net.simpleframework.content.EContentType;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.DateUtils.TimeDistance;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.UrlForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.portal.PageletBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class BlogPortalModule extends AbstractContentLayoutModuleHandle {

	public BlogPortalModule(final PageletBean pagelet) {
		super(pagelet);
	}

	private static String[] defaultOptions = new String[] { "_blog_type=0", "_blog_order=0", "_blog_time=0", "_blog_rows=6", "_blog_descLength=",
			"_blog_dateFormat=", "_show_tabs=true" };

	@Override
	protected String[] getDefaultOptions() {
		return defaultOptions;
	}

	@Override
	public IForward getPageletOptionContent(final ComponentParameter compParameter) {
		return new UrlForward(BlogUtils.deployPath + "jsp/blog_portal_option.jsp");
	}

	@Override
	public IForward getPageletContent(final ComponentParameter compParameter) {
		EContentType contentType = null;
		boolean myTopics = false;
		// 0=全部;1=推荐的;2=我发布的
		final int _blog_type = ConvertUtils.toInt(getPagelet().getOptionProperty("_blog_type"), 0);
		if (_blog_type == 1) {
			contentType = EContentType.recommended;
		} else if (_blog_type == 2) {
			myTopics = true;
		}
		TimeDistance timeDistance = null;
		// 0=不限制;1=一周;2=一月;3=一年
		final int _news_time = ConvertUtils.toInt(getPagelet().getOptionProperty("_blog_time"), 0);
		if (_news_time == 1) {
			timeDistance = TimeDistance.week;
		} else if (_news_time == 2) {
			timeDistance = TimeDistance.month;
		} else if (_news_time == 3) {
			timeDistance = TimeDistance.year;
		}
		final int _tab_param = ConvertUtils.toInt(compParameter.getParameter("_tab_param"), -1);
		final int _blog_order = ConvertUtils.toInt(getPagelet().getOptionProperty("_blog_order"), 0);

		final IQueryEntitySet<?> qs = BlogUtils.queryBlogs(compParameter, contentType, timeDistance, myTopics, _blog_order, _tab_param);
		compParameter.setRequestAttribute("__qs", qs);
		String forward = BlogUtils.deployPath + "jsp/blog_portal.jsp?btype=" + _blog_type;
		forward = doDateFormat(forward, "_blog_dateFormat");
		forward = doDescLength(forward, "_blog_descLength");
		final boolean _show_tabs = ConvertUtils.toBoolean(getPagelet().getOptionProperty("_show_tabs"), true)
				&& ConvertUtils.toBoolean(compParameter.getSessionAttribute("_show_tabs"), true);
		forward = WebUtils.addParameters(forward, "_show_tabs=" + _show_tabs);
		forward = WebUtils.addParameters(forward, "rows=" + ConvertUtils.toInt(getPagelet().getOptionProperty("_blog_rows"), 6));
		return new UrlForward(forward, "pa");
	}
}
