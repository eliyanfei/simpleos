package net.simpleframework.organization;

import net.simpleframework.core.bean.IIdBeanAware;
import net.simpleframework.core.id.ID;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IUserBeanAware extends IIdBeanAware {
	ID getUserId();

	void setUserId(ID userId);

	String getUserText();
}