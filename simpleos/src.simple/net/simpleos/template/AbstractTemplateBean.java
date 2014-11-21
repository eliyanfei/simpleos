package net.simpleos.template;

/**
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-5上午09:32:04
 */
public abstract class AbstractTemplateBean implements ITemplateBean {

	@Override
	public boolean isFullScreen() {
		return false;
	}

	@Override
	public boolean isDesign() {
		return false;
	}
}
