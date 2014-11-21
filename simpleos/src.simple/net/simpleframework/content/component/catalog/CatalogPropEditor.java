package net.simpleframework.content.component.catalog;

import java.util.Collection;

import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.propeditor.AbstractPropEditorHandle;
import net.simpleframework.web.page.component.ui.propeditor.PropEditorBean;
import net.simpleframework.web.page.component.ui.propeditor.PropField;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class CatalogPropEditor extends AbstractPropEditorHandle {

	@Override
	public Collection<PropField> getFormFields(final ComponentParameter compParameter) {
		final PropEditorBean formEditor = (PropEditorBean) compParameter.componentBean;
		final ComponentParameter nComponentParameter = CatalogUtils
				.getComponentParameter(compParameter);
		final Collection<PropField> coll = ((ICatalogHandle) nComponentParameter.getComponentHandle())
				.getPropFields(nComponentParameter, formEditor);
		if (coll == null || coll.size() == 0) {
			return formEditor.getFormFields();
		} else {
			return coll;
		}
	}
}
