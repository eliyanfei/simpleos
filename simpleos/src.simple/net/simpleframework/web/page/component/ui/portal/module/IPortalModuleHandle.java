package net.simpleframework.web.page.component.ui.portal.module;

import java.util.Map;

import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.portal.PageletBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IPortalModuleHandle {
	PortalModule getModuleBean();

	PageletBean getPagelet();

	IForward getPageletContent(ComponentParameter compParameter);

	/** ----- options ----- **/

	IForward getPageletOptionContent(ComponentParameter compParameter);

	void optionSave(ComponentParameter compParameter);

	void optionLoaded(PageParameter pageParameter, final Map<String, Object> dataBinding);

	/** ----- options ui ----- **/

	String getOptionUITitle(ComponentParameter compParameter);

	OptionWindowUI getPageletOptionUI(ComponentParameter compParameter);

	/**
	 * 唯一ID
	 * @return
	 */
	String getName();

	/**
	 * 显示名称
	 * @return
	 */
	String getText();

	/**
	 * 目录
	 * @return
	 */
	String getCatalog();

	/**
	 * 图片
	 */
	String getIcon();

	/**
	 * 描述
	 */
	String getDescription();
}
