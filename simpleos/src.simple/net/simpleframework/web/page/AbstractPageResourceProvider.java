package net.simpleframework.web.page;

import javax.servlet.ServletContext;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractPageResourceProvider extends AbstractResourceProvider implements
		IPageResourceProvider {

	public AbstractPageResourceProvider(final ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public boolean equals(final Object obj) {
		return getName().equalsIgnoreCase(obj.toString());
	}

	@Override
	public String toString() {
		return getName();
	}
}
