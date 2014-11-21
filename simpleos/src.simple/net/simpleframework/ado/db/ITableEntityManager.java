package net.simpleframework.ado.db;

import java.util.Map;

import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.event.ITableEntityListener;
import net.simpleframework.core.ado.db.Column;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.core.id.ID;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface ITableEntityManager extends IEntityManager {
	Table getTable();

	String getTablename();

	/* select single */

	Map<String, Object> queryForMap(IDataObjectValue dataObjectValue);

	Map<String, Object> queryForMap(Object[] columns, IDataObjectValue dataObjectValue);

	<T> T queryForObject(IDataObjectValue dataObjectValue, Class<T> beanClass);

	<T> T queryForObjectById(Object id, Class<T> beanClass);

	<T> T queryForObject(Object[] columns, IDataObjectValue dataObjectValue, Class<T> beanClass);

	<T> T queryForObjectById(Object[] columns, Object id, Class<T> beanClass);

	/* select multi */

	IQueryEntitySet<Map<String, Object>> query(IDataObjectValue dataObjectValue);

	IQueryEntitySet<Map<String, Object>> query(Object[] columns, IDataObjectValue dataObjectValue);

	<T> IQueryEntitySet<T> query(IDataObjectValue dataObjectValue, Class<T> beanClass);

	<T> IQueryEntitySet<T> query(Object[] columns, IDataObjectValue dataObjectValue,
			Class<T> beanClass);

	/* update */

	int update(Object[] columns, Object[] objects);

	int update(Object[] columns, Object object);

	int update(Object[] objects);

	int update(Object object);

	int updateTransaction(Object[] columns, Object[] objects);

	int updateTransaction(Object[] columns, Object[] objects, ITableEntityListener l);

	int updateTransaction(Object[] columns, Object object);

	int updateTransaction(Object[] columns, Object object, ITableEntityListener l);

	int updateTransaction(Object[] objects);

	int updateTransaction(Object[] objects, ITableEntityListener l);

	int updateTransaction(Object object);

	int updateTransaction(Object object, ITableEntityListener l);

	/* insert */

	int insert(Object object);

	int insertTransaction(Object object, ITableEntityListener l);

	int insertTransaction(Object object);

	int insert(Object[] objects);

	int insertTransaction(Object[] objects, ITableEntityListener l);

	int insertTransaction(Object[] objects);

	/* delete */

	int delete(IDataObjectValue dataObjectValue);

	int deleteTransaction(IDataObjectValue dataObjectValue, ITableEntityListener l);

	int deleteTransaction(IDataObjectValue dataObjectValue);

	int getCount(IDataObjectValue dataObjectValue);

	int getSum(String column, IDataObjectValue dataObjectValue);

	/* utils */

	Object exchange(Object o1, Object o2, Column order, boolean up);

	long nextIncValue(String column);

	ID nextId(String column);
}
