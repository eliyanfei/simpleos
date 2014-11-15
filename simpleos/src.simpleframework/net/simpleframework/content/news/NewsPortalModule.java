package net.simpleframework.content.news;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.simpleframework.content.AbstractContentLayoutModuleHandle;
import net.simpleframework.content.EContentType;
import net.simpleframework.content.component.newspager.NewsCatalog;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.DateUtils.TimeDistance;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.PageParameter;
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
public class NewsPortalModule extends AbstractContentLayoutModuleHandle {
	public NewsPortalModule(final PageletBean pagelet) {
		super(pagelet);
	}

	private static String[] defaultOptions = new String[] { "_news_catalog=", "_news_type=0", "_news_order=0", "_news_time=0", "_news_rows=6",
			"_news_descLength=0", "_news_dateFormat=", "_news_image_dimension=400*280", "_show_tabs=true" };

	@Override
	protected String[] getDefaultOptions() {
		return defaultOptions;
	}

	@Override
	public IForward getPageletOptionContent(final ComponentParameter compParameter) {
		return new UrlForward(NewsUtils.deployPath + "jsp/news_layout_option.jsp");
	}

	@Override
	public void optionLoaded(final PageParameter pageParameter, final Map<String, Object> dataBinding) {
		super.optionLoaded(pageParameter, dataBinding);
		final Properties properties = getPagelet().getOptionProperties();
		if (properties != null) {
			final List<NewsCatalog> catalogs = NewsUtils.applicationModule.listNewsCatalog(pageParameter);
			final int catalog = ConvertUtils.toInt(properties.getProperty("_news_catalog"), -1);
			if (catalog > -1) {
				dataBinding.put("_news_catalog_text", catalogs.get(catalog).getText());
			}
		}
	}

	@Override
	public IForward getPageletContent(final ComponentParameter compParameter) {
		final Object _news_catalog = ConvertUtils.toInt(getPagelet().getOptionProperty("_news_catalog"));

		EContentType contentType = null;
		// 0=全部;1=推荐的;2=图片
		final int _news_type = ConvertUtils.toInt(getPagelet().getOptionProperty("_news_type"), 0);
		if (_news_type == 1) {
			contentType = EContentType.recommended;
		} else if (_news_type == 2) {
			contentType = EContentType.image;
		}

		TimeDistance timeDistance = null;
		// 0=不限制;1=一周;2=一月;3=一年
		final int _news_time = ConvertUtils.toInt(getPagelet().getOptionProperty("_news_time"), 0);
		if (_news_time == 1) {
			timeDistance = TimeDistance.week;
		} else if (_news_time == 2) {
			timeDistance = TimeDistance.month;
		} else if (_news_time == 3) {
			timeDistance = TimeDistance.year;
		}
		final int _tab_param = ConvertUtils.toInt(compParameter.getParameter("_tab_param"), -1);
		// 0=按时间;1=按热度
		final int _news_order = ConvertUtils.toInt(getPagelet().getOptionProperty("_news_order"), 0);

		final IDataObjectQuery<News> qs = NewsUtils.queryNews(compParameter, _news_catalog, contentType, timeDistance, _news_order, _tab_param);
		compParameter.setRequestAttribute("__qs", qs);

		String forward;
		if (_news_type == 2) {
			forward = "/jsp/news_layout_image.jsp";
			final String dimension = getPagelet().getOptionProperty("_news_image_dimension");
			if (StringUtils.hasText(dimension)) {
				final String[] dimensionArr = StringUtils.split(dimension, "*");
				if (dimensionArr.length > 0) {
					forward = WebUtils.addParameters(forward, "width=" + dimensionArr[0]);
				}
				if (dimensionArr.length > 1) {
					forward = WebUtils.addParameters(forward, "height=" + dimensionArr[1]);
				}
			}
		} else {
			forward = "/jsp/news_layout.jsp";
		}
		if (_news_catalog != null) {
			forward = WebUtils.addParameters(forward, "catalog=" + _news_catalog);
		}
		final boolean _show_tabs = ConvertUtils.toBoolean(getPagelet().getOptionProperty("_show_tabs"), true)
				&& ConvertUtils.toBoolean(compParameter.getSessionAttribute("_show_tabs"), true);
		forward = WebUtils.addParameters(forward, "_show_tabs=" + _show_tabs);
		forward = doDateFormat(forward, "_news_dateFormat");
		forward = doDescLength(forward, "_news_descLength");
		forward = WebUtils.addParameters(forward, "rows=" + ConvertUtils.toInt(getPagelet().getOptionProperty("_news_rows"), 6));
		return new UrlForward(NewsUtils.deployPath + forward, "pa");
	}
}
