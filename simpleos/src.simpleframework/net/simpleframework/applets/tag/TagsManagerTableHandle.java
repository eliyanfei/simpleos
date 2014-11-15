package net.simpleframework.applets.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.content.IContentPagerHandle;
import net.simpleframework.core.IApplicationModule;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.id.ID;
import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.db.AbstractDbTablePagerHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TagsManagerTableHandle extends AbstractDbTablePagerHandle {
	@Override
	public IApplicationModule getApplicationModule() {
		return TagUtils.applicationModule;
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, IContentPagerHandle._VTYPE);
		putParameter(compParameter, parameters, ITagApplicationModule._CATALOG_ID);
		putParameter(compParameter, parameters, "_tagsSearch");
		return parameters;
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		final List<Object> ol = new ArrayList<Object>();
		final StringBuilder sql = new StringBuilder();
		sql.append("vtype=?");
		ol.add(TagUtils.getVtype(compParameter));
		final ID catalogId = TagUtils.applicationModule.getCatalogId(compParameter);
		if (catalogId != null && !catalogId.equals2(0)) {
			sql.append(" and catalogid=?");
			ol.add(catalogId);
		}
		final String tag = compParameter.getRequestParameter("_tagsSearch");
		if (StringUtils.hasText(tag)) {
			sql.append(" and tagtext like '%").append(tag).append("%'");
		}
		sql.append(" order by ttype desc");
		return TagUtils.getTableEntityManager(TagBean.class).query(new ExpressionValue(sql.toString(), ol.toArray()), TagBean.class);
	}

	@Override
	protected Map<Object, Object> getTableRow(final ComponentParameter compParameter, final Object dataObject) {
		final Map<Object, Object> rows = super.getTableRow(compParameter, dataObject);
		final TagBean tag = (TagBean) dataObject;
		final StringBuilder sb = new StringBuilder();
		final StringBuilder params = new StringBuilder();
		params.append(ITagApplicationModule._TAG_ID).append("=").append(tag.getId()).append("&").append(IContentPagerHandle._VTYPE).append("=")
				.append(TagUtils.getVtype(compParameter));
		sb.append("<a onclick=\"$Actions['_tagDelete']('").append(params).append("');\">").append("#(Delete)").append("</a>");
		sb.append(HTMLBuilder.SEP);
		sb.append("<a onclick=\"$Actions['newsTagRWindow']('").append(params).append("');\">").append("#(tags_manager.3)").append("</a>");
		sb.append(HTMLBuilder.SEP);
		sb.append("<a onclick=\"$Actions['_tagOptionsWindow']('").append(params).append("');\">#(tags_manager.6)</a>");
		rows.put("action", sb);
		return rows;
	}
}
