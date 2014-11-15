package net.itsite;

import net.simpleframework.organization.component.register.DefaultUserRegisterHandle;
import net.simpleframework.web.page.component.ComponentParameter;

public class ItSiteUserRegisterHandle extends DefaultUserRegisterHandle {

	@Override
	public String getJavascriptCallback(ComponentParameter compParameter, String jsAction, Object bean) {
		String jsCallback = super.getJavascriptCallback(compParameter, jsAction, bean);
		if ("submit".equals(jsAction)) {
			jsCallback += "$Actions.loc('/login.html');";
		}
		return jsCallback;
	}

}
