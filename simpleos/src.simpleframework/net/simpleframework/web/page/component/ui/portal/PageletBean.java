package net.simpleframework.web.page.component.ui.portal;

import java.util.Properties;

import net.simpleframework.core.AbstractElementBean;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.GenId;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.ETextAlign;
import net.simpleframework.web.page.component.ui.portal.module.IPortalModuleHandle;
import net.simpleframework.web.page.component.ui.portal.module.PortalModule;
import net.simpleframework.web.page.component.ui.portal.module.PortalModuleRegistryFactory;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PageletBean extends AbstractElementBean {

	private final ColumnBean columnBean;

	private PageletTitle title;

	private int height;

	private ETextAlign align;

	private String fontStyle;

	private String module;

	private boolean sync;

	private String options;

	public PageletBean(final Element element, final ColumnBean columnBean) {
		super(element == null ? columnBean.getElement().addElement("pagelet") : element);
		this.columnBean = columnBean;
	}

	public PageletBean(final ColumnBean columnBean) {
		this(null, columnBean);
	}

	public PageletBean() {
		super(null);
		this.columnBean = null;
	}

	@Override
	public void syncElement() {
		super.syncElement();
		if (title != null) {
			title.syncElement();
		}
	}

	public ColumnBean getColumnBean() {
		return columnBean;
	}

	public PageletTitle getTitle() {
		if (title == null) {
			title = new PageletTitle(this);
		}
		return title;
	}

	public void setTitle(final PageletTitle title) {
		this.title = title;
	}

	public String getModule() {
		return StringUtils.text(module, PortalModuleRegistryFactory.DEFAULT_MODULE_NAME);
	}

	public void setModule(final String module) {
		this.module = module;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(final int height) {
		this.height = height;
	}

	public ETextAlign getAlign() {
		return align == null ? ETextAlign.left : align;
	}

	public void setAlign(final ETextAlign align) {
		this.align = align;
	}

	public String getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(final String fontStyle) {
		this.fontStyle = fontStyle;
	}

	public boolean isSync() {
		return sync;
	}

	public void setSync(final boolean sync) {
		this.sync = sync;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(final String options) {
		this.options = options;
	}

	public Properties getOptionProperties() {
		return ConvertUtils.toProperties(getOptions());
	}

	public String getOptionProperty(final String key) {
		final Properties properties = getOptionProperties();
		return properties != null ? properties.getProperty(key) : null;
	}

	public void setOptionProperty(final String key, final Object value) {
		Properties properties = getOptionProperties();
		if (properties == null) {
			properties = new Properties();
		}
		properties.setProperty(key, ConvertUtils.toString(value));
		setOptions(ConvertUtils.toString(properties));
	}

	public boolean removeOptionProperty(final String key) {
		final Properties properties = getOptionProperties();
		if (properties != null && properties.remove(key) != null) {
			setOptions(ConvertUtils.toString(properties));
			return true;
		}
		return false;
	}

	public PortalModule getModuleBean() {
		return PortalModuleRegistryFactory.getInstance().getModule(getModule());
	}

	private IPortalModuleHandle layoutModuleHandle;

	public IPortalModuleHandle getModuleHandle() {
		if (layoutModuleHandle == null) {
			final PortalModule moduleBean = getModuleBean();
			if (moduleBean == null) {
				return null;
			}
			final String handleClass = moduleBean.getHandleClass();
			if (StringUtils.hasText(handleClass)) {
				layoutModuleHandle = (IPortalModuleHandle) BeanUtils.newInstance(handleClass, new Class<?>[] { PageletBean.class },
						new Object[] { this });
			}
		}
		return layoutModuleHandle;
	}

	private String id;

	public String id() {
		if (id == null) {
			id = "li_" + GenId.genUID();
		}
		return id;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "options" };
	}
}
