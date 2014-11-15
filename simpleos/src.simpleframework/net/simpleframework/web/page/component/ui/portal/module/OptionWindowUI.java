package net.simpleframework.web.page.component.ui.portal.module;

import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.ui.portal.PageletBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class OptionWindowUI {
	public static OptionWindowUI getOptionWindowUI(final PageletBean pagelet, final String uiString) {
		final OptionWindowUI optionWindowUI = new OptionWindowUI(pagelet);
		final String[] arr = StringUtils.split(uiString, ";");
		if (arr != null) {
			optionWindowUI.title = arr[0].substring(6);
			optionWindowUI.height = ConvertUtils.toInt(arr[1].substring(7), 0);
			optionWindowUI.width = ConvertUtils.toInt(arr[2].substring(6), 0);
		}
		return optionWindowUI;
	}

	private final PageletBean pagelet;

	public OptionWindowUI(final PageletBean pagelet) {
		this.pagelet = pagelet;
	}

	private String title;

	private int height, width;

	public String getTitle() {
		return StringUtils.hasText(title) ? title : pagelet.getModuleBean().getText();
	}

	public int getHeight() {
		return height > 0 ? height : 210;
	}

	public int getWidth() {
		return width > 0 ? width : 400;
	}

	@Override
	public String toString() {
		return "title=" + StringUtils.blank(getTitle()) + ";height=" + getHeight() + ";width="
				+ getWidth();
	}
}
