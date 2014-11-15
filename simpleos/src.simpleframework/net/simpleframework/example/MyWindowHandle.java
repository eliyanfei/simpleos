package net.simpleframework.example;

import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.window.AbstractWindowHandle;

public class MyWindowHandle extends AbstractWindowHandle {

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("content".equals(beanProperty)) {
			return "Hello Window!";
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}
}
