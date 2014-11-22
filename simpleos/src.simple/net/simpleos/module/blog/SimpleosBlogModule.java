package net.simpleos.module.blog;

import net.simpleos.module.ASimpleosModule;

/**  
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月22日 下午2:13:05 
 *	 
*/
public class SimpleosBlogModule extends ASimpleosModule {

	@Override
	public String getFrontHtml() {
		return "/blog.html";
	}

	@Override
	public String getFrontTitle() {
		return "博客";
	}

	@Override
	public String getModuleName() {
		return "blog";
	}

	@Override
	public int getOorder() {
		return 200;
	}

}
