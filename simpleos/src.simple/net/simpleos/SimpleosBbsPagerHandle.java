package net.simpleos;

import net.simpleframework.content.bbs.BbsTopicPagerHandle;
import net.simpleframework.web.page.component.ComponentParameter;
/**
 * 
 * @author 李岩飞 
 * @date 2014年11月19日 下午4:59:21 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public class SimpleosBbsPagerHandle extends BbsTopicPagerHandle {

	public String getJavascriptCallback(final ComponentParameter compParameter, final String jsAction, final Object bean) {
		final String jsCallback = super.getJavascriptCallback(compParameter, jsAction, bean);
		if ("load".equals(jsAction)) {
			return jsCallback + "$Comp.fixMaxWidth('.tp_view .inherit_c', 750);";
		}
		return jsCallback;
	}
}
