package net.simpleframework.my.file;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MyFileStat extends AbstractIdDataObjectBean {
	private static final long serialVersionUID = 5199585008779645894L;

	private long fileSizeLimit;

	private long fileUploadLimit;

	private int allFiles;

	private long allFilesSize;

	private int rootFiles;

	private long rootFilesSize;

	private int deleteFiles;

	private long deleteFilesSize;

	public long getFileSizeLimit() {
		return fileSizeLimit;
	}

	public void setFileSizeLimit(final long fileSizeLimit) {
		this.fileSizeLimit = fileSizeLimit;
	}

	public long getFileUploadLimit() {
		return fileUploadLimit;
	}

	public void setFileUploadLimit(final long fileUploadLimit) {
		this.fileUploadLimit = fileUploadLimit;
	}

	public int getAllFiles() {
		return allFiles;
	}

	public void setAllFiles(final int allFiles) {
		this.allFiles = allFiles;
	}

	public long getAllFilesSize() {
		return allFilesSize;
	}

	public void setAllFilesSize(final long allFilesSize) {
		this.allFilesSize = allFilesSize;
	}

	public int getRootFiles() {
		return rootFiles;
	}

	public void setRootFiles(final int rootFiles) {
		this.rootFiles = rootFiles;
	}

	public long getRootFilesSize() {
		return rootFilesSize;
	}

	public void setRootFilesSize(final long rootFilesSize) {
		this.rootFilesSize = rootFilesSize;
	}

	public int getDeleteFiles() {
		return deleteFiles;
	}

	public void setDeleteFiles(final int deleteFiles) {
		this.deleteFiles = deleteFiles;
	}

	public long getDeleteFilesSize() {
		return deleteFilesSize;
	}

	public void setDeleteFilesSize(final long deleteFilesSize) {
		this.deleteFilesSize = deleteFilesSize;
	}
}
