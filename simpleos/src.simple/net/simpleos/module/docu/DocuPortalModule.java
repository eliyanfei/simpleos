package net.simpleos.module.docu;

import java.util.Map;
import java.util.Properties;

import net.simpleframework.content.AbstractContentLayoutModuleHandle;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.UrlForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.portal.PageletBean;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月21日 下午4:16:41 
 * @Description: 文档的插件
 *
 */
public class DocuPortalModule extends AbstractContentLayoutModuleHandle {
	public DocuPortalModule(final PageletBean pagelet) {
		super(pagelet);
	}

	private static String[] defaultOptions = new String[] { "_docu_order=0", "_docu_rows=6", "_show_tabs=true" };

	@Override
	protected String[] getDefaultOptions() {
		return defaultOptions;
	}

	@Override
	public IForward getPageletOptionContent(final ComponentParameter compParameter) {
		return new UrlForward(DocuUtils.deploy + "/docu_portal_option.jsp");
	}

	@Override
	public void optionLoaded(final PageParameter pageParameter, final Map<String, Object> dataBinding) {
		super.optionLoaded(pageParameter, dataBinding);
		final Properties properties = getPagelet().getOptionProperties();
		if (properties != null) {
		}
	}

	@Override
	public IForward getPageletContent(final ComponentParameter compParameter) {
		final int _docu_order = ConvertUtils.toInt(getPagelet().getOptionProperty("_docu_order"), -1);
		final int _tab_param = ConvertUtils.toInt(compParameter.getParameter("_tab_param"), -1);
		final IDataObjectQuery<?> qs = DocuUtils.queryDocu(compParameter, null, null, null, _docu_order, _tab_param);
		compParameter.setRequestAttribute("__qs", qs);
		String forward = "/docu_portal.jsp";
		final boolean _show_tabs = ConvertUtils.toBoolean(getPagelet().getOptionProperty("_show_tabs"), true)
				&& ConvertUtils.toBoolean(compParameter.getSessionAttribute("_show_tabs"), true);
		forward = WebUtils.addParameters(forward, "_show_tabs=" + _show_tabs);
		forward = WebUtils.addParameters(forward, "rows=" + ConvertUtils.toInt(getPagelet().getOptionProperty("_docu_rows"), 6));
		return new UrlForward(DocuUtils.deploy + forward, "pa");
	}
}
