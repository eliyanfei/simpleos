package net.itsite.ad;

import net.itsite.i.IItSiteApplicationModule;

public interface IAdApplicationModule extends IItSiteApplicationModule {

	AdBean getAdBean(final Object id);

	AdBean getAdBean(final EAd ad);
}
