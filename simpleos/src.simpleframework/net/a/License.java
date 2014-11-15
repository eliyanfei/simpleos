package net.a;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import net.itsite.utils.DateUtils;
import net.itsite.utils.IOUtils;

public final class License {

	java.util.Date createDate;//创建日期
	java.util.Date timeoutDate;//截止日期
	java.util.Date lastmodifyDate;//上次修改日期
	String mac;
	Properties props;

	java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	String licFile;

	public License() {
		this.props = new Properties();
	}

	public void setCreateDate(java.util.Date createDate) {
		this.createDate = createDate;
		props.setProperty("createDate", sdf.format(createDate));
	}

	public void setTimeoutDate(java.util.Date timeoutDate) {
		this.timeoutDate = timeoutDate;
		props.setProperty("timeoutDate", sdf.format(timeoutDate));
	}

	public void setMac(String mac) {
		this.mac = mac;
		props.setProperty("mac", mac);
	}

	public Properties getProps() {
		return props;
	}

	public static final License LICENSE = new License("rams.lic");

	public License(String licFile) {
		this.licFile = licFile;
		Properties prop = new Properties();
		try {
			byte[] licByte = IOUtils.readFromFile(new File(licFile));
			byte[] b = org.apache.commons.codec.binary.Base64.decodeBase64(licByte);
			b = org.apache.commons.codec.binary.Base64.decodeBase64(b);
			prop.load(new ByteArrayInputStream(b));
		} catch (Exception ex) {
			prop.setProperty("createDate", "2000-01-01");
			prop.setProperty("timeoutDate", "2000-01-01");
			prop.setProperty("lastmodifyDate", "2000-01-01");
		}
		String createDate = prop.getProperty("createDate");
		this.createDate = DateUtils.parseDateDayFormat(createDate);

		String timeoutDate = prop.getProperty("timeoutDate");
		this.timeoutDate = DateUtils.parseDateDayFormat(timeoutDate);

		String lastmodifyDate = prop.getProperty("lastmodifyDate");
		this.lastmodifyDate = DateUtils.parseDateDayFormat(lastmodifyDate);

		String mac = prop.getProperty("mac");
		this.mac = mac;

		this.props = prop;
	}

	public void save() throws IOException {
		props.setProperty("lastmodifyDate", sdf.format(new Date()));
		StringBuilder sbuilder = new StringBuilder();
		for (Enumeration e = props.keys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			String val = props.getProperty(key);
			sbuilder.append(key + "=" + val).append("\r\n");
		}
		byte[] result = org.apache.commons.codec.binary.Base64.encodeBase64(sbuilder.toString().getBytes());
		result = org.apache.commons.codec.binary.Base64.encodeBase64(result);
		IOUtils.writeToFile(new File(licFile), result);
	}

	public static final int OK = 0;
	public static final int TIMEOUT = 1;
	public static final int MAC_ERROR = 2;
	public static final int TIME_ERROR = 3;

	public static final String ERROR_TIMEOUT = "许可证过期";
	public static final String ERROR_MAC = "许可证和机器码不匹配";
	public static final String ERROR_TIME = "许可证日期和系统时钟不匹配";

	public static final String getErrorMessage(int error) {
		if (error == 1)
			return ERROR_TIMEOUT;
		else if (error == 2)
			return ERROR_MAC;
		else if (error == 3)
			return ERROR_TIME;
		return "未知原因，请与厂商联系";
	}

	public int vaild() throws IOException {
		save();
		Date now = new Date();
		if (null == mac || "".equals(mac)) {//没有MAC地址为试用LICENSE
			if (now.before(this.createDate)) {
				return TIME_ERROR;
			}
			if (now.before(this.lastmodifyDate)) {
				return TIME_ERROR;
			}
			if (now.after(this.timeoutDate)) {
				return TIMEOUT;
			}
		} else {
		}
		return OK;
	}

	public void setLicFile(String licFile) {
		this.licFile = licFile;
	}

}
