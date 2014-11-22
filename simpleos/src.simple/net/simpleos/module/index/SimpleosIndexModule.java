package net.simpleos.module.index;

import net.simpleos.module.ASimpleosModule;

/**  
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月22日 下午2:13:05 
 *	 
*/
public class SimpleosIndexModule extends ASimpleosModule {

	@Override
	public String getFrontHtml() {
		return "/index.html";
	}

	@Override
	public String getFrontTitle() {
		return "首页";
	}

	@Override
	public String getModuleName() {
		return "index";
	}

	@Override
	public int getOorder() {
		return 1;
	}

}
