package net.simpleframework.web.page.component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.component.ui.menu.MenuBean;
import net.simpleframework.web.page.component.ui.menu.MenuItem;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IComponentHandle {
	void handleCreated(PageRequestResponse requestResponse, AbstractComponentBean componentBean);

	void beforeRender(ComponentParameter compParameter);

	/**
	 * 目的是通过该函数动态装载属性，在组件开发中需要通过调用该函数，否则使用者覆盖此类没有效果
	 * 
	 * @param compParameter
	 * @param beanProperty
	 * @return
	 */
	Object getBeanProperty(ComponentParameter compParameter, String beanProperty);

	Map<String, Object> toJSON(ComponentParameter compParameter);

	Map<String, Object> getFormParameters(ComponentParameter compParameter);

	/*--------------------------------- utils -----------------------------------*/

	String getManager(ComponentParameter compParameter);

	List<MenuItem> getContextMenu(ComponentParameter compParameter, MenuBean menuBean);

	static String REQUEST_HANDLE_KEY = "@handleClass_";

	public static class Utils {
		public static Map<String, Object> toFormParameters(final ComponentParameter compParameter) {
			final IComponentHandle hdl = compParameter.getComponentHandle();
			if (hdl == null) {
				return null;
			}
			final Map<String, Object> parameters = hdl.getFormParameters(compParameter);
			if (parameters != null) {
				PageUtils.removeSystemParameters(parameters);
			}
			return parameters;
		}

		private static Map<String, IComponentHandle> handleMap = new ConcurrentHashMap<String, IComponentHandle>();

		public static IComponentHandle getComponentHandle(final PageRequestResponse requestResponse,
				final AbstractComponentBean componentBean) {
			final String handleClass = componentBean.getHandleClass();
			if (!StringUtils.hasText(handleClass)) {
				return null;
			}
			IComponentHandle componentHandle = null;
			final EHandleScope handleScope = componentBean.getHandleScope();
			if (handleScope == EHandleScope.singleton) {
				componentHandle = handleMap.get(handleClass);
				if (componentHandle == null) {
					handleMap.put(handleClass,
							componentHandle = createComponentHandle(requestResponse, componentBean));
				}
			} else if (handleScope == EHandleScope.prototype) {
				final String handleKey = REQUEST_HANDLE_KEY + handleClass;
				componentHandle = (IComponentHandle) requestResponse.getRequestAttribute(handleKey);
				if (componentHandle == null) {
					requestResponse.setRequestAttribute(handleKey,
							componentHandle = createComponentHandle(requestResponse, componentBean));
				}
			}
			return componentHandle;
		}

		private static IComponentHandle createComponentHandle(
				final PageRequestResponse requestResponse, final AbstractComponentBean componentBean) {
			final String handleClass = componentBean.getHandleClass();
			IComponentHandle componentHandle = null;
			try {
				componentHandle = (IComponentHandle) BeanUtils.forName(handleClass)
						.getMethod("getInstance").invoke(null);
				if (componentHandle != null) {
					componentHandle.handleCreated(requestResponse, componentBean);
				}
			} catch (final Exception e) {
			}
			if (componentHandle == null) {
				try {
					componentHandle = (IComponentHandle) BeanUtils.newInstance(handleClass);
					componentHandle.handleCreated(requestResponse, componentBean);
				} catch (final Exception e) {
					throw HandleException.wrapException(e);
				}
			}
			return componentHandle;
		}
	}
}