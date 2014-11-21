package net.simpleframework.example;

import java.util.Map;

import net.simpleframework.content.component.catalog.Catalog;
import net.simpleframework.content.component.catalog.CatalogUtils;
import net.simpleframework.content.component.catalog.ICatalogHandle;
import net.simpleframework.content.component.filepager.DefaultFilePagerHandle;
import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.ComponentParameter;

public class MyFilePagerHandle extends DefaultFilePagerHandle {
	@Override
	public void handleCreated(final PageRequestResponse requestResponse,
			final AbstractComponentBean componentBean) {
		PageUtils.doDatabase(DefaultFilePagerHandle.class, componentBean);
	}

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		// if (beanProperty.equals("selector")) {
		// return ".appfiles form";
		// } else
		if (beanProperty.equals("title")) {
			final ComponentParameter nComponentParameter = CatalogUtils
					.getComponentParameter(compParameter);
			final StringBuilder sb = new StringBuilder();
			if (nComponentParameter.componentBean != null) {
				final ICatalogHandle handle = (ICatalogHandle) nComponentParameter.getComponentHandle();
				Catalog catalog = handle.getEntityBeanById(nComponentParameter,
						compParameter.getRequestParameter("__catalog_Id"));
				int i = 0;
				while (catalog != null) {
					if (i++ > 0) {
						sb.insert(0, HTMLBuilder.NAV);
						sb.insert(0, "</a>").insert(0, catalog.toString());
						sb.insert(0, "')\">").insert(0, catalog.getId());
						sb.insert(0, "<a onclick=\"__file_list('");
					} else {
						sb.append(catalog.toString());
					}
					catalog = (Catalog) catalog.parent();
				}
			}
			return HTMLBuilder.div(sb.toString(), new String[] { "style=margin-left: 4px;" });
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, CatalogUtils.BEAN_ID);
		return parameters;
	}
}
