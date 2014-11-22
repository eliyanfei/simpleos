package net.simpleos.i;

import java.util.Date;
import java.util.Map;

import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.content.IAttentionsBeanAware;
import net.simpleframework.content.IContentBeanAware;
import net.simpleframework.core.bean.IOrderBeanAware;
import net.simpleframework.core.bean.IViewsBeanAware;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.IUserBeanAware;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleos.SimpleosUtil;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月21日 下午4:01:32 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public interface ICommonBeanAware extends IViewsBeanAware, IUserBeanAware, IContentBeanAware, IOrderBeanAware, IAttentionsBeanAware {

	void setRemarkUserId(ID remarkUserId);

	ID getRemarkUserId();

	void setRemarkDate(Date remarkDate);

	Date getRemarkDate();

	/**
	 * 是不是有新的评论
	 * @return
	 */
	boolean isNewRemark();

	void setTodayViews(long todayViews);

	long getTodayViews();

	void setTtop(boolean ttop);

	boolean isTtop();

	String getRemarkUserText();

	String getContent();

	void setContent(String content);

	long getRemarks();

	void setRemarks(long remarks);

	long getViews();

	void setViews(long views);

	public void setDownVotes(long downVotes);

	public long getDownVotes();

	long getVotes();

	long getTotalVotes();

	void setVotes(long votes);

	Date getCreateDate();

	void setCreateDate(Date createDate);

	Date getModifyDate();

	void setModifyDate(Date modifyDate);

	public String[] getRemarkBean();

	public void setPpriority(short ppriority);

	public short getPpriority();

	public void setDdays(short ddays);

	public short getDdays();

	public static class Utils {

		public static void updateVotes(final PageRequestResponse requestResponse, final ITableEntityManager tblmgr,
				final ICommonBeanAware votesAware, final Map<String, Object> json) {
			if (tblmgr == null || votesAware == null) {
				return;
			}
			synchronized (tblmgr) {
				final String attributeName = "votes_" + votesAware.getId();
				final boolean views = ConvertUtils.toBoolean(requestResponse.getSessionAttribute(attributeName), false);
				if (!views) {
					if ("up".equals(requestResponse.getRequestParameter("voteValue"))) {
						votesAware.setVotes(votesAware.getVotes() + 1);
						json.put("votes", votesAware.getTotalVotes());
					} else {
						votesAware.setDownVotes(votesAware.getDownVotes() + 1);
						json.put("votes", votesAware.getTotalVotes());
					}
					tblmgr.update(new String[] { "votes", "downVotes" }, votesAware);
					requestResponse.setSessionAttribute(attributeName, Boolean.TRUE);
				}
			}
		}

		public static void updateViews(final PageRequestResponse requestResponse, final ITableEntityManager tblmgr, final ICommonBeanAware viewsAware) {
			if (tblmgr == null || viewsAware == null) {
				return;
			}
			synchronized (tblmgr) {
				final String attributeName = "views_" + viewsAware.getId();
				final boolean views = ConvertUtils.toBoolean(requestResponse.getSessionAttribute(attributeName), false);
				if (!views) {
					viewsAware.setViews(viewsAware.getViews() + 1);
					viewsAware.setTodayViews(viewsAware.getTodayViews() + 1);
					tblmgr.update(new String[] { "views", "todayViews" }, viewsAware);
					requestResponse.setSessionAttribute(attributeName, Boolean.TRUE);
				}
			}
		}

		public static void updateRemarks(final PageRequestResponse requestResponse, final ITableEntityManager tblmgr, final Class<?> bean,
				final Object documentId) {
			if (tblmgr == null) {
				return;
			}
			synchronized (tblmgr) {
				final ICommonBeanAware commonBean = (ICommonBeanAware) tblmgr.queryForObjectById(documentId, bean);
				if (commonBean != null) {
					commonBean.setRemarks(commonBean.getRemarks() + 1);
					commonBean.setRemarkUserId(SimpleosUtil.getLoginUser(requestResponse).getId());
					commonBean.setRemarkDate(new Date());
					tblmgr.update(commonBean.getRemarkBean(), commonBean);
				}
			}
		}
	}
}
