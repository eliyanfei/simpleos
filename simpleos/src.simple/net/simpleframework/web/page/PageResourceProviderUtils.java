package net.simpleframework.web.page;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;

import net.simpleframework.util.JavascriptUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class PageResourceProviderUtils {
	private static Map<IPageResourceProvider, Boolean> flags;

	public static void extract(final IPageResourceProvider provider, final ServletContext context) {
		if (provider == null || context == null) {
			return;
		}
		if (flags == null) {
			flags = new ConcurrentHashMap<IPageResourceProvider, Boolean>();
		}
		final Boolean b = flags.get(provider);
		if (b != null && b.booleanValue()) {
			return;
		}
		try {
			JavascriptUtils.unzipJsAndCss(provider.getRequiredResource(),
					context.getRealPath(provider.getResourceHomePath()),
					PageUtils.pageConfig.isResourceCompress());
		} catch (final IOException e) {
			throw PageException.wrapException(e);
		}
		flags.put(provider, Boolean.TRUE);
	}
}
