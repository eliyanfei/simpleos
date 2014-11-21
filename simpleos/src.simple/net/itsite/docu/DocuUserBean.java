package net.itsite.docu;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.id.ID;

/**
 * 用户上传下载次数
 *
 */
public class DocuUserBean extends AbstractIdDataObjectBean {
	private long downFiles;//下载文件数量
	private long upFiles;//上传文件数量
	private ID userId;//用户

	public ID getUserId() {
		return userId;
	}

	public void setUserId(ID userId) {
		this.userId = userId;
	}

	public long getDownFiles() {
		return downFiles;
	}

	public void setDownFiles(long downFiles) {
		this.downFiles = downFiles;
	}

	public long getUpFiles() {
		return upFiles;
	}

	public void setUpFiles(long upFiles) {
		this.upFiles = upFiles;
	}

}
