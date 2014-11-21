package net.itsite.user;

import java.util.HashMap;
import java.util.Map;

import net.itsite.impl.AbstractCatalog;
import net.simpleframework.core.ado.db.Column;

public class SchoolCatalog extends AbstractCatalog {

	private static Map<String, Column> columns;

	@Override
	public Map<String, Column> getTableColumnDefinition() {
		if (columns == null) {
			synchronized (SchoolCatalog.class) {
				columns = new HashMap<String, Column>();
				columns.put("id", new Column("id"));
				columns.put("parentId", new Column("parentId"));
				columns.put("name", new Column("name"));
				columns.put("text", new Column("text"));
				columns.put("createDate", new Column("createDate"));
				columns.put("description", new Column("description"));
				columns.put("oorder", new Column("oorder"));
			}
		}
		return columns;
	}

}
