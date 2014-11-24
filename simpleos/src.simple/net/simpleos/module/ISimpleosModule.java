package net.simpleos.module;

import java.util.List;

import net.simpleframework.web.page.component.ui.portal.module.AbstractPortalModuleHandle;

/**  
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月22日 下午1:57:57 
 *	 
*/
public interface ISimpleosModule extends Comparable<ISimpleosModule> {
	/**
	 * 唯一标示
	 * @return
	 */
	String getModuleName();

	/**
	 * 前端页面菜单的显示名称
	 * @return
	 */
	String getFrontTitle();

	/**
	 * 前端页面html地址
	 * @return
	 */
	String getFrontHtml();

	/**
	 * 获取后台处理的相应的jsp页面
	 * @return
	 */
	String getBackendJsp();

	/**
	 * 获取后端的处理
	 * @return
	 */
	List<SimpleosModuleBean> getBackendActions();

	/**
	 * 显示顺序
	 */
	int getOorder();

	/**
	 * 创建组件
	 * @return
	 */
	AbstractPortalModuleHandle getPortalModuleHandle();
}
