package net.simpleframework.content.component.catalog;

import java.util.Map;

import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.IComponentHandle;
import net.simpleframework.web.page.component.ui.dictionary.AbstractDictionaryHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class CatalogSelectDict extends AbstractDictionaryHandle {

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = CatalogUtils
				.getComponentParameter(compParameter);
		return IComponentHandle.Utils.toFormParameters(nComponentParameter);
	}
}
