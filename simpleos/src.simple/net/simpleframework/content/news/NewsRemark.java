package net.simpleframework.content.news;

import java.util.HashMap;
import java.util.Map;

import net.simpleframework.ado.db.IEntityBeanAware;
import net.simpleframework.content.component.remark.RemarkItem;
import net.simpleframework.core.ado.db.Column;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class NewsRemark extends RemarkItem implements IEntityBeanAware {
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

	@Override
	public Map<String, Column> getTableColumnDefinition() {
		return columns;
	}

	private static final long serialVersionUID = -6782586841648725299L;
}
