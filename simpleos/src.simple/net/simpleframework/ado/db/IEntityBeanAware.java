package net.simpleframework.ado.db;

import java.util.Map;

import net.simpleframework.core.ado.db.Column;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.util.StringUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IEntityBeanAware extends IDataObjectBean {

	Map<String, Column> getTableColumnDefinition();

	public static class Utils {
		public static String getColumnName(final Object beanAware, final String propertyName) {
			if (beanAware instanceof IEntityBeanAware) {
				final Map<String, Column> columns = ((IEntityBeanAware) beanAware)
						.getTableColumnDefinition();
				if (columns != null && StringUtils.hasText(propertyName)) {
					final Column column = columns.get(propertyName);
					if (column != null) {
						return column.getColumnName();
					}
				}
			}
			return propertyName;
		}

		public static String getPropertyName(final Object beanAware, final String columnName) {
			if (beanAware instanceof IEntityBeanAware) {
				final Map<String, Column> columns = ((IEntityBeanAware) beanAware)
						.getTableColumnDefinition();
				if (columns != null && StringUtils.hasText(columnName)) {
					for (final Map.Entry<String, Column> entry : columns.entrySet()) {
						if (columnName.equals(entry.getValue().getColumnName())) {
							return entry.getKey();
						}
					}
				}
			}
			return columnName;
		}
	}
}
