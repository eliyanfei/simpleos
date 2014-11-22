package net.simpleos.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月20日 下午12:00:44 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public final class NumberUtils {

	public static String formatNumber(final String format, final double fz, final double fm) {
		return NumberUtils.formatNumber(format, fm == 0 ? 0 : fz / fm, Locale.getDefault());
	}

	public static double formatFmZero(final double fz, final double fm) {
		return fm == 0 ? 0 : fz / fm;
	}

	public static String formatNumber(final String format, final Object value) {
		return NumberUtils.formatNumber(format, value, Locale.getDefault());
	}

	public static String formatNumber(final String format, final Object value, final Locale locale) {
		if (value == null)
			return null;
		if (StringsUtils.isBlank(format))
			return value.toString();

		final NumberFormat nf = NumberFormat.getNumberInstance(locale);
		final DecimalFormat df = (DecimalFormat) nf;
		df.applyLocalizedPattern(format);
		try {
			return df.format(Double.parseDouble(value.toString()));
		} catch (final Exception e) {
			return value.toString();
		}
	}

	/**
	 * 检查一个字符串中是否是一个有效的数值
	 * 
	 * @param text
	 *            检查的字符串
	 * @param flags
	 *            位表示的检查选项 bit 1 : 可含'.' , bit 2 :可含',', bit 4 : 可含'-', bit 8 :
	 *            可为空
	 * @return true : 如果检查合法, false : 如果检查不合法
	 */
	public static boolean isNumber(final String text, final int flags) {
		if (text == null) {
			return false;
		}
		final int lText = text.length();
		int i = 0;
		for (; i < lText && text.charAt(i) <= ' '; i++) {
			;
		}
		if (i == lText) {
			return (flags & 8) != 0;
		}
		if (text.charAt(i) == '-') {
			if ((flags & 4) == 0 || ++i >= lText) {
				return false;
			}
		}
		int ndig = 0;
		for (; i < lText; i++) {
			final char c = text.charAt(i);
			if (c >= '0' && c <= '9') {
				ndig++;
				continue;
			}
			if (c == ',' && (flags & 2) != 0) {
				continue;
			}
			break;
		}
		if (ndig == 0) {
			return false;
		}
		if (i < lText && text.charAt(i) == '.') {
			if ((flags & 1) == 0) {
				return false;
			}
			i++;
			for (; i < lText; i++) {
				final char c = text.charAt(i);
				if (c < '0' || c > '9') {
					break;
				}
			}
		}
		for (; i < lText && text.charAt(i) <= ' '; i++) {
			;
		}
		return i == lText;
	}

	public static int[] toString(final String[] str) {
		final int[] rest = new int[str.length];
		for (int i = 0; i < str.length; i++) {
			rest[i] = Integer.parseInt(str[i]);
		}
		return rest;
	}

	public static boolean isNumber(final String text) {
		return NumberUtils.isNumber(text, 1);
	}

	public static boolean isMinusDouble(final String text) {
		if (StringUtils.isEmpty(text)) {
			return false;
		}
		final char[] chars = text.toCharArray();
		if (chars[0] != '-' || chars.length == 1) {
			return false;
		}
		return NumberUtils.isNumber(text.replace("-", ""));
	}

	// 通过方向角和距离来获取地图上另外一点的经纬度
	public static double CaculateCos(final float Angle, final float ff) {
		return ff * 0.1 / 9600 * java.lang.Math.cos((90 - Angle) * java.lang.Math.PI / 180);
	}

	public static double CaculateSin(final float Angle, final float ff) {
		return ff * 0.1 / 11000 * java.lang.Math.sin((90 - Angle) * java.lang.Math.PI / 180);
	}

	/**
	 * 获取0~max之间的一个随机数
	 * 
	 * @param max
	 *            最大值
	 * @return 0~max之间的一个随机数
	 */
	public static int rnd(final int max) {
		return (int) (Math.random() * max);
	}

	public static double decimal2double(final Object value) {
		double rValue = 0;
		if (value instanceof BigDecimal) {
			rValue = ((BigDecimal) value).doubleValue();
		} else if (value instanceof String) {
			rValue = Double.parseDouble(String.valueOf(value).trim());
		} else if (value instanceof Double) {
			rValue = (Double) value;
		}
		return rValue;
	}

	public static int decimal2int(final Object value) {
		int rValue = 0;
		if (value instanceof BigDecimal) {
			rValue = ((BigDecimal) value).intValue();
		} else if (value instanceof String) {
			rValue = Integer.parseInt(String.valueOf(value).trim());
		}
		return rValue;
	}

	public static double getDoubleValue(final String doubleValue) {
		try {
			return NumberUtils.decimal2double(doubleValue);
		} catch (final Exception e) {
		}
		return 0;
	}

	public static double getDoubleValue(final Map<String, String> maps, final String key) {
		return NumberUtils.getDoubleValue(maps.get(key));
	}

	public static int getIntValue(final String doubleValue) {
		try {
			return NumberUtils.decimal2int(doubleValue);
		} catch (final Exception e) {
		}
		return 0;
	}

	public static int getDefaultIntValue(final String doubleValue, int defaultIntValue) {
		try {
			return NumberUtils.decimal2int(doubleValue);
		} catch (final Exception e) {
			return defaultIntValue;
		}
	}

	public static int getIntValue(final Map<String, String> maps, final String key) {
		return NumberUtils.getIntValue(maps.get(key));
	}

	public static int bitStrToInt(final String bit) {
		if (StringUtils.isEmpty(bit))
			return -1;
		int result = 0;
		int len = bit.length();
		int[] data = new int[len];
		for (int i = 0; i < len; i++) {
			data[i] = Integer.parseInt(bit.substring(i, i + 1));
			if (i == len - 1) {
				result += data[i];
			} else {
				int t = data[i];
				for (int j = 0; j < len - 1 - i; j++) {
					t *= 2;
				}

				result += t;
			}
		}
		return result;
	}

	/**
	 * 将一个Byte数字转换成int类型的数字,值范围通常在0~255, 如果给定的bValue小于0再原有值上加上128返回,否则返回原值.
	 * 
	 * @param bValue
	 * @return 0~255的int类型数字
	 */
	public static final int byteToInt(final byte bValue) {
		return bValue < 0 ? bValue + 256 : bValue;
	}

	/**
	 * 将一个int类型数字转换成字节数组
	 * 
	 * @param value
	 *            要转换的int类型数字
	 * @return 长度为4的byte数组.
	 */
	public static final byte[] intToByteArray(final int value) {
		return new byte[] { (byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) value };
	}

	/**
	 * 将一个字节数组转换成int类型数字
	 * 
	 * @param bytes
	 *            长度在1到4范围的byte数组.
	 * @return int类型数字.
	 */
	public static final int byteArrayToInt(final byte... bytes) {
		// return (b[0] << 24) + ((b[1] & 0xFF) << 16) + ((b[2] & 0xFF) << 8) +
		// (b[3] & 0xFF);
		final BigInteger bi = new BigInteger(bytes);
		return bi.intValue();
	}

	/**
	 * 将一个long类型数字转换成字节数组
	 * 
	 * @param value
	 *            要转换的long类型数值
	 * @return 长度为8的byte数组
	 */
	public static final byte[] longToByteArray(final long value) {
		final BigInteger bi = BigInteger.valueOf(value);
		final byte tmpArr[] = bi.toByteArray();
		final byte[] result = new byte[8];
		System.arraycopy(tmpArr, 0, result, 8 - tmpArr.length, tmpArr.length);
		return result;
	}

	/**
	 * 将一个字节数组转换成long类型数字
	 * 
	 * @param bytes
	 *            长度为1到8的byte数组
	 * @return 转换成的long类型数值
	 */
	public static final long byteArrayToLong(final byte... bytes) {
		final BigInteger bi = new BigInteger(bytes);
		return bi.longValue();
	}

	public static long bytesArrayToLong(final byte... bytes) {
		final int len = bytes.length - 1;
		int leftShift = len * 8;
		long longValue = ((long) bytes[0] & 0xff) << leftShift;
		int startIdx = 0;
		while (startIdx++ < len) {
			leftShift -= 8;
			longValue = longValue | ((long) bytes[startIdx] & 0xff) << leftShift;
		}
		return longValue;
	}

	public static double toDouble(final String dbl, final double dlft) {
		try {
			return Double.parseDouble(dbl);
		} catch (final Exception e) {
			return dlft;
		}
	}

	public static double toDouble(final String dbl) {
		try {
			return Double.parseDouble(dbl);
		} catch (final Exception e) {
			return 0;
		}
	}

	/**
	 * Converts a two byte array to an integer
	 * 
	 * @param b
	 *            a byte array of length 2
	 * @return an int representing the unsigned short
	 */
	public static final int unsignedShortToInt(final byte... b) {
		int i = 0;
		i |= b[0] & 0xFF;
		i <<= 8;
		i |= b[1] & 0xFF;
		return i;
	}

	public static double round(final double v, final int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		final BigDecimal b = new BigDecimal(Double.toString(v));
		final BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static final String zeros8Str = "00000000";

	public static final StringBuilder byteArrayToBin(final byte... values) {
		final StringBuilder buf = new StringBuilder(values.length * 8);
		StringBuilder bufTmp;
		for (final byte value : values) {
			bufTmp = new StringBuilder(16);
			bufTmp.append(zeros8Str).append(Integer.toBinaryString(value));
			bufTmp.delete(0, bufTmp.length() - 8);
			buf.append(bufTmp);
		}
		return buf;
	}

	public static final StringBuilder byteArrayToBin(final String splitFlag, final byte... values) {
		final StringBuilder buf = new StringBuilder(values.length * 8 + values.length * splitFlag.length());
		StringBuilder bufTmp;
		boolean isFirst = true;
		for (final int value : values) {
			if (isFirst)
				isFirst = false;
			else
				buf.append(splitFlag);
			bufTmp = new StringBuilder(16);
			bufTmp.append(zeros8Str).append(Integer.toBinaryString(value));
			bufTmp.delete(0, bufTmp.length() - 8);
			buf.append(bufTmp);
		}
		return buf;
	}

	public static final String hexStringTobin(final String hex) {
		final StringBuilder buf = new StringBuilder();
		for (int i = 0; i < hex.length(); i++) {
			final String s = hex.substring(i, i + 1);
			buf.append(NumberUtils.toBinString(Integer.parseInt(s, 16), 4));
		}
		return buf.toString();
	}

	/**
	 * 将给定的16进制数据转换成byte数组,将给定的字符串按每两位字符转换成数字
	 * 
	 * @author afei
	 * @param hexString
	 * @return 例如:给定"0000",返回byte{0,0}
	 */
	public static final byte[] hexStringToBytes(final String hexString) {
		final int hexLen = hexString.length();
		if (hexLen % 2 != 0)
			throw new IllegalArgumentException("不标准的HEX数据格式..");
		final byte[] bytes = new byte[hexLen / 2];
		int bytesIdx = 0;
		for (int i = 0; i < hexLen;) {
			bytes[bytesIdx++] = (byte) Integer.parseInt(hexString.substring(i, i += 2), 16);
		}
		return bytes;
	}

	public static final String binStringToHex(final String bin) {
		return binStringToHex(bin, true);
	}

	/**
	 * 
	 * @param bin
	 * @param header
	 *            true 前面添加 false 后面添加
	 * @return
	 */
	public static final String binStringToHex(String bin, final boolean header) {
		final StringBuilder buf = new StringBuilder();
		bin = binStringLen8(bin, header);
		final int size = bin.length() / 8;
		for (int i = 0; i < size; i++) {
			buf.append(NumberUtils.toHexString(Integer.parseInt(bin.substring(i * 8, i * 8 + 8), 2)));
		}
		return buf.toString();
	}

	public static final String binStringLen8(String bin, final boolean header) {
		if (!StringsUtils.isBlank(bin)) {
			int size = 8 - bin.length() % 8;
			size = size == 8 ? 0 : size;
			for (int i = 0; i < size; i++) {
				if (header)
					bin = "0" + bin;
				else
					bin = bin + "0";
			}
		}
		return bin;
	}

	public static final byte[] binToByteArray(final String bin) {
		final int size = bin.length() / 8;
		final byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = (byte) Integer.parseInt(bin.substring(i * 8, i * 8 + 8), 2);
		}
		return result;
	}

	public static final String toBin(final int... values) {
		return NumberUtils.toBin("", values);
	}

	public static final String toBin(final String splitFlag, final int... values) {
		final StringBuilder buf = new StringBuilder();
		boolean isFirst = true;
		for (final int value : values) {
			if (isFirst)
				isFirst = false;
			else
				buf.append(splitFlag);
			buf.append(NumberUtils.toBinString(value));
		}
		return buf.toString();
	}

	public static final int toBinToInt(final int byteValue) {
		return Integer.parseInt(Integer.toBinaryString(byteValue), 2);
	}

	/**
	 * 将byte类型的值转换成二进制字符串,不够8位时,高位补零
	 * @param byteValue byte类型数值
	 * @return 二进制字符串
	 */
	public static final String toBinString(final int byteValue) {
		return NumberUtils.toBinString(byteValue, 8);
	}

	public static final String toBinString(final int byteValue, final int length) {
		final StringBuilder buf = new StringBuilder();
		buf.append("00000000").append(Integer.toBinaryString(byteValue));
		return buf.substring(buf.length() - length);
	}

	static final String EMPTY_STR = "", EMPTY_PAD = "0";

	public static final String bytesToHex(final byte... bytes) {
		final StringBuilder buf = new StringBuilder(bytes.length * 2);
		for (final byte b : bytes) {
			final String hex = Integer.toHexString(b);
			buf.append(hex.length() == 1 ? NumberUtils.EMPTY_PAD : NumberUtils.EMPTY_STR).append(hex);
		}
		return buf.toString();
	}

	public static final short hexToShort(final byte... bytes) {
		return Short.parseShort(NumberUtils.bytesToHex(bytes).substring(2), 16);
	}

	public static final String byteArrayToHex(final byte... values) {
		return NumberUtils.byteArrayToHex("", values);
	}

	public static final String byteArrayToHex(final String splitFlag, final byte... values) {
		final StringBuilder buf = new StringBuilder();
		boolean isFirst = true;
		for (final int value : values) {
			if (isFirst)
				isFirst = false;
			else
				buf.append(splitFlag);
			buf.append(NumberUtils.toHexString(value));
		}
		return buf.toString();
	}

	public static final String toHex(final int... values) {
		return NumberUtils.toHex("", values);
	}

	public static final String toHex(final String splitFlag, final int... values) {
		final StringBuilder buf = new StringBuilder();
		boolean isFirst = true;
		for (final int value : values) {
			if (isFirst)
				isFirst = false;
			else
				buf.append(splitFlag);
			buf.append(NumberUtils.toHexString(value));
		}
		return buf.toString();
	}

	public static final String toHexString(final int value) {
		return NumberUtils.toHexString(value, 2);
	}

	public static final String toHexString(final int value, final int length) {
		final StringBuilder buf = new StringBuilder();
		buf.append("00000000000000");
		buf.append(Integer.toHexString(value).toUpperCase());
		return buf.substring(buf.length() - length);
	}

	public static final void reverse(final byte... bytes) {
		final int len = bytes.length;
		int reverseIdx = len - 1;
		for (int i = 0; i < len; i++) {
			if (i > reverseIdx)
				break;
			final byte tmpByte = bytes[i];
			bytes[i] = bytes[reverseIdx];
			bytes[reverseIdx--] = tmpByte;
		}
	}

	// 1个字节高四位低四位颠倒，最后不满8个字节的前面补0
	public static final String reversebinBy4(final String bin) {
		final int len = bin.length();
		final int i = len % 8;
		final String nbin = bin.substring(0, len - i);
		String abin = bin.substring(len - i, len);
		abin = binStringLen8(abin, true);
		return nbin + abin;
	}

	/**
	 * 根据给定的字节数组进行低位转高位,获得该数组的反向组织结构.
	 * @param bytes
	 * @return 如传入[1,2,3,4],将返回[4,3,2,1]
	 */
	public static final byte[] low2HighBytes(final byte... bytes) {
		final int len = bytes.length;
		final byte[] rst = new byte[len];
		for (int i = 0; i < len; i++) {
			rst[len - 1 - i] = bytes[i];
		}
		return rst;
	}

	/**
	 * 计算朝向为正北的base点在经过baseJiaoDu度之后与tgt点组成的一个角的顺时针角度
	 * 
	 * @param baseX
	 * @param baseY
	 * @param tgtX
	 * @param tgtY
	 * @param baseJiaoDu
	 * @return
	 */
	public static final double calcJiaoDuOfNorth(final double baseX, final double baseY, final double tgtX, final double tgtY, final double baseJiaoDu) {
		if (baseJiaoDu < 0 || baseJiaoDu > 360)
			throw new IllegalArgumentException("baseJiaoDu is outof range[0~360]");

		final double jiaoDu = NumberUtils.calcJiaoDuOfNorth(baseX, baseY, tgtX, tgtY);
		final double rs = jiaoDu - baseJiaoDu;
		if (rs < 0)
			return rs + 360;
		return rs;
	}

	/**
	 * 计算朝向为正北的base点与tgt点组成的一个角的顺时针角度
	 * 
	 * @param baseX
	 * @param baseY
	 * @param tgtX
	 * @param tgtY
	 * @return
	 */
	public static final double calcJiaoDuOfNorth(final double baseX, final double baseY, final double tgtX, final double tgtY) {
		double jiaoDu = 0;

		if (tgtX == baseX) {// 如果处在同一竖向坐标上
			if (baseY > tgtY)
				jiaoDu = 180;// 认为是在base点上方，如果在竖向坐标上 目标点大于等于基点
			else
				jiaoDu = 0;
		} else if (tgtY == baseY) {// //否则如果处在同一横向坐标上
			// 认为在base点的左边
			if (baseX > tgtX)
				return 270;
			else
				return 90;
		} else if (tgtX < baseX) {// 认为在左边位置时
			if (tgtY > baseY) // 认为在左上角位置时
				jiaoDu = 360 - Math.toDegrees(Math.atan2(baseX - tgtX, tgtY - baseY));
			else
				jiaoDu = 180 + Math.toDegrees(Math.atan2(baseX - tgtX, baseY - tgtY));// 左下角
		} else if (tgtX > baseX) {// 在右边位置时
			if (tgtY > baseY) // 认为在右上角位置时
				jiaoDu = Math.toDegrees(Math.atan2(tgtX - baseX, tgtY - baseY));
			else
				jiaoDu = 180 - Math.toDegrees(Math.atan2(tgtX - baseX, baseY - tgtY));// 右下角
		}
		return jiaoDu;
	}

	/**
	 * 用于计算垂直角度
	 * 
	 * @param fyj
	 * @param hJiaoDu
	 * @param distance
	 * @param baseStationHeight
	 * @param mobileStationHeight
	 * @return
	 */
	public static final double calcJiaoDuOfV(final double fyj, final double hJiaoDu, final double distance, final double baseStationHeight,
			final double mobileStationHeight) {
		double vJiaoDu = 0;
		double alpha = Math.atan2(Math.abs(baseStationHeight - mobileStationHeight), distance);
		alpha = Math.toDegrees(alpha);
		// 在方向角所在面的后边
		if (hJiaoDu >= 0 && hJiaoDu <= 90 || hJiaoDu >= 270 && hJiaoDu <= 360)
			vJiaoDu = 90 - fyj + alpha;
		else
			vJiaoDu = 270 - alpha - fyj;
		return vJiaoDu;
	}

	public static double distanceByM(final double wd1, final double jd1, final double wd2, final double jd2) {
		double x, y, out;
		final double PI = Math.PI;
		final double R = 6.371229 * 1e6;
		x = (jd2 - jd1) * PI * R * Math.cos((wd1 + wd2) / 2 * PI / 180) / 180;
		y = (wd2 - wd1) * PI * R / 180;
		out = NumberUtils.hypot(x, y);
		return out;
	}

	public static double hypot(final double x, final double y) {
		return Math.sqrt(x * x + y * y);
	}

	public static double decimal2double(final Object value, final double defValue) {
		double rValue = 0;
		try {
			if (value instanceof BigDecimal) {
				rValue = ((BigDecimal) value).doubleValue();
			} else if (value instanceof String) {
				rValue = Double.parseDouble(String.valueOf(value).trim());
			} else if (value instanceof Double) {
				rValue = (Double) value;
			}
		} catch (final Throwable t) {
			return defValue;
		}
		return rValue;
	}

	public static int decimal2int(final Object value, final int defValue) {
		try {
			if (value instanceof BigDecimal) {
				return ((BigDecimal) value).intValue();
			} else if (value instanceof String) {
				return Integer.parseInt(String.valueOf(value).trim());
			} else if (value instanceof Number) {
				return ((Number) value).intValue();
			}
		} catch (final Exception e) {
			// ignore
		}
		return defValue;
	}

	public static final int toInt(final Object value, final int defValue) {
		if (null == value)
			return defValue;
		try {
			if (value instanceof Number) {
				return ((Number) value).intValue();
			}
			return Integer.parseInt(value.toString());
		} catch (final Exception e) {
			try {
				return new BigDecimal(value.toString()).intValue();
			} catch (final Exception ex) {
			}
		}
		return defValue;
	}

	public static final int toInt(final Object value) {
		return NumberUtils.toInt(value, 0);
	}

	public static double toDouble(final Object dbl) {
		return toDouble(dbl, 0);
	}

	public static double toDouble(final Object dbl, final double _default) {
		try {
			if (dbl == null || "".equals(dbl.toString())) {
				return 0d;
			}
			String temp = dbl.toString();
			temp = temp.replaceAll(",", "");
			temp = temp.replaceAll("%", "");
			return Double.parseDouble(temp);
		} catch (final Exception e) {
			return _default;
		}
	}

	public static final int MAX(final int[] array) {
		if (null == array || array.length == 0)
			return 0;

		Arrays.sort(array);
		return array[array.length - 1];
	}

	public static final int MIN(final int[] array) {
		if (null == array || array.length == 0)
			return 0;

		Arrays.sort(array);
		return array[0];
	}

	public static final int MAX(final int[][] array) {
		if (null == array || array.length == 0)
			return 0;
		int[] tmpArr;
		int maxValue = 0;
		for (final int[] element : array) {
			tmpArr = element;
			for (final int element2 : tmpArr) {
				maxValue = Math.max(maxValue, element2);
			}
		}
		return maxValue;
	}

	public static final int MIN(final int[][] array) {
		if (null == array || array.length == 0)
			return 0;
		int[] tmpArr;
		int minValue = 0;
		for (final int[] element : array) {
			tmpArr = element;
			for (final int element2 : tmpArr) {
				minValue = Math.min(minValue, element2);
			}
		}
		return minValue;
	}

	public static final long avg(final int[] array, final int size) {
		final long sum = NumberUtils.SUM(array);
		if (sum == 0) {
			return 0;
		}
		return sum / size;
	}

	public static final long SUM(final int[] array) {
		if (null == array || array.length == 0)
			return 0;

		long rs = 0;
		for (final int item : array) {
			rs += item;
		}
		return rs;
	}

	public static final int SUM(final ByteBuffer buffer, final int offset, final int length) {
		int rs = 0;
		for (int i = 0; i < length; i++) {
			rs += buffer.getInt(offset + i * 4);
		}
		return rs;
	}

	public static final int[] getArray(final ByteBuffer buffer, final int offset, final int length) {
		final int arr[] = new int[length];
		for (int i = 0; i < length; i++) {
			arr[i] = buffer.getInt(offset + i * 4);
		}
		return arr;
	}

	/**
	 * 合并两个相同结构的一维数组
	 * 
	 * @param src
	 * @param dest
	 * @return
	 */
	public static final int[] merge(final int[] src, final int[] dest) {
		if (null == src && null == dest)
			return null;
		if (null == src)
			return dest;
		if (null == dest)
			return src;
		final int result[] = new int[src.length];
		for (int i = 0; i < src.length; i++)
			result[i] = src[i] + dest[i];
		return result;
	}

	/**
	 * 合并两个相同结构的二维数组
	 * 
	 * @param src
	 * @param dest
	 * @return
	 */
	public static final int[][] merge(final int[][] src, final int[][] dest) {
		if (null == src && null == dest)
			return null;
		if (null == src)
			return dest;
		if (null == dest)
			return src;
		final int result[][] = new int[src.length][];
		for (int i = 0; i < src.length; i++) {
			final int[] tmpArr1 = src[i];
			result[i] = new int[tmpArr1.length];
			for (int j = 0; j < tmpArr1.length; j++)
				result[i][j] = tmpArr1[j] + dest[i][j];
		}
		return result;
	}

	/**
	 * 合并两个相同结构的三维数组
	 * 
	 * @param src
	 * @param dest
	 * @return
	 */
	public static final int[][][] merge(final int[][][] src, final int[][][] dest) {
		if (null == src && null == dest)
			return null;
		if (null == src)
			return dest;
		if (null == dest)
			return src;
		final int result[][][] = new int[src.length][][];
		for (int i = 0; i < src.length; i++) {
			final int[][] tmpArr2 = src[i];
			result[i] = new int[tmpArr2.length][];
			for (int j = 0; j < tmpArr2.length; j++) {
				final int[] tmpArr1 = src[i][j];
				result[i][j] = new int[tmpArr1.length];
				for (int k = 0; k < tmpArr1.length; k++)
					result[i][j][k] = tmpArr1[k] + dest[i][j][k];
			}
		}
		return result;
	}

	/**
	 * 字符串转byte
	 */
	public static byte[] stringToByte(final String str, final int lens) {
		final byte[] bytes = new byte[lens];
		final byte[] strBytes = str.getBytes();
		System.arraycopy(strBytes, 0, bytes, 0, strBytes.length);
		return bytes;
	}
}
