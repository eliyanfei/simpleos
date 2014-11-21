package net.simpleos.module.ad;

import java.util.Map;

import net.itsite.ItSiteUtil;
import net.itsite.impl.AItSiteAppclicationModule;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.IInitializer;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.web.page.PageRequestResponse;

/**
 * 
 * @author 李岩飞 
 * @date 2014年11月19日 下午5:02:23 
 * @Description: 广告的初始化类
 *
 */
public class AdAppModule extends AItSiteAppclicationModule implements IAdAppModule {

	public final static Table ad = new Table("simpleos_ad", "id");
	public static IAdAppModule applicationModule;
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
								applicationModule.doUpdate(adBean);
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
