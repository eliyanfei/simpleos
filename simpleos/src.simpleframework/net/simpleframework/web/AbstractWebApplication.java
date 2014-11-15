package net.simpleframework.web;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;

import net.db.EmbedMySqlServer;
import net.itniwo.commons.io.IOUtils;
import net.itsite.utils.SQLUtils;
import net.simpleframework.ado.DataObjectManagerFactory;
import net.simpleframework.core.ALoggerAware;
import net.simpleframework.core.ConsoleThread;
import net.simpleframework.core.ExecutorRunnable;
import net.simpleframework.core.IInitializer;
import net.simpleframework.core.ITaskExecutor;
import net.simpleframework.core.ITaskExecutorAware;
import net.simpleframework.core.Logger;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.component.login.LoginRegistry;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.DateUtils;
import net.simpleframework.util.HTTPUtils;
import net.simpleframework.util.IoUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.IPageContext;
import net.simpleframework.web.page.PageContext;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.base.ajaxrequest.AjaxRequestUtils;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public abstract class AbstractWebApplication extends HttpServlet implements IWebApplication, ITaskExecutorAware {
	protected final Logger logger = ALoggerAware.getLogger(getClass());

	protected ApplicationContext applicationContext;
	protected DataSource dataSource;
	protected String pageContext;

	protected boolean ok = true;

	@Override
	public void setPageContext(final String pageContext) {
		this.pageContext = pageContext;
	}

	@Override
	public boolean isOK() {
		return this.ok;
	}

	@Override
	public void init2() {
		try {
			/**
			 * 删除已经执行的sql脚本文件
			 */
			String sqlPath = this.getServletContext().getRealPath("/$resource/deploy/");
			final File file = new File(sqlPath);
			if (file.exists()) {
				file.listFiles(new FileFilter() {

					@Override
					public boolean accept(File pathname) {
						File f = new File(pathname.getAbsolutePath() + "/sql-script");
						if (f.exists()) {
							IOUtils.delete(f);
						}
						return false;
					}
				});
			}
		} catch (Exception e1) {
		}
		if (!this.ok) {
			final File dsFile = new File(this.getServletContext().getRealPath("/base.properties"));
			if (dsFile.exists()) {
				Properties pro = new Properties();
				try {
					pro.load(new FileInputStream(dsFile));
					dataSource = BasicDataSourceFactory.createDataSource(pro);
				} catch (Exception e) {
				}
			}
		}
		this.ok = true;

		try {
			IPageContext pc;
			if (StringUtils.hasText(pageContext)) {
				pc = (IPageContext) BeanUtils.newInstance(pageContext);
			} else {
				pc = new PageContext();
			}
			IPageContext.Instance.doInit(this.getServletContext(), pc);
		} catch (IOException e) {
			e.printStackTrace();
		}

		final List<IInitializer> initializerList = getApplicationConfig().getInitializerList();
		if (initializerList != null) {
			final List<IInitializer> list = new ArrayList<IInitializer>();
			for (final IInitializer initializer : initializerList) {
				if (initializer.isSync()) {
					try {
						initializer.doInit(AbstractWebApplication.this);
					} catch (final Throwable th) {
						logger.error(th);
					}
				} else {
					list.add(initializer);
				}
			}
			if (list.size() > 0) {
				getTaskExecutor().execute(new ExecutorRunnable() {
					@Override
					public void task() {
						for (final IInitializer initializer : list) {
							try {
								initializer.doInit(AbstractWebApplication.this);
							} catch (final Throwable th) {
								logger.error(th);
							}
						}
					}
				});
			}
		}

		getTaskExecutor().addScheduledTask(DateUtils.to24Hour(), DateUtils.DAY_PERIOD, new ExecutorRunnable() {
			@Override
			public void task() {
				DataObjectManagerFactory.resetAll();
			}
		});
	}

	EmbedMySqlServer sqlServer;

	@Override
	public void init(final ServletConfig config) throws ServletException {
		super.init(config);
		final ServletContext servletContext = config.getServletContext();
		final File projectFile = new File(servletContext.getRealPath("/WEB-INF/" + getSpringProjectName()));
		if (!projectFile.exists()) {
			applicationContext = new ClassPathXmlApplicationContext(BeanUtils.getResourceClasspath(getClass(), getSpringProjectName()));
		} else {
			applicationContext = new FileSystemXmlApplicationContext(projectFile.getAbsolutePath());
		}

		final File dsFile = new File(servletContext.getRealPath("/base.properties"));
		if (dsFile.exists()) {
			Properties pro = new Properties();
			try {
				pro.load(new FileInputStream(dsFile));
				if (StringUtils.hasText(pro.getProperty("base"))) {
					final File mysqlFile = new File(servletContext.getRealPath("/mysql-em/" + pro.getProperty("base")));
					Properties dbpro = new Properties();
					dbpro.load(new FileInputStream(mysqlFile));
					EmbedMySqlServer.setEmbedMySqlHome(servletContext.getRealPath(""));
					sqlServer = new EmbedMySqlServer(dbpro);
					sqlServer.startup();
				}
				dataSource = BasicDataSourceFactory.createDataSource(pro);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		IWebApplication.Instance.doInit(servletContext, this);
		ConsoleThread.doInit();
		/**
		 * 不存在数据源
		 */
		if (SQLUtils.isOpen(getDataSource())) {
			ok = true;
			init2();
		} else {
			ok = false;
		}
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public Object getBean(final String key) {
		return getApplicationContext().getBean(key);
	}

	protected String getSpringProjectName() {
		return "project.xml";
	}

	private final static String DEPLOY_PATH = "/deploy/";

	@Override
	public String getApplicationHomePath() {
		return PageUtils.getResourcePath() + DEPLOY_PATH;
	}

	@Override
	public String getApplicationAbsolutePath(final String path) {
		return getServletContext().getRealPath(path);
	}

	@Override
	public DataSource getDataSource(final String datasourceName) {
		return dataSource != null ? dataSource : (DataSource) getBean(datasourceName);
	}

	@Override
	public DataSource getDataSource() {
		return getDataSource(defaultDataSourceName);
	}

	@Override
	public WebApplicationConfig getApplicationConfig() {
		return (WebApplicationConfig) getBean("idApplicationConfig");
	}

	@Override
	public ITaskExecutor getTaskExecutor() {
		return (ITaskExecutor) getBean("idTaskExecutor");
	}

	@Override
	public void destroy() {
		super.destroy();
		if (sqlServer != null) {
			sqlServer.shutdown();
		}
		getTaskExecutor().close();
		try {
			IoUtils.deleteAll(new File(WebUtils.getTempPath(getServletContext())));
		} catch (final IOException e) {
		}
	}

	@Override
	public boolean isSystemUrl(final PageRequestResponse requestResponse) {
		final String requestURI = HTTPUtils.getRequestURI(requestResponse.request);
		if (requestURI.indexOf(AjaxRequestUtils.getHomePath() + "/jsp/ajax_request.jsp") > -1) {
			return true;
		}
		final LoginRegistry registry = (LoginRegistry) AbstractComponentRegistry.getRegistry(LoginRegistry.login);
		if (registry != null) {
			if (requestURI.indexOf(registry.getComponentResourceProvider().getResourceHomePath() + "/jsp/location.jsp") > -1) {
				return true;
			}
		}
		if (OrgUtils.deployPath != null) {
			if (requestURI.indexOf(OrgUtils.deployPath + "jsp/") > -1) {
				return true;
			}
			if (requestURI.indexOf(OrgUtils.applicationModule.getLoginUrl(requestResponse)) > -1) {
				return true;
			}
		}
		return false;
	}
}
