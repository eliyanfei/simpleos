package net.simpleframework.web.page.component.ui.listbox;

import java.util.Collection;
import java.util.Map;

import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.IComponentHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IListboxHandle extends IComponentHandle {

	Collection<ListItem> getListItems(ComponentParameter compParameter);

	Map<String, Object> getListItemAttributes(ComponentParameter compParameter, ListItem listItem);
}
