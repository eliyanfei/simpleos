package net.simpleframework.web.page.component.ui.dhx;

import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.AbstractComponentJavascriptRender;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ComponentRenderUtils;
import net.simpleframework.web.page.component.IComponentRegistry;
import net.simpleframework.web.page.component.ui.dhx.DhxLayoutBean.Cell;

/**
 * @Description
 * @Date 2012-11-28
 * @author lxy
 */
public class DhxLayoutRender extends AbstractComponentJavascriptRender {

	public DhxLayoutRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getJavascriptCode(final ComponentParameter compParameter) {
		final DhxLayoutBean dhxLayout = (DhxLayoutBean) compParameter.componentBean;
		String strJs = "";
		StringBuilder js = new StringBuilder();
		// js.append(" function ");
		// js.append(dhxLayout.getActionFunction()).append("(){");
		js.append(" var _dhxLayout = new dhtmlXLayoutObject({");
		js.append("  parent :").append("\"" + dhxLayout.getContainerId() + "\",");
		js.append("  pattern  :").append("\"" + dhxLayout.getPattern().toString().substring(1) + "\",");
		js.append("  skin   :").append("\"" + DhxLayoutUtils.skin_name_prefix + dhxLayout.getSkin().toString() + "\",");
		js.append("  image_path   :").append("\"" + DhxLayoutUtils.getImgPath(dhxLayout.getSkin()) + "\",");
		js.append("  cells   :[");
		int i = 0;
		for (Cell c : dhxLayout.getCells()) {
			if (i != 0) {
				js.append(",");
			}
			js.append("{");
			js.append("id:").append("\"" + c.getCellName() + "\",");
			js.append("text:").append("\"" + c.getHeaderText() + "\",");
			js.append("header:").append(c.isShowHeader() + ",");
			js.append("fix_size:[").append(c.isFixWidth() + ",").append(c.isFixHeight()).append("],");
			if (c.getWidth() > 0)
				js.append("width:").append(c.getWidth() + ",");
			if (c.getHeight() > 0)
				js.append("height:").append(c.getHeight() + ",");
			js.append("url:").append("\"" + c.getUrl() + "\",");
			js.append("targetId:").append("\"" + c.getTargetId() + "\"");
			js.append("}");
			i++;
		}
		js.append("]});");
		js.append("$IT._dhxLayout = _dhxLayout;");
		if (StringUtils.hasText(dhxLayout.getJsCollapse())) {// 和上的监听
			js.append("_dhxLayout.attachEvent(\"onCollapse\",function(itemId){");
			js.append(eventString());
			js.append(dhxLayout.getJsCollapse());
			js.append("});");
		}
		if (StringUtils.hasText(dhxLayout.getJsExpand())) {// 打开的监听
			js.append("_dhxLayout.attachEvent(\"onExpand\",function(itemId){");
			js.append(eventString());
			js.append(dhxLayout.getJsExpand());
			js.append("});");
		}
		if (StringUtils.hasText(dhxLayout.getJsPanelResizeFinish())) {// 改变大小
			js.append("_dhxLayout.attachEvent(\"onPanelResizeFinish\",function(itemId){");
			js.append(eventString());
			js.append(dhxLayout.getJsPanelResizeFinish());
			js.append("});");
		}
		if (StringUtils.hasText(dhxLayout.getJsDblClick())) {// 双击
			js.append("_dhxLayout.attachEvent(\"onDblClick\",function(itemId){");
			js.append(eventString());
			js.append(dhxLayout.getJsDblClick());
			js.append("});");
		}
		js.append("  var cells  =[");
		int ii = 0;
		for (Cell c : dhxLayout.getCells()) {
			if (ii != 0) {
				js.append(",");
			}
			js.append("{");
			js.append("id:").append("\"" + c.getCellName() + "\",");
			js.append("url:").append("\"" + c.getUrl() + "\",");
			js.append("targetId:").append("\"" + c.getTargetId() + "\"");
			js.append("}");
			ii++;
		}
		js.append("];");
		js.append("for(var i=0;i<cells.length;i++){");
		js.append("if(cells[i].url != null){");
		js.append("if((cells[i].url != \"null\") && (cells[i].url != \"\")){");
		js.append(" _dhxLayout.cells(cells[i].id).attachURL(cells[i].url);");
		js.append("}} if(cells[i].targetId != null){");
		js.append("if((cells[i].targetId != \"null\") && (cells[i].targetId != \"\")){");
		js.append("_dhxLayout.cells(cells[i].id).attachObject(cells[i].targetId);");
		js.append("}}");
		js.append("}");
		// js.append("$Actions['" + dhxLayout.getName() + "']=" +
		// dhxLayout.getActionFunction() + ";");
		// if (dhxLayout.isRunImmediately()) {
		// js.append("$ready(function() { ");
		// js.append("$Actions['" + dhxLayout.getName() + "']();");
		// js.append("});");
		// }
		return ComponentRenderUtils.wrapActionFunction(compParameter, js.toString());

	}

	private String eventString() {
		StringBuilder js = new StringBuilder();
		js.append("var cells=new Array();var itemIds=(itemId+'').split(',');");
		js.append(" for(var i=0;i<itemIds.length;i++){");
		js.append("cells[i]=_dhxLayout.cells(itemIds[i]);");
		js.append("}");
		return js.toString();
	}
}