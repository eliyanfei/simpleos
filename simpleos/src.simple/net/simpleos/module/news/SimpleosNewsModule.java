package net.simpleos.module.news;

import net.simpleos.module.ASimpleosModule;

/**  
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月22日 下午2:13:05 
 *	 
*/
public class SimpleosNewsModule extends ASimpleosModule {

	@Override
	public String getFrontHtml() {
		return "/news.html";
	}

	@Override
	public String getFrontTitle() {
		return "新闻";
	}

	@Override
	public String getModuleName() {
		return "news";
	}

	@Override
	public int getOorder() {
		return 100;
	}

}
