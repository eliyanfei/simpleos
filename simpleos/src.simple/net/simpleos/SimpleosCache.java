package net.simpleos;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author 李岩飞 
 * @date 2014年11月19日 下午4:58:58 
 * @Description: 公共缓存
 *
 */
public class SimpleosCache {
	//找回秘密的请求
	public static Map<String, PasswordCache> passwordMap = new HashMap<String, PasswordCache>();

	static class PasswordCache {
		public PasswordCache(final String email) {
			this.date = new Date();
			this.email = email;
		}

		Date date;
		String email;
	}

	static {
		new Thread() {
			public void run() {
				final int t = 60 * 1000 * 60;
				//一个小时候运行
				try {
					Thread.sleep(t);
				} catch (InterruptedException e) {
				}
				while (true) {
					try {
						final Set<String> sids = new HashSet<String>(passwordMap.keySet());
						for (final String sid : sids) {
							final PasswordCache pc = passwordMap.get(sid);
							if (pc != null) {
								if ((pc.date.getTime() - System.currentTimeMillis()) > t) {
									passwordMap.remove(sid);
								}
							}
						}
						//每2个小时清理一次
						try {
							Thread.sleep(60 * 1000 * 60 * 2);
						} catch (InterruptedException e) {
						}
					} catch (Exception e) {
					}
				}
			};
		}.start();
	}

	public static void findPassword(final String id, final String email) {
		passwordMap.put(id, new PasswordCache(email));
	}

	public static String getEmail(final String sid) {
		final PasswordCache pc = passwordMap.remove(sid);
		if (pc != null) {
			return pc.email;
		}
		return null;
	}
}
