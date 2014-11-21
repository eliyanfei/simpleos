package net.simpleframework.util;

import java.io.IOException;
import java.io.StringWriter;

import net.simpleframework.core.SimpleException;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class JSONUtils {
	static final ObjectMapper mapper = new ObjectMapper();

	/*-------------------------------json-to-bean-------------------------------*/

	// public static Map<?, ?> toMapObject(final String json) {
	// return json == null ? null : JSONObject.fromObject(json);
	// }

	// public static List<?> toListObject(final String json) {
	// return json == null ? null : JSONArray.fromObject(json);
	// }

	/*-------------------------------bean-to-json-------------------------------*/
	public static String toJSON(final Object object) {
		try {
			final StringWriter sw = new StringWriter();
			mapper.writeValue(sw, object);
			return sw.toString();
		} catch (final IOException e) {
			throw SimpleException.wrapException(JsonException.class, null, e);
		}
	}

	public static class JsonException extends SimpleException {
		private static final long serialVersionUID = 5950671267387022886L;

		public JsonException(final String msg, final Throwable cause) {
			super(msg, cause);
		}
	}
}
