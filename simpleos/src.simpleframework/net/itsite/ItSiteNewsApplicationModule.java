package net.itsite;

import java.util.List;

import net.simpleframework.content.component.newspager.NewsBean;
import net.simpleframework.content.component.newspager.NewsCatalog;
import net.simpleframework.content.news.DefaultNewsApplicationModule;
import net.simpleframework.core.IInitializer;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;

public class ItSiteNewsApplicationModule extends DefaultNewsApplicationModule {
	@Override
	public String getViewUrl(final ComponentParameter compParameter, final NewsBean news) {
		final StringBuilder sb = new StringBuilder();
		sb.append("/news/v/").append(news.getId()).append(".html");
		return sb.toString();
	}

	@Override
	public String getCatalogUrl(final PageRequestResponse requestResponse, final Object catalog) {
		final StringBuilder sb = new StringBuilder();
		sb.append("/news/").append(((catalog instanceof NewsCatalog) ? ((NewsCatalog) catalog).getId() : catalog)).append(".html");
		return sb.toString();
	}

	@Override
	public void init(IInitializer initializer) {
		super.init(initializer);
	}

	public void setNewsCatalogs(final List<NewsCatalog> newsCatalogs) {
	}
}
