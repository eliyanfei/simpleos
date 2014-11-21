package net.simpleos.backend.slide;

import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.imageslide.AbstractImageSlideHandle;
import net.simpleframework.web.page.component.ui.imageslide.ImageItem;
import net.simpleos.backend.BackendUtils;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月21日 下午12:00:12 
 * @Description: 首页幻灯片轮播组件
 *
 */
public class IndexSlideImageHandle extends AbstractImageSlideHandle {

	@Override
	public Collection<ImageItem> getImageItems(ComponentParameter compParameter) {
		final Collection<ImageItem> items = new ArrayList<ImageItem>();
		IQueryEntitySet<IndexSlideBean> qs = BackendUtils.applicationModule.queryBean(new ExpressionValue("1=1 order by oorder"), IndexSlideBean.class);
		IndexSlideBean navBean;
		while ((navBean = qs.next()) != null) {
			ImageItem item = new ImageItem();
			item.setImageUrl(compParameter.getContextPath() + "/nav/" + navBean.getImage());
			item.setLink(navBean.getUrl());
			item.setTitle(navBean.getTitle());
			items.add(item);
		}
		return items;
	}

}
