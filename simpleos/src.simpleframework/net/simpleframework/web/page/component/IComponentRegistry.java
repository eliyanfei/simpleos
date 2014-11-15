package net.simpleframework.web.page.component;

import javax.servlet.ServletContext;

import net.simpleframework.web.page.IPageResourceProvider;
import net.simpleframework.web.page.PageParameter;

import org.dom4j.Element;

/**
 * 提供给开发者开发自定义组件组件接口
 * 
 * 
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IComponentRegistry {
	ServletContext getServletContext();

	/**
	 * 组件的名称，这个值就是在XML描述中声明的组件标签，且必须唯一
	 */
	String getComponentName();

	/**
	 * 组件的部署名称
	 */
	String getComponentDeploymentName();

	/**
	 * 获取组件的渲染器实例
	 */
	IComponentRender getComponentRender();

	/**
	 * 获取组件的资源提供者实例
	 */
	IComponentResourceProvider getComponentResourceProvider();

	/**
	 * 创建组件的元信息定义实例,组件的元信息来自XML描述文件， 该实例将按XML中的定义来初始化Bean的属性
	 */
	AbstractComponentBean createComponentBean(PageParameter pageParameter, Element component);

	/**
	 * 获得页面资源
	 */
	IPageResourceProvider getPageResourceProvider();
}
