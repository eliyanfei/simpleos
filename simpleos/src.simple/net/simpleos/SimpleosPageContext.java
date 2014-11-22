package net.simpleos;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import net.simpleframework.web.page.PageConfig;
import net.simpleframework.web.page.PageContext;
/**
 * 
 * @author 李岩飞 
 * @date 2014年11月19日 下午4:57:38 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public class SimpleosPageContext extends PageContext {
	@Override
	public void doInit(ServletContext servletContext) throws IOException {
		super.doInit(servletContext);
	}

	@Override
	protected PageConfig createPageConfig() {
		return new PageConfig(this) {
			@Override
			public String getPagePath() {
				return "/demo";
			}

			private final Map<String, String> packages = new HashMap<String, String>();

			@Override
			public Map<String, String> getPagePackages() {
				return packages;
			}
		};
	}
}
