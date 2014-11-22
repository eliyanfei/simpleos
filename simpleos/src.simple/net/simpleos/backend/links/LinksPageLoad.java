package net.simpleos.backend.links;

import java.util.List;
import java.util.Map;

import net.simpleframework.web.page.DefaultPageHandle;
import net.simpleframework.web.page.PageParameter;
import net.simpleos.utils.DateUtils;

/**
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-11-8上午11:32:08
 */
public class LinksPageLoad extends DefaultPageHandle {

	public void linksLoad(final PageParameter pageParameter, final Map<String, Object> dataBinding, final List<String> invisibleSelector,
			final List<String> readonlySelector, final List<String> disabledSelector) throws Exception {
		final String linksId = pageParameter.getRequestParameter("linksId");
		LinksBean linksBean = LinksUtils.appModule.getBean(LinksBean.class, linksId);
		if (linksBean != null) {
			dataBinding.put("l_title", linksBean.getTitle());
			dataBinding.put("l_description", linksBean.getDescription());
			dataBinding.put("l_url", linksBean.getUrl());
			dataBinding.put("l_color", linksBean.getColor());
			dataBinding.put("l_date", DateUtils.formatDate(linksBean.getCreatedDate(), "yyyy-MM-dd"));
			dataBinding.put("linksId", linksId);
		}
	}

}
