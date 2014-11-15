package net.simpleframework.organization;

import net.simpleframework.ado.db.AbstractBeanManager;
import net.simpleframework.core.IApplicationModule;
import net.simpleframework.core.bean.IDataObjectBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractOrganizationManager<T extends IDataObjectBean> extends
		AbstractBeanManager<T> {
	@Override
	public IApplicationModule getApplicationModule() {
		return OrgUtils.applicationModule;
	}
}
