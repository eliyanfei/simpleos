package net.simpleos;

import java.util.List;

import net.simpleframework.content.component.newspager.NewsBean;
import net.simpleframework.content.component.newspager.NewsCatalog;
import net.simpleframework.content.news.DefaultNewsApplicationModule;
import net.simpleframework.core.IInitializer;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;
/**
 * 
 * @author 李岩飞 
 * @date 2014年11月19日 下午4:55:48 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public class SimpleosNewsApplicationModule extends DefaultNewsApplicationModule {
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
