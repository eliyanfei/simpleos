package net.simpleframework.sysmgr.dict.component.dictselect;

import java.util.Collection;
import java.util.Map;

import net.simpleframework.sysmgr.dict.SysDict;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.dictionary.IDictionaryHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IDictSelectHandle extends IDictionaryHandle {

	Collection<SysDict> getDictItems(ComponentParameter compParameter, SysDict parent);

	Map<String, Object> getDictItemAttributes(ComponentParameter compParameter, SysDict dictItem);
}
