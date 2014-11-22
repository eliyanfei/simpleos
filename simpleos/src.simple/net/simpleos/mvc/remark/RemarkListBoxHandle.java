package net.simpleos.mvc.remark;

import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.listbox.AbstractListboxHandle;
import net.simpleframework.web.page.component.ui.listbox.ListItem;
import net.simpleframework.web.page.component.ui.listbox.ListboxBean;

/**
 * 
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-3上午10:16:00
 */
public class RemarkListBoxHandle extends AbstractListboxHandle {
	@Override
	public Collection<ListItem> getListItems(ComponentParameter compParameter) {
		ListboxBean listboxBean = (ListboxBean) compParameter.componentBean;
		Collection<ListItem> listItems = new ArrayList<ListItem>();
		ListItem item = new ListItem(listboxBean, "");
		item.setId("news");
		item.setRun("true");
		item.setText(LocaleI18n.getMessage("EFunctionModule.2"));
		item.setJsClickCallback("$IT.A('remarkNews','type=news');");
		listItems.add(item);

		item = new ListItem(listboxBean, "");
		item.setId("blog");
		item.setText(LocaleI18n.getMessage("EFunctionModule.1"));
		item.setJsClickCallback("$IT.A('remarkBlog','type=blog');");
		listItems.add(item);

		item = new ListItem(listboxBean, "");
		item.setId("bbs");
		item.setText(LocaleI18n.getMessage("EFunctionModule.0"));
		item.setJsClickCallback("$IT.A('remarkBbs','type=bbs');");
		listItems.add(item);
		
		item = new ListItem(listboxBean, "");
		item.setId("docu");
		item.setText("文档");
		item.setJsClickCallback("$IT.A('remarkDocu','type=docu');");
		listItems.add(item);

		return listItems;
	}

}
