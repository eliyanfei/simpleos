package net.simpleframework.content.component.filepager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.core.ALoggerAware;
import net.simpleframework.core.AbstractXmlDocument;
import net.simpleframework.core.Logger;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.util.AlgorithmUtils;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.JavascriptUtils;
import net.simpleframework.util.LangUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.ComponentParameter;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class FilePagerUtils {
	static Logger logger = ALoggerAware.getLogger(FilePagerUtils.class);

	public static final String BEAN_ID = "file_@bid";

	public static ComponentParameter getComponentParameter(final PageRequestResponse requestResponse) {
		return ComponentParameter.get(requestResponse, BEAN_ID);
	}

	public static ComponentParameter getComponentParameter(final HttpServletRequest request,
			final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}

	public static String getFileImage(final ComponentParameter compParameter, final FileBean file) {
		return getFileImage(compParameter, file, 0);
	}

	public static String getFileImage(final ComponentParameter compParameter, final FileBean file,
			final int size) {
		final StringBuilder sb = new StringBuilder();
		final IconW iconW = getIconW(file);
		if (iconW != null) {
			sb.append("<img style=\"vertical-align: middle;\" src=\"").append(
					compParameter.componentBean.getResourceHomePath(compParameter));
			sb.append("/images/");
			if (size > 0) {
				sb.append(size).append("_");
			}
			sb.append(iconW.icon);
			sb.append("\" ");
			if (StringUtils.hasText(iconW.desc)) {
				sb.append("title=\"").append(JavascriptUtils.escape(iconW.desc)).append("\" ");
			}
			sb.append("/>");
		}
		return sb.toString();
	}

	public static void doDownload(final ComponentParameter compParameter) {
		final IFilePagerHandle fHandle = (IFilePagerHandle) compParameter.getComponentHandle();
		final FileBean fileBean = fHandle.getEntityBeanByRequest(compParameter);
		try {
			fHandle.doDownload(compParameter, fileBean);
		} catch (final Exception e) {
			logger.warn(e.getMessage());
		}
	}

	public static class IconW {
		public String icon;

		public String[] ext;

		public String desc;
	}

	static IconW none = new IconW() {
		{
			icon = "none.png";
			desc = LocaleI18n.getMessage("FilePagerUtils.0");
		}
	};

	static List<IconW> icons;

	public static IconW getIconW(final FileBean file) {
		synchronized (none) {
			if (icons == null) {
				new AbstractXmlDocument(FilePagerUtils.class.getClassLoader().getResourceAsStream(
						BeanUtils.getResourceClasspath(FilePagerUtils.class, "file_type.xml"))) {
					@Override
					protected void init() {
						icons = new ArrayList<IconW>();
						final Iterator<?> it = getRoot().elementIterator();
						while (it.hasNext()) {
							final IconW iconW = new IconW();
							final Element element = (Element) it.next();
							iconW.icon = element.attributeValue("icon");
							iconW.ext = StringUtils.split(element.attributeValue("ext"));
							iconW.desc = LocaleI18n.replaceI18n(element.attributeValue("desc"));
							icons.add(iconW);
						}
					}
				};
			}
		}
		for (final IconW iconW : icons) {
			final String type = StringUtils.text(file.getFiletype(),
					StringUtils.getFilenameExtension(file.getFilename())).toLowerCase();
			if (LangUtils.contains(iconW.ext, type)) {
				return iconW;
			}
		}
		return none;
	}

	public static String getMd5(final ComponentParameter compParameter, final FileBean file) {
		String md5 = file.getMd5();
		if (!StringUtils.hasText(md5)) {
			final IFilePagerHandle pagerHandle = (IFilePagerHandle) compParameter.getComponentHandle();
			final InputStream inputStream = pagerHandle.getFileInputStream(compParameter, file);
			if (inputStream != null) {
				file.setMd5(md5 = AlgorithmUtils.md5Hex(inputStream));
				pagerHandle.getTableEntityManager(compParameter, FileBean.class).update(file);
			}
		}
		return StringUtils.blank(md5);
	}

	public static String getSha1(final ComponentParameter compParameter, final FileBean file) {
		String sha1 = file.getSha1();
		if (!StringUtils.hasText(sha1)) {
			final IFilePagerHandle pagerHandle = (IFilePagerHandle) compParameter.getComponentHandle();
			final InputStream inputStream = pagerHandle.getFileInputStream(compParameter, file);
			if (inputStream != null) {
				file.setSha1(sha1 = AlgorithmUtils.shaHex(inputStream));
				pagerHandle.getTableEntityManager(compParameter, FileBean.class).update(file);
			}
		}
		return StringUtils.blank(sha1);
	}

	public final static Table table_file = new Table("simple_file");

	public final static Table table_file_lob = new Table("simple_file_lob");
	static {
		table_file_lob.setNoCache(true);
	}

	public static String getHomePath() {
		return AbstractComponentRegistry.getRegistry(FilePagerRegistry.filePager)
				.getResourceHomePath();
	}
}
