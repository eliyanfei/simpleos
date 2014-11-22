package net.simpleos.impl;

import java.util.Calendar;
import java.util.Date;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.IUser;
import net.simpleframework.util.HTMLUtils;
import net.simpleframework.util.IConstants;
import net.simpleos.SimpleosUtil;
import net.simpleos.i.ICommonBeanAware;

public abstract class AbstractCommonBeanAware extends AbstractIdDataObjectBean implements ICommonBeanAware {
	private ID userId;// 发布者
	private ID lastUserId;// 修改者
	private String content;// 内容描述
	private int attentions;// 关注
	private long remarks;// 评论数
	private long views;// 访问量
	private long votes;//顶投票
	private long downVotes;//踩
	private long todayViews;// 今日访问量
	private boolean ttop = false;// 置顶
	private int mark;
	private short ddays = 0;//置顶时间
	private short ppriority = 0;//置顶优先级 ,越小优先级越大,共7个优先级
	private long oorder;// 排序
	private ID remarkUserId;//最后发评论的用户
	private Date remarkDate;//最后发评论的时间
	private Date createDate;// 发布日期
	private Date modifyDate;//修改时间

	public AbstractCommonBeanAware() {
		this.createDate = new Date();
		this.modifyDate = new Date();
	}

	public String getTopicName() {
		return null;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	public int getMark() {
		return mark;
	}

	public String getTtopShow() {
		if (ttop) {
			if (ppriority > 0) {
				return "<span class='ttop_" + ppriority + "'></span>";
			}
		}
		return "";
	}

	public void setPpriority(short ppriority) {
		this.ppriority = ppriority;
	}

	public short getPpriority() {
		return ppriority;
	}

	public void setDdays(short ddays) {
		this.ddays = ddays;
	}

	public short getDdays() {
		return ddays;
	}

	public ID getLastUserId() {
		return lastUserId;
	}

	public void setLastUserId(ID lastUserId) {
		this.lastUserId = lastUserId;
	}

	public void setRemarkUserId(ID remarkUserId) {
		this.remarkUserId = remarkUserId;
	}

	public ID getRemarkUserId() {
		return remarkUserId;
	}

	public void setRemarkDate(Date remarkDate) {
		this.remarkDate = remarkDate;
	}

	public Date getRemarkDate() {
		return remarkDate;
	}

	/**
	 * 是不是有新的评论
	 * @return
	 */
	public boolean isNewRemark() {
		if (remarkDate == null)
			return false;
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -12);
		if (remarkDate.after(cal.getTime())) {
			return true;
		}
		return false;
	}

	public void setTodayViews(long todayViews) {
		this.todayViews = todayViews;
	}

	public long getTodayViews() {
		return todayViews;
	}

	public void setTtop(boolean ttop) {
		this.ttop = ttop;
	}

	public boolean isTtop() {
		return ttop;
	}

	@Override
	public void setOorder(long oorder) {
		this.oorder = oorder;
	}

	@Override
	public long getOorder() {
		return oorder;
	}

	public String getRemarkUserText() {
		final IUser user = SimpleosUtil.getUserById(getRemarkUserId());
		return user != null ? user.getText() : IConstants.HTML_BLANK_STRING;
	}

	@Override
	public String getUserText() {
		final IUser user = SimpleosUtil.getUserById(getUserId());
		return user != null ? user.getText() : IConstants.HTML_BLANK_STRING;
	}

	public ID getUserId() {
		return userId;
	}

	public void setUserId(ID userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = HTMLUtils.stripScripts(content);
	}

	public int getAttentions() {
		return attentions;
	}

	public void setAttentions(int attentions) {
		this.attentions = attentions;
	}

	public long getRemarks() {
		return remarks;
	}

	public void setRemarks(long remarks) {
		this.remarks = remarks;
	}

	public long getViews() {
		return views;
	}

	public void setViews(long views) {
		this.views = views;
	}

	public void setDownVotes(long downVotes) {
		this.downVotes = downVotes;
	}

	public long getDownVotes() {
		return downVotes;
	}

	public long getVotes() {
		return votes;
	}

	public void setVotes(long votes) {
		this.votes = votes;
	}

	@Override
	public long getTotalVotes() {
		long v = this.votes - this.downVotes;
		return v > 0 ? v : 0;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String[] getRemarkBean() {
		return new String[] { "remarks", "remarkUserId", "remarkDate" };
	}
}
