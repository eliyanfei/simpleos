package net.simpleframework.example;

import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.progressbar.AbstractProgressBarHandle;
import net.simpleframework.web.page.component.ui.progressbar.ProgressState;

public class ProgressbarHandle extends AbstractProgressBarHandle {

	@Override
	public void doProgressState(final ComponentParameter compParameter, final ProgressState state) {
		state.step++;
		state.messages.add("My Message " + state.step);
	}
}
