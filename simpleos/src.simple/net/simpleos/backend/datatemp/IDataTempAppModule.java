package net.simpleos.backend.datatemp;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.itsite.i.IItSiteApplicationModule;
import net.simpleframework.content.component.catalog.CatalogBean;
import net.simpleframework.web.page.component.ui.propeditor.PropEditorBean;
import net.simpleframework.web.page.component.ui.propeditor.PropField;

/**
 * 
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-3下午12:29:48
 */
public interface IDataTempAppModule extends IItSiteApplicationModule {
	Collection<PropField> doDictItemPropEditor(final HttpServletRequest request, final HttpServletResponse response, final CatalogBean catalogBean,
			final PropEditorBean formEditor);
}
