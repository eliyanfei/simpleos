package net.simpleframework.util;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.LinkedHashSet;

import org.apache.commons.lang3.ArrayUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class LangUtils {

	public static boolean objectEquals(final Object newVal, final Object oldVal) {
		if (newVal == oldVal) {
			return true;
		} else if ((newVal == null) || (oldVal == null)) {
			return false;
		} else {
			if (newVal.getClass().isArray() && oldVal.getClass().isArray()) {
				final int nLength = Array.getLength(newVal);
				final int oLength = Array.getLength(oldVal);
				if (nLength != oLength) {
					return false;
				}
				for (int i = 0; i < nLength; i++) {
					if (!objectEquals(Array.get(newVal, i), Array.get(oldVal, i))) {
						return false;
					}
				}
				return true;
			} else {
				return newVal.equals(oldVal);
			}
		}
	}

	public static Object[] removeDuplicatesAndNulls(final Object[] array) {
		if (array == null) {
			return null;
		}
		final LinkedHashSet<Object> ht = new LinkedHashSet<Object>();
		for (final Object element : array) {
			if (element == null) {
				continue;
			}
			ht.add(element);
		}

		final Object[] ret = (Object[]) Array.newInstance(array.getClass().getComponentType(),
				ht.size());
		int j = 0;

		final Iterator<?> it = ht.iterator();
		while (it.hasNext()) {
			ret[j++] = it.next();
		}
		return ret;
	}

	public static boolean contains(final Object[] array, final Object objectToFind) {
		return ArrayUtils.contains(array, objectToFind);
	}

	public static boolean contains(final int[] array, final int intToFind) {
		return ArrayUtils.contains(array, intToFind);
	}

	public static Object[] add(final Object[] arr, final Object object) {
		return ArrayUtils.add(arr, object);
	}

	public static Object[] addAll(final Object[] arr1, final Object[] arr2) {
		return ArrayUtils.addAll(arr1, arr2);
	}

	public static boolean isAscii(final byte[] bytes) {
		for (final byte b : bytes) {
			if ((b & 0x80) != 0) {
				return false;
			}
		}
		return true;
	}

	public static boolean isUTF8(final byte[] rawtext) {
		int score = 0;
		int i, rawtextlen = 0;
		int goodbytes = 0, asciibytes = 0;
		rawtextlen = rawtext.length;
		for (i = 0; i < rawtextlen; i++) {
			if ((rawtext[i] & (byte) 0x7F) == rawtext[i]) {
				asciibytes++;
			} else if (-64 <= rawtext[i] && rawtext[i] <= -33 && // Two bytes
					i + 1 < rawtextlen && -128 <= rawtext[i + 1] && rawtext[i + 1] <= -65) {
				goodbytes += 2;
				i++;
			} else if (-32 <= rawtext[i] && rawtext[i] <= -17
					&& // Three bytes
					i + 2 < rawtextlen && -128 <= rawtext[i + 1] && rawtext[i + 1] <= -65
					&& -128 <= rawtext[i + 2] && rawtext[i + 2] <= -65) {
				goodbytes += 3;
				i += 2;
			}
		}
		if (asciibytes == rawtextlen) {
			return false;
		}
		score = 100 * goodbytes / (rawtextlen - asciibytes);
		if (score > 98) {
			return true;
		} else if (score > 95 && goodbytes > 30) {
			return true;
		} else {
			return false;
		}
	}
}
