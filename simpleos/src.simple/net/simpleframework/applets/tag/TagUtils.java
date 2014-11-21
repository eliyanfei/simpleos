package net.simpleframework.applets.tag;

import java.util.ArrayList;
import java.util.List;

import net.simpleframework.ado.DataObjectManagerUtils;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.content.IContentPagerHandle;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.id.ID;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.util.db.DbUtils;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.PageRequestResponse;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class TagUtils {
	public static ITagApplicationModule applicationModule;

	public static String deployPath;

	public static ITableEntityManager getTableEntityManager(final Class<?> beanClazz) {
		return DataObjectManagerUtils.getTableEntityManager(applicationModule, beanClazz);
	}

	public static ITableEntityManager getTableEntityManager() {
		return DataObjectManagerUtils.getTableEntityManager(applicationModule);
	}

	public static EFunctionModule getVtype(final PageRequestResponse requestResponse) {
		return ConvertUtils.toEnum(EFunctionModule.class, requestResponse.getRequestParameter(IContentPagerHandle._VTYPE));
	}

	public static void syncTags(final EFunctionModule vtype, final ID catalogId, String keywords, final ID rid) {
		if (vtype == null) {
			return;
		}
		if (!StringUtils.hasText(keywords)) {
			return;
		}
		final ITableEntityManager tag_mgr = getTableEntityManager(TagBean.class);
		final ITableEntityManager tagr_mgr = getTableEntityManager(TagRBean.class);
		keywords = StringUtils.replace(keywords, ",", " ");
		keywords = StringUtils.replace(keywords, ";", " ");

		final ArrayList<TagBean> inserts = new ArrayList<TagBean>();
		final ArrayList<TagBean> updates = new ArrayList<TagBean>();
		for (String tag : StringUtils.split(keywords, " ")) {
			tag = tag.trim().toLowerCase();
			TagBean tagBean = tag_mgr.queryForObject(new ExpressionValue("vtype=? and catalogid=? and tagtext=?", new Object[] { vtype, catalogId,
					tag }), TagBean.class);
			if (tagBean == null) {
				tagBean = new TagBean();
				tagBean.setVtype(vtype);
				tagBean.setFrequency(1);
				tagBean.setCatalogId(catalogId);
				tagBean.setTagText(tag);
				inserts.add(tagBean);
			} else {
				tagBean.setFrequency(tagBean.getFrequency() + 1);
				tag_mgr.update(tagBean);
				updates.add(tagBean);
			}
		}
		tag_mgr.insert(inserts.toArray());
		tag_mgr.update(updates.toArray());

		inserts.addAll(updates);
		final ArrayList<TagRBean> inserts2 = new ArrayList<TagRBean>();
		tagr_mgr.delete(new ExpressionValue("rid=?", new Object[] { rid }));
		for (final TagBean tagBean : inserts) {
			final TagRBean tagRBean = new TagRBean();
			tagRBean.setTagId(tagBean.getId());
			tagRBean.setRid(rid);
			inserts2.add(tagRBean);
		}
		tagr_mgr.insert(inserts2.toArray());
	}

	public static void deleteRTags(final Object[] rid) {
		getTableEntityManager(TagRBean.class).delete(new ExpressionValue(DbUtils.getIdsSQLParam("rid", rid.length), rid));
	}

	public static TagBean getTagBeanById(final Object id) {
		return getTableEntityManager(TagBean.class).queryForObjectById(id, TagBean.class);
	}

	public static IDataObjectQuery<TagBean> layoutTags(final PageRequestResponse requestResponse) {
		final EFunctionModule vtype = getVtype(requestResponse);
		final ID catalogId = applicationModule.getCatalogId(requestResponse);
		final StringBuffer sql = new StringBuffer();
		final List<Object> ol = new ArrayList<Object>();
		sql.append("vtype=?");
		ol.add(vtype);
		if (catalogId != null && !catalogId.equals2(0)) {
			sql.append(" and catalogid=?");
			ol.add(catalogId);
		}
		final IDataObjectQuery<TagBean> qs = getTableEntityManager(TagBean.class).query(
				new ExpressionValue(sql.toString() + " order by ttype desc, views desc", ol.toArray()), TagBean.class);
		if (qs != null) {
			final int rows = ConvertUtils.toInt(requestResponse.getRequestParameter("rows"), 6);
			if (rows > 0) {
				qs.setCount(rows);
			}
		}
		return qs;
	}

	public static IDataObjectQuery<TagBean> layoutAllTags(final PageRequestResponse requestResponse) {
		final ITableEntityManager tMgr = TagUtils.getTableEntityManager(TagBean.class);
		return tMgr.query(new ExpressionValue("1=1 order by views desc,frequency desc", null), TagBean.class);
	}

	public static void doTagsRebuild(final EFunctionModule vtype, final ID catalogId) {
		final ITableEntityManager tag_mgr = getTableEntityManager(TagBean.class);
		final ITableEntityManager tagr_mgr = getTableEntityManager(TagRBean.class);

		final String tag_name = tag_mgr.getTablename();
		final StringBuilder sql = new StringBuilder();
		final Object[] params = new Object[] { vtype, catalogId };
		sql.append("update ").append(tag_name);
		sql.append(" a set frequency=(select count(tagid) from ");
		sql.append(tagr_mgr.getTablename()).append(" where tagid=a.id) where a.vtype=? and a.catalogid=?");
		tag_mgr.execute(new SQLValue(sql.toString(), params));

		sql.setLength(0);
		sql.append("delete from ").append(tag_name).append(" where frequency=0 and vtype=? and catalogid=?");
		tag_mgr.execute(new SQLValue(sql.toString(), params));
		tag_mgr.reset();
	}

	public static void updateViews(final PageRequestResponse requestResponse, final TagBean tagBean) {
		WebUtils.updateViews(requestResponse.getSession(), getTableEntityManager(TagBean.class), tagBean);
	}

	public static final String[] fontWeight = new String[] { "arial", "comic sans ms", "lucida sans unicode", "georgia", "trebuchet ms",
			"courier new", "times new roman", "Lucida Console", "Monaco", "mono", "monospace" };

	public static String xmlTagsLayout() {
		return deployPath + "jsp/tags_layout.xml";
	}
}
