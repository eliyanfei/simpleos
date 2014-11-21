package net.simpleframework.content.bbs;

import java.util.HashMap;
import java.util.Map;

import net.simpleframework.ado.db.IEntityBeanAware;
import net.simpleframework.content.component.topicpager.TopicBean;
import net.simpleframework.core.ado.db.Column;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class BbsTopic extends TopicBean implements IEntityBeanAware {

	private final static Map<String, Column> columns;
	static {
		columns = new HashMap<String, Column>();
		columns.put("id", new Column("id"));
		columns.put("catalogId", new Column("catalogId"));
		columns.put("topic", new Column("topic"));
		columns.put("keywords", new Column("keywords"));
		columns.put("lastpostId", new Column("lastpostId"));
		columns.put("views", new Column("views"));
		columns.put("replies", new Column("replies"));
		columns.put("attentions", new Column("attentions"));
		columns.put("star", new Column("star"));
		columns.put("createDate", new Column("createDate"));
		columns.put("userId", new Column("userId"));
		columns.put("lastUpdate", new Column("lastUpdate"));
		columns.put("lastUserId", new Column("lastUserId"));
		columns.put("lastPostUpdate", new Column("lastPostUpdate"));
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

	private static final long serialVersionUID = -7674518296017829131L;
}
