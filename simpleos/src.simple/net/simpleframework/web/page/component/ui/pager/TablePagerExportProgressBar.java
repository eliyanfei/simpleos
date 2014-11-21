package net.simpleframework.web.page.component.ui.pager;

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
public class TablePagerExportProgressBar extends AbstractProgressBarHandle {
	static final String MAX_PROGRESS = "__export_maxprogressvalue";

	static final String STEP = "__export_step";

	@Override
	public void doProgressState(final ComponentParameter compParameter, final ProgressState state) {
		final Integer maxProgressValue = (Integer) compParameter.getSessionAttribute(MAX_PROGRESS);
		if (maxProgressValue != null) {
			state.maxProgressValue = maxProgressValue;
			compParameter.removeSessionAttribute(MAX_PROGRESS);
		} else {
			state.maxProgressValue = 0;
		}
		final Integer step = (Integer) compParameter.getSessionAttribute(STEP);
		if (step != null) {
			state.step = step;
		}
	}
}
