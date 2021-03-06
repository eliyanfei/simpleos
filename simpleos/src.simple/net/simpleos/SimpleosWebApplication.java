package net.simpleos;

import java.util.Locale;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import net.simpleframework.util.HTTPUtils;
import net.simpleframework.web.AbstractWebApplication;
import net.simpleframework.web.page.PageRequestResponse;
/**
 * 
 * @author 李岩飞 
 * @date 2014年11月19日 下午4:57:15 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public class SimpleosWebApplication extends AbstractWebApplication {

	@Override
	public void init(ServletConfig config) throws ServletException {
		Locale.setDefault(Locale.CHINA);
		super.init(config);
	}

	@Override
	public boolean isSystemUrl(final PageRequestResponse requestResponse) {
		final String requestURI = HTTPUtils.getRequestURI(requestResponse.request);
		if (requestURI.indexOf("/regist.html") > -1 || requestURI.indexOf("/openid.html") > -1) {
			return true;
		}
		return super.isSystemUrl(requestResponse);
	}

	@Override
	public void destroy() {
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.destroy();
	}

	private static final long serialVersionUID = 2617847076944785953L;
}
