package net.simpleframework.content.news;

import java.util.HashMap;
import java.util.Map;

import net.simpleframework.core.ado.db.Column;
import net.simpleframework.core.bean.ITreeBeanAware;
import net.simpleos.impl.AbstractCatalog;

public class NewsExtCatalog extends AbstractCatalog {
	private static Map<String, Column> columns;

	@Override
	public Map<String, Column> getTableColumnDefinition() {
		if (columns == null) {
			synchronized (NewsExtCatalog.class) {
				columns = new HashMap<String, Column>();
				columns.put("parentId", new Column("parentId"));
				columns.put("id", new Column("id"));
				columns.put("name", new Column("name"));
				columns.put("text", new Column("text"));
				columns.put("createDate", new Column("createDate"));
				columns.put("counter", new Column("counter"));
				columns.put("description", new Column("description"));
				columns.put("oorder", new Column("oorder"));
			}
		}
		return columns;
	}

	@Override
	public ITreeBeanAware parent() {
		return NewsUtils.getTableEntityManager(NewsExtCatalog.class).queryForObjectById(getParentId(), NewsExtCatalog.class);
	}

	private static final long serialVersionUID = -4757318228056631881L;
}
