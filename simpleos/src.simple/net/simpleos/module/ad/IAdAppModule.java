package net.simpleos.module.ad;

import net.itsite.i.IItSiteApplicationModule;

/**
 * 
 * @author 李岩飞 
 * @date 2014年11月19日 下午5:03:22 
 * @Description: 接口
 *
 */
public interface IAdAppModule extends IItSiteApplicationModule {

	AdBean getAdBean(final Object id);

	AdBean getAdBean(final EAd ad);
}
