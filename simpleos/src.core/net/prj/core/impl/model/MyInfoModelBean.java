package net.prj.core.impl.model;

import net.prj.core.impl.AbstractModelBean;

/**
 * 个人信息
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-11-29上午11:34:40
 */
public class MyInfoModelBean extends AbstractModelBean {
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
