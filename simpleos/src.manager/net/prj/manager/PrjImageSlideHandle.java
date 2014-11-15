package net.prj.manager;

import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.imageslide.AbstractImageSlideHandle;
import net.simpleframework.web.page.component.ui.imageslide.ImageItem;

/**
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2014-1-20下午05:00:45
 */
public class PrjImageSlideHandle extends AbstractImageSlideHandle {

	@Override
	public Collection<ImageItem> getImageItems(ComponentParameter compParameter) {
		final Collection<ImageItem> items = new ArrayList<ImageItem>();
		IQueryEntitySet<PrjNavBean> qs = PrjMgrUtils.appModule.queryBean(new ExpressionValue("1=1 order by oorder"), PrjNavBean.class);
		PrjNavBean navBean;
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
