package net.simpleframework.core;

import java.util.List;

import net.simpleframework.ado.db.TableEntityManager;
import net.simpleframework.util.IConstants;
import net.simpleframework.util.StringUtils;
import net.simpleos.SimpleosUtil;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class ApplicationConfig {
	static {
		// LocaleI18n.addBasename(ApplicationConfig.class);
	}

	private List<IInitializer> initializerList;

	private String tempDir;

	private String datePattern;

	private String title;

	private String charset;

	private String tableEntityManagerClass;

	private boolean resourceCompress = true, autoDatabase = true;

	public String getTitle() {
		if (title == null) {
			return SimpleosUtil.title;
		}
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public List<IInitializer> getInitializerList() {
		return initializerList;
	}

	public void setInitializerList(final List<IInitializer> initializerList) {
		this.initializerList = initializerList;
	}

	public String getTempDir() {
		return tempDir;
	}

	public void setTempDir(final String tempDir) {
		this.tempDir = tempDir;
	}

	public String getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(final String datePattern) {
		this.datePattern = datePattern;
	}

	public String getTableEntityManagerClass() {
		return StringUtils.text(tableEntityManagerClass,
				TableEntityManager.class.getName());
	}

	public void setTableEntityManagerClass(final String tableEntityManagerClass) {
		this.tableEntityManagerClass = tableEntityManagerClass;
	}

	public String getCharset() {
		return StringUtils.text(charset, IConstants.UTF8);
	}

	public void setCharset(final String charset) {
		this.charset = charset;
	}

	public boolean isResourceCompress() {
		return resourceCompress;
	}

	public void setResourceCompress(final boolean resourceCompress) {
		this.resourceCompress = resourceCompress;
	}

	public boolean isAutoDatabase() {
		return autoDatabase;
	}

	public void setAutoDatabase(final boolean autoDatabase) {
		this.autoDatabase = autoDatabase;
	}
}
