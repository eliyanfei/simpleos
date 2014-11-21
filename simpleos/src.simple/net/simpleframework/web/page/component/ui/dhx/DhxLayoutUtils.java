package net.simpleframework.web.page.component.ui.dhx;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Date 2012-12-4
 * @author lxy
 */
public class DhxLayoutUtils {

	public static String skin_name_prefix = "dhx_";
	public static String templateFileName = "/$resource/default/dhxlayout/temp/template.txt";
	public static List<String> additionalPattern = new ArrayList<String>();
	static {
		for (EDhxLayoutPattern ep : EDhxLayoutPattern.values()) {
			additionalPattern.add(ep.name().substring(1));
		}
	}
	static String[] cellsName = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t" };

	public static String getCellName(int i) {
		return cellsName[i];
	}

	public static String getImgPath(EDhxLayoutSkin skin) {
		return "/dhxlayout/imgs/dhxlayout_dhx_" + skin.name();
	}

}
