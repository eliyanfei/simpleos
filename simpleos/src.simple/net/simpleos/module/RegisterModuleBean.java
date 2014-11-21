package net.simpleos.module;


/**
 * 登入
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-11-29上午11:34:40
 */
public class RegisterModuleBean extends AbstractModuleBean {

	@Override
	public String getName() {
		return "register";
	}

	@Override
	public String getTitle() {
		return "注册";
	}

	@Override
	public String getUrl() {
		return "/register.html";
	}

	@Override
	public boolean isMenu() {
		return false;
	}
}
