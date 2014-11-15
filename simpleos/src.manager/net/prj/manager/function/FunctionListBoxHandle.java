package net.prj.manager.function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.itsite.document.docu.DocuUtils;
import net.itsite.document.docu.EDocuStatus;
import net.prj.core.i.IModelBean;
import net.prj.mvc.news.MVCNewsUtils;
import net.simpleframework.content.EContentStatus;
import net.simpleframework.content.EContentType;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
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
public class FunctionListBoxHandle extends AbstractListboxHandle {

	static List<IModelBean> modelList = new ArrayList<IModelBean>();

	@Override
	public Collection<ListItem> getListItems(ComponentParameter compParameter) {
		ListboxBean listboxBean = (ListboxBean) compParameter.componentBean;
		String type = compParameter.getParameter("type");
		String op = compParameter.getParameter("op");
		Collection<ListItem> listItems = new ArrayList<ListItem>();
		ListItem item = new ListItem(listboxBean, "");
		item.setId("news");
		if (StringUtils.hasText(type) && "news".equals(type)) {
			item.setRun("true");
		} else {
			item.setRun("true");
		}
		item.setText(LocaleI18n.getMessage("EFunctionModule.2"));
		item.setJsClickCallback("$IT.A('functionNews','type=news&op=" + op + "');");
		listItems.add(item);

		item = new ListItem(listboxBean, "");
		item.setId("news_audit");
		item.setText(LocaleI18n.getMessage("EFunctionModule.7") + MVCNewsUtils.getAuditNews(compParameter, 0));
		item.setSub(true);
		item.setJsClickCallback("$IT.A('functionNews','status=" + EContentStatus.audit.name() + "');");
		listItems.add(item);

		item = new ListItem(listboxBean, "");
		item.setId("news_recommended");
		item.setText(LocaleI18n.getMessage("EFunctionModule.8"));
		item.setSub(true);
		item.setJsClickCallback("$IT.A('functionNews','status=" + EContentStatus.publish.name() + "&t=" + EContentType.recommended.name() + "');");
		listItems.add(item);

		item = new ListItem(listboxBean, "");
		item.setId("blog");
		if (StringUtils.hasText(type) && "blog".equals(type)) {
			item.setRun("true");
		}
		item.setText(LocaleI18n.getMessage("EFunctionModule.1"));
		item.setJsClickCallback("$IT.A('functionBlog','type=blog');");
		listItems.add(item);

		item = new ListItem(listboxBean, "");
		item.setId("blog_recommended");
		item.setText(LocaleI18n.getMessage("EFunctionModule.8"));
		item.setSub(true);
		item.setJsClickCallback("$IT.A('functionBlog','t=" + EContentType.recommended.name() + "');");
		listItems.add(item);

		item = new ListItem(listboxBean, "");
		item.setId("bbs");
		if (StringUtils.hasText(type) && "bbs".equals(type)) {
			item.setRun("true");
		}
		item.setText(LocaleI18n.getMessage("EFunctionModule.0"));
		item.setJsClickCallback("$IT.A('functionBbs','type=bbs&op_act=" + op + "');");
		listItems.add(item);

		item = new ListItem(listboxBean, "");
		item.setId("bbs_star");
		item.setText(LocaleI18n.getMessage("EFunctionModule.9"));
		item.setSub(true);
		item.setJsClickCallback("$IT.A('functionBbs','star=-1');");
		listItems.add(item);

		item = new ListItem(listboxBean, "");
		item.setId("docu");
		if (StringUtils.hasText(type) && "docu".equals(type)) {
			item.setRun("true");
		}
		item.setText("文档");
		item.setJsClickCallback("$IT.A('functionDocu','type=docu');");
		listItems.add(item);

		item = new ListItem(listboxBean, "");
		item.setId("docu_audit");
		item.setText(LocaleI18n.getMessage("EFunctionModule.7") + DocuUtils.getAuditDocus(compParameter, 0));
		item.setSub(true);
		item.setJsClickCallback("$IT.A('functionDocu','status=" + EDocuStatus.audit.name() + "');");
		listItems.add(item);

		return listItems;
	}

}
