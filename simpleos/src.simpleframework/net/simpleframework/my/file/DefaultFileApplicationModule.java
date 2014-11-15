package net.simpleframework.my.file;

import java.util.Map;

import net.simpleframework.content.component.filepager.FileLobBean;
import net.simpleframework.core.ExecutorRunnable;
import net.simpleframework.core.IInitializer;
import net.simpleframework.core.ITaskExecutorAware;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.organization.IJob;
import net.simpleframework.util.DateUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.AbstractWebApplicationModule;
import net.simpleframework.web.page.PageRequestResponse;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultFileApplicationModule extends AbstractWebApplicationModule implements IFileApplicationModule {
	static final String deployName = "my/file";

	@Override
	protected void putTables(final Map<Class<?>, Table> tables) {
		tables.put(MyFileStat.class, new Table("simple_my_file_stat"));
		tables.put(MyFolder.class, new Table("simple_my_folder"));
		tables.put(MyFile.class, new Table("simple_my_file"));
		tables.put(FileLobBean.class, new Table("simple_my_file_lob", true));
	}

	@Override
	public void init(final IInitializer initializer) {
		super.init(initializer);
		doInit(MyFileUtils.class, deployName);

		((ITaskExecutorAware) getApplication()).getTaskExecutor().addScheduledTask(60 * 5, DateUtils.DAY_PERIOD, new ExecutorRunnable() {
			@Override
			public void task() {
				MyFileUtils.doStatRebuild();
			}
		});
	}

	private long defaultFileSizeLimit;

	private long defaultFileUploadLimit;

	@Override
	public String getManager(final Object... params) {
		return IJob.sj_account_normal;
	}

	@Override
	public long getDefaultFileSizeLimit(final PageRequestResponse requestResponse) {
		return defaultFileSizeLimit > 0 ? defaultFileSizeLimit : (5 * 1024 * 1024 * 1024);
	}

	public void setDefaultFileSizeLimit(final long defaultFileSizeLimit) {
		this.defaultFileSizeLimit = defaultFileSizeLimit;
	}

	@Override
	public long getDefaultFileUploadLimit(final PageRequestResponse requestResponse) {
		return defaultFileUploadLimit > 0 ? defaultFileUploadLimit : (7 * 1024 * 1024);
	}

	public void setDefaultFileUploadLimit(final long defaultFileUploadLimit) {
		this.defaultFileUploadLimit = defaultFileUploadLimit;
	}

	@Override
	public String getOptionsPath(final PageRequestResponse requestResponse) {
		return "file_options.jsp";
	}

	@Override
	public String getApplicationText() {
		return StringUtils.text(super.getApplicationText(), LocaleI18n.getMessage("DefaultFileApplicationModule.0"));
	}
}
