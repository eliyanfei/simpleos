package net.simpleframework.content.component.vote;

import net.simpleframework.web.page.component.AbstractComponentHtmlRender;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.IComponentRegistry;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class VoteRender extends AbstractComponentHtmlRender {
	public VoteRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	protected String getRelativePath(final ComponentParameter compParameter) {
		final StringBuilder sb = new StringBuilder();
		sb.append("/jsp/vote.jsp?").append(VoteUtils.BEAN_ID);
		sb.append("=").append(compParameter.componentBean.hashId());
		return sb.toString();
	}
}
