package net.simpleos.mvc.myfavorite;

import java.util.HashMap;
import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractTablePagerData;
import net.simpleframework.web.page.component.ui.pager.db.AbstractDbTablePagerHandle;
import net.simpleos.SimpleosUtil;
import net.simpleos.utils.StringsUtils;

/**
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-15下午09:20:42
 */
public class MyFavoriteTableHandle extends AbstractDbTablePagerHandle {

	@Override
	public Object getBeanProperty(ComponentParameter compParameter, String beanProperty) {
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, "type");
		return parameters;
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(ComponentParameter compParameter) {
		final IAccount account = SimpleosUtil.getLoginAccount(compParameter);
		if (account == null)
			return null;
		final String type = StringsUtils.trimNull(compParameter.getParameter("type"), EFunctionModule.news.name());
		return MyFavoriteUtils.appModule.queryBean(
				new ExpressionValue("type=? and userId=? order by createdate desc", new Object[] { EFunctionModule.valueOf(type), account.getId() }),
				MyFavoriteBean.class);
	}

	@Override
	public AbstractTablePagerData createTablePagerData(final ComponentParameter compParameter) {
		return new AbstractTablePagerData(compParameter) {

			@Override
			protected Map<Object, Object> getRowData(Object dataObject) {
				final Map<Object, Object> data = new HashMap<Object, Object>();
				final MyFavoriteBean favoriteBean = (MyFavoriteBean) dataObject;
				data.put("createdate", ConvertUtils.toDateString(favoriteBean.getCreatedate(), "yyyy-MM-dd HH:mm"));
				final StringBuffer title = new StringBuffer();
				title.append("<a href='" + favoriteBean.getUrl() + "' target='blank'>");
				title.append(favoriteBean.getTitle());
				title.append("</a>");
				data.put("title", title.toString());
				final StringBuffer act = new StringBuffer();
				act.append("<input type='button' onclick=\"$IT.A('cancelFavorite','id=" + favoriteBean.getId() + "');\" value='取消'>");
				data.put("act", act.toString());
				return data;
			}
		};
	}

}
