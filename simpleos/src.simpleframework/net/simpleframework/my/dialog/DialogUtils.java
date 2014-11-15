package net.simpleframework.my.dialog;

import net.a.ItSiteUtil;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.id.ID;
import net.simpleframework.core.id.LongID;
import net.simpleframework.my.space.MySpaceUtils;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageRequestResponse;

public class DialogUtils {

	public static String showDialog(final PageRequestResponse requestResponse) {
		final StringBuffer buf = new StringBuffer();
		final IAccount account = AccountSession.getLogin(requestResponse.getSession());
		if (account != null) {
			final ITableEntityManager dialog_mgr = MySpaceUtils.getTableEntityManager(SimpleDialog.class);
			SimpleDialog dialog = dialog_mgr.queryForObject(new ExpressionValue("toid=? and toRead=?", new Object[] { account.getId(), false }),
					SimpleDialog.class);
			if (dialog != null) {
				buf.append("$Actions['myDialogWindow']('userid=" + dialog.getSentId() + "');");
			} else {
				dialog = dialog_mgr.queryForObject(new ExpressionValue("sentId=? and sendRead=?", new Object[] { account.getId(), false }),
						SimpleDialog.class);
				if (dialog != null) {
					buf.append("$Actions['myDialogWindow']('userid=" + dialog.getToId() + "');");
				}
			}
		}
		return buf.toString();
	}

	public static IDataObjectQuery<SimpleDialogItem> queryDialog(PageRequestResponse requestResponse, final SimpleDialog dialog) {
		if (dialog == null)
			return null;
		final IAccount account = AccountSession.getLogin(requestResponse.getSession());
		if (account == null) {
			return null;
		}
		if (account.getId().equals2(dialog.getSentId())) {
			dialog.setSendRead(true);
		} else {
			dialog.setToRead(true);
		}
		MySpaceUtils.getTableEntityManager(SimpleDialog.class).update(dialog);
		final ITableEntityManager dialog_tmgr = MySpaceUtils.getTableEntityManager(SimpleDialogItem.class);
		IDataObjectQuery<SimpleDialogItem> qs = dialog_tmgr.query(
				new ExpressionValue("dialogId=? order by sentDate desc", new Object[] { dialog.getId() }), SimpleDialogItem.class);
		if (qs != null) {
			qs.setCount(10);
		}
		return qs;
	}

	public static SimpleDialog getSimpleDialog(final PageRequestResponse requestResponse) {
		final ID sentId = ItSiteUtil.getLoginUser(requestResponse).getId();
		final String userId = requestResponse.getRequestParameter("userid");
		if (StringUtils.hasText(userId)) {
			final ITableEntityManager dialog_mgr = MySpaceUtils.getTableEntityManager(SimpleDialog.class);
			dialog_mgr.reset();
			SimpleDialog dialog = dialog_mgr.queryForObject(new ExpressionValue("(sentId=? and toId=?) or (sentId=? and toId=?)", new Object[] {
					sentId, userId, userId, sentId }), SimpleDialog.class);
			if (dialog == null) {
				dialog = new SimpleDialog();
				dialog.setSentId(sentId);
				dialog.setToId(new LongID(userId));
				dialog_mgr.insert(dialog);
			}
			return dialog;
		}
		return null;
	}

	/**
	 * 建立对话
	 * @param requestResponse
	 * @return
	 */
	public static Object getDialogId(final PageRequestResponse requestResponse) {
		SimpleDialog dialog = getSimpleDialog(requestResponse);
		if (dialog == null)
			return null;
		return dialog.getId();
	}
}
