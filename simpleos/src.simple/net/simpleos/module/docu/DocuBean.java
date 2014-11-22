package net.simpleos.module.docu;

import java.util.Date;

import net.simpleframework.content.EContentType;
import net.simpleframework.core.id.ID;
import net.simpleos.impl.AbstractCommonBeanAware;

/**
 * 
 * 一般按总排行显示
 */
public class DocuBean extends AbstractCommonBeanAware {
	private String title;//标题
	private String keyworks;//关键字
	private ID catalogId;//目录
	private EDocuStatus status;//状态
	private EContentType ttype;//推荐
	private String path1;//文件存储路径
	private String extension;//后缀
	private String fileName;//fileanme
	private long fileSize;//文件大小
	private long downCounter;//被下载次数
	private Date lastDownDate;//最后下载日志
	private ID sameId;//相同ID

	public void setSameId(ID sameId) {
		this.sameId = sameId;
	}

	public ID getSameId() {
		return sameId;
	}

	@Override
	public String getTopicName() {
		return title;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setKeyworks(String keyworks) {
		this.keyworks = keyworks;
	}

	public String getKeyworks() {
		return keyworks;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getExtension() {
		return extension;
	}

	public long getDownCounter() {
		return downCounter;
	}

	public void setDownCounter(long downCounter) {
		this.downCounter = downCounter;
	}

	public Date getLastDownDate() {
		return lastDownDate;
	}

	public void setLastDownDate(Date lastDownDate) {
		this.lastDownDate = lastDownDate;
	}

	public EDocuStatus getStatus() {
		return status == null ? EDocuStatus.edit : status;
	}

	public void setStatus(EDocuStatus status) {
		this.status = status;
	}

	public void setTtype(EContentType ttype) {
		this.ttype = ttype;
	}

	public EContentType getTtype() {
		return ttype == null ? EContentType.normal : ttype;
	}

	public String getPath1() {
		return path1;
	}

	public void setPath1(String path1) {
		this.path1 = path1;
	}

	public void setCatalogId(ID catalogId) {
		this.catalogId = catalogId;
	}

	public ID getCatalogId() {
		return catalogId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
