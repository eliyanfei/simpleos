package net.simpleframework.sysmgr.dict.component.dictselect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import net.simpleframework.sysmgr.dict.SysDict;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.listbox.AbstractListboxHandle;
import net.simpleframework.web.page.component.ui.listbox.ListItem;
import net.simpleframework.web.page.component.ui.listbox.ListboxBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DictListbox extends AbstractListboxHandle {

	@Override
	public Collection<ListItem> getListItems(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = ComponentParameter.get(compParameter,
				(DictSelectBean) compParameter.componentBean.getAttribute("__dictSelect"));
		final IDictSelectHandle dHandle = (IDictSelectHandle) nComponentParameter
				.getComponentHandle();
		final Collection<SysDict> dictItems = dHandle.getDictItems(nComponentParameter, null);
		if (dictItems != null) {
			final ArrayList<ListItem> al = new ArrayList<ListItem>();
			for (final SysDict dictItem : dictItems) {
				final ListItem listItem = new ListItem((ListboxBean) compParameter.componentBean,
						dictItem);
				listItem.setId(dictItem.getName());
				al.add(listItem);
			}
			return al;
		} else {
			return null;
		}
	}

	@Override
	public Map<String, Object> getListItemAttributes(final ComponentParameter compParameter,
			final ListItem listItem) {
		final ComponentParameter nComponentParameter = ComponentParameter.get(compParameter,
				(DictSelectBean) compParameter.componentBean.getAttribute("__dictSelect"));
		final IDictSelectHandle dHandle = (IDictSelectHandle) nComponentParameter
				.getComponentHandle();
		return dHandle.getDictItemAttributes(nComponentParameter, (SysDict) listItem.getDataObject());
	}
}
