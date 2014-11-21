package net.simpleframework.organization.component.jobselect;

import javax.servlet.ServletContext;

import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentResourceProvider;
import net.simpleframework.web.page.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.web.page.component.ui.dictionary.DictionaryRegistry;
import net.simpleframework.web.page.component.ui.dictionary.DictionaryUtils;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class JobSelectRegistry extends DictionaryRegistry {
	public static final String jobSelect = "jobSelect";

	public JobSelectRegistry(final ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public String getComponentName() {
		return jobSelect;
	}

	@Override
	protected Class<? extends AbstractComponentBean> getBeanClass() {
		return JobSelectBean.class;
	}

	@Override
	protected Class<? extends AbstractComponentResourceProvider> getResourceProviderClass() {
		return JobSelectResourceProvider.class;
	}

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pageParameter,
			final Element component) {
		final JobSelectBean jobSelect = (JobSelectBean) super.createComponentBean(pageParameter,
				component);

		final AjaxRequestBean ajaxRequest = DictionaryUtils.createAjaxRequest(jobSelect);
		final StringBuilder sb = new StringBuilder();
		sb.append(getComponentResourceProvider().getResourceHomePath());
		sb.append("/jsp/job_select.jsp?").append(JobSelectUtils.BEAN_ID).append("=")
				.append(jobSelect.hashId());
		ajaxRequest.setUrlForward(sb.toString());
		return jobSelect;
	}
}
