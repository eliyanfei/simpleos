package net.simpleframework.content.bbs;

import java.util.Map;

import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.content.AbstractContentLayoutModuleHandle;
import net.simpleframework.content.EContentType;
import net.simpleframework.content.component.topicpager.ETopicQuery;
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
public class BbsPortalModule extends AbstractContentLayoutModuleHandle {

	public BbsPortalModule(final PageletBean pagelet) {
		super(pagelet);
	}

	private static String[] defaultOptions = new String[] { "_bbs_forum=", "_bbs_type=0", "_bbs_star=false", "_bbs_time=0", "_bbs_order=0",
			"_bbs_rows=6", "_bbs_descLength=0", "_bbs_dateFormat=", "_show_tabs=true" };

	@Override
	protected String[] getDefaultOptions() {
		return defaultOptions;
	}

	@Override
	public void optionLoaded(final PageParameter pageParameter, final Map<String, Object> dataBinding) {
		super.optionLoaded(pageParameter, dataBinding);
		final String _bbs_forum = getPagelet().getOptionProperty("_bbs_forum");
		if (StringUtils.hasText(_bbs_forum)) {
			final BbsForum forum = BbsUtils.getTableEntityManager(BbsForum.class).queryForObjectById(_bbs_forum, BbsForum.class);
			if (forum != null) {
				dataBinding.put("_bbs_forum_text", forum.getText());
			}
		}
	}

	@Override
	public IForward getPageletOptionContent(final ComponentParameter compParameter) {
		return new UrlForward(BbsUtils.deployPath + "jsp/bbs_portal_option.jsp");
	}

	@Override
	public IForward getPageletContent(final ComponentParameter compParameter) {
		final Object _bbs_forum = ConvertUtils.toInt(getPagelet().getOptionProperty("_bbs_forum"));

		EContentType contentType = null;
		ETopicQuery topicQuery = null;
		// 0=全部的;1=推荐的;2=我发布的;3=我参与的
		final int _bbs_type = ConvertUtils.toInt(getPagelet().getOptionProperty("_bbs_type"), 0);
		if (_bbs_type == 1) {
			contentType = EContentType.recommended;
		} else if (_bbs_type == 2) {
			topicQuery = ETopicQuery.onlyTopic;
		} else if (_bbs_type == 3) {
			topicQuery = ETopicQuery.postsAndTopic;
		}
		TimeDistance timeDistance = null;
		// 0=不限制;1=一周;2=一月;3=一年
		final int _news_time = ConvertUtils.toInt(getPagelet().getOptionProperty("_bbs_time"), 0);
		if (_news_time == 1) {
			timeDistance = TimeDistance.week;
		} else if (_news_time == 2) {
			timeDistance = TimeDistance.month;
		} else if (_news_time == 3) {
			timeDistance = TimeDistance.year;
		}
		final int _tab_param = ConvertUtils.toInt(compParameter.getParameter("_tab_param"), -1);
		final boolean _bbs_star = ConvertUtils.toBoolean(getPagelet().getOptionProperty("_bbs_star"), false);
		// 0=按时间;1=按热度
		final int _bbs_order = ConvertUtils.toInt(getPagelet().getOptionProperty("_bbs_order"), 0);

		final IQueryEntitySet<?> qs = BbsUtils.queryTopics(compParameter, _bbs_forum, contentType, _bbs_star, timeDistance, topicQuery, _bbs_order,
				_tab_param);
		compParameter.setRequestAttribute("__qs", qs);

		String forward = BbsUtils.deployPath + "jsp/bbs_portal.jsp?btype=" + _bbs_type;
		final String forumId = getPagelet().getOptionProperty("_bbs_forum");
		if (StringUtils.hasText(forumId)) {
			forward = WebUtils.addParameters(forward, "forumId=" + forumId);
		}
		final boolean _show_tabs = ConvertUtils.toBoolean(getPagelet().getOptionProperty("_show_tabs"), true)
				&& ConvertUtils.toBoolean(compParameter.getSessionAttribute("_show_tabs"), true);
		forward = WebUtils.addParameters(forward, "_show_tabs=" + _show_tabs);
		forward = doDateFormat(forward, "_bbs_dateFormat");
		forward = doDescLength(forward, "_bbs_descLength");
		forward = WebUtils.addParameters(forward, "rows=" + ConvertUtils.toInt(getPagelet().getOptionProperty("_bbs_rows"), 6));
		return new UrlForward(forward, "pa");
	}
}
