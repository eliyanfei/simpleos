package net.simpleframework.web;

import net.simpleframework.core.ApplicationConfig;
import net.simpleframework.util.StringUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class WebApplicationConfig extends ApplicationConfig {
	private String serverUrl;

	public String getServerUrl() {
		return StringUtils.text(serverUrl, "/");
	}

	public void setServerUrl(final String serverUrl) {
		this.serverUrl = serverUrl;
	}
}
