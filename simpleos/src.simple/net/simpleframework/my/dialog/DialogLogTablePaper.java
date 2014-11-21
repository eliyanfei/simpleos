package net.simpleframework.my.dialog;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.my.space.MySpaceUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractPagerHandle;

public class DialogLogTablePaper extends AbstractPagerHandle {

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		final ITableEntityManager tMgr = MySpaceUtils.getTableEntityManager(SimpleDialogItem.class);
		final String dialogId = compParameter.getRequestParameter("dialogId");
		return tMgr.query(new ExpressionValue("dialogId=? order by sentDate desc", new Object[] { dialogId }), SimpleDialogItem.class);
	}

}
