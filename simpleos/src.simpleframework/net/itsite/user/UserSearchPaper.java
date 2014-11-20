package net.itsite.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.itsite.ItSiteUtil;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.my.space.MySpaceUtils;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractTablePagerData;
import net.simpleframework.web.page.component.ui.pager.db.AbstractDbTablePagerHandle;

public class UserSearchPaper extends AbstractDbTablePagerHandle {

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(ComponentParameter compParameter) {
		final ITableEntityManager tMgr = ItSiteUtil.getTableEntityManager(ItSiteUtil.applicationModule, UserSearchBean.class);
		final StringBuffer sql = new StringBuffer();
		final String catalogId = compParameter.getRequestParameter("catalogId");
		final List<Object> lv = new ArrayList<Object>();
		sql.append("catalogId=?");
		lv.add(catalogId);
		sql.append(" order by createDate desc");
		return tMgr.query(new ExpressionValue(sql.toString(), lv.toArray()), UserSearchBean.class);
	}

	public static String getApplictionUrl(final String id) {
		if ("os".equals(id)) {
			return "/os.html?s=1&q=q";
		} else if ("cs".equals(id)) {
			return "/cs.html?q=q";
		} else if ("docu".equals(id)) {
			return "docusearch.html?docu_=all&q=q";
		} else if ("question".equals(id)) {
			return "/question.html?q=q";
		} else if ("news".equals(id)) {
			return "/news.html?q=q";
		} else if ("blog".equals(id)) {
			return "/blog.html?q=q";
		} else if ("bbs".equals(id)) {
			return "/bbs/tl.html?q=q";
		} else {
			return "/special.html?q=q";
		}
	}

	@Override
	public AbstractTablePagerData createTablePagerData(final ComponentParameter compParameter) {
		return new AbstractTablePagerData(compParameter) {

			@Override
			protected Map<Object, Object> getRowData(Object dataObject) {
				final UserSearchBean searchBean = (UserSearchBean) dataObject;
				final Map<Object, Object> row = new HashMap<Object, Object>();
				final EFunctionModule functionModule = searchBean.getFunctionModule();
				if (functionModule == null) {
					return row;
				}
				final IUser user = OrgUtils.um().queryForObjectById(searchBean.getUserId());
				row.put("userId", user == null ? "匿名" : MySpaceUtils.getAccountAware().wrapAccountHref(compParameter, user));
				row.put("content", "<a target='_blank' onclick=\"$Actions.loc('" + getApplictionUrl(functionModule.name()) + "'.addParameter('c="
						+ searchBean.getContent() + "'))\">" + searchBean.getContent() + "</a>");
				row.put("functionModule", "<a href='" + functionModule.getApplicationUrl() + "'>" + functionModule + "</a>");
				row.put("createDate", ConvertUtils.toDateString(searchBean.getCreateDate(), "yyyy-MM-dd HH"));
				return row;
			}

		};
	}

	@Override
	public Map<String, Object> getFormParameters(ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, "catalogId");
		return parameters;
	}

}
