package net.itsite.cache;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import net.itsite.utils.IOUtils;
import net.itsite.utils.StringsUtils;
import net.itsite.utils.W3cUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * 
 * @author 李岩飞 
 * @date 2014年11月19日 下午5:40:18 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public final class CachesUtil {

	private CachesUtil() {
	}

	/**
	 * 从给定的数据输入流中加入缓存
	 * 
	 * @param caches
	 * @param stream
	 */
	public static final void loadCaches(final Caches caches, final InputStream stream, final boolean forceJoinThread) {
		if (null == stream)
			return;
		try {
			loadCaches(caches, W3cUtils.readXmlDocument(stream), forceJoinThread);
		} catch (final Exception e) {
		} finally {
			IOUtils.closeIO(stream);
		}
	}

	/**
	 * 从给定的XML配置文档中加载缓存
	 * 
	 * @param caches
	 * @param dom
	 */
	public static final void loadCaches(final Caches caches, final Document dom, final boolean forceJoinThread) {
		try {
			final List<Element> cacheEles = W3cUtils.childElementList(dom.getDocumentElement(), "Cache");
			for (final Element cacheEle : cacheEles) {
				// 如果定义的SQL为空,不做操作,直接继续下一个
				final String sql = cacheEle.getTextContent().trim();
				if (StringsUtils.isBlank(sql))
					return;
				final String cacheName = cacheEle.getAttribute("name");// 缓存名称
				// 缓存存储类型(磁盘或内存),默认为内存
				final ECacheEngine cacheEngine = ECacheEngine.valueOf(StringsUtils.trimNull(cacheEle.getAttribute("cacheEngine"),
						ECacheEngine.EHCACHE_DISK.name()));
				// 获得旧的定义,如果存在,只增加SQL
				final CacheConfig cacheConfig = new CacheConfig(cacheName, cacheEngine);
				cacheConfig.cacheList.add(sql);

				final NamedNodeMap attrsMap = cacheEle.getAttributes();// 属性集
				// 记录所有自定义属性
				cacheConfig.properties = new Properties();
				for (int i = 0; i < attrsMap.getLength(); i++) {
					final Node attrNode = attrsMap.item(i);
					cacheConfig.properties.setProperty(attrNode.getNodeName(), attrNode.getNodeValue());
				}

				final boolean useThread = "true".equals(cacheEle.getAttribute("thread"));
				final PrivateCacheThread thread = new PrivateCacheThread(cacheConfig, caches);

				if (!useThread || forceJoinThread) {
					thread.run();
				} else
					thread.start();
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private static class PrivateCacheThread extends Thread {
		final CacheConfig cacheConfig;
		final Caches caches;

		/**
		 * @param cacheName
		 * @param sql
		 * @param props
		 */
		public PrivateCacheThread(final CacheConfig cacheConfig, final Caches caches) {
			super();
			this.cacheConfig = cacheConfig;
			this.caches = caches;
		}

		@Override
		public void run() {
			caches.registryMap(null, cacheConfig);
		}
	}
}
