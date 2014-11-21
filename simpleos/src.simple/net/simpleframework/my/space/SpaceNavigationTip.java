package net.simpleframework.my.space;

import java.util.Collection;

import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.tooltip.AbstractTooltipHandle;
import net.simpleframework.web.page.component.ui.tooltip.TipBean;
import net.simpleframework.web.page.component.ui.tooltip.TooltipBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class SpaceNavigationTip extends AbstractTooltipHandle {

	@Override
	public Collection<TipBean> getElementTips(final ComponentParameter compParameter) {
		final Collection<TipBean> tips = ((TooltipBean) compParameter.componentBean).getTips();
		final TipBean tip = tips.iterator().next();
		if (tip != null) {
			tip.setContent(MySpaceUtils.applicationModule.getMySpaceNavigationHtml(compParameter));
		}
		return tips;
	}
}
