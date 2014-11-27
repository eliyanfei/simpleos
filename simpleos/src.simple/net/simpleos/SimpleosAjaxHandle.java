package net.simpleos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;
import java.util.Properties;

import net.simpleframework.applets.notification.NotificationUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.IWebApplication;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;
import net.simpleos.commons.mysql.EmbedMySqlServer;
import net.simpleos.utils.SQLUtils;

/**
 * 
 * @author 李岩飞 
 * @date 2014年11月19日 下午4:59:34 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public class SimpleosAjaxHandle extends AbstractAjaxRequestHandle {

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

	private static EmbedMySqlServer sqlServer;

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
				final String dbtype = compParameter.getParameter("dbtype");
				if ("in".equals(dbtype)) {
					final File mysqlFile = new File(compParameter.getApplicationAbsolutePath("/mysql-em/MySql_medium.properties"));
					Properties dbpro = new Properties();
					dbpro.load(new FileInputStream(mysqlFile));
					EmbedMySqlServer.setEmbedMySqlHome(compParameter.getApplicationAbsolutePath(""));
					sqlServer = new EmbedMySqlServer(dbpro);
					sqlServer.startup();
				}
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
				final String dbtype = compParameter.getParameter("dbtype");
				final String driverClassName = compParameter.getParameter("driverClassName");
				final String url = compParameter.getParameter("url");
				final String username = compParameter.getParameter("username");
				final String password = compParameter.getParameter("password");
				try {
					final String path = IWebApplication.Instance.getApplication().getServletContext().getRealPath("/WEB-INF/db.properties");
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

					if ("in".equals(dbtype)) {
						if (StringUtils.hasText(pro.getProperty("base")) && !sqlServer.isRunning()) {
							final File mysqlFile = new File(compParameter.getApplicationAbsolutePath("/mysql-em/" + pro.getProperty("base")));
							Properties dbpro = new Properties();
							dbpro.load(new FileInputStream(mysqlFile));
							EmbedMySqlServer.setEmbedMySqlHome(compParameter.getApplicationAbsolutePath(""));
							EmbedMySqlServer sqlServer = new EmbedMySqlServer(dbpro);
							sqlServer.startup();
						}
					}
					if (!isConnection(compParameter)) {
						json.put("rs", "连接失败!");
						return;
					}

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
				SimpleosUtil.setLanguage(language);
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
				SimpleosUtil.setSkin(skin, SimpleosUtil.getLoginAccount(compParameter), compParameter.request);
			}
		});
	}
}
