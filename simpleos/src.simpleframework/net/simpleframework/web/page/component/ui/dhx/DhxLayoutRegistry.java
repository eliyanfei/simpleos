package net.simpleframework.web.page.component.ui.dhx;

import java.util.Iterator;

import javax.servlet.ServletContext;

import net.simpleframework.util.StringUtils;
import net.simpleframework.util.script.IScriptEval;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.AbstractComponentRender;
import net.simpleframework.web.page.component.AbstractComponentResourceProvider;
import net.simpleframework.web.page.component.ComponentException;
import net.simpleframework.web.page.component.ui.dhx.DhxLayoutBean.Cell;
import net.simpleframework.web.page.component.ui.dhx.DhxLayoutBean.Margins;

import org.dom4j.Element;

/**
 * @Description
 * @Date 2012-11-28
 * @author lxy
 */
public class DhxLayoutRegistry extends AbstractComponentRegistry {

	public static final String componentName = "dhxlayout";

	public DhxLayoutRegistry(final ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public String getComponentName() {
		return componentName;
	}

	@Override
	protected Class<? extends AbstractComponentBean> getBeanClass() {
		return DhxLayoutBean.class;
	}

	@Override
	protected Class<? extends AbstractComponentRender> getRenderClass() {
		return DhxLayoutRender.class;
	}

	@Override
	protected Class<? extends AbstractComponentResourceProvider> getResourceProviderClass() {
		return DhxLayoutResourceProvider.class;
	}

	@Override
	public AbstractComponentBean createComponentBean(PageParameter pageParameter, Element component) {
		DhxLayoutBean layoutBean = (DhxLayoutBean) super.createComponentBean(pageParameter, component);
		final IScriptEval scriptEval = pageParameter.getScriptEval();
		layoutBean.parseElement(scriptEval);
		Element element = component.element("cells");
		if (element != null) {
			final Iterator<?> it = element.elementIterator("cell");
			int index = 0;
			while (it.hasNext()) {
				Element cellEle = (Element) it.next();
				Cell cell = new Cell(cellEle, layoutBean);
				cell.parseElement(scriptEval);
				cell.setCellName(DhxLayoutUtils.getCellName(index++));
				// ���� url:
				Element subElement = cellEle.element("attachUrl");
				if (subElement != null) {
					cell.setUrl(subElement.attributeValue("url"));
				}
				// ����ҳ��������� div֮ID
				subElement = cellEle.element("attachObject");
				if (subElement != null) {
					cell.setTargetId(subElement.attributeValue("targetId"));
				}
				// Ƕ��Layout
				subElement = cellEle.element("refLayout");
				String ref = null;
				if (subElement != null) {
					ref = subElement.attributeValue("refLayout");
					cell.setRefLayout(ref);
				}
				if (StringUtils.hasText(ref)) {
					final DhxLayoutBean refLayout = (DhxLayoutBean) pageParameter.getComponentBean(ref);
					if (refLayout == null) {
						if (!isComponentInCache(pageParameter, ref)) {
							throw ComponentException.getComponentRefException();
						}
					} else {
						refLayout.setParentLayout(layoutBean);
						refLayout.setParentCellName(cell.getCellName());
					}
				}
				layoutBean.getCells().add(cell);
			}
			element = component.element("margins");
			if (element != null) {
				Margins margins = new Margins(element, layoutBean);
				margins.parseElement(scriptEval);
				layoutBean.set_margins(margins);
			}

		}
		return layoutBean;
	}
}