package net.simpleframework.content.blog;

import java.util.HashMap;
import java.util.Map;

import net.simpleframework.ado.db.IEntityBeanAware;
import net.simpleframework.content.component.catalog.Catalog;
import net.simpleframework.core.ado.db.Column;
import net.simpleframework.core.bean.ITreeBeanAware;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class BlogCatalog extends Catalog implements IEntityBeanAware {
	private static Map<String, Column> columns = new HashMap<String, Column>();
	static {
		columns.put("id", new Column("id"));
		columns.put("parentId", new Column("parentId"));
		columns.put("text", new Column("text"));
		columns.put("createDate", new Column("createDate"));
		columns.put("userId", new Column("userId"));
		columns.put("blogs", new Column("blogs"));
		columns.put("description", new Column("description"));
		columns.put("oorder", new Column("oorder"));
	}

	@Override
	public Map<String, Column> getTableColumnDefinition() {
		return columns;
	}

	private int blogs;

	public int getBlogs() {
		return blogs;
	}

	public void setBlogs(final int blogs) {
		this.blogs = blogs;
	}

	@Override
	public ITreeBeanAware parent() {
		return BlogUtils.getTableEntityManager(BlogCatalog.class).queryForObjectById(getParentId(),
				BlogCatalog.class);
	}

	private static final long serialVersionUID = -4757318228056631881L;
}
