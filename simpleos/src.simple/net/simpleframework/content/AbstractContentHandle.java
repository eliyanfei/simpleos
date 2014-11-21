package net.simpleframework.content;

import java.util.ArrayList;
import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.core.bean.IIdBeanAware;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ado.AbstractDbComponentHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractContentHandle extends AbstractDbComponentHandle implements
		IContentHandle {

	@Override
	public Object getDocumentId(final ComponentParameter compParameter) {
		try {
			Object documentId = compParameter
					.getRequestParameter(getDocumentIdParameterName(compParameter));
			if (!StringUtils.hasTextObject(documentId)) {
				documentId = compParameter.getBeanProperty("documentId");
			}
			return ConvertUtils.toLong(documentId);
		} catch (final Exception e) {
			return null;
		}
	}

	public static final String DOCUMENT_ID = "__document_Id";

	public String getDocumentIdParameterName(final ComponentParameter compParameter) {
		return DOCUMENT_ID;
	}

	protected <T extends IIdBeanAware> IDataObjectQuery<T> beans(
			final ComponentParameter compParameter, final Object parentId, final String orderBySQL) {
		@SuppressWarnings("unchecked")
		final Class<T> t = (Class<T>) getEntityBeanClass();
		final ExpressionValue ev = getBeansSQL(compParameter, parentId);
		final IQueryEntitySet<T> set;
		if (StringUtils.hasText(orderBySQL)) {
			set = getTableEntityManager(compParameter, t).query(
					new ExpressionValue(ev.getExpression() + " " + orderBySQL, ev.getValues()), t);
		} else {
			set = getTableEntityManager(compParameter, t).query(ev, t);
		}
		set.getQueryDialect().setCountSQL(ev);
		return set;
	}

	protected ExpressionValue getBeansSQL(final ComponentParameter compParameter,
			final Object parentId) {
		final ArrayList<Object> al = new ArrayList<Object>();
		final StringBuilder sql = new StringBuilder();
		final Object documentId = getDocumentId(compParameter);
		if (documentId == null) {
			sql.append(Table.nullExpr(getTableEntityManager(compParameter).getTable(), "documentid"));
		} else {
			sql.append("documentid=?");
			al.add(documentId);
		}
		sql.append(" and ");
		if (parentId == null) {
			sql.append(Table.nullExpr(getTableEntityManager(compParameter).getTable(), "parentid"));
		} else {
			sql.append("parentid=?");
			al.add(parentId);
		}
		return new ExpressionValue(sql.toString(), al.toArray());
	}

	@Override
	public <T extends IDataObjectBean> void doBeforeEdit(final ComponentParameter compParameter,
			final ITableEntityManager temgr, final T t, final Map<String, Object> data,
			final Class<T> beanClazz) {
		if (t instanceof AbstractContentBase) {
			((AbstractContentBase) t).updateLastUpdate(compParameter);
		}
	}

	@Override
	public String getIdParameterName(final ComponentParameter compParameter) {
		return null;
	}
}
