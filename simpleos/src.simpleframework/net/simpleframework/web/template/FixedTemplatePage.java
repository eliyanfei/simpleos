package net.simpleframework.web.template;

import javax.servlet.ServletContext;

import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.PageParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class FixedTemplatePage extends ComponentsTemplatePage {

	@Override
	protected void init(final ServletContext servletContext, final PageDocument pageDocument) {
		super.init(servletContext, pageDocument);
		addHtmlViewVariable(FixedTemplatePage.class, "body");
		addHtmlViewVariable(getClass(), "content");
	}

	public int pageWidth(final PageParameter pageParameter) {
		return 1024;
	}
}
