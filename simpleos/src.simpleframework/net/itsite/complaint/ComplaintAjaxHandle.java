package net.itsite.complaint;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import net.a.ItSiteUtil;
import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.applets.notification.NotificationUtils;
import net.simpleframework.core.id.LongID;
import net.simpleframework.my.message.EMessageType;
import net.simpleframework.my.message.MessageUtils;
import net.simpleframework.organization.IDepartment;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;

public class ComplaintAjaxHandle extends AbstractAjaxRequestHandle {

	@Override
	public Object getBeanProperty(ComponentParameter compParameter, String beanProperty) {
		if ("jobExecute".equals(beanProperty)) {
			return IJob.sj_account_normal;
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	/**
	 * 删除发布的资料
	 */
	public IForward deleteComplaint(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(final Map<String, Object> json) throws Exception {
				final ComplaintBean complaintBean = ComplaintUtils.applicationModule.getBean(ComplaintBean.class,
						compParameter.getRequestParameter(ComplaintUtils.complaintId));
				if (complaintBean != null) {
					if (ItSiteUtil.isManageOrSelf(compParameter, ComplaintUtils.applicationModule, complaintBean.getUserId())) {
						ComplaintUtils.applicationModule.doDelete(complaintBean, new TableEntityAdapter() {
							@Override
							public void afterDelete(ITableEntityManager manager, IDataObjectValue dataObjectValue) {
								NotificationUtils.deleteMessageNotificationByMessageId(complaintBean.getId());//删除通知
							}
						});
						return;
					}
				}
				json.put("act", "非法操作!");
			}
		});
	}

	/**
	 * 确认，不在弹出消息
	 * @param compParameter
	 * @return
	 */
	public IForward okComplaint(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(final Map<String, Object> json) throws Exception {
				final ComplaintBean complaintBean = ComplaintUtils.applicationModule.getBean(ComplaintBean.class,
						compParameter.getRequestParameter(ComplaintUtils.complaintId));
				if (complaintBean != null) {
					if (ItSiteUtil.isManageOrSelf(compParameter, ComplaintUtils.applicationModule, complaintBean.getUserId())) {
						NotificationUtils.deleteMessageNotificationByMessageId(complaintBean.getId());//删除通知
					}
				}
			}
		});
	}

	/**
	 * 发布资料
	 */
	public IForward saveComplaint(final ComponentParameter compParameter) throws Exception {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(final Map<String, Object> json) throws Exception {
				final ComplaintBean complaintBean = new ComplaintBean();
				BeanUtils.setProperty(complaintBean, "content", compParameter.getRequestParameter("comp_content"));
				complaintBean.setComplaint(EComplaint.valueOf(compParameter.getRequestParameter("comp_complaint")));
				complaintBean.setRefModule(EFunctionModule.valueOf(compParameter.getRequestParameter("comp_refModule")));
				complaintBean.setRefId(new LongID(compParameter.getRequestParameter("comp_refId")));
				complaintBean.setUserId(ItSiteUtil.getLoginUser(compParameter).getId());
				ComplaintUtils.applicationModule.doUpdate(complaintBean);
				final IDepartment department = OrgUtils.dm().queryForObject(new ExpressionValue("name='itniwo'"));
				if (department != null) {
					final Collection<? extends IUser> users = department.users();
					final StringBuilder sb = new StringBuilder();
					sb.append("<p>").append("<a class=\"a2\" href=\"").append("/message_comp.html").append("\">")
							.append("举报" + complaintBean.getComplaint().toString()).append("</a>").append("</p>");
					sb.append("<div>").append(ConvertUtils.toDateString(complaintBean.getCreateDate())).append("</div>");
					for (final IUser user : users) {
						MessageUtils.createNotifation(compParameter, "举报", sb.toString(), complaintBean.getUserId(), user.getId());
					}
					MessageUtils.createNotifation(compParameter, "系统通知", "您好，您的举报信息我们已经收到并会尽快处理，非常感谢您对我们工作的支持!", OrgUtils.um().getUserByName("admin")
							.getId(), complaintBean.getUserId(), EMessageType.complaint);
				}
			}
		});
	}

	/**
	 * 处理资料
	 */
	public IForward dealComplaint(final ComponentParameter compParameter) throws Exception {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(final Map<String, Object> json) throws Exception {
				final ComplaintBean complaintBean = ComplaintUtils.applicationModule.getBean(ComplaintBean.class,
						compParameter.getRequestParameter(ComplaintUtils.complaintId));
				if (complaintBean != null) {
					complaintBean.setDeal(true);
					complaintBean.setDealDate(new Date());
					BeanUtils.setProperty(complaintBean, "dealContent", compParameter.getRequestParameter("comp_dealContent"));
					ComplaintUtils.applicationModule.doUpdate(new Object[] { "deal", "dealContent" }, complaintBean, new TableEntityAdapter() {
						@Override
						public void afterUpdate(ITableEntityManager manager, Object[] objects) {
							final StringBuilder sb = new StringBuilder();
							sb.append("<p>").append("<a class=\"a2\" href=\"").append("/message_comp.html").append("\">")
									.append(complaintBean.getDealContent()).append("</a>").append("</p>");
							sb.append("<div>").append(ConvertUtils.toDateString(complaintBean.getDealDate())).append("</div>");
							NotificationUtils.updateMessageNotification(complaintBean.getId(), "举报处理结果", sb.toString());//更改通知用户
						}
					});
				}
			}
		});
	}
}