package net.simpleframework.organization.component.register;

import java.util.Map;

import net.simpleframework.organization.account.IAccount;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.IComponentHandle;
import net.simpleframework.web.page.component.IComponentJavascriptCallback;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IUserRegisterHandle extends IComponentHandle, IComponentJavascriptCallback {

	IAccount regist(ComponentParameter compParameter, Map<String, Object> data);

	boolean checked(ComponentParameter compParameter, String userAccount);

	void mailRegistActivation(IAccount account);
}
