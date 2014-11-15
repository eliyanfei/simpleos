package net.simpleframework.content;

import javax.servlet.ServletContext;

import net.simpleframework.content.bbs.DefaultBbsApplicationModule;
import net.simpleframework.content.bbs.IBbsApplicationModule;
import net.simpleframework.content.blog.DefaultBlogApplicationModule;
import net.simpleframework.content.blog.IBlogApplicationModule;
import net.simpleframework.content.component.catalog.CatalogRegistry;
import net.simpleframework.content.component.catalog.ICatalogHandle;
import net.simpleframework.content.component.filepager.FilePagerRegistry;
import net.simpleframework.content.component.newspager.NewsPagerRegistry;
import net.simpleframework.content.component.remark.RemarkRegistry;
import net.simpleframework.content.component.topicpager.TopicPagerRegistry;
import net.simpleframework.content.component.vote.VoteRegistry;
import net.simpleframework.content.news.DefaultNewsApplicationModule;
import net.simpleframework.content.news.INewsApplicationModule;
import net.simpleframework.core.AbstractInitializer;
import net.simpleframework.core.IApplication;
import net.simpleframework.core.IInitializer;
import net.simpleframework.web.IWebApplication;
import net.simpleframework.web.page.component.ComponentRegistryFactory;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ContentInitializer extends AbstractInitializer {
	private IBbsApplicationModule bbsApplicationModule;

	private INewsApplicationModule newsApplicationModule;

	private IBlogApplicationModule blogApplicationModule;

	@Override
	public void doInit(final IApplication application) {
		IInitializer.Utils.deploySqlScript(ContentInitializer.class, application, "content");
		IInitializer.Utils.deploySqlScript(ICatalogHandle.class, application, "catalog");
		super.doInit(application);
		regist(application);
	}

	@Override
	protected Class<?>[] getI18n() {
		return new Class<?>[] { ContentInitializer.class };
	}

	private void regist(final IApplication application) {
		if (!(application instanceof IWebApplication)) {
			return;
		}
		final ComponentRegistryFactory factory = ComponentRegistryFactory.getInstance();
		final ServletContext servletContext = ((IWebApplication) application).getServletContext();
		factory.regist(new RemarkRegistry(servletContext));
		factory.regist(new VoteRegistry(servletContext));
		factory.regist(new NewsPagerRegistry(servletContext));
		factory.regist(new FilePagerRegistry(servletContext));
		factory.regist(new CatalogRegistry(servletContext));
		factory.regist(new TopicPagerRegistry(servletContext));
	}

	public IBbsApplicationModule getBbsApplicationModule() {
		if (bbsApplicationModule == null) {
			bbsApplicationModule = new DefaultBbsApplicationModule();
		}
		return bbsApplicationModule;
	}

	public void setBbsApplicationModule(final IBbsApplicationModule bbsApplicationModule) {
		this.bbsApplicationModule = bbsApplicationModule;
	}

	public INewsApplicationModule getNewsApplicationModule() {
		if (newsApplicationModule == null) {
			newsApplicationModule = new DefaultNewsApplicationModule();
		}
		return newsApplicationModule;
	}

	public void setNewsApplicationModule(final INewsApplicationModule newsApplicationModule) {
		this.newsApplicationModule = newsApplicationModule;
	}

	public IBlogApplicationModule getBlogApplicationModule() {
		if (blogApplicationModule == null) {
			blogApplicationModule = new DefaultBlogApplicationModule();
		}
		return blogApplicationModule;
	}

	public void setBlogApplicationModule(final IBlogApplicationModule blogApplicationModule) {
		this.blogApplicationModule = blogApplicationModule;
	}
}
