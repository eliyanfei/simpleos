package net.simpleframework.core;

import java.io.Serializable;
import java.util.StringTokenizer;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */

public final class Version implements Serializable, Comparable<Version> {

	private static final long serialVersionUID = -5739492907539237198L;

	public static Version getVersion(final String version) throws NumberFormatException, IllegalArgumentException {
		final StringTokenizer tokenizer = new StringTokenizer(version, ".");
		final String[] levels = new String[tokenizer.countTokens()];
		for (int i = 0; i < levels.length; i++) {
			levels[i] = tokenizer.nextToken();
		}

		if ((0 == levels.length) || (3 < levels.length)) {
			throw new IllegalArgumentException("Malformed version string " + version);
		}

		final int major = Integer.parseInt(levels[0]);

		int minor = 0;
		if (1 < levels.length) {
			minor = Integer.parseInt(levels[1]);
		}

		int micro = 0;
		if (2 < levels.length) {
			micro = Integer.parseInt(levels[2]);
		}

		return new Version(major, minor, micro);
	}

	private final int major;

	private final int minor;

	private final int micro;

	public Version(final int major, final int minor, final int micro) {
		this.major = major;
		this.minor = minor;
		this.micro = micro;
	}

	/**
	 * 判断当前版本是否大于给定版本tgtVer
	 * @param tgtVer
	 * @return
	 */
	public final boolean great(final Version tgtVer) {
		return this.compareTo(tgtVer) > 0;
	}

	/**
	 * 判断当前版本是否大于等于给定版本tgtVer
	 * @param tgtVer
	 * @return
	 */
	public final boolean greatOrEquals(final Version tgtVer) {
		return this.compareTo(tgtVer) >= 0;
	}

	/**
	 * 判断当前版本是否小于给定版本tgtVer
	 * @param tgtVer
	 * @return
	 */
	public final boolean less(final Version tgtVer) {
		return this.compareTo(tgtVer) < 0;
	}

	/**
	 * 判断当前版本是否小于等于给定版本tgtVer
	 * @param tgtVer
	 * @return
	 */
	public final boolean lessOrEquals(final Version tgtVer) {
		return this.compareTo(tgtVer) <= 0;
	}

	@Override
	public int compareTo(final Version o) {
		if (equals(o)) {
			return 0;
		} else if (complies(o)) {
			return 1;
		} else {
			return -1;
		}
	}

	public boolean complies(final Version other) {
		if (major > other.major) {
			return true;
		} else if (major == other.major) {
			if (minor > other.minor) {
				return true;
			} else if (minor == other.minor) {
				if (micro > other.micro) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean equals(final Object other) {
		if (other instanceof Version) {
			return equals((Version) other);
		} else {
			return false;
		}
	}

	public boolean equals(final Version other) {
		if (major != other.major) {
			return false;
		} else if (minor != other.minor) {
			return false;
		} else if (micro != other.micro) {
			return false;
		} else {
			return true;
		}
	}

	public int getMajor() {
		return major;
	}

	public int getMicro() {
		return micro;
	}

	public int getMinor() {
		return minor;
	}

	@Override
	public String toString() {
		return major + "." + minor + "." + micro;
	}

	/**
	 * 从一个字符串构建一个版本信息对象.
	 * @param version 通常如"1.0.0","1.12.345";
	 * @return
	 */
	public static Version valueOf(final String version) {
		if (null == version || "".equals(version))
			throw new IllegalArgumentException("The giving version cannot be null.");
		final String[] items = version.split("\\.");
		if (0 == items.length || 3 < items.length)
			throw new IllegalArgumentException("Malformed version string " + version);

		final int major = Integer.parseInt(items[0]);
		final int minor = items.length > 1 ? Integer.parseInt(items[1]) : 0;
		final int micro = items.length > 2 ? Integer.parseInt(items[2]) : 0;
		return new Version(major, minor, micro);
	}
}
