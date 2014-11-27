package net.simpleframework.example;

import net.simpleframework.web.page.UrlForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.submit.AbstractSubmitHandle;

public class MySubmitHandle extends AbstractSubmitHandle {
	@Override
	public UrlForward submit(final ComponentParameter compParameter) {
		compParameter.setRequestAttribute("dp2", compParameter.getRequestParameter("dp2"));
		compParameter.setRequestAttribute("dp1", getMultipartFile(compParameter, "dp1"));
		return new UrlForward("/app/demo/comps/submit/demo_submit.jsp");
	}
}
