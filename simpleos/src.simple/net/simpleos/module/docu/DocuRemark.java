package net.simpleos.module.docu;

import java.util.HashMap;
import java.util.Map;

import net.simpleframework.ado.db.IEntityBeanAware;
import net.simpleframework.content.component.remark.RemarkItem;
import net.simpleframework.core.ado.db.Column;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月21日 下午4:15:01 
 * @Description: 文档评论
 *
 */
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
