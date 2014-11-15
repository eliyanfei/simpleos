package net.simpleframework.web.page;

import java.util.List;
import java.util.Map;

import net.simpleframework.core.IApplicationAware;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IPageHandle extends IApplicationAware {

	Object getBeanProperty(PageParameter pageParameter, String beanProperty);

	void pageLoad(PageParameter pageParameter, Map<String, Object> dataBinding,
			List<String> visibleToggleSelector, List<String> readonlySelector,
			List<String> disabledSelector);

	void documentInit(PageParameter pageParameter);
}
