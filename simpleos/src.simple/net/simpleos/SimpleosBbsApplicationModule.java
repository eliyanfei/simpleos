package net.simpleos;

import net.simpleframework.content.bbs.BbsForum;
import net.simpleframework.content.bbs.BbsTopic;
import net.simpleframework.content.bbs.DefaultBbsApplicationModule;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;
/**
 * 
 * @author 李岩飞 
 * @date 2014年11月19日 下午4:59:25 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public class SimpleosBbsApplicationModule extends DefaultBbsApplicationModule {
	@Override
	public String getTopicUrl(final PageRequestResponse requestResponse, final BbsForum forum) {
		final StringBuilder sb = new StringBuilder();
		sb.append("/bbs/tl");
		if (forum != null) {
			sb.append("/").append(forum.getId());
		}
		sb.append(".html");
		return sb.toString();
	}

	@Override
	public String getPostUrl(final ComponentParameter compParameter, final BbsTopic topic) {
		final StringBuilder sb = new StringBuilder();
		sb.append("/bbs/").append(topic.getCatalogId()).append("/");
		sb.append(topic.getId()).append(".html");
		return sb.toString();
	}

	@Override
	public String getUsersUrl(final PageRequestResponse requestResponse) {
		return "/bbs/user.html";
	}
}
