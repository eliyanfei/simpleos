package net.simpleframework.example;

import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.progressbar.AbstractProgressBarHandle;
import net.simpleframework.web.page.component.ui.progressbar.ProgressState;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ProgressbarHandle extends AbstractProgressBarHandle {

	@Override
	public void doProgressState(final ComponentParameter compParameter, final ProgressState state) {
		state.step++;
		state.messages.add("My Message " + state.step);
	}
}
