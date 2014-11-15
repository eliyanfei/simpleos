package net.simpleframework.web.page.component.ui.progressbar;

import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.IComponentHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IProgressBarHandle extends IComponentHandle {

	void doProgressState(ComponentParameter compParameter, ProgressState state);
}
