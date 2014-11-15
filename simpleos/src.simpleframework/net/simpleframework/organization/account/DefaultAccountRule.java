package net.simpleframework.organization.account;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultAccountRule extends AbstractAccountRule {
	/* 某一事件在某一时间内的最大值 */
	private int expMaxValue, pointsMaxValue;

	private TimeUnit expTimeUnit, pointsTimeUnit;

	public int getExpMaxValue() {
		return expMaxValue;
	}

	public void setExpMaxValue(final int expMaxValue) {
		this.expMaxValue = expMaxValue;
	}

	public TimeUnit getExpTimeUnit() {
		return expTimeUnit != null ? expTimeUnit : TimeUnit.day;
	}

	public void setExpTimeUnit(final TimeUnit expTimeUnit) {
		this.expTimeUnit = expTimeUnit;
	}

	public int getPointsMaxValue() {
		return pointsMaxValue;
	}

	public void setPointsMaxValue(final int pointsMaxValue) {
		this.pointsMaxValue = pointsMaxValue;
	}

	public TimeUnit getPointsTimeUnit() {
		return pointsTimeUnit;
	}

	public void setPointsTimeUnit(final TimeUnit pointsTimeUnit) {
		this.pointsTimeUnit = pointsTimeUnit;
	}
}
