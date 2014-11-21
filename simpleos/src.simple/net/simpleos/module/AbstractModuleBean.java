package net.simpleos.module;


/**
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-11-29上午11:33:07
 */
public abstract class AbstractModuleBean implements IModuleBean {

	@Override
	public boolean isDesign() {
		return false;
	}

	@Override
	public int getOorder() {
		return 0;
	}
}
