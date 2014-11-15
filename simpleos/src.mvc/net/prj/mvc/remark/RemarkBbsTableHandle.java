package net.prj.mvc.remark;

import java.util.Map;

import net.a.ItSiteUtil;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.content.bbs.BbsUtils;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractPagerHandle;

/**
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-15下午09:20:42
 */
public class RemarkBbsTableHandle extends AbstractPagerHandle {

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
		final StringBuffer sql = new StringBuffer();
		sql.append("select p.id,subject ,content,userid,ip,topicid,createdate from simple_bbs_posts p, simple_bbs_posts_text  pt where p.id=pt.id order by createdate desc");
		return BbsUtils.getTableEntityManager().query(new SQLValue(sql.toString()));
	}
}
