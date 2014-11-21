package net.simpleframework.web.page.component.ui.htmleditor;

import net.simpleframework.web.page.component.AbstractComponentRegistry;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class HtmlEditorUtils {
	public static String getHomePath() {
		return AbstractComponentRegistry.getRegistry(HtmlEditorRegistry.HTMLEDITOR).getResourceHomePath();
	}
}
