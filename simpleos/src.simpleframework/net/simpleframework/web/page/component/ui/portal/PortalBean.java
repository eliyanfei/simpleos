package net.simpleframework.web.page.component.ui.portal;

import java.util.ArrayList;

import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.AbstractContainerBean;
import net.simpleframework.web.page.component.IComponentHandle;
import net.simpleframework.web.page.component.IComponentRegistry;
import net.simpleframework.web.page.component.ui.portal.module.PortalModuleRegistryFactory;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PortalBean extends AbstractContainerBean {

	private ArrayList<ColumnBean> columns;

	private boolean draggable = false;

	private boolean showMenu = false; // 无组织机构依赖时

	private String autoPagelet = PortalModuleRegistryFactory.DEFAULT_MODULE_NAME;

	private String jobManager;

	public PortalBean(final IComponentRegistry componentRegistry, final PageDocument pageDocument,
			final Element element) {
		super(componentRegistry, pageDocument, element);
	}

	public ArrayList<ColumnBean> getColumns() {
		if (columns == null) {
			columns = new ArrayList<ColumnBean>();
		}
		return columns;
	}

	public boolean isDraggable() {
		return draggable;
	}

	public void setDraggable(final boolean draggable) {
		this.draggable = draggable;
	}

	public boolean isShowMenu() {
		return showMenu;
	}

	public void setShowMenu(final boolean showMenu) {
		this.showMenu = showMenu;
	}

	@Override
	public void syncElement() {
		setElementAttribute("draggable", isDraggable());
		setElementAttribute("showMenu", isShowMenu());
		setElementAttribute("autoPagelet", getAutoPagelet());
		removeChildren("column");
		for (final ColumnBean column : getColumns()) {
			column.syncElement();
			addElement(column);
		}
	}

	@Override
	protected Class<? extends IComponentHandle> getDefaultHandleClass() {
		return DefaultPortalHandle.class;
	}

	public String getAutoPagelet() {
		return autoPagelet;
	}

	public void setAutoPagelet(final String autoPagelet) {
		this.autoPagelet = autoPagelet;
	}

	public String getJobManager() {
		return jobManager;
	}

	public void setJobManager(final String jobManager) {
		this.jobManager = jobManager;
	}
}
