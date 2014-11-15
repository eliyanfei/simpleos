package net.simpleframework.util;

import java.io.IOException;
import java.io.InputStream;

import net.simpleframework.core.SimpleException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AlgorithmUtils {
	public static byte[] base64Decode(final String encoded) {
		return encoded == null ? null : Base64.decodeBase64(encoded.getBytes());
	}

	public static String base64Encode(final byte[] binaryData) {
		return binaryData == null ? null : new String(Base64.encodeBase64(binaryData));
	}

	public static String md5Hex(final String data) {
		return DigestUtils.md5Hex(data);
	}

	public static String md5Hex(final InputStream inputStream) {
		try {
			return DigestUtils.md5Hex(inputStream);
		} catch (final IOException e) {
			throw AlgorithmException.wrapException(e);
		}
	}

	public static String shaHex(final InputStream inputStream) {
		try {
			return DigestUtils.shaHex(inputStream);
		} catch (final IOException e) {
			throw AlgorithmException.wrapException(e);
		}
	}

	public static class AlgorithmException extends SimpleException {
		private static final long serialVersionUID = 8126498724567410454L;

		public AlgorithmException(final String msg, final Throwable cause) {
			super(msg, cause);
		}

		public static RuntimeException wrapException(final Throwable throwable) {
			return wrapException(AlgorithmException.class, null, throwable);
		}
	}
}
