package net.itsite.utils;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月20日 下午12:00:44 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public class WebUtils {

	static Log logger = LogFactory.getLog(WebUtils.class);

	public static final String BLANK_SPACE = " ";

	public static String getLinkHtml(final String href, final String text) {
		final StringBuffer linkBuffer = new StringBuffer(100);
		linkBuffer.append("<a href=\"#\" ");
		linkBuffer.append("onclick=\"");
		linkBuffer.append(href);
		linkBuffer.append("\"");
		linkBuffer.append(">");
		linkBuffer.append(text);
		linkBuffer.append("</a>");
		return linkBuffer.toString();
	}

	public static void toggleButtonElement(final List<String> toggleSelector, final String parentElement, final String[] childElements) {
		if (toggleSelector == null || StringUtils.isBlank(parentElement)) {
			return;
		}
		if (childElements == null || childElements.length <= 0) {
			return;
		}
		for (final String childElement : childElements) {
			toggleSelector.add(parentElement + BLANK_SPACE + childElement);
		}
	}

	public static void toggleButtonElement(final List<String> toggleSelector, final String parentElement) {
		toggleButtonElement(toggleSelector, parentElement,
				new String[] { ".textButton", "input[type=button]", "input[type=submit],input[type=reset]" });
	}

	public static String getDashedLineDivElement() {
		return getDashedLineElement("div");
	}

	public static String getDashedLineTableElement() {
		return getDashedLineElement("table");
	}

	public static String getDefaultTableElement() {
		final StringBuilder builder = new StringBuilder();
		builder.append("<table");
		builder.append(" ");
		builder.append("width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"");
		builder.append(">");
		return builder.toString();
	}

	public static String getBlockDivElement() {
		final StringBuilder builder = new StringBuilder();
		builder.append("<div");
		builder.append(" ");
		builder.append("class=\"project_block\" style=\"width:95%;\" align='center'");
		builder.append(">");
		return builder.toString();
	}

	public static String getDashedLineElement(final String elementName) {
		return getStyleElement(elementName, "");
	}

	public static String getStyleElement(final String elementName, String style) {
		if (StringUtils.isBlank(style)) {
			style = "";
		}
		final StringBuilder divBuilder = new StringBuilder();
		divBuilder.append("<").append(elementName).append(" ").append(style);
		divBuilder.append(" ");
		divBuilder.append(getDefaultStyleClass());
		divBuilder.append(">");
		return divBuilder.toString();
	}

	public static String getRowHtml(final boolean header, final String... params) {
		final StringBuilder builder = new StringBuilder();
		builder.append("<tr>");
		for (String value : params) {
			if (header) {
				value = "<b>" + value + "</b>";
			}
			builder.append(WebUtils.getColumeHtml(value));
		}
		builder.append("</tr>");
		return builder.toString();
	}

	public static String getColumeHtml(final String value) {
		final StringBuilder builder = new StringBuilder();
		builder.append(WebUtils.getStyleElement("td", "nowrap=\"nowrap\""));
		builder.append(value);
		builder.append("</td>");
		return builder.toString();
	}

	public static String getDefaultStyleClass() {
		final StringBuilder cssBuilder = new StringBuilder();
		cssBuilder.append("style=\"");
		cssBuilder.append("border-top-color:#CCCCCC;");
		cssBuilder.append("border-top-style:dashed;");
		cssBuilder.append("border-top-width:1px;");
		cssBuilder.append("border-bottom-width:1px;");
		cssBuilder.append("padding-left:0px;");
		cssBuilder.append("padding-right:0px;");
		cssBuilder.append("padding-top:8px;");
		cssBuilder.append("padding-bottom:8px;");
		cssBuilder.append("\"");
		return cssBuilder.toString();
	}

	public static String getSelectOptionHtml(final String name, final String value, final String style, final String selected) {
		final StringBuilder optionBuilder = new StringBuilder();
		optionBuilder.append("<option ");
		optionBuilder.append(" id=\"").append(name).append("\" ");
		optionBuilder.append(" name=\"").append(name).append("\" ");
		optionBuilder.append(" style=\"").append(style).append("\" ");
		if (StringUtils.isNotBlank(selected)) {
			optionBuilder.append(" selected=\"").append(selected).append("\" ");
		}
		optionBuilder.append(">");
		optionBuilder.append(value);
		optionBuilder.append("</option>");
		return optionBuilder.toString();
	}

	public static String getSelectOptionHtml(final String name, final String value) {
		return getSelectOptionHtml(name, value, "");
	}

	public static String getSelectOptionHtml(final String name, final String value, final String selected) {
		return getSelectOptionHtml(name, value, "width:100%;", selected);
	}

	/**
	 * 
	 * Description: 处理文本输入值中的HTML字符
	 * 
	 * @param str
	 * @param htmlToString如果是将html特殊字符替换为转义字符
	 *           ，设置为true.如果是将转义字符替换为html特殊字符，设置为false.
	 *           <p>
	 *           HTML特殊字符包括：<,>,",空格
	 *           </p>
	 * @return
	 */
	public static String replaceHtml(String str, final boolean htmlToString) {
		if (htmlToString) {
			str = str.replaceAll("<", "&lt;");
			str = str.replaceAll(">", "&gt;");
			str = str.replaceAll("\"", "&quot;");
			str = str.replaceAll(" ", "&nbsp;");
		} else {
			str = str.replaceAll("&lt;", "<");
			str = str.replaceAll("&gt;", ">");
			str = str.replaceAll("&quot;", "\"");
			str = str.replaceAll("&nbsp;", " ");
		}
		return str;
	}

	public static String splitHtml(final String str, final int split) {
		final StringBuffer result = new StringBuffer();
		if (str.length() > split) {
			int index = 0;
			if (str.length() % split == 0) {
				index = str.length() / split;
			} else {
				index = str.length() / split + 1;
			}
			for (int i = 0; i < index; i++) {
				if (i == index - 1) {
					result.append(str.substring(i * split, str.length()));
				} else {
					result.append(str.substring(i * split, (i + 1) * split)).append("\n");
				}
			}
			return result.toString();
		} else {
			return str;
		}
	}

}
