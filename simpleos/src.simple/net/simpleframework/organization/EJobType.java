package net.simpleframework.organization;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public enum EJobType {
	normal,

	/**
	 * 角色需要实现接口IJobRule
	 */
	handle,

	/**
	 * 表达式返回Boolean，判断是否角色成员
	 */
	script
}
