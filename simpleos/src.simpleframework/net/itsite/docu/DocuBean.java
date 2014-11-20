package net.itsite.docu;

import java.util.Date;

import net.itsite.impl.AbstractCommonBeanAware;
import net.simpleframework.content.EContentType;
import net.simpleframework.core.id.ID;

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
	private EDocuFunction docuFunction = EDocuFunction.data;//分类
	private String language;//代码语言
	private String path1;//文件存储路径
	private String path2;//部分存储路径
	private String extension;//后缀
	private String fileName;//fileanme
	private long fileSize;//文件大小
	private int success;//转换成功
	private int fileNum;//swf的数量或者压缩包的数量
	private double allowRead;//允许读取的页面 
	private int point;//积分 
	private long downCounter;//被下载次数
	private Date lastDownDate;//最后下载日志
	private float totalGrade;// 总评分
	private long gradeCounter;//评分次数
	private float adminGrade;// 管理员评分
	private String permissions;//查看权限
	private ID sameId;//相同ID
	private boolean canRun = false;//可以运行

	public void setCanRun(boolean canRun) {
		this.canRun = canRun;
	}

	public boolean isCanRun() {
		return canRun;
	}

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

	public void setSuccess(int success) {
		this.success = success;
	}

	public int getSuccess() {
		return success;
	}

	public String getSuccessStatus() {
		switch (success) {
		case 1:
			return "转换中";
		case 2:
			return "转换成功";
		case 3:
			return "转换失败";
		default:
			return "待转换";
		}
	}

	public String getPermissions() {
		return permissions;
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLanguage() {
		return language == null ? "java" : language;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setGradeCounter(long gradeCounter) {
		this.gradeCounter = gradeCounter;
	}

	public long getGradeCounter() {
		return gradeCounter;
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

	public int getFileNum() {
		return fileNum;
	}

	public void setFileNum(int fileNum) {
		this.fileNum = fileNum;
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

	public float getTotalGrade() {
		return totalGrade;
	}

	public int getStarGrade() {
		return (int) (this.totalGrade * 10);
	}

	public void setTotalGrade(float totalGrade) {
		this.totalGrade = totalGrade;
	}

	/**
	 * 获得最后总分
	 * @param totalGrade
	 * @return
	 */
	public void statGrade(float totalGrade) {
		float t = totalGrade;
		if (t != 0) {
			if (this.totalGrade != 0) {
				t = (t + this.totalGrade) / 2;
			}
		} else {
			t = this.totalGrade;
		}
		float g = adminGrade + (5 - adminGrade) / 5f * t;
		g = ((int) (Math.floor(g * 2) / 2 * 10)) / 10f;
		this.totalGrade = (float) g;
	}

	public float getAdminGrade() {
		return adminGrade;
	}

	public void setAdminGrade(float adminGrade) {
		this.adminGrade = adminGrade;
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

	public EDocuFunction getDocuFunction() {
		return docuFunction;
	}

	public void setDocuFunction(EDocuFunction docuFunction) {
		this.docuFunction = docuFunction;
	}

	public String getPath1() {
		return path1;
	}

	public void setPath1(String path1) {
		this.path1 = path1;
	}

	public String getPath2() {
		return path2;
	}

	public void setPath2(String path2) {
		this.path2 = path2;
	}

	public double getAllowRead() {
		return allowRead;
	}

	public void setAllowRead(double allowRead) {
		this.allowRead = allowRead;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
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
