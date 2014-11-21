package net.simpleframework.content.component.filepager;

import net.simpleframework.content.AbstractContent;
import net.simpleframework.core.id.ID;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class FileBean extends AbstractContent {
	private static final long serialVersionUID = -6601627946226253367L;

	private ID catalogId;

	private String topic;

	private String filename;

	private String filetype;

	private long filesize;

	private String description;

	private long downloads;

	private String md5, sha1;

	private String ip;

	private int type;//类型

	private ID refId;//引用id

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setRefId(ID refId) {
		this.refId = refId;
	}

	public ID getRefId() {
		return refId;
	}

	public ID getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(final ID catalogId) {
		this.catalogId = catalogId;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(final String topic) {
		this.topic = topic;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(final String filename) {
		this.filename = filename;
	}

	public String getFiletype() {
		return filetype;
	}

	public void setFiletype(final String filetype) {
		this.filetype = filetype;
	}

	public long getFilesize() {
		return filesize;
	}

	public void setFilesize(final long filesize) {
		this.filesize = filesize;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public long getDownloads() {
		return downloads;
	}

	public void setDownloads(final long downloads) {
		this.downloads = downloads;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(final String md5) {
		this.md5 = md5;
	}

	public String getSha1() {
		return sha1;
	}

	public void setSha1(final String sha1) {
		this.sha1 = sha1;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(final String ip) {
		this.ip = ip;
	}
}
