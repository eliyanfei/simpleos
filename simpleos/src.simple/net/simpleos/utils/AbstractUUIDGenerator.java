package net.simpleos.utils;

import java.net.InetAddress;

/**
 * The base class for identifier generators that use a UUID algorithm. This
 * class implements the algorithm, subclasses define the identifier
 * format.
 *
 * @see UUIDHexGenerator
 * @author Gavin King
 */

abstract class AbstractUUIDGenerator {

	private static final int IP;
	static {
		int ipadd;
		try {
			ipadd = toInt(InetAddress.getLocalHost().getAddress());
		} catch (final Exception e) {
			ipadd = 0;
		}
		IP = ipadd;
	}

	public static int toInt(final byte[] bytes) {
		int result = 0;
		for (int i = 0; i < 4; i++) {
			result = (result << 8) - Byte.MIN_VALUE + bytes[i];
		}
		return result;
	}

	private static short counter = (short) 0;
	private static final int JVM = (int) (System.currentTimeMillis() >>> 8);

	public AbstractUUIDGenerator() {
	}

	/**
	 * Unique across JVMs on this machine (unless they load this class
	 * in the same quater second - very unlikely)
	 */
	protected int getJVM() {
		return JVM;
	}

	/**
	 * Unique in a millisecond for this JVM instance (unless there
	 * are > Short.MAX_VALUE instances created in a millisecond)
	 */
	protected short getCount() {
		synchronized (AbstractUUIDGenerator.class) {
			if (counter < 0)
				counter = 0;
			return counter++;
		}
	}

	/**
	 * Unique in a local network
	 */
	protected int getIP() {
		return IP;
	}

	/**
	 * Unique down to millisecond
	 */
	protected short getHiTime() {
		return (short) (System.currentTimeMillis() >>> 32);
	}

	protected int getLoTime() {
		return (int) System.currentTimeMillis();
	}
}
