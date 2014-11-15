package net.prj.core.impl;

import net.prj.core.i.IModelBean;

/**
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-11-29上午11:33:07
 */
public abstract class AbstractModelBean implements IModelBean {

	@Override
	public boolean isDesign() {
		return false;
	}

	@Override
	public int getOorder() {
		return 0;
	}
}
