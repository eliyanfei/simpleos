package net.itsite;

import net.simpleframework.content.bbs.BbsTopicPagerHandle;
import net.simpleframework.web.page.component.ComponentParameter;

public class ItSiteBbsPagerHandle extends BbsTopicPagerHandle {

	public String getJavascriptCallback(final ComponentParameter compParameter, final String jsAction, final Object bean) {
		final String jsCallback = super.getJavascriptCallback(compParameter, jsAction, bean);
		if ("load".equals(jsAction)) {
			return jsCallback + "$Comp.fixMaxWidth('.tp_view .inherit_c', 750);";
		}
		return jsCallback;
	}
}
