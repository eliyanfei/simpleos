package net.simpleos.module.docu;

import java.util.HashMap;
import java.util.Map;

import net.simpleframework.core.ado.db.Column;
import net.simpleframework.core.bean.ITreeBeanAware;
import net.simpleos.impl.AbstractCatalog;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月21日 下午4:13:34 
 * @Description: 文档的目录
 *
 */
public class DocuCatalog extends AbstractCatalog {
	private static Map<String, Column> columns;

	@Override
	public Map<String, Column> getTableColumnDefinition() {
		if (columns == null) {
			synchronized (DocuCatalog.class) {
				columns = new HashMap<String, Column>();
				columns.put("parentId", new Column("parentId"));
				columns.put("id", new Column("id"));
				columns.put("name", new Column("name"));
				columns.put("text", new Column("text"));
				columns.put("createDate", new Column("createDate"));
				columns.put("counter", new Column("counter"));
				columns.put("description", new Column("description"));
				columns.put("oorder", new Column("oorder"));
			}
		}
		return columns;
	}

	@Override
	public ITreeBeanAware parent() {
		return DocuUtils.applicationModule.getBean(getClass(), getParentId());
	}

	private static final long serialVersionUID = -4757318228056631881L;
}
