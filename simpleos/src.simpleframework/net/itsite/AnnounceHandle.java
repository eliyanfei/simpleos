package net.itsite;

import java.util.Map;

import net.itniwo.commons.db.DataSourceFactory;
import net.prj.manager.PrjMgrUtils;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;

public class AnnounceHandle extends AbstractAjaxRequestHandle {

	@Override
	public Object getBeanProperty(ComponentParameter compParameter, String beanProperty) {
		return super.getBeanProperty(compParameter, beanProperty);
	}

	public static String queryAnnounce() {
		Map<String, Object> map = null;//PrjMgrUtils.appModule.queryBean(new SQLValue("select text from gaotie_announce where id=0"));
		return (String) (map == null ? "无公告" : map.get("TEXT"));
	}

	public IForward saveAnnounce(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				String text = compParameter.getRequestParameter("announceValue");
				IAccount account = AccountSession.getLogin(compParameter.getSession());
				Map<String, Object> map = PrjMgrUtils.appModule.queryBean(new SQLValue("select text from gaotie_announce where id=0"));
				if (map == null) {
					DataSourceFactory.getJdbcTemplate().execute("insert into gaotie_announce value(0,'" + text + "'," + account.getId() + ")");
				} else {
					DataSourceFactory.getJdbcTemplate().execute(
							"update gaotie_announce set text='" + text + "',userId=" + account.getId() + " where id=0");
				}
			}
		});
	}

	public IForward getAnnounce(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				json.putAll(PrjMgrUtils.appModule.queryBean(new SQLValue("select text from gaotie_announce where id=0")));
			}
		});
	}
}
