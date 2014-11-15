package net.simpleframework.applets.notification.component.window;

import javax.servlet.ServletContext;

import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.AbstractComponentRender;
import net.simpleframework.web.page.component.AbstractComponentResourceProvider;
import net.simpleframework.web.page.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.web.page.component.ui.window.WindowRegistry;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MessageWindowRegistry extends WindowRegistry {
	public static final String messageWindow = "messageWindow";

	public MessageWindowRegistry(final ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public String getComponentName() {
		return messageWindow;
	}

	@Override
	protected Class<? extends AbstractComponentBean> getBeanClass() {
		return MessageWindowBean.class;
	}

	@Override
	protected Class<? extends AbstractComponentRender> getRenderClass() {
		return MessageWindowRender.class;
	}

	@Override
	protected Class<? extends AbstractComponentResourceProvider> getResourceProviderClass() {
		return MessageWindowResourceProvider.class;
	}

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pageParameter,
			final Element component) {
		final MessageWindowBean messageWindowBean = (MessageWindowBean) super.createComponentBean(
				pageParameter, component);

		final PageDocument pageDocument = messageWindowBean.getPageDocument();
		final String beanId = messageWindowBean.hashId();
		if (!StringUtils.hasText(messageWindowBean.getContent())
				&& !StringUtils.hasText(messageWindowBean.getContentRef())) {
			final String ajaxRequestName2 = "ajaxRequest2_" + beanId;
			final AjaxRequestBean ajaxRequestBean2 = new AjaxRequestBean(pageDocument, null);
			ajaxRequestBean2.setName(ajaxRequestName2);
			ajaxRequestBean2.setShowLoading(false);
			ajaxRequestBean2.setUrlForward(getComponentResourceProvider().getResourceHomePath()
					+ "/jsp/message_window.jsp?" + MessageWindowUtils.messageWindowId + "=" + beanId);
			pageDocument.addComponentBean(ajaxRequestBean2);
			messageWindowBean.setContent(AbstractComponentRegistry.getLoadingContent());
			messageWindowBean.setContentRef(ajaxRequestName2);
		}

		final String ajaxRequestName = "ajaxRequest_" + beanId;
		final AjaxRequestBean ajaxRequestBean = new AjaxRequestBean(pageDocument, null);
		ajaxRequestBean.setName(ajaxRequestName);
		ajaxRequestBean.setShowLoading(false);
		ajaxRequestBean.setHandleClass(MessageAction.class.getName());
		ajaxRequestBean.setParameters(MessageWindowUtils.messageWindowId + "=" + beanId);
		ajaxRequestBean.setJsCompleteCallback("$Actions['" + messageWindowBean.getName()
				+ "'].ajaxRequestCallback(json);");
		pageDocument.addComponentBean(ajaxRequestBean);
		return messageWindowBean;
	}
}
