package net.simpleframework.applets.tag;

import java.util.HashMap;
import java.util.Map;

import net.simpleframework.ado.DataObjectManagerUtils;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.content.AbstractContent;
import net.simpleframework.content.IContentApplicationModule;
import net.simpleframework.content.IContentPagerHandle;
import net.simpleframework.core.IApplicationModule;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.db.AbstractDbTablePagerHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TagRTableHandle extends AbstractDbTablePagerHandle {
	@Override
	public IApplicationModule getApplicationModule() {
		return TagUtils.applicationModule;
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, IContentPagerHandle._VTYPE);
		putParameter(compParameter, parameters, ITagApplicationModule._TAG_ID);
		return parameters;
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		final IContentApplicationModule module = TagUtils.applicationModule.getContentApplicationModule(compParameter, null);
		if (module == null) {
			return null;
		}
		final ITableEntityManager tag_mgr = DataObjectManagerUtils.getTableEntityManager(module);
		final SQLValue sqlValue = new SQLValue("select n.* from " + tag_mgr.getTablename() + " n inner join "
				+ TagUtils.getTableEntityManager(TagRBean.class).getTablename() + " r on r.rid = n.id where r.tagid=?",
				new Object[] { compParameter.getRequestParameter(ITagApplicationModule._TAG_ID) });
		return tag_mgr.query(sqlValue, module.getEntityBeanClass());
	}

	@Override
	protected Map<Object, Object> getTableRow(final ComponentParameter compParameter, final Object dataObject) {
		final AbstractContent news = (AbstractContent) dataObject;
		final Map<Object, Object> rows = new HashMap<Object, Object>();
		final StringBuilder sb = new StringBuilder();
		sb.append(news);
		sb.append("<div class=\"gray-color\" style=\"margin-top: 4px;\">");
		sb.append(ConvertUtils.toDateString(news.getCreateDate(), "yyyy-MM-dd"));
		sb.append(" By ").append(news.getUserText());
		sb.append("<a onclick=\"$Actions['_tagRDelete']('c=").append(news.getId());
		sb.append("&t=").append(compParameter.getRequestParameter(ITagApplicationModule._TAG_ID));
		sb.append("');\" style=\"margin-left: 20px;\">#(Delete)</a>");
		sb.append("</div>");
		rows.put("topic", sb.toString());
		return rows;
	}
}
