package net.itsite.docu;

import java.util.HashMap;
import java.util.Map;

import net.itsite.impl.AbstractCatalog;
import net.simpleframework.core.ado.db.Column;
import net.simpleframework.core.bean.ITreeBeanAware;

public class DocuCatalog extends AbstractCatalog {
	private static Map<String, Column> columns;

	@Override
	public Map<String, Column> getTableColumnDefinition() {
		if (columns == null) {
			synchronized (DocuCatalog.class) {
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
		return DocuUtils.applicationModule.getBean(getClass(), getParentId());
	}

	private static final long serialVersionUID = -4757318228056631881L;
}
