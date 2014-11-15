package net.simpleframework.web.page.component.ui.tree;

import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.AbstractContainerBean;
import net.simpleframework.web.page.component.IComponentRegistry;
import net.simpleframework.web.page.component.ui.menu.MenuItem;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractTreeBean extends AbstractContainerBean {
	private boolean dynamicLoading;

	private ETreeLineStyle lineStyle;

	private String contextMenu;

	private boolean checkboxes;

	private boolean checkboxesThreeState;

	private boolean cookies = true;

	// event
	private String jsLoadedCallback;

	private String jsCheckCallback;

	private String jsClickCallback, jsDblclickCallback;

	// job

	private String jobDrop;

	public AbstractTreeBean(final IComponentRegistry componentRegistry, final PageDocument pageDocument, final Element element) {
		super(componentRegistry, pageDocument, element);
	}

	public boolean isDynamicLoading() {
		return dynamicLoading;
	}

	public void setDynamicLoading(final boolean dynamicLoading) {
		this.dynamicLoading = dynamicLoading;
	}

	public String getContextMenu() {
		return contextMenu;
	}

	public void setContextMenu(final String contextMenu) {
		this.contextMenu = contextMenu;
	}

	public ETreeLineStyle getLineStyle() {
		return lineStyle == null ? ETreeLineStyle.line : lineStyle;
	}

	public void setLineStyle(final ETreeLineStyle lineStyle) {
		this.lineStyle = lineStyle;
	}

	public boolean isCheckboxes() {
		return checkboxes;
	}

	public void setCheckboxes(final boolean checkboxes) {
		this.checkboxes = checkboxes;
	}

	public boolean isCheckboxesThreeState() {
		return checkboxesThreeState;
	}

	public void setCheckboxesThreeState(final boolean checkboxesThreeState) {
		this.checkboxesThreeState = checkboxesThreeState;
	}

	public boolean isCookies() {
		return cookies;
	}

	public void setCookies(final boolean cookies) {
		this.cookies = cookies;
	}

	public String getJsLoadedCallback() {
		return jsLoadedCallback;
	}

	public void setJsLoadedCallback(final String jsLoadedCallback) {
		this.jsLoadedCallback = jsLoadedCallback;
	}

	public String getJsCheckCallback() {
		return jsCheckCallback;
	}

	public void setJsCheckCallback(final String jsCheckCallback) {
		this.jsCheckCallback = jsCheckCallback;
	}

	public String getJsClickCallback() {
		return jsClickCallback;
	}

	public void setJsClickCallback(final String jsClickCallback) {
		this.jsClickCallback = jsClickCallback;
	}

	public String getJsDblclickCallback() {
		return jsDblclickCallback;
	}

	public void setJsDblclickCallback(final String jsDblclickCallback) {
		this.jsDblclickCallback = jsDblclickCallback;
	}

	public String getJobDrop() {
		return jobDrop;
	}

	public void setJobDrop(final String jobDrop) {
		this.jobDrop = jobDrop;
	}

	private Collection<AbstractTreeNode> treeNodes;

	public Collection<? extends AbstractTreeNode> getTreeNodes() {
		if (treeNodes == null) {
			treeNodes = new ArrayList<AbstractTreeNode>();
		}
		return treeNodes;
	}

	public void parseElement() {
		super.parseElement();
		if (treeNodes != null)
			for (AbstractTreeNode menuItem : treeNodes) {
				menuItem.parseElement();
			}
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsLoadedCallback", "jsClickCallback", "jsDblclickCallback", "jsCheckCallback" };
	}
}
