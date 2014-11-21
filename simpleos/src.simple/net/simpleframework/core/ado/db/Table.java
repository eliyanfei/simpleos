package net.simpleframework.core.ado.db;

import java.io.Serializable;

import net.simpleframework.core.id.ID;
import net.simpleframework.util.StringUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class Table implements Serializable {
	private static final long serialVersionUID = -6445073606291514860L;

	private final String name;

	private final String[] uniqueColumns;

	private boolean noCache;

	public Table(final String name) {
		this(name, "id");
	}

	public Table(final String name, final boolean noCache) {
		this(name);
		this.noCache = noCache;
	}

	public Table(final String name, final String uniqueColumn) {
		this(name, new String[] { uniqueColumn });
	}

	public Table(final String name, final String[] uniqueColumns) {
		this.name = name;
		this.uniqueColumns = uniqueColumns;
	}

	public String getName() {
		return name;
	}

	public String[] getUniqueColumns() {
		return uniqueColumns;
	}

	public boolean isNoCache() {
		return noCache;
	}

	public void setNoCache(final boolean noCache) {
		this.noCache = noCache;
	}

	public Class<?> getIdType() {
		return Long.class;
	}

	public ID getNullId() {
		if (Number.class.isAssignableFrom(getIdType())) {
			return ID.zero;
		} else {
			return ID.nullId;
		}
	}

	public ID newID(final Object id) {
		return ID.Utils.newID(id, getIdType());
	}

	@Override
	public String toString() {
		return name + ", unique[" + StringUtils.join(uniqueColumns, "-") + "]";
	}

	public static String nullExpr(final Table tbl, final String column) {
		final StringBuilder sb = new StringBuilder();
		sb.append(column);
		if (Number.class.isAssignableFrom(tbl.getIdType())) {
			sb.append(" = 0");
		} else {
			sb.append(" is null");
		}
		return sb.toString();
	}
}
