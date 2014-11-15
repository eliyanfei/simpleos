package net.simpleframework.web.page.component;

import java.io.IOException;
import java.util.zip.ZipInputStream;

import net.simpleframework.util.IoUtils;
import net.simpleframework.util.JavascriptUtils;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.component.ui.htmleditor.HtmlEditorRegistry;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class ComponentResourceProviderUtils {

	public static void extract(final IComponentRegistry componentRegistry) throws IOException {
		final IComponentResourceProvider provider = componentRegistry.getComponentResourceProvider();
		if (provider == null) {
			return;
		}

		final ZipInputStream inputStream = provider.getRequiredResource();
		if (inputStream != null) {
			final String target = componentRegistry.getServletContext().getRealPath(
					provider.getResourceHomePath());
			if (componentRegistry instanceof HtmlEditorRegistry) {
				IoUtils.unzip(inputStream, target);
			} else {
				JavascriptUtils.unzipJsAndCss(inputStream, target,
						PageUtils.pageConfig.isResourceCompress());
			}
		}
	}
}
