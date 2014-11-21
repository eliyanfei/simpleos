package net.simpleframework.organization.account;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IAccountRule {
	String getEventId();

	void setEventId(final String eventId);

	/**
	 * 积分值
	 * 
	 * @param context
	 * @return
	 */
	int getPoints(IAccount account);

	/**
	 * 经验值
	 * 
	 * @param context
	 * @return
	 */
	int getExp(IAccount account);

	boolean isLogOnlyonce();

	void setLogOnlyonce(boolean logOnlyonce);

	String getText();

	String getModule();

	String getDescription();

	public static enum TimeUnit {
		day, hour
	}
}