package net.simpleframework.organization.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntityManager;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.content.component.catalog.DefaultCatalogHandle;
import net.simpleframework.core.IApplicationModule;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.core.bean.IIdBeanAware;
import net.simpleframework.organization.EUserStatus;
import net.simpleframework.organization.IDepartment;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.EAccountStatus;
import net.simpleframework.organization.impl.DepartmentManager;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeBean;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeNode;
import net.simpleframework.web.page.component.ui.tree.TreeNode;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DepartmentTreeHandle extends DefaultCatalogHandle implements IOrgConst {

	@Override
	public Class<? extends IIdBeanAware> getEntityBeanClass() {
		return OrgUtils.dm().getBeanClass();
	}

	@Override
	public IApplicationModule getApplicationModule() {
		return OrgUtils.applicationModule;
	}

	@Override
	protected boolean hideOwnerMgrMenu() {
		return true;
	}

	@Override
	protected ExpressionValue getBeansSQL(final ComponentParameter compParameter,
			final Object parentId) {
		final ArrayList<Object> al = new ArrayList<Object>();
		final StringBuilder sql = new StringBuilder();
		if (parentId == null) {
			sql.append(Table.nullExpr(getTableEntityManager(compParameter).getTable(), "parentid"));
		} else {
			sql.append("parentid=?");
			al.add(parentId);
		}
		return new ExpressionValue(sql.toString(), al.toArray());
	}

	static {
		final StatTableEntityAdapter adapter = new StatTableEntityAdapter() {
			@Override
			void resetStat() {
				OrgUtils.um().removeAttribute("__user_stat");
			}
		};
		OrgUtils.um().addListener(adapter);
		OrgUtils.am().addListener(adapter);
	}

	@SuppressWarnings("unchecked")
	private Map<Object, Integer> getUserStat() {
		Map<Object, Integer> userStat = (Map<Object, Integer>) OrgUtils.um().getAttribute(
				"__user_stat");
		if (userStat == null) {
			userStat = new HashMap<Object, Integer>();
			final IQueryEntityManager qmgr = OrgUtils.getQueryEntityManager();
			final String aTable = OrgUtils.am().tblname();
			final String uTable = OrgUtils.um().tblname();
			final IQueryEntitySet<Map<String, Object>> qs = qmgr.query(new SQLValue(
					"select count(*) as uc, departmentid from " + uTable
							+ " where status<>? group by departmentid",
					new Object[] { EUserStatus.delete }));
			Map<String, Object> m;
			final DepartmentManager dm = OrgUtils.dm();
			while ((m = qs.next()) != null) {
				userStat.put(dm.id(m.get("departmentid")), ConvertUtils.toInt(m.get("uc"), 0));
			}

			final String sql = "select count(*) from ";
			final String sql2 = sql + uTable;
			int count = qmgr.queryForInt(new SQLValue(sql2 + " where status=?",
					new Object[] { EUserStatus.delete }));
			userStat.put(STATE_DELETE_ID, count);
			count = qmgr
					.queryForInt(new SQLValue(sql2 + " where status<>? and "
							+ Table.nullExpr(dm.getTable(), "departmentid"),
							new Object[] { EUserStatus.delete }));
			userStat.put(NO_DEPARTMENT_ID, count);
			count = qmgr.queryForInt(new SQLValue(sql + aTable + " where login=? and status=?",
					new Object[] { Boolean.TRUE, EAccountStatus.normal }));
			userStat.put(ONLINE_ID, count);

			final int c1 = qmgr.queryForInt(new SQLValue(sql + aTable + " where status=?",
					new Object[] { EAccountStatus.normal }));
			userStat.put(STATE_NORMAL_ID, c1);
			final int c2 = qmgr.queryForInt(new SQLValue(sql + aTable + " where status=?",
					new Object[] { EAccountStatus.register }));
			userStat.put(STATE_REGISTER_ID, c2);
			final int c3 = qmgr.queryForInt(new SQLValue(sql + aTable + " where status=?",
					new Object[] { EAccountStatus.locked }));
			userStat.put(STATE_LOCKED_ID, c3);
			userStat.put(0, c1 + c2 + c3);

			OrgUtils.um().setAttribute("__user_stat", userStat);
		}
		return userStat;
	}

	static final int[] treeNodeFlag = new int[] { 0, ONLINE_ID, NO_DEPARTMENT_ID, STATE_NORMAL_ID,
			STATE_REGISTER_ID, STATE_LOCKED_ID, STATE_DELETE_ID };

	static final String[] treeNodeImage = new String[] { "users.png", "user_online.png",
			"users_nodept.png", "users_normal.png", "users_register.png", "users_locked.png",
			"users_delete.png" };

	@Override
	public Collection<? extends AbstractTreeNode> getCatalogTreenodes(
			final ComponentParameter compParameter, final AbstractTreeBean treeBean,
			final AbstractTreeNode treeNode, final boolean dictionary) {
		final String imgBase = OrgUtils.getCssPath(compParameter) + "/images/";
		final Map<Object, Integer> uStat = getUserStat();
		final Collection<AbstractTreeNode> treeNodes = new ArrayList<AbstractTreeNode>();
		if (!dictionary && treeNode == null) {
			for (int i = 0; i < treeNodeFlag.length - 4; i++) {
				final AbstractTreeNode treeNode2 = new TreeNode(treeBean, treeNode,
						LocaleI18n.getMessage("DepartmentTreeHandle." + i));
				treeNodes.add(treeNode2);
				treeNode2.setId(StringUtils.hash(treeNode2.getDataObject()));
				treeNode2.setJsClickCallback(IOrgConst.Helper.__user_list(treeNodeFlag[i]));
				treeNode2.setImage(imgBase + treeNodeImage[i]);
				treeNode2.setPostfixText(WebUtils.enclose(uStat.get(treeNodeFlag[i])));
			}
		}
		String image;
		if (treeNode != null && (image = treeNode.getImage()) != null
				&& image.endsWith(treeNodeImage[0])) {
			for (int i = 3; i < treeNodeFlag.length; i++) {
				final AbstractTreeNode treeNode2 = new TreeNode(treeBean, treeNode,
						LocaleI18n.getMessage("DepartmentTreeHandle." + i));
				treeNode2.setId(StringUtils.hash(treeNode2.getDataObject()));
				treeNode2.setImage(imgBase + treeNodeImage[i]);
				treeNode2.setJsClickCallback(IOrgConst.Helper.__user_list(treeNodeFlag[i]));
				treeNode2.setPostfixText(WebUtils.enclose(uStat.get(treeNodeFlag[i])));
				treeNodes.add(treeNode2);
			}
		}
		final Collection<? extends AbstractTreeNode> coll = super.getCatalogTreenodes(compParameter,
				treeBean, treeNode, dictionary);
		for (final AbstractTreeNode treeNode2 : coll) {
			treeNodes.add(treeNode2);
			if (!dictionary) {
				if (treeNode == null) {
					treeNode2.setImage(imgBase + "dept_root.png");
				} else {
					treeNode2.setImage(imgBase + "dept.png");
					treeNode2.setJsClickCallback(IOrgConst.Helper.__user_list(treeNode2.getId()));
				}
				final IDepartment dept = (IDepartment) treeNode2.getDataObject();
				if (dept != null) {
					final Integer uc = getUserStat().get(dept.getId());
					treeNode2.setPostfixText(WebUtils.enclose(uc == null ? 0 : uc));
				}
			} else {
				treeNode2.setImage(imgBase + "dept.png");
			}
		}
		return treeNodes;
	}
}
