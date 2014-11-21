package net.simpleframework.sysmgr.dict;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.content.component.catalog.CatalogBean;
import net.simpleframework.web.IWebApplicationModule;
import net.simpleframework.web.page.component.ui.propeditor.PropEditorBean;
import net.simpleframework.web.page.component.ui.propeditor.PropField;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface ISysDictApplicationModule extends IWebApplicationModule {
	public static final String DT_DISTRICT = "district";

	Collection<PropField> doDictItemPropEditor(final HttpServletRequest request,
			final HttpServletResponse response, final CatalogBean catalogBean,
			final PropEditorBean formEditor);
}
