package net.simpleframework.content.component.vote;

import javax.servlet.ServletContext;

import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.AbstractComponentRender;
import net.simpleframework.web.page.component.AbstractComponentResourceProvider;
import net.simpleframework.web.page.component.ui.portal.module.PortalModuleRegistryFactory;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class VoteRegistry extends AbstractComponentRegistry {
	public static final String vote = "vote";

	public VoteRegistry(final ServletContext servletContext) {
		super(servletContext);

		PortalModuleRegistryFactory.regist(VotePortalModule.class, "vote",
				LocaleI18n.getMessage("vote.2"), LocaleI18n.getMessage("VoteRegistry.0"),
				getComponentResourceProvider().getResourceHomePath() + "/images/vote.png",
				LocaleI18n.getMessage("VoteRegistry.1"));
	}

	@Override
	public String getComponentName() {
		return vote;
	}

	@Override
	protected Class<? extends AbstractComponentBean> getBeanClass() {
		return VoteBean.class;
	}

	@Override
	protected Class<? extends AbstractComponentRender> getRenderClass() {
		return VoteRender.class;
	}

	@Override
	protected Class<? extends AbstractComponentResourceProvider> getResourceProviderClass() {
		return VoteResourceProvider.class;
	}

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pageParameter,
			final Element component) {
		final VoteBean voteBean = (VoteBean) super.createComponentBean(pageParameter, component);
		return voteBean;
	}
}
