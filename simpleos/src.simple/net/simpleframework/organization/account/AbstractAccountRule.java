package net.simpleframework.organization.account;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractAccountRule implements IAccountRule {
	private int exp, points;

	/* 日志仅记录一次，其含义为，下次遇到同事件类型的日志，将不再记录，比如更改头像 */
	private boolean logOnlyonce;

	private String eventId;

	private String text;

	private String module;

	private String description;

	@Override
	public String getEventId() {
		return eventId;
	}

	@Override
	public void setEventId(final String eventId) {
		this.eventId = eventId;
	}

	@Override
	public int getExp(final IAccount account) {
		return exp;
	}

	public void setExp(final int exp) {
		this.exp = exp;
	}

	@Override
	public int getPoints(final IAccount account) {
		return points;
	}

	public void setPoints(final int points) {
		this.points = points;
	}

	@Override
	public String getModule() {
		return module;
	}

	public void setModule(final String module) {
		this.module = module;
	}

	@Override
	public String getText() {
		return text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@Override
	public boolean isLogOnlyonce() {
		return logOnlyonce;
	}

	@Override
	public void setLogOnlyonce(final boolean logOnlyonce) {
		this.logOnlyonce = logOnlyonce;
	}

	@Override
	public String toString() {
		return getText();
	}
}
