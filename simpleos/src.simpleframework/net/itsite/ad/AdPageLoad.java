package net.itsite.ad;

import java.util.List;
import java.util.Map;

import net.simpleframework.util.ConvertUtils;
import net.simpleframework.web.AbstractTitleAwarePageLoad;
import net.simpleframework.web.page.PageParameter;

/**
 * 
 * @author 李岩飞 
 * @date 2014年11月19日 下午5:02:57 
 * @Description: 还原表单数据
 *
 */
public class AdPageLoad extends AbstractTitleAwarePageLoad {

	public void adLoad(final PageParameter pageParameter, final Map<String, Object> dataBinding, final List<String> visibleToggleSelector,
			final List<String> readonlySelector, final List<String> disabledSelector) throws Exception {
		AdBean adBean = AdUtils.adMap.get(pageParameter.getRequestParameter("adId"));
		if (adBean != null) {
			dataBinding.put("adId", adBean.getAd().name());
			dataBinding.put("ad_days", adBean.getDays());
			dataBinding.put("ad_content", adBean.getContent());
			dataBinding.put("ad_pcontent", adBean.getContent());
			dataBinding.put("ad_url", adBean.getUrl());
			dataBinding.put("ad_src", adBean.getSrc());
			dataBinding.put("ad_adType", adBean.getAdType());
			dataBinding.put("ad_startDate", ConvertUtils.toDateString(adBean.getStartDate(), "yyyy-MM-dd"));
			dataBinding.put("ad_status", adBean.getStartText());
		}
	}

}
