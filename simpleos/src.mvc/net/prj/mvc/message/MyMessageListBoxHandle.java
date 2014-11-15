package net.prj.mvc.message;

import java.util.ArrayList;
import java.util.Collection;

import net.a.ItSiteUtil;
import net.simpleframework.my.message.MessageUtils;
import net.simpleframework.organization.account.IAccount;
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
public class MyMessageListBoxHandle extends AbstractListboxHandle {

	@Override
	public Collection<ListItem> getListItems(ComponentParameter compParameter) {
		final IAccount account = ItSiteUtil.getLoginAccount(compParameter);
		Collection<ListItem> listItems = new ArrayList<ListItem>();
		if (account == null) {
			return listItems;
		}
		ListboxBean listboxBean = (ListboxBean) compParameter.componentBean;
		ListItem item = new ListItem(listboxBean, "");
		item.setId("system");
		item.setRun("true");
		item.setText(LocaleI18n.getMessage("Message.List.0") + ItSiteUtil.getMessages(compParameter, 1));
		item.setJsClickCallback("$('messageType').value='1';$IT.A('myMessageTable','issend=false&messagetype=1');");
		listItems.add(item);

		if (ItSiteUtil.isManage(compParameter, MessageUtils.applicationModule)) {
			item = new ListItem(listboxBean, "");
			item.setId("complaint");
			item.setText(LocaleI18n.getMessage("Message.List.4") + ItSiteUtil.getMessages(compParameter, 4));
			item.setJsClickCallback("$('messageType').value='4';$IT.A('myMessageTable','issend=false&messagetype=4');");
			listItems.add(item);
		}

		item = new ListItem(listboxBean, "");
		item.setId("notification");
		item.setText(LocaleI18n.getMessage("Message.List.1") + ItSiteUtil.getMessages(compParameter, 2));
		item.setJsClickCallback("$('messageType').value='2';$IT.A('myMessageTable','issend=false&messagetype=2');");
		listItems.add(item);

		item = new ListItem(listboxBean, "");
		item.setId("private");
		item.setText(LocaleI18n.getMessage("Message.List.2") + ItSiteUtil.getMessages(compParameter, 0));
		item.setJsClickCallback("$('messageType').value='0';$IT.A('myMessageTable','issend=false&messagetype=0');");
		listItems.add(item);

		item = new ListItem(listboxBean, "");
		item.setId("send");
		item.setText(LocaleI18n.getMessage("Message.List.3"));
		item.setSub(true);
		item.setJsClickCallback("$('messageType').value='0';$IT.A('myMessageTable','issend=true&messagetype=0');");
		listItems.add(item);

		item = new ListItem(listboxBean, "");
		item.setId("dialog");
		item.setText("对话" + ItSiteUtil.getDialog(compParameter));
		item.setJsClickCallback("$IT.A('myDialogAjax1','');");
		listItems.add(item);
		return listItems;
	}

}
