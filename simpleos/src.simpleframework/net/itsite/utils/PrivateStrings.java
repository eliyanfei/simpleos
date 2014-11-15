package net.itsite.utils;

/**
 * @author QianFei.Xu qianfei.xu@rosense.cn
 * @dateTime Apr 23, 2010 12:07:48 PM
 */
abstract class PrivateStrings {
	/**
	 * �ж�һ���ַ�valueItem�Ƿ��������ַ�����array�е�һ��Ԫ��,�����õ��˱ȽϷ���:ֱ�����(equals),�Կ�ͷ(
	 * startsWith),�Խ�β(endsWith);
	 * 
	 * @param array
	 * @param valueItem
	 * @return
	 */
	public static final boolean likesIn(final String valueItem, final String[] array) {
		if (null == array || null == valueItem || "".equals(valueItem.trim()))
			return false;

		for (final String value : array) {
			if (valueItem.equals(value) || valueItem.startsWith(value) || valueItem.endsWith(value))
				return true;
		}
		return false;
	}

}
