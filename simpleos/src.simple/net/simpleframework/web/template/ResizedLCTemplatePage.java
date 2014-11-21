package net.simpleframework.web.template;

import javax.servlet.ServletContext;

import net.simpleframework.web.page.PageDocument;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ResizedLCTemplatePage extends ResizedTemplatePage {
	@Override
	protected void init(final ServletContext servletContext, final PageDocument pageDocument) {
		super.init(servletContext, pageDocument);
		final Class<?> pageClass = getClass();
		addHtmlViewVariable(pageClass, "content_left", pageClass.getSimpleName() + "_Left.html");
		addHtmlViewVariable(pageClass, "content_center", pageClass.getSimpleName() + "_Center.html");
		addHtmlViewVariable(ResizedLCTemplatePage.class, "content");
	}
}
