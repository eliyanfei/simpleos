package net.prj.core.impl.model;

import net.prj.core.impl.AbstractModelBean;
import net.simpleframework.core.Version;

/**
 * 登入
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-11-29上午11:34:40
 */
public class LoginModelBean extends AbstractModelBean {

	@Override
	public String getName() {
		return "login";
	}

	@Override
	public String getTitle() {
		return "登入";
	}

	@Override
	public Version getVersion() {
		return Version.valueOf("1.0.0");
	}

	@Override
	public String getUrl() {
		return "/login.html";
	}

	@Override
	public boolean isMenu() {
		return false;
	}
}
