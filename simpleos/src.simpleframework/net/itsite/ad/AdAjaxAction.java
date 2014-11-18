package net.itsite.ad;

import java.util.Map;

import net.itsite.utils.StringsUtils;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.my.file.component.fileselect.FileSelectUtils;
import net.simpleframework.organization.IJob;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;

public class AdAjaxAction extends AbstractAjaxRequestHandle {
	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("jobExecute".equals(beanProperty)) {
			if (compParameter.componentBean != null) {
				return IJob.sj_manager;
			}
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	/**
	* @param compParameter
	* @return
	*/
	public IForward adSave(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {
			AdBean adBean;

			@Override
			public void doAction(final Map<String, Object> json) {
				final EAd ad = EAd.valueOf(compParameter.getRequestParameter("adId"));
				adBean = AdUtils.applicationModule.getAdBean(ad);
				if (adBean == null) {
					adBean = new AdBean();
					adBean.setStartDate(ConvertUtils.toDate(compParameter.getRequestParameter("ad_startDate"), "yyyy-MM-dd"));
					adBean.setAd(ad);
				}
				adBean.setAdType(ConvertUtils.toInt(compParameter.getRequestParameter("ad_adType"), adBean.getAdType()));
				if (adBean.getAdType() == 0) {
					adBean.setSrc(StringsUtils.replace(compParameter.getRequestParameter("ad_src"), FileSelectUtils.DOWNLOAD_FLAG, ""));
					adBean.setUrl(compParameter.getRequestParameter("ad_url"));
				}else if (adBean.getAdType() == 1) {
					adBean.setContent(compParameter.getRequestParameter("ad_content"));
				} else {
					adBean.setContent(compParameter.getRequestParameter("ad_pcontent"));
				}
				adBean.setDays(ConvertUtils.toInt(compParameter.getRequestParameter("ad_days"), adBean.getDays()));
				AdUtils.applicationModule.doUpdate(adBean, new TableEntityAdapter() {
					@Override
					public void afterInsert(ITableEntityManager manager, Object[] objects) {
						AdUtils.adMap.put(ad.name(), adBean);
					}

					@Override
					public void afterUpdate(ITableEntityManager manager, Object[] objects) {
						AdUtils.adMap.put(ad.name(), adBean);
					}
				});
				json.put("json", ad.name());
			}
		});
	}

	/**
	* @param compParameter
	* @return
	*/
	public IForward adStop(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				AdUtils.stopAd(compParameter.getRequestParameter("adId"));
			}
		});
	}

	/**
	* @param compParameter
	* @return
	*/
	public IForward adStart(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				AdUtils.startAd(compParameter.getRequestParameter("adId"));
			}
		});
	}
}
