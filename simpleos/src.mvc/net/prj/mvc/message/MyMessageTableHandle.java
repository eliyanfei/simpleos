package net.prj.mvc.message;

import java.util.HashMap;
import java.util.Map;

import net.a.ItSiteUtil;
import net.itsite.utils.StringsUtils;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.content.ContentUtils;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.my.message.MessageUtils;
import net.simpleframework.my.message.SimpleMessage;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractTablePagerData;
import net.simpleframework.web.page.component.ui.pager.TablePagerColumn;
import net.simpleframework.web.page.component.ui.pager.db.AbstractDbTablePagerHandle;

/**
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-15下午09:20:42
 */
public class MyMessageTableHandle extends AbstractDbTablePagerHandle {

	@Override
	public Object getBeanProperty(ComponentParameter compParameter, String beanProperty) {
		if ("title".equals(beanProperty)) {
			final StringBuffer sb = new StringBuffer();
			final int messagetype = ConvertUtils.toInt(compParameter.getParameter("messagetype"), -1);
			final int mymessage_read = ConvertUtils.toInt(compParameter.getParameter("messageread"), -1);
			sb.append("<div style='float:left;' class='mymessage_read'>");
			sb.append("<a " + (mymessage_read == -1 ? "class='selected'" : "")
					+ " href=\"javascript:void(0);\" onclick=\"$IT.A('myMessageTable','messagetype=" + messagetype + "&messageread=-1');\">全部</a>");
			sb.append("<a " + (mymessage_read == 1 ? "class='selected'" : "")
					+ " href=\"javascript:void(0);\" onclick=\"$IT.A('myMessageTable','messagetype=" + messagetype + "&messageread=1');\">已读</a>");
			sb.append("<a " + (mymessage_read == 0 ? "class='selected'" : "")
					+ " href=\"javascript:void(0);\" onclick=\"$IT.A('myMessageTable','messagetype=" + messagetype + "&messageread=0');\">未读</a>");
			sb.append("</div>");
			sb.append("<div style='float:right;' class='mymessage_set'>");
			if (messagetype == 0) {
				sb.append("<a href=\"javascript:void(0);\" class=\"simple_btn simple_btn_all\" onclick=\"$IT.A('myMessageSendWin');\"><span class=\"left_icon icon-envelope\"></span>发私信</a>");
			}
			sb.append("<a href=\"javascript:void(0);\" id=\"idAbstractMyMessageTPage_markMenu\" class=\"simple_btn simple_btn_all\" style=\"padding-right: 7px;\" ><span class=\"left_icon icon-eye-open\"></span>标记为<span class=\"right_down_menu\"></span></a>");
			sb.append("<a href=\"javascript:void(0);\" class=\"simple_btn simple_btn_all\" onclick=\"del_mymessage();\" ><span class=\"left_icon icon-trash\"></span>删除</a>");
			sb.append("</div>");
			return sb.toString();
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, "messagetype");
		putParameter(compParameter, parameters, "messageread");
		putParameter(compParameter, parameters, "issend");
		return parameters;
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(ComponentParameter compParameter) {
		final IAccount account = ItSiteUtil.getLoginAccount(compParameter);
		if (account == null)
			return null;
		final int messagetype = ConvertUtils.toInt(compParameter.getParameter("messagetype"), -1);
		final int messageread = ConvertUtils.toInt(compParameter.getParameter("messageread"), -1);
		final boolean issend = ConvertUtils.toBoolean(compParameter.getParameter("issend"), false);
		final StringBuffer sql = new StringBuffer();
		if (issend) {
			sql.append("sentid=" + account.getId());
		} else {
			sql.append("toid=" + account.getId());
		}
		if (messagetype != -1) {
			sql.append(" and messageType=" + messagetype);
		}
		if (messageread != -1) {
			sql.append(" and messageread=" + messageread);
		}
		sql.append(" order by messageread, sentdate desc");
		final IQueryEntitySet<SimpleMessage> qs = MessageUtils.getTableEntityManager(SimpleMessage.class).query(new ExpressionValue(sql.toString()),
				SimpleMessage.class);
		return qs;
	}

	@Override
	public AbstractTablePagerData createTablePagerData(final ComponentParameter compParameter) {
		return new AbstractTablePagerData(compParameter) {
			final boolean issend = ConvertUtils.toBoolean(compParameter.getParameter("issend"), false);
			TablePagerColumn sendCol, toCol;

			@Override
			public Map<String, TablePagerColumn> getTablePagerColumns() {
				Map<String, TablePagerColumn> map = super.getTablePagerColumns();
				if (issend) {
					sendCol = map.remove("sendid");
					if (toCol != null)
						map.put("toid", toCol);
				} else {
					toCol = map.remove("toid");
					if (sendCol != null)
						map.put("sendid", sendCol);
				}
				return map;
			}

			@Override
			protected Map<Object, Object> getRowData(Object dataObject) {
				final Map<Object, Object> data = new HashMap<Object, Object>();
				final SimpleMessage message = (SimpleMessage) dataObject;
				final StringBuffer send = new StringBuffer();
				final String uParam = OrgUtils.um().getUserIdParameterName() + "=" + message.getSentId();
				send.append("<a onclick=\"$Actions['myMessageSentWindow']('" + uParam + "');\"");
				send.append(">发消息</a>");
				send.append("&nbsp;&nbsp;<a onclick=\"$Actions['myDialogWindow']('userid=" + message.getSentId() + "');\"");
				send.append(">对话</a>");
				data.put("sendid", ContentUtils.getAccountAware().wrapAccountHref(compParameter, message.getSentId()) + "->" + send.toString());
				data.put("toid", message.getUserText(message.getToId()));
				data.put("sentdate", ConvertUtils.toDateString(message.getSentDate(), "yyyy-MM-dd HH:mm"));
				final StringBuffer title = new StringBuffer();
				title.append("<a style=\"" + (message.isMessageRead() ? "" : "font-weight: bold;")
						+ "\" href='javascript:void(0);' onclick=\"$IT.A('myMessageWin','myMessageId=" + message.getId() + "');\">");
				title.append(ItSiteUtil.getShortContent(StringsUtils.trimNull(message.getSubject(), message.getTextBody()), 30, true));
				title.append("</a>");
				data.put("subject", title.toString());
				return data;
			}
		};
	}
}
