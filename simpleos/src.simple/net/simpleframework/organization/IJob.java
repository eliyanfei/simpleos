package net.simpleframework.organization;

import java.util.Collection;

import net.simpleframework.core.bean.IDescriptionBeanAware;
import net.simpleframework.core.bean.ITextBeanAware;
import net.simpleframework.core.bean.ITreeBeanAware;
import net.simpleframework.core.bean.IUniqueNameBeanAware;
import net.simpleframework.core.id.ID;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IJob extends IUniqueNameBeanAware, ITextBeanAware, IDescriptionBeanAware,
		ITreeBeanAware {
	/**
	 * 内置的超级用户
	 */
	static final String sj_admin = "sys_admin";
	/**
	 * 匿名用户
	 */
	static final String sj_anonymous = "sys_anonymous";
	/**
	 * 未激活的注册用户
	 */
	static final String sj_account_register = "sys_account_register";
	/**
	 * 正常的注册用户
	 */
	static final String sj_account_normal = "sys_account_normal";
	/**
	 * 已锁定的注册用户
	 */
	static final String sj_account_locked = "sys_account_locked";
	/**
	 * 系统管理员
	 */
	static final String sj_manager = "sys_manager";

	ID getJobChartId();

	void setJobChartId(final ID jobChartId);

	EJobType getJobType();

	void setJobType(final EJobType jobType);

	String getRuleHandle();

	void setRuleHandle(final String ruleHandle);

	String getRuleScript();

	void setRuleScript(final String ruleScript);

	/*----------------------------------关联操作 --------------------------------*/

	IJobRule jobRuleHandle();

	Collection<? extends IJob> children();

	Collection<? extends IJob> root();

	IJobChart jobChart();

	Collection<? extends IUser> users();

	boolean member(IUser user);
}
