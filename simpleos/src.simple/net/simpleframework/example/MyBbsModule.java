package net.simpleframework.example;

import net.simpleframework.content.bbs.BbsForum;
import net.simpleframework.content.bbs.BbsTopic;
import net.simpleframework.content.bbs.DefaultBbsApplicationModule;
import net.simpleframework.content.component.topicpager.ITopicPagerHandle;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;

public class MyBbsModule extends DefaultBbsApplicationModule {

	@Override
	public String getApplicationUrl(final PageRequestResponse requestResponse) {
		return "/developer/app/bbs/m.jsp";
	}

	@Override
	public String getTopicUrl(final PageRequestResponse requestResponse, final BbsForum forum) {
		final StringBuilder sb = new StringBuilder();
		sb.append("/developer/app/bbs/tl.jsp");
		if (forum != null) {
			sb.append("?").append(getCatalogIdName(requestResponse)).append("=").append(forum.getId());
		}
		return sb.toString();
	}

	@Override
	public String getPostUrl(final ComponentParameter compParameter, final BbsTopic topic) {
		final StringBuilder sb = new StringBuilder();
		sb.append("/developer/app/bbs/pl.jsp?")
				.append(getCatalogIdName(compParameter))
				.append("=")
				.append(topic.getCatalogId())
				.append("&")
				.append(
						((ITopicPagerHandle) compParameter.getComponentHandle())
								.getIdParameterName(compParameter)).append("=").append(topic.getId());
		return sb.toString();
	}
}
