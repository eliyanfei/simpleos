package net.simpleframework.my.dialog;

import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.window.AbstractWindowHandle;

public class DialogWindow extends AbstractWindowHandle {
	@Override
	public Object getBeanProperty(ComponentParameter compParameter, String beanProperty) {
		if ("title".equals(beanProperty)) {
			SimpleDialog dialog = DialogUtils.getSimpleDialog(compParameter);
			if (dialog != null) {
				return "和("+ dialog.getToUserText()+")的对话";
			}else{
				return "对话";
			}
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}
}
