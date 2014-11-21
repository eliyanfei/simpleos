package net.simpleos.module;


/**
 * 主页
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-11-29上午11:34:40
 */
public class IndexModuleBean extends AbstractModuleBean {

	@Override
	public String getName() {
		return "index";
	}

	@Override
	public String getTitle() {
		return "首页";
	}

	@Override
	public String getUrl() {
		return "/index.html";
	}

	@Override
	public boolean isMenu() {
		return true;
	}

	@Override
	public int getOorder() {
		return 1;
	}
}
