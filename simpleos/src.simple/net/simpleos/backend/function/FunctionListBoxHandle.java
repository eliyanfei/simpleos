package net.simpleos.backend.function;

import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.listbox.AbstractListboxHandle;
import net.simpleframework.web.page.component.ui.listbox.ListItem;
import net.simpleframework.web.page.component.ui.listbox.ListboxBean;
import net.simpleos.module.ISimpleosModule;
import net.simpleos.module.SimpleosModuleBean;
import net.simpleos.module.SimpleosModuleUtils;

/**
 * 
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-3上午10:16:00
 * 
 * 功能模块菜单
 */
public class FunctionListBoxHandle extends AbstractListboxHandle {

	public Collection<ListItem> getListItems(ComponentParameter compParameter) {
		ListboxBean listboxBean = (ListboxBean) compParameter.componentBean;
		Collection<ListItem> listItems = new ArrayList<ListItem>();
		boolean run = true;
		for (final ISimpleosModule module : SimpleosModuleUtils.moduleMap.values()) {
			for (final SimpleosModuleBean moduleBean : module.getBackendActions()) {
				ListItem item = new ListItem(listboxBean, "");
				item.setId(moduleBean.name);
				item.setText(moduleBean.text);
				item.setJsClickCallback(moduleBean.action);
				item.setRun(String.valueOf(run));
				run = false;
				listItems.add(item);
				for (final SimpleosModuleBean subModuleBean : moduleBean.moduleList) {
					item = new ListItem(listboxBean, "");
					item.setSub(true);
					item.setId(subModuleBean.name);
					item.setText(subModuleBean.text);
					item.setJsClickCallback(subModuleBean.action);
					listItems.add(item);
				}
			}
		}
		return listItems;
	}

}
