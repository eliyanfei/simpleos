package net.prj.core.impl.model;

import net.prj.core.impl.AbstractModelBean;

/**
 * 交流目录
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-11-29上午11:34:40
 */
public class BbsModelBean extends AbstractModelBean {
	public static String name = "bbs";

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getTitle() {
		return "交流";
	}

	@Override
	public String getUrl() {
		return "/bbs.html";
	}

	@Override
	public boolean isMenu() {
		return true;
	}

	@Override
	public int getOorder() {
		return 30;
	}

}
