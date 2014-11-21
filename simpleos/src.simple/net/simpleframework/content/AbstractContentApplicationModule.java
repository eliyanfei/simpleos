package net.simpleframework.content;

import java.util.Map;

import net.itsite.impl.AItSiteAppclicationModule;
import net.simpleframework.applets.attention.ISentCallback;
import net.simpleframework.content.component.newspager.NewsBean;
import net.simpleframework.organization.EJobType;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.IJobChart;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.IoUtils;
import net.simpleframework.util.script.ScriptEvalUtils;
import net.simpleframework.web.AbstractWebApplicationModule;
import net.simpleframework.web.WebApplicationConfig;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.HandleException;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractContentApplicationModule extends AbstractWebApplicationModule implements IContentApplicationModule {

	// 创建管理员角色
	protected void createManagerJob(final String manager, final String msanagerText) {
		IJob job = OrgUtils.jm().getJobByName(manager);
		if (job == null) {
			final IJobChart jc = OrgUtils.jcm().getJobChartByName(IJobChart.sysjc);
			if (jc != null) {
				job = OrgUtils.jm().createJob(jc, manager, msanagerText);
				job.setJobType(EJobType.normal);
				OrgUtils.jm().insert(job);
			}
		}
	}

	@Override
	public String getCatalogIdName(final PageRequestResponse requestResponse) {
		return "catalog";
	}

	private String pagerHandleClass;

	@Override
	public String getPagerHandleClass() {
		return pagerHandleClass;
	}

	public void setPagerHandleClass(final String pagerHandleClass) {
		this.pagerHandleClass = pagerHandleClass;
	}

	@Override
	public String tabs2(final PageRequestResponse requestResponse) {
		return null;
	}

	protected abstract class AttentionContentMail implements ISentCallback {
		private final ComponentParameter compParameter;

		public final NewsBean newsBean;

		protected AttentionContentMail(final ComponentParameter compParameter, final NewsBean attentionsBean) {
			this.compParameter = compParameter;
			this.newsBean = attentionsBean;
		}

		@Override
		public Object getId() {
			return newsBean.getId();
		}

		@Override
		public boolean isSent(final IUser toUser) {
			return true;
		}

		protected String linkUrl(final String url) {
			final StringBuilder sb = new StringBuilder();
			final String href = ((WebApplicationConfig) PageUtils.pageContext.getApplication().getApplicationConfig()).getServerUrl()
					+ compParameter.wrapContextPath(url);
			sb.append("<a target=\"_blank\" href=\"").append(href).append("\">");
			sb.append(href).append("</a>");
			return sb.toString();
		}

		protected String getFromTemplate(final Map<String, Object> variable, final Class<?> templateClazz) {
			try {
				final String readerString = IoUtils.getStringFromInputStream(AItSiteAppclicationModule.class.getClassLoader().getResourceAsStream(
						BeanUtils.getResourceClasspath(AItSiteAppclicationModule.class, "attention.html")));
				return ScriptEvalUtils.replaceExpr(variable, readerString);
			} catch (final Exception e) {
				throw HandleException.wrapException(e);
			}
		}
	}
}
