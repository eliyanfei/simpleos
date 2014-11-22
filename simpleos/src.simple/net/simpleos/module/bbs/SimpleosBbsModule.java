package net.simpleos.module.bbs;

import net.simpleos.module.ASimpleosModule;

/**  
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月22日 下午2:13:05 
 *	 
*/
public class SimpleosBbsModule extends ASimpleosModule {

	@Override
	public String getFrontHtml() {
		return "/bbs.html";
	}

	@Override
	public String getFrontTitle() {
		return "论坛";
	}

	@Override
	public String getModuleName() {
		return "bbs";
	}

	@Override
	public int getOorder() {
		return 300;
	}

}
