package net.simpleframework.my.space;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractPagerHandle;

public class MoreSapceLogPagerHandle extends AbstractPagerHandle {

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("title".equals(beanProperty)) {
			final StringBuilder sb = new StringBuilder();
			sb.append("<span style=\"color:#882222;font-size:14px;font-weight:bold;\">今天你Say了吗?</span>");
			return sb.toString();
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		final ITableEntityManager log_mgr = MySpaceUtils.getTableEntityManager(SapceLogBean.class);
		return log_mgr.query(new ExpressionValue("content is not null and refModule=6 order by createDate desc"), SapceLogBean.class);
	}
}