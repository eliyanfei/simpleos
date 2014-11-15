package net.simpleframework.content.component.filepager;

import net.simpleframework.content.AbstractContentPagerBean;
import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.IComponentHandle;
import net.simpleframework.web.page.component.IComponentRegistry;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class FilePagerBean extends AbstractContentPagerBean {
	private String fileSizeLimit = "32MB";

	private int fileQueueLimit = 0;

	private String jobDownload;

	public FilePagerBean(final IComponentRegistry componentRegistry,
			final PageDocument pageDocument, final Element element) {
		super(componentRegistry, pageDocument, element);
		setAssertCatalogNull(true);
	}

	@Override
	public String getDataPath() {
		return getResourceHomePath() + "/jsp/filepager.jsp";
	}

	@Override
	protected Class<? extends IComponentHandle> getDefaultHandleClass() {
		return DefaultFilePagerHandle.class;
	}

	public String getFileSizeLimit() {
		return fileSizeLimit;
	}

	public void setFileSizeLimit(final String fileSizeLimit) {
		this.fileSizeLimit = fileSizeLimit;
	}

	public int getFileQueueLimit() {
		return fileQueueLimit;
	}

	public void setFileQueueLimit(final int fileQueueLimit) {
		this.fileQueueLimit = fileQueueLimit;
	}

	public String getJobDownload() {
		return jobDownload;
	}

	public void setJobDownload(final String jobDownload) {
		this.jobDownload = jobDownload;
	}
}
