package net.simpleframework.organization.component.login;

import java.util.Map;

import net.simpleframework.organization.IUser;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.organization.account.LoginObject;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.IComponentHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface ILoginHandle extends IComponentHandle {

	Map<String, Object> login(ComponentParameter compParameter);

	void beforeLogin(ComponentParameter compParameter, LoginObject loginObject, String password);

	void afterLogin(ComponentParameter compParameter, LoginObject loginObject);

	void mailGetPassword(IUser user);

	void mailRegistActivation(IAccount account);
}
