package net.simpleframework.workflow.web.component.worklist;

import javax.servlet.ServletContext;

import net.simpleframework.web.page.component.ui.pager.PagerRegistry;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class WorklistRegistry extends PagerRegistry {

	public WorklistRegistry(final ServletContext servletContext) {
		super(servletContext);
	}
}
