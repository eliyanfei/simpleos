package net.simpleframework.my.file;

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
public class MyFolder extends Catalog implements IEntityBeanAware {
	private static Map<String, Column> columns = new HashMap<String, Column>();
	static {
		columns.put("id", new Column("id"));
		columns.put("parentId", new Column("parentId"));
		columns.put("text", new Column("text"));
		columns.put("description", new Column("description"));
		columns.put("createDate", new Column("createDate"));
		columns.put("userId", new Column("userId"));
		columns.put("files", new Column("files"));
		columns.put("filesSize", new Column("filesSize"));
		columns.put("status", new Column("status"));
		columns.put("mark", new Column("mark"));
		columns.put("oorder", new Column("oorder"));
	}

	@Override
	public Map<String, Column> getTableColumnDefinition() {
		return columns;
	}

	private int files;

	private long filesSize;

	public int getFiles() {
		return files;
	}

	public void setFiles(final int files) {
		this.files = files;
	}

	public long getFilesSize() {
		return filesSize;
	}

	public void setFilesSize(final long filesSize) {
		this.filesSize = filesSize;
	}

	@Override
	public ITreeBeanAware parent() {
		return MyFileUtils.getTableEntityManager(MyFolder.class).queryForObjectById(getParentId(),
				MyFolder.class);
	}

	private static final long serialVersionUID = -6090745657107399870L;
}
