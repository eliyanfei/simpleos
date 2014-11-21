package net.simpleos.module;


/**
 * 个人信息
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-11-29上午11:34:40
 */
public class MyInfoModuleBean extends AbstractModuleBean {
	@Override
	public String getName() {
		return "my";
	}

	@Override
	public String getTitle() {
		return "用户中心";
	}

	@Override
	public String getUrl() {
		return "/my.html";
	}

	@Override
	public boolean isMenu() {
		return false;
	}

}
