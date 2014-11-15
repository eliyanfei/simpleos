package net.simpleframework.web.page.component.ui.portal;

import java.util.ArrayList;

import net.simpleframework.core.AbstractElementBean;
import net.simpleframework.util.GenId;
import net.simpleframework.util.StringUtils;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ColumnBean extends AbstractElementBean {

	private final PortalBean portalBean;

	private ArrayList<PageletBean> pagelets;

	private String width;

	public ColumnBean(final Element element, final PortalBean portalBean) {
		super(element == null ? portalBean.getElement().addElement("column") : element);
		this.portalBean = portalBean;
	}

	@Override
	public void syncElement() {
		super.syncElement();
		removeChildren("pagelet");
		for (final PageletBean pagelet : getPagelets()) {
			pagelet.syncElement();
			addElement(pagelet);
		}
	}

	public PortalBean getPortalBean() {
		return portalBean;
	}

	public ArrayList<PageletBean> getPagelets() {
		if (pagelets == null) {
			pagelets = new ArrayList<PageletBean>();
		}
		return pagelets;
	}

	public String getWidth() {
		return StringUtils.hasText(width) ? width : "auto";
	}

	public void setWidth(final String width) {
		this.width = width;
	}

	private String id;

	public String id() {
		if (id == null) {
			id = "ul_" + GenId.genUID();
		}
		return id;
	}
}
