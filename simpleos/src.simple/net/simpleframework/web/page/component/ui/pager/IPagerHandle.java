package net.simpleframework.web.page.component.ui.pager;

import java.util.Map;

import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.IComponentHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IPagerHandle extends IComponentHandle {

	static final String PAGER_LIST = "__pager_list";

	void process(ComponentParameter compParameter, int start);

	int getCount(ComponentParameter compParameter);

	IDataObjectQuery<?> createDataObjectQuery(ComponentParameter compParameter);

	/********************************* utils **********************************/

	String getPagerUrl(ComponentParameter compParameter, EPagerPosition pagerPosition,
			int pageItems, final Map<String, Integer> pageVar);
}
