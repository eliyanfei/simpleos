package net.prj.mvc.remark;

import java.util.Map;

import net.itsite.ItSiteUtil;
import net.itsite.docu.DocuRemark;
import net.itsite.docu.DocuUtils;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractPagerHandle;

/**
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-15下午09:20:42
 */
public class RemarkDocuTableHandle extends AbstractPagerHandle {

	@Override
	public Object getBeanProperty(ComponentParameter compParameter, String beanProperty) {
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		return parameters;
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(ComponentParameter compParameter) {
		final IAccount account = ItSiteUtil.getLoginAccount(compParameter);
		if (account == null)
			return null;
		final IQueryEntitySet<DocuRemark> qs = DocuUtils.applicationModule.queryBean(new ExpressionValue("1=1  order by createdate desc"),
				DocuRemark.class);
		return qs;
	}
}
