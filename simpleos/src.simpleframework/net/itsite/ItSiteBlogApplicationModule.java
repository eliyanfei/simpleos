package net.itsite;

import net.simpleframework.content.blog.Blog;
import net.simpleframework.content.blog.DefaultBlogApplicationModule;
import net.simpleframework.organization.IUser;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;

public class ItSiteBlogApplicationModule extends DefaultBlogApplicationModule {

	@Override
	public String getBlogUrl(final PageRequestResponse requestResponse, final IUser user) {
		final StringBuilder sb = new StringBuilder();
		sb.append("/blog");
		if (user != null) {
			sb.append("/").append(user.getId());
		}
		sb.append(".html");
		return sb.toString();
	}

	@Override
	public String getBlogViewUrl(final ComponentParameter compParameter, final Blog blog) {
		final StringBuilder sb = new StringBuilder();
		sb.append("/blog/v/").append(blog.getId()).append(".html");
		return sb.toString();
	}
}
