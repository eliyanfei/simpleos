package net.simpleframework.my.file;

import java.util.HashMap;
import java.util.Map;

import net.simpleframework.ado.db.IEntityBeanAware;
import net.simpleframework.content.component.filepager.FileBean;
import net.simpleframework.core.ado.db.Column;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MyFile extends FileBean implements IEntityBeanAware {
	private final static Map<String, Column> columns;
	static {
		columns = new HashMap<String, Column>();
		columns.put("id", new Column("id"));
		columns.put("catalogId", new Column("catalogId"));
		columns.put("topic", new Column("topic"));
		columns.put("filename", new Column("filename"));
		columns.put("filetype", new Column("filetype"));
		columns.put("filesize", new Column("filesize"));
		columns.put("downloads", new Column("downloads"));
		columns.put("md5", new Column("md5"));
		columns.put("sha1", new Column("sha1"));
		columns.put("ip", new Column("ip"));
		columns.put("description", new Column("description"));
		columns.put("createDate", new Column("createDate"));
		columns.put("userId", new Column("userId"));
		columns.put("status", new Column("status"));
		columns.put("mark", new Column("mark"));
		columns.put("ttop", new Column("ttop"));
		columns.put("oorder", new Column("oorder"));
		columns.put("refId", new Column("refId"));
		columns.put("type", new Column("type"));
	}

	@Override
	public Map<String, Column> getTableColumnDefinition() {
		return columns;
	}

	private static final long serialVersionUID = -9151129267289532127L;
}
