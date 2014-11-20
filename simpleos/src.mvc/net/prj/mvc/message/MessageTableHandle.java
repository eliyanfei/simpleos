package net.prj.mvc.message;

import java.util.HashMap;
import java.util.Map;

import net.itsite.ItSiteUtil;
import net.itsite.utils.StringsUtils;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.my.message.MessageUtils;
import net.simpleframework.my.message.SimpleMessage;
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
public class MessageTableHandle extends AbstractDbTablePagerHandle {

	@Override
	public Object getBeanProperty(ComponentParameter compParameter, String beanProperty) {
		if ("title".equals(beanProperty)) {
			final StringBuffer sb = new StringBuffer();
			sb.append("<div style='float:right;' class='mymessage_set'>");
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
		return parameters;
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(ComponentParameter compParameter) {
		final IAccount account = ItSiteUtil.getLoginAccount(compParameter);
		if (account == null)
			return null;
		final int messagetype = ConvertUtils.toInt(compParameter.getParameter("messagetype"), -1);
		final StringBuffer sql = new StringBuffer("1=1");
		if (messagetype != -1) {
			sql.append(" and messageType=" + messagetype);
		}
		sql.append(" order by messageread, sentdate desc");
		final IQueryEntitySet<SimpleMessage> qs = MessageUtils.getTableEntityManager(SimpleMessage.class).query(new ExpressionValue(sql.toString()),
				SimpleMessage.class);
		return qs;
	}

	@Override
	public AbstractTablePagerData createTablePagerData(final ComponentParameter compParameter) {
		return new AbstractTablePagerData(compParameter) {

			@Override
			public Map<String, TablePagerColumn> getTablePagerColumns() {
				Map<String, TablePagerColumn> map = super.getTablePagerColumns();
				return map;
			}

			@Override
			protected Map<Object, Object> getRowData(Object dataObject) {
				final Map<Object, Object> data = new HashMap<Object, Object>();
				final SimpleMessage message = (SimpleMessage) dataObject;
				data.put("sendid", message.getUserText(message.getSentId()));
				data.put("toid", message.getUserText(message.getToId()));
				data.put("sentdate", ConvertUtils.toDateString(message.getSentDate(), "yyyy-MM-dd HH:mm"));
				final StringBuffer title = new StringBuffer();
				title.append("<a style=\"" + (message.isMessageRead() ? "" : "font-weight: bold;")
						+ "\" href='javascript:void(0);' onclick=\"$IT.A('messageWin','myMessageId=" + message.getId() + "');\">");
				title.append(ItSiteUtil.getShortContent(StringsUtils.trimNull(message.getSubject(), message.getTextBody()), 20, true));
				title.append("</a>");
				data.put("subject", title.toString());
				return data;
			}
		};
	}

}
