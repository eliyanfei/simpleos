package net.simpleframework.web.page.component.ui.pager;

import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.validation.AbstractValidationHandle;
import net.simpleframework.web.page.component.base.validation.ValidationBean;
import net.simpleframework.web.page.component.base.validation.ValidatorBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TablePagerFilterValidation extends AbstractValidationHandle {

	@Override
	public Collection<ValidatorBean> getValidators(final ComponentParameter compParameter) {
		final ValidationBean validationBean = (ValidationBean) compParameter.componentBean;
		final ArrayList<ValidatorBean> coll = new ArrayList<ValidatorBean>(validationBean.getValidators());
		final ComponentParameter nComponentParameter = PagerUtils.getComponentParameter(compParameter);
		try {
			final ITablePagerHandle tHandle = (ITablePagerHandle) nComponentParameter.getComponentHandle();
			final Collection<ValidatorBean> coll2 = tHandle.getFilterColumnValidators(nComponentParameter,
					TablePagerUtils.getSelectedColumn(nComponentParameter));
			if (coll2 != null) {
				coll.addAll(coll2);
			}
		} catch (Exception e) {
		}
		return coll;
	}
}
