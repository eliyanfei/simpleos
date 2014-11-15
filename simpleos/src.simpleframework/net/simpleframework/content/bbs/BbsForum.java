package net.simpleframework.content.bbs;

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
public class BbsForum extends Catalog implements IEntityBeanAware {

	private static Map<String, Column> columns = new HashMap<String, Column>();
	static {
		columns.put("id", new Column("id"));
		columns.put("parentId", new Column("parentId"));
		columns.put("name", new Column("name"));
		columns.put("text", new Column("text"));
		columns.put("description", new Column("description"));
		columns.put("createDate", new Column("createDate"));
		columns.put("status", new Column("status"));
		columns.put("showTags", new Column("showTags"));
		columns.put("oorder", new Column("oorder"));
	}

	@Override
	public Map<String, Column> getTableColumnDefinition() {
		return columns;
	}

	private boolean showTags;

	public boolean isShowTags() {
		return showTags;
	}

	public void setShowTags(final boolean showTags) {
		this.showTags = showTags;
	}

	@Override
	public ITreeBeanAware parent() {
		return BbsUtils.getTableEntityManager(BbsForum.class).queryForObjectById(getParentId(),
				BbsForum.class);
	}

	private static final long serialVersionUID = -2044931010025997556L;
}
