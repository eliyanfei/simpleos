package net.simpleframework.organization.component.userselect;

import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.IComponentHandle;
import net.simpleframework.web.page.component.IComponentRegistry;
import net.simpleframework.web.page.component.ui.dictionary.DictionaryBean;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class UserSelectBean extends DictionaryBean {

	private boolean showDepartmentFilter = true;

	private boolean showCheckbox = true;

	private int pageItems;

	public UserSelectBean(final IComponentRegistry componentRegistry,
			final PageDocument pageDocument, final Element element) {
		super(componentRegistry, pageDocument, element);
		setShowHelpTooltip(false);
		setTitle(LocaleI18n.getMessage("UserSelectBean.0"));
		setMinWidth(360);
		setWidth(360);
		setHeight(445);
	}

	public boolean isShowDepartmentFilter() {
		return showDepartmentFilter;
	}

	public void setShowDepartmentFilter(final boolean showDepartmentFilter) {
		this.showDepartmentFilter = showDepartmentFilter;
	}

	public boolean isShowCheckbox() {
		return showCheckbox;
	}

	public void setShowCheckbox(final boolean showCheckbox) {
		this.showCheckbox = showCheckbox;
	}

	public int getPageItems() {
		return pageItems;
	}

	public void setPageItems(final int pageItems) {
		this.pageItems = pageItems;
	}

	@Override
	protected Class<? extends IComponentHandle> getDefaultHandleClass() {
		return DefaultUserSelectHandle.class;
	}
}
