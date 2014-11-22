package net.simpleos;

import net.simpleframework.content.blog.Blog;
import net.simpleframework.content.blog.DefaultBlogApplicationModule;
import net.simpleframework.organization.IUser;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;
/**
 * 
 * @author 李岩飞 
 * @date 2014年11月19日 下午4:59:17 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public class SimpleosBlogApplicationModule extends DefaultBlogApplicationModule {

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
