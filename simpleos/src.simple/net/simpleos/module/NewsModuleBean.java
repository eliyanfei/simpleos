package net.simpleos.module;


/**
 * 新闻列表
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-11-29上午11:34:40
 */
public class NewsModuleBean extends AbstractModuleBean {

	public static String name = "news";

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getTitle() {
		return "新闻";
	}

	@Override
	public String getUrl() {
		return "/news.html";
	}

	@Override
	public boolean isMenu() {
		return true;
	}

	@Override
	public int getOorder() {
		return 10;
	}
}
