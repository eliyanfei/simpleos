package net.simpleframework.organization.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.organization.EUserStatus;
import net.simpleframework.organization.IDepartment;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.EAccountStatus;
import net.simpleframework.organization.component.userpager.DefaultUserPagerHandle;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.util.LangUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.menu.MenuBean;
import net.simpleframework.web.page.component.ui.menu.MenuItem;
import net.simpleframework.web.page.component.ui.pager.AbstractTablePagerData;
import net.simpleframework.web.page.component.ui.pager.TablePagerColumn;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class UserListPagerHandle extends DefaultUserPagerHandle implements
		IOrgConst {
	@Override
	public Object getBeanProperty(final ComponentParameter compParameter,
			final String beanProperty) {
		if ("title".equals(beanProperty)) {
			final StringBuilder sb = new StringBuilder();
			final String sv = compParameter.getRequestParameter("sv");
			if (StringUtils.hasText(sv) || isAdvancedSearch(compParameter)) {
				sb.append("<a onclick=\"")
						.append(IOrgConst.Helper.__user_list(null))
						.append("\">");
				sb.append(LocaleI18n.getMessage("DepartmentTreeHandle.0"));
				sb.append("</a>").append(HTMLBuilder.NAV);
				sb.append(LocaleI18n.getMessage("DepartmentTreeHandle.7"));
			} else {
				final long departmentId = ConvertUtils.toLong(
						getCatalogId(compParameter), 0);
				if (departmentId == 0) {
					sb.append(LocaleI18n.getMessage("DepartmentTreeHandle.0"));
				} else if (departmentId == ONLINE_ID) {
					sb.append(LocaleI18n.getMessage("DepartmentTreeHandle.1"));
				} else if (departmentId == NO_DEPARTMENT_ID) {
					sb.append(LocaleI18n.getMessage("DepartmentTreeHandle.2"));
				} else if (departmentId < 0) {
					sb.append("<a onclick=\"").append(
							IOrgConst.Helper.__user_list(null));
					sb.append("\">");
					sb.append(LocaleI18n.getMessage("DepartmentTreeHandle.0"));
					sb.append("</a>").append(HTMLBuilder.NAV);
					if (departmentId == STATE_NORMAL_ID) {
						sb.append(LocaleI18n
								.getMessage("DepartmentTreeHandle.3"));
					} else if (departmentId == STATE_REGISTER_ID) {
						sb.append(LocaleI18n
								.getMessage("DepartmentTreeHandle.4"));
					} else if (departmentId == STATE_LOCKED_ID) {
						sb.append(LocaleI18n
								.getMessage("DepartmentTreeHandle.5"));
					} else if (departmentId == STATE_DELETE_ID) {
						sb.append(LocaleI18n
								.getMessage("DepartmentTreeHandle.6"));
					}
				} else {
					IDepartment dept = OrgUtils.dm().queryForObjectById(
							departmentId);
					int i = 0;
					while (dept != null) {
						if (i++ > 0) {
							final StringBuilder sb2 = new StringBuilder();
							sb2.append("<a onclick=\"");
							sb2.append(
									IOrgConst.Helper.__user_list(dept.getId()))
									.append("\">");
							sb2.append(dept.getText()).append("</a>")
									.append(HTMLBuilder.NAV);
							sb.insert(0, sb2.toString());
						} else {
							sb.append(dept.getText());
						}
						dept = (IDepartment) dept.parent();
					}
				}
			}
			wrapNavImage(compParameter, sb);
			return sb.toString();
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	private boolean isAdvancedSearch(final PageRequestResponse requestResponse) {
		return ConvertUtils.toBoolean(
				requestResponse.getRequestParameter("du_f0"), false);
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(
			final ComponentParameter compParameter) {
		final ArrayList<Object> al = new ArrayList<Object>();
		final StringBuilder sql = new StringBuilder();
		sql.append("1=1");
		boolean delete = false;
		final String sv = compParameter.getRequestParameter("sv");
		if (StringUtils.hasText(sv)) {
			sql.append(" and (name like '%").append(sv);
			sql.append("%' or text like '%").append(sv).append("%')");
		} else {
			final boolean aSearch = isAdvancedSearch(compParameter);
			if (aSearch) {
				final String f1 = compParameter.getRequestParameter("du_f1");
				if (StringUtils.hasText(f1)) {
					sql.append(" and (text like '%").append(f1).append("%')");
				}
				final String f2 = compParameter.getRequestParameter("du_f2");
				if (StringUtils.hasText(f2)) {
					sql.append(" and (name like '%").append(f2).append("%')");
				}
				final String f3 = compParameter.getRequestParameter("du_f3");
				if (StringUtils.hasText(f3)) {
					sql.append(" and (email like '%").append(f3).append("%')");
				}
				final String f4 = compParameter.getRequestParameter("du_f4");
				if (StringUtils.hasText(f4)) {
					sql.append(" and (mobile like '%").append(f4).append("%')");
				}
			}

			if (!aSearch
					|| ConvertUtils.toBoolean(
							compParameter.getRequestParameter("du_f5"), false)) {
				final long departmentId = ConvertUtils.toLong(
						getCatalogId(compParameter), 0);
				if (departmentId == NO_DEPARTMENT_ID) {
					sql.append(" and ");
					sql.append(Table.nullExpr(
							getTableEntityManager(compParameter).getTable(),
							"departmentId"));
				} else if (departmentId == ONLINE_ID
						|| departmentId == STATE_NORMAL_ID
						|| departmentId == STATE_REGISTER_ID
						|| departmentId == STATE_LOCKED_ID) {
					final StringBuilder sql2 = new StringBuilder();
					sql2.append("select b.* from ");
					sql2.append(OrgUtils.am().tblname());
					sql2.append(" a left join ");
					sql2.append(OrgUtils.um().tblname());
					sql2.append(" b on a.id = b.id where ");
					final ArrayList<Object> params = new ArrayList<Object>();
					if (departmentId == ONLINE_ID) {
						sql2.append("a.login=? and ");
						params.add(Boolean.TRUE);
					}
					sql2.append("a.status=? order by b.oorder desc");
					if (departmentId == ONLINE_ID
							|| departmentId == STATE_NORMAL_ID) {
						params.add(EAccountStatus.normal);
					} else if (departmentId == STATE_REGISTER_ID) {
						params.add(EAccountStatus.register);
					} else {
						params.add(EAccountStatus.locked);
					}
					return OrgUtils.um().query(
							new SQLValue(sql2.toString(), params.toArray()));
				} else if (departmentId == STATE_DELETE_ID) {
					sql.append(" and status=?");
					al.add(EUserStatus.delete);
					delete = true;
				} else if (departmentId > 0) {
					sql.append(" and departmentId=?");
					al.add(departmentId);
				}
			}
		}
		if (!delete) {
			sql.append(" and status <> ?");
			al.add(EUserStatus.delete);
		}
		sql.append(" order by oorder desc");
		return OrgUtils.um().query(
				new ExpressionValue(sql.toString(), al.toArray()));
	}

	@Override
	public List<MenuItem> getContextMenu(
			final ComponentParameter compParameter, final MenuBean menuBean) {
		final List<MenuItem> items = super.getContextMenu(compParameter,
				menuBean);
		final int departmentId = ConvertUtils.toInt(
				getCatalogId(compParameter), 0);
		if (departmentId == STATE_DELETE_ID) {
			final ArrayList<MenuItem> al = new ArrayList<MenuItem>();
			al.add(items.get(2));
			al.add(items.get(1));
			final MenuItem item = new MenuItem(menuBean);
			al.add(item);
			item.setTitle("#(UserListPagerHandle.1)");
			item.setJsSelectCallback("__pager_action(item).undel(item);");
			return al;
		} else if (departmentId == ONLINE_ID) {
			final ArrayList<MenuItem> al = new ArrayList<MenuItem>(items);
			final MenuItem item = new MenuItem(menuBean);
			al.add(2, item);
			item.setTitle("#(UserListPagerHandle.2)");
			item.setJsSelectCallback("$Actions['__user_logout']('userId=' + __pager_action(item).rowId(item));");
			return al;
		} else if (departmentId == STATE_LOCKED_ID) {
			final ArrayList<MenuItem> al = new ArrayList<MenuItem>();
			final MenuItem item = new MenuItem(menuBean);
			al.add(item);
			item.setTitle("解锁");
			item.setJsSelectCallback("__pager_action(item).unlock(item);");
			return al;
		}
		return items;
	}

	@Override
	public AbstractTablePagerData createTablePagerData(
			final ComponentParameter compParameter) {
		final int departmentId = ConvertUtils.toInt(
				getCatalogId(compParameter), 0);
		return new UserTablePagerData(compParameter) {
			@Override
			public Map<String, TablePagerColumn> getTablePagerColumns() {
				final String sv = compParameter.getRequestParameter("sv");
				if (StringUtils.hasText(sv) || isAdvancedSearch(compParameter)
						|| departmentId <= 0) {
					final Map<String, TablePagerColumn> columns = cloneTablePagerColumns();
					final TablePagerColumn dept = new TablePagerColumn("dept");
					dept.setColumnText(LocaleI18n
							.getMessage("UserListPagerHandle.0"));
					dept.setHeaderStyle("width: 150px;");
					dept.setStyle("width: 150px;text-align: center;");
					dept.setSort(false);
					dept.setSeparator(true);
					putColumn(columns, columns.size() - 2, "dept", dept);
					return columns;
				}
				return super.getTablePagerColumns();
			}

			@Override
			protected Map<Object, Object> getRowData(final Object dataObject) {
				final Map<Object, Object> rowData = super
						.getRowData(dataObject);
				final IUser user = (IUser) dataObject;
				final IDepartment department = user.department();
				if (department != null) {
					final StringBuilder sb = new StringBuilder();
					sb.append("<label class=\"mark1\">");
					sb.append(department.getText());
					sb.append("</label>");
					rowData.put("dept", sb.toString());
				}
				return rowData;
			}
		};
	}

	@Override
	public String getJavascriptCallback(final ComponentParameter compParameter,
			final String jsAction, final Object bean) {
		String jsCallback = StringUtils.blank(super.getJavascriptCallback(
				compParameter, jsAction, bean));
		if (LangUtils.contains(new String[] { "add", "delete", "undelete",
				"move2" }, jsAction)) {
			jsCallback += "$Actions['__dept_tree'].getTree().refresh();";
		}
		return jsCallback;
	}
}
