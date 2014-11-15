package net.itsite.ad;

import java.util.Map;

import net.a.ItSiteUtil;
import net.itsite.impl.AItSiteAppclicationModule;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.IInitializer;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.web.page.PageRequestResponse;

public class AdApplicationModule extends AItSiteAppclicationModule implements IAdApplicationModule {

	public final static Table ad = new Table("it_ad", "id");

	final String deployName = "ad";

	private void loadAd() {
		final IQueryEntitySet<AdBean> qs = this.queryBean("1=1", AdBean.class);
		AdBean adBean;
		AdUtils.adMap.clear();
		while ((adBean = qs.next()) != null) {
			AdUtils.adMap.put(adBean.getAd().name(), adBean);
		}
	}

	@Override
	public void init(final IInitializer initializer) {
		super.init(initializer);
		doInit(AdUtils.class, deployName);
		loadAd();
		new Thread() {
			public void run() {
				//第二天的凌晨启动
				try {
					Thread.sleep(ItSiteUtil.get0Time());
				} catch (Exception e) {
				}
				while (true) {
					try {
						loadAd();
						//检查广告运行状体
						for (final String ad : AdUtils.adMap.keySet()) {
							final AdBean adBean = AdUtils.adMap.get(ad);
							if (adBean.getStatus() == 2) {
								continue;
							}
							if (adBean.checkAdStatus()) {
								AdUtils.applicationModule.doUpdate(adBean);
							}
						}
						Thread.sleep(24 * 60 * 60 * 1000);
					} catch (Exception e) {
					}
				}
			};
		}.start();
	}


	@Override
	protected void putTables(Map<Class<?>, Table> tables) {
		tables.put(AdBean.class, ad);
	}

	@Override
	public String getApplicationUrl(PageRequestResponse requestResponse) {
		return "/manager/ad.html";
	}

	@Override
	public Class<?> getEntityBeanClass() {
		return AdBean.class;
	}

	@Override
	public String getDeployPath() {
		return deployName;
	}

	@Override
	public AdBean getAdBean(Object id) {
		final ITableEntityManager tMgr = getDataObjectManager();
		return tMgr.queryForObjectById(id, AdBean.class);
	}

	@Override
	public AdBean getAdBean(EAd ad) {
		final ITableEntityManager tMgr = getDataObjectManager();
		return tMgr.queryForObject(new ExpressionValue("ad=?", new Object[] { ad }), AdBean.class);
	}
}
