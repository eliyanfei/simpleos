package net.simpleframework.web.page.component.ui.pager.db;

import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractDbTablePagerAction extends AbstractAjaxRequestHandle {

	protected abstract ComponentParameter getComponentParameter(final PageRequestResponse requestResponse);

	public IForward exchange(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = getComponentParameter(compParameter);
		final IDbTablePagerHandle handle = (IDbTablePagerHandle) nComponentParameter.getComponentHandle();
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final Object id = nComponentParameter.getRequestParameter("rowId");
				final Object id2 = nComponentParameter.getRequestParameter("rowId2");
				final boolean up = ConvertUtils.toBoolean(nComponentParameter.getRequestParameter("up"), false);
				handle.doExchange(nComponentParameter, id, id2, !up);
				final String jsCallback = handle.getJavascriptCallback(nComponentParameter, "exchange", null);
				if (StringUtils.hasText(jsCallback)) {
					json.put("jsCallback", jsCallback);
				}
			}
		});
	}

	public IForward publish(ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = getComponentParameter(compParameter);
		final IDbTablePagerHandle tHandle = (IDbTablePagerHandle) nComponentParameter.getComponentHandle();
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				final StringBuilder sql = new StringBuilder();
				final String[] idArray = StringUtils.split(nComponentParameter.request.getParameter(tHandle.getIdParameterName(nComponentParameter)));
				if (idArray != null) {
					for (int i = 0; i < idArray.length; i++) {
						if (i > 0) {
							sql.append(" or ");
						}
						sql.append("id=?");
					}
					final ITableEntityManager tMgr = tHandle.getTableEntityManager(nComponentParameter);
					tMgr.getTable().getName();
					tMgr.execute(new SQLValue("update " + tMgr.getTablename() + " set status=2 where " + sql.toString(),
							idArray));
					final String jsCallback = tHandle.getJavascriptCallback(nComponentParameter, "delete", null);
					if (StringUtils.hasText(jsCallback)) {
						json.put("jsCallback", jsCallback);
					}
				}
			}
		});
	}

	public IForward delete(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = getComponentParameter(compParameter);
		final IDbTablePagerHandle tHandle = (IDbTablePagerHandle) nComponentParameter.getComponentHandle();
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final ExpressionValue ev = getDeleteExpressionValue(nComponentParameter, tHandle.getIdParameterName(nComponentParameter));
				if (ev != null) {
					tHandle.doDelete(nComponentParameter, ev);
					final String jsCallback = tHandle.getJavascriptCallback(nComponentParameter, "delete", null);
					if (StringUtils.hasText(jsCallback)) {
						json.put("jsCallback", jsCallback);
					}
				}
			}
		});
	}

	public IForward undelete(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = getComponentParameter(compParameter);
		final IDbTablePagerHandle tHandle = (IDbTablePagerHandle) nComponentParameter.getComponentHandle();
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final ExpressionValue ev = getDeleteExpressionValue(nComponentParameter, tHandle.getIdParameterName(nComponentParameter));
				if (ev != null) {
					tHandle.doUnDelete(nComponentParameter, ev);
					final String jsCallback = tHandle.getJavascriptCallback(nComponentParameter, "undelete", null);
					if (StringUtils.hasText(jsCallback)) {
						json.put("jsCallback", jsCallback);
					}
				}
			}
		});
	}

	public static ExpressionValue getDeleteExpressionValue(final ComponentParameter compParameter, final String idParameterName) {
		final StringBuilder sql = new StringBuilder();
		final String[] idArray = StringUtils.split(compParameter.request.getParameter(idParameterName));
		if (idArray != null) {
			for (int i = 0; i < idArray.length; i++) {
				if (i > 0) {
					sql.append(" or ");
				}
				sql.append("id=?");
			}
			return new ExpressionValue(sql.toString(), idArray);
		}
		return null;
	}
}
