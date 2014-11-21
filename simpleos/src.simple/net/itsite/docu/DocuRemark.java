package net.itsite.docu;

import java.util.HashMap;
import java.util.Map;

import net.simpleframework.ado.db.IEntityBeanAware;
import net.simpleframework.content.component.remark.RemarkItem;
import net.simpleframework.core.ado.db.Column;

public class DocuRemark extends RemarkItem implements IEntityBeanAware {

	private static final long serialVersionUID = -3704659703177686923L;

	public DocuRemark() {
	}

	private final static Map<String, Column> columns;
	static {
		columns = new HashMap<String, Column>();
		columns.put("id", new Column("id"));
		columns.put("parentId", new Column("parentid"));
		columns.put("documentId", new Column("documentid"));
		columns.put("content", new Column("content"));
		columns.put("support", new Column("support"));
		columns.put("opposition", new Column("opposition"));
		columns.put("ip", new Column("ip"));
		columns.put("createDate", new Column("createdate"));
		columns.put("userId", new Column("userid"));
		columns.put("lastUpdate", new Column("lastupdate"));
		columns.put("lastUserId", new Column("lastuserid"));
	}

	public Map<String, Column> getTableColumnDefinition() {
		return columns;
	}

}
