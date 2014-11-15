package net.simpleframework.my.friends;

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
public class FriendsGroup extends Catalog implements IEntityBeanAware {
	private static Map<String, Column> columns = new HashMap<String, Column>();
	static {
		columns.put("id", new Column("id"));
		columns.put("parentId", new Column("parentId"));
		columns.put("text", new Column("text"));
		columns.put("createDate", new Column("createDate"));
		columns.put("userId", new Column("userId"));
		columns.put("friends", new Column("friends"));
		columns.put("description", new Column("description"));
		columns.put("oorder", new Column("oorder"));
	}

	@Override
	public Map<String, Column> getTableColumnDefinition() {
		return columns;
	}

	private int friends;

	public int getFriends() {
		return friends;
	}

	public void setFriends(final int friends) {
		this.friends = friends;
	}

	@Override
	public ITreeBeanAware parent() {
		return FriendsUtils.getTableEntityManager(FriendsGroup.class).queryForObjectById(
				getParentId(), FriendsGroup.class);
	}

	private static final long serialVersionUID = 4160884990539593267L;
}
