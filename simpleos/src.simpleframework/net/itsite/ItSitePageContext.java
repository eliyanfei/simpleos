package net.itsite;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import net.simpleframework.web.page.PageConfig;
import net.simpleframework.web.page.PageContext;

public class ItSitePageContext extends PageContext {
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
