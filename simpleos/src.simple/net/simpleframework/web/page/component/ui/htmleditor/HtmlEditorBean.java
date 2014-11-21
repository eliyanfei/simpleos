package net.simpleframework.web.page.component.ui.htmleditor;

import net.simpleframework.util.BeanDefaults;
import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.AbstractContainerBean;
import net.simpleframework.web.page.component.IComponentRegistry;

import org.dom4j.Element;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class HtmlEditorBean extends AbstractContainerBean {

	private String textarea;

	private boolean toolbarCanCollapse = BeanDefaults.getBool(getClass(), "toolbarCanCollapse", true);

	private boolean resizeEnabled = BeanDefaults.getBool(getClass(), "resizeEnabled", false);

	private boolean elementsPath = BeanDefaults.getBool(getClass(), "elementsPath", false);

	private boolean startupFocus = BeanDefaults.getBool(getClass(), "startupFocus", true);

	private EEditorLineMode enterMode, shiftEnterMode;

	private String htmlContent;

	private String jsLoadedCallback;

	private String toolbar;

	public HtmlEditorBean(final IComponentRegistry componentRegistry, final PageDocument pageDocument, final Element xmlElement) {
		super(componentRegistry, pageDocument, xmlElement);
	}

	public String getToolbar() {
		return toolbar == null ? "Basic" : toolbar;
	}

	public void setToolbar(final String toolbar) {
		this.toolbar = toolbar;
	}

	public String getTextarea() {
		return textarea;
	}

	public void setTextarea(final String textarea) {
		this.textarea = textarea;
	}

	public boolean isToolbarCanCollapse() {
		return toolbarCanCollapse;
	}

	public void setToolbarCanCollapse(final boolean toolbarCanCollapse) {
		this.toolbarCanCollapse = toolbarCanCollapse;
	}

	public boolean isResizeEnabled() {
		return resizeEnabled;
	}

	public void setResizeEnabled(final boolean resizeEnabled) {
		this.resizeEnabled = resizeEnabled;
	}

	public boolean isElementsPath() {
		return elementsPath;
	}

	public void setElementsPath(final boolean elementsPath) {
		this.elementsPath = elementsPath;
	}

	public boolean isStartupFocus() {
		return startupFocus;
	}

	public void setStartupFocus(final boolean startupFocus) {
		this.startupFocus = startupFocus;
	}

	public EEditorLineMode getEnterMode() {
		return enterMode == null ? EEditorLineMode.p : enterMode;
	}

	public void setEnterMode(final EEditorLineMode enterMode) {
		this.enterMode = enterMode;
	}

	public EEditorLineMode getShiftEnterMode() {
		return shiftEnterMode == null ? EEditorLineMode.br : shiftEnterMode;
	}

	public void setShiftEnterMode(final EEditorLineMode shiftEnterMode) {
		this.shiftEnterMode = shiftEnterMode;
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(final String htmlContent) {
		this.htmlContent = htmlContent;
	}

	public String getJsLoadedCallback() {
		return jsLoadedCallback;
	}

	public void setJsLoadedCallback(final String jsLoadedCallback) {
		this.jsLoadedCallback = jsLoadedCallback;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "htmlContent", "jsLoadedCallback" };
	}
}
