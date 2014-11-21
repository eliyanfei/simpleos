package net.simpleframework.content.component.newspager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.simpleframework.core.bean.ITextBeanAware;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.DefaultPageHandle;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class NewsEditPageLoad extends DefaultPageHandle {
	@Override
	public Object getBeanProperty(final PageParameter pageParameter, final String beanProperty) {
		if ("importPage".equals(beanProperty)) {
			final ComponentParameter nComponentParameter = NewsPagerUtils.getComponentParameter(pageParameter);
			if (nComponentParameter.componentBean != null) {
				final INewsPagerHandle nHandle = (INewsPagerHandle) nComponentParameter.getComponentHandle();
				final String tb = nHandle.getHtmlEditorToolbar(nComponentParameter);
				if (StringUtils.hasText(tb)) {
					final ArrayList<String> al = new ArrayList<String>();
					al.add(nComponentParameter.componentBean.getResourceHomePath() + "/jsp/news_edit_toolbar.xml");
					return al.toArray(new String[al.size()]);
				}
			}
		}
		return super.getBeanProperty(pageParameter, beanProperty);
	}

	public void newsLoaded(final PageParameter pageParameter, final Map<String, Object> dataBinding, final List<String> visibleToggleSelector,
			final List<String> readonlySelector, final List<String> disabledSelector) {
		dataBinding.put("np_allowComments", true);
		dataBinding.put("np_att2", true);
		dataBinding.put("np_attention", true);
		final ComponentParameter nComponentParameter = NewsPagerUtils.getComponentParameter(pageParameter);
		final INewsPagerHandle npHandle = (INewsPagerHandle) nComponentParameter.getComponentHandle();
		final NewsBean news = npHandle.getEntityBeanByRequest(nComponentParameter);
		Object catalogId = null;
		if (news != null) {
			for (final String attri : new String[] { "topic", "author", "source", "keywords", "allowComments", "description", "content" }) {
				dataBinding.put("np_" + attri, BeanUtils.getProperty(news, attri));
			}
			dataBinding.put("np_vote", news.getMark());
			catalogId = news.getCatalogId();
		} else {
			final String sParam = pageParameter.getRequestParameter(npHandle.getCatalogIdName(pageParameter));
			if (StringUtils.hasText(sParam)) {
				catalogId = sParam;
			}
		}
		if (catalogId != null) {
			final List<NewsCatalog> catalogs;
			if ((catalogs = npHandle.listNewsCatalog(nComponentParameter)) != null) {
				dataBinding.put("np_catalog", catalogId);
				dataBinding.put("np_catalog_text", catalogs.get(ConvertUtils.toInt(catalogId, 0)).getText());
			} else {
				final ITextBeanAware catalog = npHandle.getCatalogById(nComponentParameter, catalogId);
				if (catalog != null) {
					dataBinding.put("np_catalog", catalogId);
					dataBinding.put("np_catalog_text", catalog.getText());
				}
			}
		}
	}

	public void news2Loaded(final PageParameter pageParameter, final Map<String, Object> dataBinding, final List<String> visibleToggleSelector,
			final List<String> readonlySelector, final List<String> disabledSelector) {
		final ComponentParameter nComponentParameter = NewsPagerUtils.getComponentParameter(pageParameter);
		final NewsBean news = ((INewsPagerHandle) nComponentParameter.getComponentHandle()).getEntityBeanByRequest(nComponentParameter);
		if (news != null) {
			dataBinding.put("nv_template", news.getViewTemplate());
			dataBinding.put("nv_status", news.getStatus());
			dataBinding.put("nv_type", news.getTtype());
			dataBinding.put("nv_ttop", news.isTtop());
		}
	}
}
