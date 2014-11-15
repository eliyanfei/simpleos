package net.simpleframework.applets.tag;

import java.util.Map;

import net.simpleframework.ado.DataObjectManagerUtils;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.content.IContentApplicationModule;
import net.simpleframework.content.bbs.BbsUtils;
import net.simpleframework.content.bbs.IBbsApplicationModule;
import net.simpleframework.content.blog.BlogUtils;
import net.simpleframework.content.blog.IBlogApplicationModule;
import net.simpleframework.content.component.catalog.Catalog;
import net.simpleframework.content.news.INewsApplicationModule;
import net.simpleframework.content.news.NewsUtils;
import net.simpleframework.core.IInitializer;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.core.id.ID;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.AbstractWebApplicationModule;
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
public class DefaultTagApplicationModule extends AbstractWebApplicationModule implements ITagApplicationModule {

	static final String deployName = "applets/tag";

	@Override
	protected void putTables(final Map<Class<?>, Table> tables) {
		tables.put(TagBean.class, new Table("simple_tag"));
		tables.put(TagRBean.class, new Table("simple_rtag", new String[] { "tagid", "rid" }));
	}

	@Override
	public void init(final IInitializer initializer) {
		super.init(initializer);
		doInit(TagUtils.class, deployName);
	}

	@Override
	public Class<?> getEntityBeanClass() {
		return TagBean.class;
	}

	@Override
	public IContentApplicationModule getContentApplicationModule(final PageRequestResponse requestResponse, EFunctionModule vtype) {
		if (vtype == null) {
			vtype = TagUtils.getVtype(requestResponse);
		}
		if (vtype == EFunctionModule.news) {
			return NewsUtils.applicationModule;
		} else if (vtype == EFunctionModule.blog) {
			return BlogUtils.applicationModule;
		} else if (vtype == EFunctionModule.bbs) {
			return BbsUtils.applicationModule;
		} else {
			return null;
		}
	}

	@Override
	public String getManager(final Object... params) {
		final PageRequestResponse requestResponse = (PageRequestResponse) params[0];
		final IContentApplicationModule module = getContentApplicationModule(requestResponse, null);
		if (module != null) {
			return module.getManager(requestResponse);
		}
		return super.getManager(requestResponse);
	}

	@Override
	public ID getCatalogId(final PageRequestResponse requestResponse) {
		final IContentApplicationModule module = getContentApplicationModule(requestResponse, null);
		ID id = null;
		if (module instanceof IBbsApplicationModule) {
			id = DataObjectManagerUtils.getTableEntityManager(module, Catalog.class).getTable()
					.newID(requestResponse.getRequestParameter(ITagApplicationModule._CATALOG_ID));
		}
		return id == null ? ID.zero : id;
	}

	@Override
	public String getTagUrl(final PageRequestResponse requestResponse, final TagBean tag) {
		final IContentApplicationModule module = getContentApplicationModule(requestResponse, tag.getVtype());
		final StringBuilder sb = new StringBuilder();
		if (module instanceof INewsApplicationModule) {
			return WebUtils.addParameters(module.getApplicationUrl(requestResponse), _TAG_ID + "=" + tag.getId());
		} else if (module instanceof IBbsApplicationModule) {
			return WebUtils.addParameters(((IBbsApplicationModule) module).getTopicUrl(requestResponse, BbsUtils.getForum(requestResponse)), _TAG_ID
					+ "=" + tag.getId());
		} else if (module instanceof IBlogApplicationModule) {
			return WebUtils.addParameters(((IBlogApplicationModule) module).getApplicationUrl(requestResponse), _TAG_ID + "=" + tag.getId());
		}
		return sb.toString();
	}

	@Override
	public void reCreateAllTags(final PageRequestResponse requestResponse) {
		final IContentApplicationModule module = getContentApplicationModule(requestResponse, null);
		if (module != null) {
			final EFunctionModule vtype = TagUtils.getVtype(requestResponse);
			final ID catalogId = getCatalogId(requestResponse);
			ExpressionValue ev = null;
			if (vtype == EFunctionModule.bbs) {
				ev = new ExpressionValue("catalogid=?", new Object[] { catalogId });
			}
			final IQueryEntitySet<Map<String, Object>> qs = DataObjectManagerUtils.getTableEntityManager(module).query(
					new String[] { "id", "keywords" }, ev);
			Map<String, Object> map;
			while ((map = qs.next()) != null) {
				final String keywords = (String) map.get("keywords");
				if (StringUtils.hasText(keywords)) {
					TagUtils.syncTags(vtype, catalogId, keywords, ID.Utils.newID(map.get("id")));
				}
			}
			TagUtils.doTagsRebuild(vtype, catalogId);
		}
	}
}
