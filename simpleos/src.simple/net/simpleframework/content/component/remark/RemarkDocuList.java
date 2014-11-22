package net.simpleframework.content.component.remark;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractPagerHandle;
import net.simpleos.module.docu.DocuRemark;
import net.simpleos.module.docu.DocuUtils;

public class RemarkDocuList extends AbstractPagerHandle {

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		final StringBuffer buf = new StringBuffer();
		buf.append("parentId=0 order by createDate desc,documentId");
		final ITableEntityManager tMgr = DocuUtils.applicationModule.getDataObjectManager(DocuRemark.class);
		return tMgr.query(new ExpressionValue(buf.toString()), DocuRemark.class);
	}
}
