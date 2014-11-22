package net.simpleos.i;

import java.util.List;
import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.ado.db.event.ITableEntityListener;
import net.simpleframework.ado.lucene.AbstractLuceneManager;
import net.simpleframework.applets.attention.AttentionBean;
import net.simpleframework.content.IContentApplicationModule;
import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleos.impl.PrjColumns;
/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月20日 上午11:45:58 
 * @Description: 公共接口
 *
 */
public interface ISimpleosApplicationModule extends IContentApplicationModule {
	void doUpdate(final String sql);

	void doUpdate(final List<? extends AbstractIdDataObjectBean> beanList);

	void doUpdate(final AbstractIdDataObjectBean bean, ITableEntityListener tel);

	void doUpdate(final AbstractIdDataObjectBean bean);

	void doDelete(final AbstractIdDataObjectBean bean, ITableEntityListener tel);

	void doDelete(final AbstractIdDataObjectBean bean);

	void doDelete(final List<? extends AbstractIdDataObjectBean> beanList);

	void doDelete(final List<? extends Object> idList, Class<? extends Object> beanClass);

	void doDelete(String field, final List<? extends Object> idList, Class<? extends Object> beanClass);

	void doDelete(final Class<?> bean, final Object id, ITableEntityListener tel);

	void doDelete(final Class<?> bean, final Object id);

	void doDelete(final String where, final Class<?> bean);

	void doDelete(final PageRequestResponse requestResponse);

	void doUpdate(final Object[] columns, final Object bean);

	void doUpdate(final Object[] columns, final Object bean, ITableEntityListener tel);

	IQueryEntitySet<Map<String, Object>> queryBean(final String sql, final Object[] values);

	<T> IQueryEntitySet<T> queryBean(final ExpressionValue exp, Class<T> t);

	<T> IQueryEntitySet<T> queryBean(Class<T> t);

	Map<String, Object> queryBean(SQLValue dataObjectValue);

	<T> IQueryEntitySet<T> queryBean(final String exp, Class<T> t);

	<T> IQueryEntitySet<T> queryBean(final String exp, final Object[] values, Class<T> t);

	<T> List<T> doQuery(final String sql, final String col, Class<T> t);

	<T> T getBean(final Class<T> t, final Object id);

	<T> T getBeanByExp(final Class<T> t, final String exp, final Object[] params);

	String getDeployPath();

	String getCatalogName(Object id, Class<?> cla);

	long getValue(final String sql, Object[] values);

	long count(final String sql, Object[] values);

	<T> long count(final String exp, Object[] values, Class<T> t);

	long nextId();

	AbstractLuceneManager createLuceneManager(final ComponentParameter compParameter);

	String tabs13(PageRequestResponse requestResponse);

	void deleteAttentions(final PageRequestResponse requestResponse, final Object attentionId);

	void deleteRemarks(final Object documentId);

	AttentionBean getAttention(final PageRequestResponse requestResponse, final Object id);

	EFunctionModule getEFunctionModule();

	String getViewUrl(Object id);

	String getViewUrl1(PageRequestResponse requestResponse, final Object... params);

	String getViewUrl1(final String url, final Object... params);

	ITableEntityManager getDataObjectManager(final Class<?> t);

	ITableEntityManager getDataObjectManager();

	String getActionHTML(final PageRequestResponse requestResponse, final ICommonBeanAware commonBean);

	PrjColumns getPrjColumns(final String type);

}
