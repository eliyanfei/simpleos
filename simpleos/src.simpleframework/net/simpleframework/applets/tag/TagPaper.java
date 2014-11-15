package net.simpleframework.applets.tag;

import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractPagerHandle;

public class TagPaper extends AbstractPagerHandle {
	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(ComponentParameter compParameter) {
		final ITableEntityManager tMgr = TagUtils.getTableEntityManager(TagBean.class);
		return tMgr.query(new ExpressionValue("1=1 order by views desc,frequency desc", null), TagBean.class);
	}

	@Override
	public Map<String, Object> getFormParameters(ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		return parameters;
	}

}
