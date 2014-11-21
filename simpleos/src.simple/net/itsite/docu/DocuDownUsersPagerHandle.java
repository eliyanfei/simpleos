package net.itsite.docu;

import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractPagerHandle;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月21日 下午5:19:07 
 *
 */
public class DocuDownUsersPagerHandle extends AbstractPagerHandle {
	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, DocuUtils.docuId);
		return parameters;
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		final String docuId = compParameter.getRequestParameter(DocuUtils.docuId);
		if (StringUtils.hasText(docuId)) {
			final ITableEntityManager tMgr = DocuUtils.applicationModule.getDataObjectManager(DocuLogBean.class);
			return tMgr.query(new ExpressionValue(DocuUtils.docuId + "=?", new Object[] { docuId }), DocuLogBean.class);
		} else {
			return null;
		}
	}
}
