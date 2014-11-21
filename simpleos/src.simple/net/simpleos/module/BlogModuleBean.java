package net.simpleos.module;


/**
 * 博客列表
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-11-29上午11:34:40
 */
public class BlogModuleBean extends AbstractModuleBean {

	public static String name = "blog";

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getTitle() {
		return "博客";
	}

	@Override
	public String getUrl() {
		return "/blog.html";
	}

	@Override
	public boolean isMenu() {
		return true;
	}

	@Override
	public int getOorder() {
		return 20;
	}
}
