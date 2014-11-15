package net.simpleframework.organization.account;

import java.util.Date;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.bean.IDescriptionBeanAware;
import net.simpleframework.core.id.ID;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class AccountLog extends AbstractIdDataObjectBean implements IDescriptionBeanAware {
	private ID accountId;

	private String eventId;

	private Date createDate;

	private int points, exp, reputation, money;

	private ID logId;

	private String description;

	public ID getAccountId() {
		return accountId;
	}

	public void setAccountId(final ID accountId) {
		this.accountId = accountId;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(final String eventId) {
		this.eventId = eventId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(final Date createDate) {
		this.createDate = createDate;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(final int points) {
		this.points = points;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(final int exp) {
		this.exp = exp;
	}

	public int getReputation() {
		return reputation;
	}

	public void setReputation(final int reputation) {
		this.reputation = reputation;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(final int money) {
		this.money = money;
	}

	public ID getLogId() {
		return logId;
	}

	public void setLogId(final ID logId) {
		this.logId = logId;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(final String description) {
		this.description = description;
	}

	private static final long serialVersionUID = 8403922246540874708L;
}
