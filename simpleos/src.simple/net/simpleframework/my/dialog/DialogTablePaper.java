package net.simpleframework.my.dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.itsite.ItSiteUtil;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.my.space.MySpaceUtils;
import net.simpleframework.organization.IJob;
import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractPagerHandle;

public class DialogTablePaper extends AbstractPagerHandle {
	@Override
	public Object getBeanProperty(ComponentParameter compParameter, String beanProperty) {
		if ("jobExecute".equals(beanProperty)) {
			return IJob.sj_account_normal;
		} else if ("title".equals(beanProperty)) {
			final StringBuilder sb = new StringBuilder();
			final String box = compParameter.getRequestParameter("box");
			final boolean r = "me".equals(box);
			final boolean s = "you".equals(box);
			final String act = "\" onclick=\"$Actions['dialogTableAct']('box=";
			sb.append("<a class=\"a2");
			if (!r && !s) {
				sb.append(" nav_arrow");
			}
			sb.append(act).append("all');\">全部对话</a>").append(HTMLBuilder.SEP);
			sb.append("<a class=\"a2");
			if (r) {
				sb.append(" nav_arrow");
			}
			sb.append(act).append("me');\">发起对话</a>").append(HTMLBuilder.SEP);
			sb.append("<a class=\"a2");
			if (s) {
				sb.append(" nav_arrow");
			}
			sb.append(act).append("you');\">收到对话</a>");
			return sb.toString();
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		final ITableEntityManager tMgr = MySpaceUtils.getTableEntityManager(SimpleDialog.class);
		final List<Object> ol = new ArrayList<Object>();
		final StringBuffer sql = new StringBuffer();
		final String box = compParameter.getRequestParameter("box");
		sql.append("1=1");
		if ("me".equals(box)) {
			sql.append(" and sentId=? and sentDel=?");
			ol.add(ItSiteUtil.getLoginUser(compParameter).getId());
			ol.add(false);
		} else if ("you".equals(box)) {
			sql.append(" and toId=? and toDel=?");
			ol.add(ItSiteUtil.getLoginUser(compParameter).getId());
			ol.add(false);
		} else {
			sql.append(" and ((sentId=? and sentDel=?) or (toId=? and toDel=?))");
			ol.add(ItSiteUtil.getLoginUser(compParameter).getId());
			ol.add(false);
			ol.add(ItSiteUtil.getLoginUser(compParameter).getId());
			ol.add(false);
		}

		sql.append(" order by lastDate desc");
		return tMgr.query(new ExpressionValue(sql.toString(), ol.toArray()), SimpleDialog.class);
	}

	@Override
	public Map<String, Object> getFormParameters(ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, "box");
		return parameters;
	}

}
