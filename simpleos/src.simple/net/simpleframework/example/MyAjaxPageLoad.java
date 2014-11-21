package net.simpleframework.example;

import java.util.HashMap;
import java.util.Map;

import net.simpleframework.web.AbstractTitleAwarePageLoad;
import net.simpleframework.web.page.PageParameter;

public class MyAjaxPageLoad extends AbstractTitleAwarePageLoad {
	public static String version = "V1.12";
	public static Map<String, String> map = new HashMap<String, String>();
	static {
		map.put("pulldown", "个性化下拉列表");
		map.put("max", "最大化组件");
		map.put("totop", "置顶置底");
		map.put("dialog", "弹出窗口");
		map.put("tablesort", "表格排序");
		map.put("tip", "JS文字热点链接提示");
		map.put("contextmenu", "自定义多级右键菜单");
		map.put("votes", "纯div+css投票结果图效果");
		map.put("rating", "页面星级评分功能");
		map.put("calendar", "Js日期组件");
		map.put("progress", "Js进度条");
		map.put("autoComplete", "Js自动完成");
		map.put("colorPicker", "Js颜色选择器");
		map.put("inserttext", "文本域光标处插入值");
		map.put("radio", "获取radio的值或者属性");
		map.put("texttip", "文本框提示文字");
		map.put("timer", "Javascript定时器");
		map.put("checkbox", "全选反选Checkbox");
		map.put("ittool", "ittool Api");
		map.put("tooltipmenu", "tooltip菜单");
	}

	@Override
	public Object getBeanProperty(PageParameter pageParameter, String beanProperty) {
		if ("title".equals(beanProperty)) {
			return wrapApplicationTitle("JS工具包-" + map.get(pageParameter.getRequestParameter("p")));
		}
		return super.getBeanProperty(pageParameter, beanProperty);
	}

}
