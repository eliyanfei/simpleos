package net.simpleframework.workflow.web.component.modellist;

import java.util.HashMap;
import java.util.Map;

import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractTablePagerData;
import net.simpleframework.workflow.ProcessModelBean;
import net.simpleframework.workflow.WorkflowUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultModelListHandle extends AbstractModelListHandle {

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		return WorkflowUtils.pmm().query();
	}

	@Override
	protected Map<Object, Object> getTableRow(final ComponentParameter compParameter,
			final Object dataObject) {
		final ProcessModelBean processModel = (ProcessModelBean) dataObject;
		final Map<Object, Object> rowData = new HashMap<Object, Object>();
		rowData.put("title", processModel.getTitle());
		rowData.put("action", AbstractTablePagerData.ACTIONc);
		return rowData;
	}
}
