package net.simpleos.module.docu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.content.EContentStatus;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.organization.IJob;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractPagerHandle;
import net.simpleframework.web.page.component.ui.pager.EPagerPosition;
import net.simpleos.impl.AbstractCatalog;

public class DocuPaperHandle extends AbstractPagerHandle {
	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("jobView".equals(beanProperty)) {
			if (StringUtils.hasText(compParameter.getRequestParameter("c")) || StringUtils.hasText(compParameter.getRequestParameter("_docu_topic")))
				return IJob.sj_account_normal;
			else
				return IJob.sj_anonymous;
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public String getPagerUrl(ComponentParameter compParameter, EPagerPosition pagerPosition, int pageItems, Map<String, Integer> pageVar) {
		final int t = ConvertUtils.toInt(compParameter.getRequestParameter("t"), 0);
		//		if (StringUtils.hasText(compParameter.getRequestParameter("c")) || t != 0 || pagerPosition == EPagerPosition.pageNumber) {
		if (t == 0) {
			return super.getPagerUrl(compParameter, pagerPosition, pageItems, pageVar);
		}
		final StringBuilder sb = new StringBuilder();
		sb.append("/docu/");
		final String s = compParameter.getRequestParameter("s");
		final String od = compParameter.getRequestParameter("od");
		final String tagId = compParameter.getRequestParameter("tagId");
		final String catalogId = compParameter.getRequestParameter("catalogId");
		sb.append(t).append("-");
		sb.append(ConvertUtils.toInt(s, 0)).append("-");
		sb.append(ConvertUtils.toInt(od, 0)).append("-");
		sb.append(ConvertUtils.toInt(tagId, 0)).append("-");
		sb.append(ConvertUtils.toInt(catalogId, 0)).append("-");
		final int pageNumber = ConvertUtils.toInt(pageVar.get("pageNumber"), 0);
		final int currentPageNumber = ConvertUtils.toInt(pageVar.get("currentPageNumber"), 0);
		final int pageCount = ConvertUtils.toInt(pageVar.get("pageCount"), 0);
		if (pagerPosition == EPagerPosition.left2) {
			sb.append(1);
		} else if (pagerPosition == EPagerPosition.left) {
			sb.append(currentPageNumber > 1 ? (currentPageNumber - 1) : 1);
		} else if (pagerPosition == EPagerPosition.number) {
			sb.append(pageNumber);
		} else if (pagerPosition == EPagerPosition.right) {
			sb.append(currentPageNumber >= pageCount ? pageCount : currentPageNumber + 1);
		} else if (pagerPosition == EPagerPosition.right2) {
			sb.append(pageCount);
		} else if (pagerPosition == EPagerPosition.pageItems) {
			sb.append(1);
		}
		sb.append(".html");
		return sb.toString();
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		final String c = WebUtils.toLocaleString(compParameter.getRequestParameter("c"));
		if (StringUtils.hasText(c)) {
			try {
				return DocuUtils.queryRelatedDocu(compParameter, c);
			} catch (IOException e) {
			}
		}
		final ITableEntityManager tMgr = DocuUtils.applicationModule.getDataObjectManager();
		final int catalogId = ConvertUtils.toInt(compParameter.getRequestParameter("catalogId"), 0);
		final List<Object> ol = new ArrayList<Object>();
		final StringBuffer sql = new StringBuffer();
		final StringBuffer where = new StringBuffer();
		final String _docu_topic = compParameter.getRequestParameter("_docu_topic");
		final int od = ConvertUtils.toInt(compParameter.getRequestParameter("od"), 0);
		sql.append("select d.* from ");
		sql.append(DocuAppModule.docu_documentshare.getName()).append(" d ");
		if (StringUtils.hasText(_docu_topic)) {
			where.append(" and title like ?");
			ol.add("%" + _docu_topic + "%");
		}
		where.append(" and status=?");
		ol.add(EContentStatus.publish);
		if (catalogId != 0) {
			where.append(" and catalogId in(")
					.append(AbstractCatalog.Utils.getJoinCatalog(catalogId, DocuUtils.applicationModule, DocuCatalog.class)).append(")");
		}
		sql.append(" where 1=1 ").append(where);
		sql.append(" order by ");
		if (1 == od) {
			sql.append("views desc");
		} else if (0 == od) {
			sql.append("createDate desc");
		} else if (3 == od) {
			sql.append("remarkDate desc");
		} else if (4 == od) {
			sql.append("attentions desc");
		} else if (5 == od) {
			sql.append("downCounter desc");
		}
		return tMgr.query(new SQLValue(sql.toString(), ol.toArray(new Object[] {})), DocuBean.class);
	}

	@Override
	public Map<String, Object> getFormParameters(ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, "catalogId");
		putParameter(compParameter, parameters, "_docu_topic");

		putParameter(compParameter, parameters, "t");
		putParameter(compParameter, parameters, "c");
		putParameter(compParameter, parameters, "od");
		return parameters;
	}

}
