package net.simpleframework.content.news;

import java.util.Collection;

import net.simpleframework.content.component.newspager.INewsPagerHandle;
import net.simpleframework.core.ado.IDataObjectQuery;
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
public class NewsAnnounceTooltip extends AbstractTooltipHandle {
	@Override
	public Collection<TipBean> getElementTips(final ComponentParameter compParameter) {
		final Collection<TipBean> tips = ((TooltipBean) compParameter.componentBean).getTips();
		final TipBean tip = tips.iterator().next();
		final StringBuilder sb = new StringBuilder();
		final IDataObjectQuery<?> qs = (IDataObjectQuery<?>) compParameter
				.getRequestAttribute("queryNews");
		if (qs == null || qs.getCount() == 0) {
			return tips;
		}
		qs.reset();
		final ComponentParameter nComponentParameter = ComponentParameter.get(compParameter,
				NewsUtils.applicationModule.getComponentBean(compParameter));
		final INewsPagerHandle nHandle = (INewsPagerHandle) nComponentParameter.getComponentHandle();
		News news;
		sb.append("<ul style=\"padding-left: 15px; line-height: 150%;\">");
		while ((news = (News) qs.next()) != null) {
			sb.append("<li>").append(nHandle.wrapOpenLink(nComponentParameter, news)).append("</li>");
		}
		sb.append("</ul>");
		tip.setContent(sb.toString());
		return tips;
	}
}
