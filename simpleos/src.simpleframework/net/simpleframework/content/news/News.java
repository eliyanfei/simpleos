package net.simpleframework.content.news;

import java.util.HashMap;
import java.util.Map;

import net.simpleframework.ado.db.IEntityBeanAware;
import net.simpleframework.content.component.newspager.NewsBean;
import net.simpleframework.core.ado.db.Column;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class News extends NewsBean implements IEntityBeanAware {

	private final static Map<String, Column> columns;
	static {
		columns = new HashMap<String, Column>();
		columns.put("id", new Column("id"));
		columns.put("catalogId", new Column("catalogId"));
		columns.put("topic", new Column("topic"));
		columns.put("viewTemplate", new Column("viewtemplate"));
		columns.put("content", new Column("content"));
		columns.put("keywords", new Column("keywords"));
		columns.put("author", new Column("author"));
		columns.put("source", new Column("source"));
		columns.put("views", new Column("views"));
		columns.put("remarks", new Column("remarks"));
		columns.put("attentions", new Column("attentions"));
		columns.put("allowComments", new Column("allowcomments"));
		columns.put("description", new Column("description"));
		columns.put("createDate", new Column("createdate"));
		columns.put("userId", new Column("userid"));
		columns.put("lastUpdate", new Column("lastupdate"));
		columns.put("lastUserId", new Column("lastuserid"));
		columns.put("status", new Column("status"));
		columns.put("mark", new Column("mark"));
		columns.put("ttype", new Column("ttype"));
		columns.put("ttop", new Column("ttop"));
		columns.put("oorder", new Column("oorder"));
	}

	@Override
	public Map<String, Column> getTableColumnDefinition() {
		return columns;
	}

	private static final long serialVersionUID = 2867011289252893766L;
}
