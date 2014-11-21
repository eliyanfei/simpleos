package net.simpleframework.organization;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpSession;

import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.AccountSession.IJobProperty;
import net.simpleframework.util.AlgorithmUtils;
import net.simpleframework.util.HTTPUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.IWebApplication;
import net.simpleframework.web.page.IFilterListener;
import net.simpleframework.web.page.IPageConstants;
import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.PageUtils;
import net.simpleos.configmgr.SimpleosConfigMgr;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class JobFilterListener implements IFilterListener {

	public static void main(String[] args) {
		String str = "simpleos.net/base.properties.xml";
		Pattern pattern = Pattern.compile(".*(.xml*)");
		Matcher matcher = pattern.matcher(str);

		System.out.println(matcher.matches());
	}

	@Override
	public boolean doFilter(final PageParameter pageParameter, final FilterChain filterChain) throws IOException {
		final HttpSession httpSession = pageParameter.getSession();

		final String url = HTTPUtils.getRequestURI(pageParameter.request);
		if (!IWebApplication.Instance.getApplication().isOK() && !url.endsWith("ajax_request.jsp")) {
			if (!url.contains("db_config.jsp") && !url.contains("db.html")) {
				pageParameter.loc("/db.html");
				return false;
			}
		}
		/**
		 * 过滤所有的XML文件，不能显示
		 */
		try {
			Pattern pattern = Pattern.compile(".*(.xml*)");
			Matcher matcher = pattern.matcher(url);
			if (matcher.matches()) {
				pageParameter.loc("/index.html");
				return false;
			}
		} catch (Exception e) {
		}

		if (httpSession.getAttribute(IPageConstants.THROWABLE) != null) {
			return true;
		}
		final PageDocument pageDocument = pageParameter.getPageDocument();
		final String jobView = pageDocument == null ? null : (String) pageParameter.getPageHandle().getBeanProperty(pageParameter, "jobView");
		final String redirectUrl = AccountSession.getLoginRedirectUrl(pageParameter, new IJobProperty() {
			@Override
			public String getJobProperty() {
				if (!StringUtils.hasText(jobView)) {
					final PageDocument blpd = PageUtils.getBrowserLocationPageDocument(httpSession);
					if (blpd != null && !blpd.equals(pageDocument)) {
						return (String) PageParameter.get(pageParameter, blpd).getBeanProperty("jobView");
					}
				}
				return jobView;
			}
		});
		if (StringUtils.hasText(redirectUrl)) {
			pageParameter.loc(redirectUrl);
			return false;
		}
		if (StringUtils.hasText(jobView)) {
			if (!OrgUtils.isMember(jobView, httpSession)) {
				pageParameter.loc(OrgUtils.deployPath + "jsp/job_http_access.jsp?v="
						+ AlgorithmUtils.base64Encode(HTTPUtils.getRequestAndQueryStringUrl(pageParameter.request).getBytes()) + "&job=" + jobView);
				return false;
			}
		}
		return true;
	}
}
