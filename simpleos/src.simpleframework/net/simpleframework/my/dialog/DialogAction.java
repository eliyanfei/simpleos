package net.simpleframework.my.dialog;

import java.util.Map;

import javax.servlet.http.HttpSession;

import net.a.ItSiteUtil;
import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.applets.notification.NotificationUtils;
import net.simpleframework.core.id.ID;
import net.simpleframework.core.id.LongID;
import net.simpleframework.my.space.MySpaceUtils;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.HTMLUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;
import net.simpleframework.web.page.component.ui.dictionary.SmileyUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DialogAction extends AbstractAjaxRequestHandle {

	@Override
	public Object getBeanProperty(ComponentParameter compParameter, String beanProperty) {
		if ("jobExecute".equals(beanProperty)) {
			return IJob.sj_account_normal;
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	public IForward dialogSave(final ComponentParameter compParameter) {
		final String content = compParameter.getRequestParameter("dialog_editor");
		final String dialogId = compParameter.getRequestParameter("dialogId");
		if (!StringUtils.hasText(content) || !StringUtils.hasText(dialogId)) {
			return null;
		}
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final HttpSession httpSession = compParameter.getSession();
				final IAccount account = AccountSession.getLogin(httpSession);
				if (account == null) {
					return;
				}
				final ITableEntityManager dialog_mgr = MySpaceUtils.getTableEntityManager(SimpleDialog.class);
				dialog_mgr.reset();
				final SimpleDialog dialog = dialog_mgr.queryForObjectById(dialogId, SimpleDialog.class);
				final ITableEntityManager dialog_item_mgr = MySpaceUtils.getTableEntityManager(SimpleDialogItem.class);
				final SimpleDialogItem dialogItem = new SimpleDialogItem();
				dialogItem.setContent(HTMLUtils.stripScripts(content));
				if (account.getId().equals2(dialog.getSentId())) {
					dialogItem.setMe(true);
					dialog.setToRead(false);
				} else {
					dialog.setSendRead(false);
					dialogItem.setMe(false);
				}
				dialogItem.setDialogId(new LongID(dialogId));
				dialog_item_mgr.insertTransaction(dialogItem, new TableEntityAdapter() {
					@Override
					public void afterInsert(ITableEntityManager manager, Object[] objects) {
						final StringBuffer buf = new StringBuffer();
						buf.append("<div>");
						buf.append("<div class='tit " + (dialogItem.isMe() ? "tome" : "toyou") + "'>");
						buf.append(account.user().getText());
						buf.append(ConvertUtils.toDateString(dialogItem.getSentDate()));
						buf.append("</div>");
						buf.append("<div class='cnt'>");
						buf.append(SmileyUtils.replaceSmiley(dialogItem.getContent()));
						buf.append("</div>");
						buf.append("</div>");
						dialog_mgr.update(dialog);
						AccountSession.getLoginObject(compParameter.getSession()).setDialogUpdate(true);
						json.put("rs", buf.toString());
					}
				});
			}
		});
	}

	public IForward dialogDelete(final ComponentParameter compParameter) {
		final String dialogId = compParameter.getRequestParameter("dialogId");
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final ITableEntityManager dialog_mgr = MySpaceUtils.getTableEntityManager(SimpleDialog.class);
				final ITableEntityManager dialog_item_mgr = MySpaceUtils.getTableEntityManager(SimpleDialogItem.class);
				final SimpleDialog dialog = dialog_mgr.queryForObjectById(dialogId, SimpleDialog.class);
				final ID userId = ItSiteUtil.getLoginUser(compParameter).getId();
				if (dialog != null) {
					if (dialog.getSentId().equals2(userId)) {
						dialog.setSentDel(true);
					} else if (dialog.getToId().equals2(userId)) {
						dialog.setToDel(true);
					}
					if (dialog.canDel()) {
						dialog_mgr.deleteTransaction(new ExpressionValue("id=?", new Object[] { dialog.getId() }), new TableEntityAdapter() {
							public void afterDelete(IDataObjectValue dataObjectValue) {
								dialog_item_mgr.delete(new ExpressionValue("dialogId=?", new Object[] { dialog.getId() }));
								json.put("act", "删除成功!");
							};
						});
					} else {
						dialog_mgr.update(dialog);
					}
				}
			}
		});
	}
}
