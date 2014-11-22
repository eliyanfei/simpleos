package net.simpleos;

import net.simpleframework.organization.component.register.DefaultUserRegisterHandle;
import net.simpleframework.web.page.component.ComponentParameter;
/**
 * 
 * @author 李岩飞 
 * @date 2014年11月19日 下午4:57:20 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public class SimpleosUserRegisterHandle extends DefaultUserRegisterHandle {

	@Override
	public String getJavascriptCallback(ComponentParameter compParameter, String jsAction, Object bean) {
		String jsCallback = super.getJavascriptCallback(compParameter, jsAction, bean);
		if ("submit".equals(jsAction)) {
			jsCallback += "$Actions.loc('/login.html');";
		}
		return jsCallback;
	}

}
