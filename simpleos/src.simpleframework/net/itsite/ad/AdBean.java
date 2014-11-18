package net.itsite.ad;

import java.util.Calendar;
import java.util.Date;

import net.itsite.utils.DateUtils;
import net.simpleframework.core.bean.AbstractIdDataObjectBean;

public class AdBean extends AbstractIdDataObjectBean {
	private int adType;// 0表示图片，1表示文字
	private String src;// 地址
	private String url;// 地址
	private String content;// 文字
	private int days;// 运行天数
	private int status;// 0,表示还没有开始，1，表示运行中，2，表示已经结束
	private EAd ad;// 位置
	private Date startDate;// 开始时间

	public boolean startup() {
		return this.status == 1;
	}

	public String getStartText() {
		final StringBuffer buf = new StringBuffer();
		if (status == 0) {
			buf.append("广告还没有运行");
		} else if (status == 2) {
			buf.append("广告被暂停或者已结束");
		} else if (status == 1) {
			buf.append("广告正在运行,已运行"
					+ DateUtils.onLineTimeInfo(startDate.getTime()));
		}
		return buf.toString();
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public EAd getAd() {
		return ad;
	}

	public void setAd(EAd ad) {
		this.ad = ad;
	}

	public int getAdType() {
		return adType;
	}

	public void setAdType(int adType) {
		this.adType = adType;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getSrc() {
		return src;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public Date getStartDate() {
		return startDate;
	}

	/**
	 * 检查广告状态
	 * 
	 * @return
	 */
	public boolean checkAdStatus() {
		if (days == -1) {
			return false;
		}
		final Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		final Calendar cal1 = Calendar.getInstance();
		cal1.setTime(startDate);
		cal1.add(Calendar.DATE, days);
		if (cal.getTimeInMillis() <= cal1.getTimeInMillis()) {// 启动广告
			this.status = 1;
			return true;
		} else if (cal.getTimeInMillis() > cal1.getTimeInMillis()) {// 停止广告
			this.status = 2;
			return true;
		}
		return false;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

}
