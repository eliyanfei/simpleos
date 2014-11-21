package net.itsite;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.itsite.utils.SQLUtils;
import net.itsite.utils.StringsUtils;
import net.simpleframework.applets.notification.NotificationUtils;
import net.simpleframework.web.IWebApplication;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;

/**
 * 
 * @author 李岩飞 
 * @date 2014年11月19日 下午4:59:34 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public class ItSiteAjaxHandle extends AbstractAjaxRequestHandle {

	public IForward deleteMessageNotification(final ComponentParameter compParameter) throws Exception {
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				NotificationUtils.deleteMessageNotificationByMessageId(compParameter.getRequestParameter("messageId"));
			}
		});
	}

	/**
	 * 是否有数据源连接
	 * @param compParameter
	 * @return
	 */
	private boolean isConnection(final ComponentParameter compParameter) {
		final String driverClassName = compParameter.getParameter("driverClassName");
		final String url = compParameter.getParameter("url");
		final String username = compParameter.getParameter("username");
		final String password = compParameter.getParameter("password");
		try {
			Class.forName(driverClassName);
			Connection conn = DriverManager.getConnection(url, username, password);
			SQLUtils.closeAll(null, null, conn);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 测试数据源
	 * @param compParameter
	 * @return
	 * @throws Exception
	 */
	public IForward doConnectionTest(final ComponentParameter compParameter) throws Exception {
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				if (isConnection(compParameter)) {
					json.put("rs", "连接成功!");
				} else {
					json.put("rs", "连接失败!");
				}
			}
		});
	}

	/**
	 * 保存数据源
	 * @param compParameter
	 * @return
	 * @throws Exception
	 */
	public IForward doConnectionSave(final ComponentParameter compParameter) throws Exception {
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				if (!isConnection(compParameter)) {
					json.put("rs", "连接失败!");
					return;
				}
				final String driverClassName = compParameter.getParameter("driverClassName");
				final String url = compParameter.getParameter("url");
				final String username = compParameter.getParameter("username");
				final String password = compParameter.getParameter("password");
				final String dbtype = compParameter.getParameter("dbtype");
				try {
					final String path = IWebApplication.Instance.getApplication().getServletContext().getRealPath("/base.properties");
					final File dsFile = new File(path);
					Properties pro = new Properties();
					if (dsFile.exists()) {
						pro.load(new FileInputStream(dsFile));
					}
					if ("in".equals(dbtype)) {
						pro.put("base", "MySql_medium.properties");
					} else {
						pro.remove("base");
					}
					pro.put("driverClassName", driverClassName);
					pro.put("driverClassName", driverClassName);
					pro.put("url", url);
					pro.put("username", username);
					pro.put("password", password);
					pro.put("testWhileIdle", "true");
					pro.put("timeBetweenEvictionRunsMillis", "14400000");
					FileOutputStream fos = new FileOutputStream(path);
					pro.store(fos, null);
					fos.flush();
					fos.close();
					IWebApplication.Instance.getApplication().init2();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 设置语言
	 * @throws Exception
	 */
	public IForward doLanguage(final ComponentParameter compParameter) throws Exception {
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				String language = compParameter.getParameter("language");
				ItSiteUtil.setLanguage(language);
			}
		});
	}

	/**
	 * 设置皮肤
	 * @throws Exception
	 */
	public IForward doSkin(final ComponentParameter compParameter) throws Exception {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				String skin = compParameter.getParameter("skin");
				ItSiteUtil.setSkin(skin, ItSiteUtil.getLoginAccount(compParameter), compParameter.request);
			}
		});
	}
}
