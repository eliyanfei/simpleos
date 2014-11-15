package net.prj.manager.datatemp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.simpleframework.ado.db.IEntityBeanAware;
import net.simpleframework.content.component.catalog.Catalog;
import net.simpleframework.core.ado.db.Column;
import net.simpleframework.core.bean.ITreeBeanAware;

/**
 * 
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-3上午11:43:30
 */
public class PrjProductCatalogBean extends Catalog implements IEntityBeanAware {
	public static final short MARK_SELECTED = 0;

	public static final short MARK_UNSELECTED = 1;

	private boolean buildIn = false;
	public List<PrjProductCatalogBean> childs = new ArrayList<PrjProductCatalogBean>();

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
		columns.put("url", new Column("url"));
		columns.put("oorder", new Column("oorder"));
		columns.put("buildIn", new Column("buildIn"));
	}

	public String url;

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

	@Override
	public ITreeBeanAware parent() {
		return PrjDataTempUtils.appModule.getBean(PrjProductCatalogBean.class, getParentId());
	}

	private static final long serialVersionUID = -8093282704274832307L;
}
