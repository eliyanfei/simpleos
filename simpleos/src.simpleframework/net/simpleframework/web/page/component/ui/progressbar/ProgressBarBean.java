package net.simpleframework.web.page.component.ui.progressbar;

import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.AbstractContainerBean;
import net.simpleframework.web.page.component.IComponentRegistry;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ProgressBarBean extends AbstractContainerBean {
	private float interval = 0.5f;

	private int maxProgressValue = 100;

	private int step;

	private boolean startAfterCreate = true;

	private boolean showAbortAction = true, showDetailAction = true;

	private int detailHeight = 120;

	private String changeCallback;

	public ProgressBarBean(final IComponentRegistry componentRegistry,
			final PageDocument pageDocument, final Element element) {
		super(componentRegistry, pageDocument, element);
	}

	public float getInterval() {
		return interval;
	}

	public void setInterval(final float interval) {
		this.interval = interval;
	}

	public int getMaxProgressValue() {
		return maxProgressValue;
	}

	public void setMaxProgressValue(final int maxProgressValue) {
		this.maxProgressValue = maxProgressValue;
	}

	public int getStep() {
		return step;
	}

	public void setStep(final int step) {
		this.step = step;
	}

	public boolean isStartAfterCreate() {
		return startAfterCreate;
	}

	public void setStartAfterCreate(final boolean startAfterCreate) {
		this.startAfterCreate = startAfterCreate;
	}

	public boolean isShowAbortAction() {
		return showAbortAction;
	}

	public void setShowAbortAction(final boolean showAbortAction) {
		this.showAbortAction = showAbortAction;
	}

	public boolean isShowDetailAction() {
		return showDetailAction;
	}

	public void setShowDetailAction(final boolean showDetailAction) {
		this.showDetailAction = showDetailAction;
	}

	public int getDetailHeight() {
		return detailHeight;
	}

	public void setDetailHeight(final int detailHeight) {
		this.detailHeight = detailHeight;
	}

	public String getChangeCallback() {
		return changeCallback;
	}

	public void setChangeCallback(final String changeCallback) {
		this.changeCallback = changeCallback;
	}
}
