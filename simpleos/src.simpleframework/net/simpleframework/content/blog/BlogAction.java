package net.simpleframework.content.blog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.content.AbstractMgrToolsAction;
import net.simpleframework.content.IContentApplicationModule;
import net.simpleframework.core.id.ID;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class BlogAction extends AbstractMgrToolsAction {

	public IForward blogDrop(final ComponentParameter compParameter) {
		final String[] drags = StringUtils.split(compParameter.getRequestParameter("drag"), ";");
		final ArrayList<Blog> blogUpdates = new ArrayList<Blog>();
		final Map<ID, BlogCatalog> catalogUpdates = new HashMap<ID, BlogCatalog>();
		if (drags != null && drags.length > 0) {
			final ITableEntityManager catalog_mgr = BlogUtils.getTableEntityManager(BlogCatalog.class);
			final BlogCatalog drop = catalog_mgr.queryForObjectById(
					compParameter.getRequestParameter("drop"), BlogCatalog.class);
			if (drop != null) {
				final ITableEntityManager blog_mgr = BlogUtils.getTableEntityManager(Blog.class);
				for (final String drag : drags) {
					final Blog blog = blog_mgr.queryForObjectById(drag, Blog.class);
					final ID catalogId = blog.getCatalogId();
					if (blog != null && !drop.getId().equals2(catalogId)) {
						blog.setCatalogId(drop.getId());
						blogUpdates.add(blog);

						BlogCatalog catalog = catalogUpdates.get(catalogId);
						if (catalog == null) {
							catalog = catalog_mgr.queryForObjectById(catalogId, BlogCatalog.class);
							if (catalog != null) {
								catalogUpdates.put(catalogId, catalog);
							}
						}
						if (catalog != null) {
							catalog.setBlogs(catalog.getBlogs() - 1);
						}
						drop.setBlogs(drop.getBlogs() + 1);
					}
				}
				if (blogUpdates.size() > 0) {
					catalogUpdates.put(drop.getId(), drop);
				}
				blog_mgr.update(blogUpdates.toArray());
				catalog_mgr.update(catalogUpdates.values().toArray());
			}
		}
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				json.put("ok", blogUpdates.size() > 0);
			}
		});
	}

	@Override
	protected void doStatRebuild() {
		BlogUtils.doStatRebuild();
	}

	@Override
	public IContentApplicationModule getApplicationModule() {
		return BlogUtils.applicationModule;
	}
}
