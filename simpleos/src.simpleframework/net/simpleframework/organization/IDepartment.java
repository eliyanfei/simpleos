package net.simpleframework.organization;

import java.util.Collection;

import net.simpleframework.core.bean.IDescriptionBeanAware;
import net.simpleframework.core.bean.ITextBeanAware;
import net.simpleframework.core.bean.ITreeBeanAware;
import net.simpleframework.core.bean.IUniqueNameBeanAware;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IDepartment extends IUniqueNameBeanAware, ITextBeanAware, ITreeBeanAware,
		IDescriptionBeanAware {

	Collection<? extends IDepartment> children();

	Collection<? extends IUser> users();

	Collection<? extends IJobChart> charts();
}
