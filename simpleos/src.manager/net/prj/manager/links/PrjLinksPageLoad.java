package net.prj.manager.links;

import java.util.List;
import java.util.Map;

import net.itniwo.commons.DateUtils;
import net.simpleframework.web.page.DefaultPageHandle;
import net.simpleframework.web.page.PageParameter;

/**
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-11-8上午11:32:08
 */
public class PrjLinksPageLoad extends DefaultPageHandle {

	public void linksLoad(final PageParameter pageParameter, final Map<String, Object> dataBinding, final List<String> invisibleSelector,
			final List<String> readonlySelector, final List<String> disabledSelector) throws Exception {
		final String linksId = pageParameter.getRequestParameter("linksId");
		PrjLinksBean linksBean = PrjLinksUtils.appModule.getBean(PrjLinksBean.class, linksId);
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
