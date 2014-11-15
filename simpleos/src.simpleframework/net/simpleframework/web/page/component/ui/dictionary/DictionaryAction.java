package net.simpleframework.web.page.component.ui.dictionary;

import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.UrlForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;
import net.simpleframework.web.page.component.ui.dictionary.DictionaryBean.AbstractDictionaryTypeBean;
import net.simpleframework.web.page.component.ui.dictionary.DictionaryBean.DictionaryColorBean;
import net.simpleframework.web.page.component.ui.dictionary.DictionaryBean.DictionaryFontBean;
import net.simpleframework.web.page.component.ui.dictionary.DictionaryBean.DictionaryListBean;
import net.simpleframework.web.page.component.ui.dictionary.DictionaryBean.DictionarySmileyBean;
import net.simpleframework.web.page.component.ui.dictionary.DictionaryBean.DictionaryTreeBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DictionaryAction extends AbstractAjaxRequestHandle {
	@Override
	public IForward ajaxProcess(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = DictionaryUtils
				.getComponentParameter(compParameter);
		final DictionaryBean dictionaryBean = (DictionaryBean) nComponentParameter.componentBean;
		if (dictionaryBean != null) {
			final AbstractDictionaryTypeBean dictionaryType = dictionaryBean.getDictionaryTypeBean();
			String jsp = null;
			if (dictionaryType instanceof DictionaryTreeBean) {
				jsp = "tree.jsp";
			} else if (dictionaryType instanceof DictionaryListBean) {
				jsp = "list.jsp";
			} else if (dictionaryType instanceof DictionaryColorBean) {
				jsp = "color.jsp";
			} else if (dictionaryType instanceof DictionaryFontBean) {
				jsp = "font.jsp";
			} else if (dictionaryType instanceof DictionarySmileyBean) {
				jsp = "smiley.jsp";
			}
			if (jsp != null) {
				final StringBuilder sb = new StringBuilder();
				sb.append(DictionaryUtils.getHomePath()).append("/jsp/").append(jsp).append("?")
						.append(DictionaryUtils.BEAN_ID).append("=").append(dictionaryBean.hashId());
				return new UrlForward(sb.toString());
			}
		}
		return null;
	}
}
