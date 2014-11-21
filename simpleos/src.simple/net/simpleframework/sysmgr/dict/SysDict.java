package net.simpleframework.sysmgr.dict;

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
public class SysDict extends Catalog implements IEntityBeanAware {
	public static final short MARK_SELECTED = 0;

	public static final short MARK_UNSELECTED = 1;

	private boolean buildIn = false;

	private static Map<String, Column> columns = new HashMap<String, Column>();
	static {
		columns.put("id", new Column("id"));
		columns.put("parentId", new Column("parentId"));
		columns.put("documentId", new Column("documentId"));
		columns.put("name", new Column("name"));
		columns.put("text", new Column("text"));
		columns.put("description", new Column("description"));
		columns.put("createDate", new Column("createDate"));
		columns.put("status", new Column("status"));
		columns.put("mark", new Column("mark"));
		columns.put("oorder", new Column("oorder"));
		columns.put("extend1", new Column("extend1"));
		columns.put("extend2", new Column("extend2"));
		columns.put("extend3", new Column("extend3"));
		columns.put("extend4", new Column("extend4"));
		columns.put("buildIn", new Column("buildIn"));
	}

	@Override
	public boolean isBuildIn() {
		return this.buildIn;
	}

	public void setBuildIn(boolean buildIn) {
		this.buildIn = buildIn;
	}

	@Override
	public Map<String, Column> getTableColumnDefinition() {
		return columns;
	}

	private long extend1, extend2;

	private String extend3, extend4;

	public long getExtend1() {
		return extend1;
	}

	public void setExtend1(final long extend1) {
		this.extend1 = extend1;
	}

	public long getExtend2() {
		return extend2;
	}

	public void setExtend2(final long extend2) {
		this.extend2 = extend2;
	}

	public String getExtend3() {
		return extend3;
	}

	public void setExtend3(final String extend3) {
		this.extend3 = extend3;
	}

	public String getExtend4() {
		return extend4;
	}

	public void setExtend4(final String extend4) {
		this.extend4 = extend4;
	}

	@Override
	public ITreeBeanAware parent() {
		return DictUtils.getTableEntityManager(SysDict.class).queryForObjectById(getParentId(), SysDict.class);
	}

	private static final long serialVersionUID = -8093282704274832307L;
}
