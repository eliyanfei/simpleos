package net.simpleframework.organization.component.userselect;

import java.util.Map;

import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.component.userpager.DefaultUserPagerHandle;
import net.simpleframework.organization.component.userpager.UserPagerBean;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.IComponentHandle;
import net.simpleframework.web.page.component.ui.pager.AbstractTablePagerData;
import net.simpleframework.web.page.component.ui.pager.TablePagerColumn;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class UserSelectPagerHandle extends DefaultUserPagerHandle {

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		final ComponentParameter nComponentParameter = UserSelectUtils
				.getComponentParameter(compParameter);
		if (nComponentParameter.componentBean != null) {
			if ("containerId".equals(beanProperty)) {
				return "container_" + nComponentParameter.componentBean.hashId();
			} else if ("showCheckbox".equals(beanProperty)) {
				return nComponentParameter.getBeanProperty("showCheckbox");
			} else if ("pageItems".equals(beanProperty)) {
				final int pageItems = ConvertUtils.toInt(
						nComponentParameter.getBeanProperty("pageItems"), 0);
				if (pageItems > 0) {
					return pageItems;
				}
			}
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = UserSelectUtils
				.getComponentParameter(compParameter);
		return ((IUserSelectHandle) nComponentParameter.getComponentHandle()).getUsers(
				nComponentParameter, (UserPagerBean) compParameter.componentBean);
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, OrgUtils.um().getUserTextParameterName());
		putParameter(compParameter, parameters, UserSelectUtils.BEAN_ID);
		final ComponentParameter nComponentParameter = UserSelectUtils
				.getComponentParameter(compParameter);
		parameters.putAll(IComponentHandle.Utils.toFormParameters(nComponentParameter));
		return parameters;
	}

	@Override
	public AbstractTablePagerData createTablePagerData(final ComponentParameter compParameter) {
		return new UserTablePagerData(compParameter) {
			@Override
			public Map<String, TablePagerColumn> getTablePagerColumns() {
				final Map<String, TablePagerColumn> columns = super.getTablePagerColumns();
				final TablePagerColumn tc = columns.get("text");
				tc.setStyle("text-align: left;");
				final TablePagerColumn tc2 = columns.get("name");
				tc2.setStyle("text-align: left;");
				tc2.setHeaderStyle("width: 50%;");
				return columns;
			}

			@Override
			protected Map<Object, Object> getRowAttributes(final Object dataObject) {
				final Map<Object, Object> attributes = super.getRowAttributes(dataObject);
				final IUser user = (IUser) dataObject;
				attributes.put("userName", user.getName());
				attributes.put("userText", user.getText());
				return attributes;
			}
		};
	}
}
