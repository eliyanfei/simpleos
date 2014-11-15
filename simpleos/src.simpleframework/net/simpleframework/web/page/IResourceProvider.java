package net.simpleframework.web.page;

import java.io.IOException;
import java.util.Collection;
import java.util.zip.ZipInputStream;

import net.simpleframework.web.page.component.AbstractComponentBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IResourceProvider extends IHomePathAware {
	final static String PROTOTYPE_FILE = "/js/prototype.js?v=1.7";

	final static String EFFECTS_FILE = "/js/effects.js?v=1.9";

	final static String DRAGDROP_FILE = "/js/dragdrop.js?v=1.9";

	// final static String S2_FILE = "/js/s2.js?v=2.0.0_b1";

	/**
	 * 需要资源的ZIP压缩包，用户可以自己组织目录结构
	 * 
	 * @return
	 * @throws IOException
	 */
	ZipInputStream getRequiredResource() throws IOException;

	/**
	 * 压缩包下的css路径
	 * 
	 * @param request
	 * @return
	 */
	String[] getCssPath(PageRequestResponse requestResponse,
			Collection<AbstractComponentBean> componentBeans);

	/**
	 * 压缩包下的javascript路径
	 * 
	 * @param request
	 * @return
	 */
	String[] getJavascriptPath(PageRequestResponse requestResponse,
			Collection<AbstractComponentBean> componentBeans);

	String[] getJarPath();

	String getCurrentSkin(PageRequestResponse requestResponse);

	String getSkin();

	void setSkin(final String skin);
}
