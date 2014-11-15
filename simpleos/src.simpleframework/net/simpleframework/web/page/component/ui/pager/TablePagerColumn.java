package net.simpleframework.web.page.component.ui.pager;

import java.util.HashSet;

import net.simpleframework.core.AbstractElementBean;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.ETextAlign;

import org.dom4j.Element;
import org.springframework.util.ClassUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TablePagerColumn extends AbstractElementBean {

	private String columnName;

	private String columnText;

	private String beanPropertyType;

	private String beanPropertyValue;

	private String headerStyle, style;

	private int width;

	private ETextAlign textAlign;

	private boolean separator;

	private String title;

	private String format;

	private boolean sort = true;

	private boolean filter;

	private boolean defaultExport = true;

	private boolean visible = true;

	private String columnSqlName;

	public TablePagerColumn(final Element dom4jElement) {
		super(dom4jElement);
	}

	public TablePagerColumn() {
		this((Element) null);
	}

	public TablePagerColumn(final String columnName) {
		this((Element) null);
		setColumnName(columnName);
	}

	public TablePagerColumn(final String columnName, final String columnText) {
		this((Element) null);
		setColumnName(columnName);
		setColumnText(columnText);
	}

	public void setBeanPropertyValue(String beanPropertyValue) {
		this.beanPropertyValue = beanPropertyValue;
	}

	public String getBeanPropertyValue() {
		return beanPropertyValue;
	}

	public String getBeanPropertyType() {
		return beanPropertyType;
	}

	public void setBeanPropertyType(final String beanPropertyType) {
		this.beanPropertyType = beanPropertyType;
	}

	public Class<?> beanPropertyType() {
		final String type = getBeanPropertyType();
		if (StringUtils.hasText(type)) {
			try {
				return BeanUtils.forName(type);
			} catch (final ClassNotFoundException e) {
			}
		}
		return String.class;
	}

	public String getHeaderStyle() {
		return toStyle(headerStyle, true);
	}

	public void setHeaderStyle(final String headerStyle) {
		this.headerStyle = headerStyle;
	}

	private String toStyle(final String style, final boolean header) {
		final String[] styles = StringUtils.split(style, ";");
		final HashSet<String> set = new HashSet<String>();
		if (styles != null) {
			for (final String s : styles) {
				set.add(StringUtils.replace(s.toLowerCase(), " ", ""));
			}
		}
		if (width > 0) {
			set.add("width:" + width + "px");
		}
		if (!header) {
			final ETextAlign textAlign = getTextAlign();
			if (textAlign != null) {
				set.add("text-align:" + textAlign);
			}
		}
		return set.size() > 0 ? StringUtils.join(set, ";") : null;
	}

	public String getPropertyClass() {
		return beanPropertyType;
	}

	public String getFilterVal(final String val) {
		return val;
	}

	public Class<?> propertyClass() {
		final String clazz = getPropertyClass();
		if (StringUtils.hasText(clazz)) {
			try {
				return ClassUtils.forName(clazz);
			} catch (final ClassNotFoundException e) {
			}
		}
		return String.class;
	}

	protected Enum<?>[] getEnumConstants() {
		final Class<?> pClass = propertyClass();
		if (Enum.class.isAssignableFrom(pClass)) {
			return (Enum<?>[]) pClass.getEnumConstants();
		}
		return null;
	}

	public String getStyle() {
		return toStyle(style, false);
	}

	public void setStyle(final String style) {
		this.style = style;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(final int width) {
		this.width = width;
	}

	public ETextAlign getTextAlign() {
		return textAlign;
	}

	public void setTextAlign(final ETextAlign textAlign) {
		this.textAlign = textAlign;
	}

	public boolean isSeparator() {
		return separator;
	}

	public void setSeparator(final boolean separator) {
		this.separator = separator;
	}

	public boolean isDefaultExport() {
		return defaultExport;
	}

	public void setDefaultExport(final boolean defaultExport) {
		this.defaultExport = defaultExport;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(final String columnName) {
		this.columnName = columnName;
	}

	public String getColumnText() {
		return StringUtils.text(columnText, getColumnName());
	}

	public void setColumnText(final String columnText) {
		this.columnText = columnText;
	}

	public boolean isSort() {
		return sort;
	}

	public void setSort(final boolean sort) {
		this.sort = sort;
	}

	public boolean isFilter() {
		return filter;
	}

	public void setFilter(final boolean filter) {
		this.filter = filter;
	}

	public String getColumnSqlName() {
		return StringUtils.text(columnSqlName, getColumnName());
	}

	public void setColumnSqlName(final String columnSqlName) {
		this.columnSqlName = columnSqlName;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(final boolean visible) {
		this.visible = visible;
	}

	public String getFormat() {
		return StringUtils.text(format, "yyyy-MM-dd HH:mm");
	}

	public void setFormat(final String format) {
		this.format = format;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "title" };
	}
}
