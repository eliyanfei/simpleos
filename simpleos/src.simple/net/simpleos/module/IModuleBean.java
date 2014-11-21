package net.simpleos.module;

import net.simpleframework.core.Version;

/**
 * 总的模块接口
 * 
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-11-29上午11:22:53
 */
public interface IModuleBean {
	/**
	 * 唯一ID，不能重复
	 * @return
	 */
	String getName();

	/**
	 * 中文名称,描述
	 * @return
	 */
	String getTitle();

	/**
	 * 是否是设计阶段
	 * @return
	 */
	boolean isDesign();

	/**
	 * 访问地址
	 * @return
	 */
	String getUrl();

	/**
	 * 是否在菜单里面
	 * @return
	 */
	boolean isMenu();

	/**
	 * 排序方式
	 * @return
	 */
	int getOorder();
}
