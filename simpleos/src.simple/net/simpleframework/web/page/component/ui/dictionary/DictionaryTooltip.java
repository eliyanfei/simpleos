package net.simpleframework.web.page.component.ui.dictionary;

import java.util.Collection;

import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.dictionary.DictionaryBean.AbstractDictionaryTypeBean;
import net.simpleframework.web.page.component.ui.dictionary.DictionaryBean.DictionaryListBean;
import net.simpleframework.web.page.component.ui.dictionary.DictionaryBean.DictionaryTreeBean;
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
public class DictionaryTooltip extends AbstractTooltipHandle {
	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("name".equals(beanProperty)) {
			return "tooltip_"
					+ DictionaryUtils.getComponentParameter(compParameter).componentBean.hashId();
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public Collection<TipBean> getElementTips(final ComponentParameter compParameter) {
		final TooltipBean tooltipBean = (TooltipBean) compParameter.componentBean;
		final Collection<TipBean> tips = tooltipBean.getTips();
		if (tips != null && tips.size() > 0) {
			final TipBean tip = tips.iterator().next();
			final ComponentParameter nComponentParameter = DictionaryUtils
					.getComponentParameter(compParameter);
			final DictionaryBean dictionaryBean = (DictionaryBean) nComponentParameter.componentBean;
			tip.setSelector("#help" + dictionaryBean.hashId());
			final AbstractDictionaryTypeBean dictionaryType = dictionaryBean.getDictionaryTypeBean();
			String key = null;
			if (dictionaryType instanceof DictionaryTreeBean) {
				if (DictionaryUtils.isMultiple(nComponentParameter)) {
					key = "getElementTips.0";
				} else {
					key = "getElementTips.1";
				}
			} else if (dictionaryType instanceof DictionaryListBean) {
				if (DictionaryUtils.isMultiple(nComponentParameter)) {
					key = "getElementTips.2";
				} else {
					key = "getElementTips.3";
				}
			}
			if (key != null) {
				tip.setContent(LocaleI18n.getMessage(key));
			}
			final IDictionaryHandle dHandle = (IDictionaryHandle) nComponentParameter
					.getComponentHandle();
			if (dHandle != null) {
				dHandle.doTipBean(nComponentParameter, tip);
			}
		}
		return tips;
	}
}
