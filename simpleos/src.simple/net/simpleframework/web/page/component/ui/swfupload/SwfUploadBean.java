package net.simpleframework.web.page.component.ui.swfupload;

import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.AbstractContainerBean;
import net.simpleframework.web.page.component.IComponentRegistry;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class SwfUploadBean extends AbstractContainerBean {

	private String fileSizeLimit; // 单位： B、KB、MB、GB

	private int fileQueueLimit;

	private String fileTypes; // *.jpg;*.jpeg;*.gif;*.png;*.bmp

	private String fileTypesDesc;

	private boolean multiFileSelected;

	private String jsCompleteCallback;

	private String jobUpload;

	public SwfUploadBean(final IComponentRegistry componentRegistry,
			final PageDocument pageDocument, final Element element) {
		super(componentRegistry, pageDocument, element);
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

	public String getFileTypes() {
		return fileTypes;
	}

	public void setFileTypes(final String fileTypes) {
		this.fileTypes = fileTypes;
	}

	public String getFileTypesDesc() {
		return fileTypesDesc;
	}

	public void setFileTypesDesc(final String fileTypesDesc) {
		this.fileTypesDesc = fileTypesDesc;
	}

	public boolean isMultiFileSelected() {
		return multiFileSelected;
	}

	public void setMultiFileSelected(final boolean multiFileSelected) {
		this.multiFileSelected = multiFileSelected;
	}

	public String getJsCompleteCallback() {
		return jsCompleteCallback;
	}

	public void setJsCompleteCallback(final String jsCompleteCallback) {
		this.jsCompleteCallback = jsCompleteCallback;
	}

	public String getJobUpload() {
		return jobUpload;
	}

	public void setJobUpload(final String jobUpload) {
		this.jobUpload = jobUpload;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsCompleteCallback" };
	}
}
