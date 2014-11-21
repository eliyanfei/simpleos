package net.simpleframework.core.ado.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.core.AAttributeAware;
import net.simpleframework.util.StringUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class Column extends AAttributeAware implements Serializable {
	private static final long serialVersionUID = -241399268622218668L;

	private Table table;

	private String columnName;

	/** 列为表达式函数? */
	private String columnSqlName;

	private String columnText;

	private Class<?> beanPropertyType;

	private boolean visible = true;

	private EOrder order;

	public Collection<FilterItem> filterItems;

	public Column(final String columnName) {
		this(columnName, null);
	}

	public Column(final String columnName, final String columnText) {
		this(columnName, columnText, null);
	}

	public Column(final String columnName, final String columnText, final Class<?> beanPropertyType) {
		this.columnName = columnName;
		this.columnText = columnText;
		this.beanPropertyType = beanPropertyType;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(final Table table) {
		this.table = table;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(final String columnName) {
		this.columnName = columnName;
	}

	public String getColumnText() {
		return StringUtils.text(columnText, getColumnName());
	}

	public void setColumnText(final String columnText) {
		this.columnText = columnText;
	}

	public Class<?> getBeanPropertyType() {
		return beanPropertyType;
	}

	public void setBeanPropertyType(final Class<?> beanPropertyType) {
		this.beanPropertyType = beanPropertyType;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(final boolean visible) {
		this.visible = visible;
	}

	public EOrder getOrder() {
		return order == null ? EOrder.normal : order;
	}

	public void setOrder(final EOrder order) {
		this.order = order;
	}

	public String getColumnSqlName() {
		return StringUtils.text(columnSqlName, getColumnName());
	}

	public void setColumnSqlName(final String columnSqlName) {
		this.columnSqlName = columnSqlName;
	}

	public Collection<FilterItem> getFilterItems() {
		if (filterItems == null) {
			filterItems = new ArrayList<FilterItem>();
		}
		return filterItems;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		final Table table = getTable();
		if (table != null) {
			sb.append(table.getName()).append(".");
		}
		sb.append(getColumnSqlName());
		return sb.toString();
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		} else {
			if (obj instanceof Column) {
				return toString().equals(((Column) obj).toString());
			} else {
				return super.equals(obj);
			}
		}
	}
}
