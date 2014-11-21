package net.simpleframework.example;

import java.util.HashMap;
import java.util.Map;

import net.simpleframework.web.page.PageConfig;
import net.simpleframework.web.page.PageContext;

public class MyPageContext extends PageContext {
	@Override
	protected PageConfig createPageConfig() {
		return new PageConfig(this) {
			@Override
			public String getPagePath() {
				return "/demo";
			}

			private final Map<String, String> packages = new HashMap<String, String>();
			{
				packages.put("/", "net.simpleframework");
			}

			@Override
			public Map<String, String> getPagePackages() {
				return packages;
			}
		};
	}
}
