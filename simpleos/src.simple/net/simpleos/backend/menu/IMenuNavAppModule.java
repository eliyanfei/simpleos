package net.simpleos.backend.menu;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.content.component.catalog.CatalogBean;
import net.simpleframework.web.page.component.ui.propeditor.PropEditorBean;
import net.simpleframework.web.page.component.ui.propeditor.PropField;
import net.simpleos.i.ISimpleosApplicationModule;

/**
 * 
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-3下午12:29:48
 */
public interface IMenuNavAppModule extends ISimpleosApplicationModule {
	Collection<PropField> doDictItemPropEditor(final HttpServletRequest request, final HttpServletResponse response, final CatalogBean catalogBean,
			final PropEditorBean formEditor);
}
