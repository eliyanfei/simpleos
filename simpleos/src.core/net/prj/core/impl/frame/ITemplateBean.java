package net.prj.core.impl.frame;

/**
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-5上午09:07:34
 */
public interface ITemplateBean {
	/**
	 * 获取标示
	 * @return
	 */
	String getName();

	/**
	 * 获取名称
	 * @return
	 */
	String getTitle();

	/**
	 * 是否是宽屏显示
	 * @return
	 */
	boolean isFullScreen();

	/**
	 * 是否设计阶段
	 * @return
	 */
	boolean isDesign();

}
