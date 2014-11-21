package net.simpleframework.web.page;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class SessionCache {
	private static Map<String, Map<Object, Object>> sessionObjects = new ConcurrentHashMap<String, Map<Object, Object>>();

	public static HttpSessionListener sessionListener = new HttpSessionListener() {
		@Override
		public void sessionCreated(final HttpSessionEvent httpSessionEvent) {
		}

		@Override
		public void sessionDestroyed(final HttpSessionEvent httpSessionEvent) {
			final String sessionId = httpSessionEvent.getSession().getId();
			// System.out.println("remove objects from session: " + sessionId);
			sessionObjects.remove(sessionId);
		}
	};

	/**
	 * 获得缓存数量
	 * @return
	 */
	public static int getSessionCaches() {
		return sessionObjects.size();
	}

	private static Map<Object, Object> getObjectMap(final HttpSession httpSession) {
		final String httpSessionId = httpSession.getId();
		Map<Object, Object> objectMap = sessionObjects.get(httpSessionId);
		if (objectMap == null) {
			sessionObjects.put(httpSessionId, objectMap = new ConcurrentHashMap<Object, Object>());
		}
		return objectMap;
	}

	public static void put(final HttpSession httpSession, final Object key, final Object value) {
		if (key != null) {
			getObjectMap(httpSession).put(key, value);
		}
	}

	public static Object get(final HttpSession httpSession, final Object key) {
		if (key != null) {
			return getObjectMap(httpSession).get(key);
		} else {
			return null;
		}
	}

	public static Object remove(final HttpSession httpSession, final Object key) {
		return key != null ? getObjectMap(httpSession).remove(key) : null;
	}
}
