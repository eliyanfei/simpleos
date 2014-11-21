package net.simpleos.backend.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.simpleframework.ado.db.IEntityBeanAware;
import net.simpleframework.content.component.catalog.Catalog;
import net.simpleframework.core.ado.db.Column;
import net.simpleframework.core.bean.ITreeBeanAware;

/**
 * 导航菜单
 * 
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-3上午11:43:30
 */
public class MenuNavBean extends Catalog implements IEntityBeanAware {
	public static final short MARK_SELECTED = 0;

	public static final short MARK_UNSELECTED = 1;

	private boolean buildIn = false;
	private String url;
	public List<MenuNavBean> childs = new ArrayList<MenuNavBean>();

	private static Map<String, Column> columns = new HashMap<String, Column>();
	static {
		columns.put("id", new Column("id"));
		columns.put("parentId", new Column("parentId"));
		columns.put("documentId", new Column("documentId"));
		columns.put("name", new Column("name"));
		columns.put("text", new Column("text"));
		columns.put("url", new Column("url"));
		columns.put("description", new Column("description"));
		columns.put("createDate", new Column("createDate"));
		columns.put("status", new Column("status"));
		columns.put("mark", new Column("mark"));
		columns.put("oorder", new Column("oorder"));
		columns.put("extend1", new Column("extend1"));
		columns.put("extend2", new Column("extend2"));
		columns.put("buildIn", new Column("buildIn"));
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
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

	private String extend1, extend2;

	public String getExtend1() {
		return extend1;
	}

	public void setExtend1(String extend1) {
		this.extend1 = extend1;
	}

	public String getExtend2() {
		return extend2;
	}

	public void setExtend2(String extend2) {
		this.extend2 = extend2;
	}

	@Override
	public ITreeBeanAware parent() {
		return MenuNavUtils.appModule.getBean(MenuNavBean.class, getParentId());
	}

	private static final long serialVersionUID = -8093282704274832307L;
}
