package net.simpleos.i;

import net.simpleframework.applets.attention.ISentCallback;
import net.simpleframework.organization.IUser;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月20日 上午11:46:16 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public interface ISendMail extends ISentCallback {
	/**
	 * 获取用户
	 * @return
	 */
	IUser getUser();
}
