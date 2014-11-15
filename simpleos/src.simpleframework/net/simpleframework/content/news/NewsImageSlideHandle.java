package net.simpleframework.content.news;

import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.content.IContentPagerHandle;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.FilePathWrapper;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.imageslide.AbstractImageSlideHandle;
import net.simpleframework.web.page.component.ui.imageslide.ImageItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class NewsImageSlideHandle extends AbstractImageSlideHandle {

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("width".equals(beanProperty)) {
			final String width = compParameter.getRequestParameter("width");
			if (StringUtils.hasText(width)) {
				return width;
			}
		} else if ("height".equals(beanProperty)) {
			final String height = compParameter.getRequestParameter("height");
			if (StringUtils.hasText(height)) {
				return height;
			}
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public Collection<ImageItem> getImageItems(final ComponentParameter compParameter) {
		final AbstractComponentBean newsPager = NewsUtils.applicationModule.getComponentBean(compParameter);
		if (newsPager == null) {
			return null;
		}
		final ComponentParameter nComponentParameter = ComponentParameter.get(compParameter, newsPager);
		final IQueryEntitySet<?> qs = (IQueryEntitySet<?>) nComponentParameter.getRequestAttribute("__qs");
		if (qs == null) {
			return null;
		}
		final int rows = ConvertUtils.toInt(compParameter.getRequestParameter("rows"), 4);
		int i = 0;
		final FilePathWrapper fp = ((IContentPagerHandle) nComponentParameter.getComponentHandle()).getFileCache(nComponentParameter);
		final ArrayList<ImageItem> al = new ArrayList<ImageItem>();
		News news;
		while ((news = (News) qs.next()) != null) {
			final Element img = Jsoup.parse(StringUtils.blank(news.getContent())).select("img").first();
			if (img == null) {
				continue;
			}
			if (i++ >= rows) {
				break;
			}
			final String pUrl = NewsUtils.applicationModule.getViewUrl(nComponentParameter, news);
			final ImageItem ii = new ImageItem();
			ii.setTitle(news.getTopic());
			ii.setLink(nComponentParameter.wrapContextPath(pUrl));
			ii.setImageUrl(fp.getImagePath(nComponentParameter, img.attr("src"), 480, 360));
			al.add(ii);
		}
		return al;
	}
}
