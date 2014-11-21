package net.simpleframework.content.component.catalog;

import java.util.HashMap;
import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.organization.EMemberType;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.AbstractUrlForward;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class CatalogAction extends AbstractAjaxRequestHandle {

	public IForward itemSave(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = CatalogUtils
				.getComponentParameter(compParameter);
		final ICatalogHandle cHandle = (ICatalogHandle) nComponentParameter.getComponentHandle();
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final Map<String, Object> data = cHandle.doSavePropEditor(nComponentParameter);
				final String catalogId = compParameter.getRequestParameter(cHandle
						.getIdParameterName(nComponentParameter));
				final String jsCallback;
				if (StringUtils.hasText(catalogId)) {
					cHandle.doEdit(nComponentParameter, catalogId, data);
					jsCallback = cHandle.getJavascriptCallback(nComponentParameter, "edit", null);
				} else {
					final Catalog catalog = cHandle.doAdd(nComponentParameter, data);
					jsCallback = cHandle.getJavascriptCallback(nComponentParameter, "add", catalog);
				}
				json.put("next",
						ConvertUtils.toBoolean(compParameter.getRequestParameter("next"), false));
				if (StringUtils.hasText(jsCallback)) {
					json.put("jsCallback", jsCallback);
				}
			}
		});
	}

	public IForward itemDelete(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = CatalogUtils
				.getComponentParameter(compParameter);
		final ICatalogHandle cHandle = (ICatalogHandle) nComponentParameter.getComponentHandle();
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final Catalog catalog = cHandle.getEntityBeanByRequest(nComponentParameter);
				if (catalog != null) {
					cHandle.doDelete(nComponentParameter, new ExpressionValue("id=?",
							new Object[] { catalog.getId() }));
					final String jsCallback = cHandle.getJavascriptCallback(nComponentParameter,
							"delete", null);
					if (StringUtils.hasText(jsCallback)) {
						json.put("jsCallback", jsCallback);
					}
				}
			}
		});
	}

	public IForward itemMove(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = CatalogUtils
				.getComponentParameter(compParameter);
		final ICatalogHandle cHandle = (ICatalogHandle) nComponentParameter.getComponentHandle();
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final Object id = compParameter.getRequestParameter("b1");
				final Object id2 = compParameter.getRequestParameter("b2");
				final boolean up = ConvertUtils.toBoolean(compParameter.getRequestParameter("up"),
						false);
				cHandle.doExchange(nComponentParameter, id, id2, !up);
				final String jsCallback = cHandle.getJavascriptCallback(nComponentParameter,
						"exchange", null);
				if (StringUtils.hasText(jsCallback)) {
					json.put("jsCallback", jsCallback);
				}
			}
		});
	}

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("jobExecute".equals(beanProperty)) {
			final ComponentParameter nComponentParameter = CatalogUtils
					.getComponentParameter(compParameter);
			if (nComponentParameter.componentBean != null) {
				final String componentName = (String) compParameter.getBeanProperty("name");
				if ("ajaxAddCatalogPage".equals(componentName)) {
					return nComponentParameter.getBeanProperty("jobAdd");
				} else if ("ajaxCatalogMove".equals(componentName)) {
					return nComponentParameter.getBeanProperty("jobExchange");
				} else {
					final ICatalogHandle cHandle = (ICatalogHandle) nComponentParameter
							.getComponentHandle();
					final Catalog catalog = cHandle.getEntityBeanByRequest(nComponentParameter);
					if (catalog != null
							&& AccountSession.isAccount(compParameter.getSession(), catalog.getUserId())) {
						return IJob.sj_account_normal;
					} else {
						if ("ajaxEditCatalogPage".equals(componentName)) {
							return nComponentParameter.getBeanProperty("jobEdit");
						} else if ("ajaxCatalogDelete".equals(componentName)) {
							return nComponentParameter.getBeanProperty("jobDelete");
						} else if ("ajaxCatalogOwnerPage".equals(componentName)) {
							return nComponentParameter.getBeanProperty("jobOwner");
						}
					}
				}
			}
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	public IForward catalogUrl(final ComponentParameter compParameter) {
		return AbstractUrlForward.componentUrl(CatalogRegistry.catalog, "/jsp/catalog_edit.jsp");
	}

	public IForward catalogOwnerUrl(final ComponentParameter compParameter) {
		return AbstractUrlForward.componentUrl(CatalogRegistry.catalog, "/jsp/catalog_owner.jsp");
	}

	public IForward addUserOwner(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = CatalogUtils
				.getComponentParameter(compParameter);
		final ICatalogHandle cHandle = (ICatalogHandle) nComponentParameter.getComponentHandle();
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final Catalog catalog = cHandle.getEntityBeanByRequest(nComponentParameter);
				final String[] ids = StringUtils.split(compParameter.getRequestParameter("ids"));
				if (catalog != null && ids != null) {
					for (final Object id : ids) {
						final Map<String, Object> data = new HashMap<String, Object>();
						data.put("catalogId", catalog.getId());
						data.put("ownerType", EMemberType.user);
						data.put("ownerId", OrgUtils.um().queryForObjectById(id).getId());
						cHandle.doAdd(nComponentParameter, data, CatalogOwner.class);
					}
				}
			}
		});
	}

	public IForward ownerDelete(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = CatalogUtils
				.getComponentParameter(compParameter);
		final ICatalogHandle cHandle = (ICatalogHandle) nComponentParameter.getComponentHandle();
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final EMemberType ownerType = EMemberType.valueOf(compParameter
						.getRequestParameter("ownerType"));
				cHandle.doDelete(
						nComponentParameter,
						new ExpressionValue("catalogId=? and ownerType=? and ownerId=?", new Object[] {
								compParameter.getRequestParameter(cHandle
										.getIdParameterName(nComponentParameter)), ownerType,
								compParameter.getRequestParameter("ownerId") }), CatalogOwner.class);
			}
		});
	}
}
