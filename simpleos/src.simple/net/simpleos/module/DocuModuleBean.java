package net.simpleos.module;


/**
 * 交流目录
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-11-29上午11:34:40
 */
public class DocuModuleBean extends AbstractModuleBean {
	public static String name = "docu";

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getTitle() {
		return "文档";
	}

	@Override
	public String getUrl() {
		return "/docu.html";
	}

	@Override
	public boolean isMenu() {
		return true;
	}

	@Override
	public int getOorder() {
		return 40;
	}

}
