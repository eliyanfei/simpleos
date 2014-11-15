package net.prj.core.i;

import net.simpleframework.core.Version;

/**
 * 总的模块接口
 * 
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-11-29上午11:22:53
 */
public interface IModelBean {
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
	 * 该功能模块的当前版本
	 * @return
	 */
	Version getVersion();

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
