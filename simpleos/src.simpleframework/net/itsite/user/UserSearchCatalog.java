package net.itsite.user;

import java.util.HashMap;
import java.util.Map;

import net.a.ItSiteUtil;
import net.itsite.impl.AbstractCatalog;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.ado.db.Column;
import net.simpleframework.core.bean.ITreeBeanAware;

public class UserSearchCatalog extends AbstractCatalog {
	private static Map<String, Column> columns;
	private int yday;//该类目下的项目数
	private int tday;//该类目下的项目数

	@Override
	public Map<String, Column> getTableColumnDefinition() {
		if (columns == null) {
			synchronized (UserSearchCatalog.class) {
				columns = new HashMap<String, Column>();
				columns.put("id", new Column("id"));
				columns.put("parentId", new Column("parentId"));
				columns.put("name", new Column("name"));
				columns.put("text", new Column("text"));
				columns.put("createDate", new Column("createDate"));
				columns.put("counter", new Column("counter"));
				columns.put("yday", new Column("yday"));
				columns.put("tday", new Column("tday"));
				columns.put("description", new Column("description"));
				columns.put("oorder", new Column("oorder"));
			}
		}
		return columns;
	}

	public void setYday(int yday) {
		this.yday = yday;
	}

	public int getYday() {
		return yday;
	}

	public int getTday() {
		return tday;
	}

	public void setTday(int tday) {
		this.tday = tday;
	}

	@Override
	public ITreeBeanAware parent() {
		final ITableEntityManager tMgr = ItSiteUtil.getTableEntityManager(ItSiteUtil.applicationModule, getClass());
		return tMgr.queryForObjectById(getParentId(), getClass());
	}

}
