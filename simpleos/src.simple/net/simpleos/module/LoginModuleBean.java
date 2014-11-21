package net.simpleos.module;


/**
 * 登入
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-11-29上午11:34:40
 */
public class LoginModuleBean extends AbstractModuleBean {

	@Override
	public String getName() {
		return "login";
	}

	@Override
	public String getTitle() {
		return "登入";
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
