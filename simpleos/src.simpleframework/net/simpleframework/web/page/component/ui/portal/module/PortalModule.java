package net.simpleframework.web.page.component.ui.portal.module;

import net.simpleframework.util.StringUtils;
import net.simpleframework.util.TextObject;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.component.ui.portal.PortalUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PortalModule extends TextObject {
	private static final long serialVersionUID = -6208810607063471077L;

	private static final String OTHER_MODULE_CATALOG = "OTHERs";

	private String icon;

	private String handleClass;

	private String catalog;

	private boolean tools;//是不是显示在工具里面

	public PortalModule() {
		super(null);
	}

	public void setTools(boolean tools) {
		this.tools = tools;
	}

	public boolean isTools() {
		return tools;
	}

	public String getIcon() {
		if (StringUtils.hasText(icon)) {
			if (icon.charAt(0) == '/') {
				return icon;
			} else {
				return PortalUtils.getHomePath() + "/images/" + icon;
			}
		} else {
			return PageUtils.getResourcePath() + "/images/none.png";
		}
	}

	public void setIcon(final String icon) {
		this.icon = icon;
	}

	public String getHandleClass() {
		return handleClass;
	}

	public void setHandleClass(final String handleClass) {
		this.handleClass = handleClass;
	}

	public String getCatalog() {
		return StringUtils.hasText(catalog) ? catalog : OTHER_MODULE_CATALOG;
	}

	public void setCatalog(final String catalog) {
		this.catalog = catalog;
	}
}
