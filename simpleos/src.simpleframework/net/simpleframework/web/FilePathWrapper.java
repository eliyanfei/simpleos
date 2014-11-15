package net.simpleframework.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.ServletContext;

import net.simpleframework.core.ALoggerAware;
import net.simpleframework.util.HTTPUtils;
import net.simpleframework.util.ImageUtils;
import net.simpleframework.util.IoUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.AbstractUrlForward;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.PageUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class FilePathWrapper extends ALoggerAware {
	private final File file;

	private final String path;

	public FilePathWrapper(final ServletContext servletContext, final String path) {
		this.path = path;
		file = new File(servletContext.getRealPath(path));
		if (!file.exists()) {
			IoUtils.createDirectoryRecursively(file);
		}
	}

	public File getFile() {
		return file;
	}

	public String getPath() {
		return path;
	}

	public String getImagePath(final PageRequestResponse requestResponse, final String src,
			final int width, final int height) {
		return getImagePath(requestResponse, new AbstractPathInputStreamAware(src) {
			@Override
			public InputStream getInputStream() throws IOException {
				return new URL(HTTPUtils.isAbsoluteUrl(src) ? src : AbstractUrlForward
						.getLocalhostUrl(requestResponse.request) + requestResponse.wrapContextPath(src))
						.openStream();
			}
		}, width, height);
	}

	public String getImagePath(final PageRequestResponse requestResponse,
			final IInputStreamAware inputStreamAware, final int width, final int height) {
		final String filename = inputStreamAware.getFilename(requestResponse, width, height);
		final File imageFile = new File(file.getAbsolutePath() + File.separator + filename);
		if (!imageFile.exists() || imageFile.length() == 0) {
			InputStream inputStream = null;
			try {
				ImageUtils.thumbnail((inputStream = inputStreamAware.getInputStream()), width, height,
						new FileOutputStream(imageFile), StringUtils.getFilenameExtension(filename));
				if (inputStream == null) {
					createNoImage(requestResponse, width, height, imageFile);
				}
			} catch (final Throwable e) {
				logger.warn(e);
				createNoImage(requestResponse, width, height, imageFile);
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (final IOException ex) {
					}
				}
			}
		}
		return requestResponse.wrapContextPath(path + filename);
	}

	private void createNoImage(final PageRequestResponse requestResponse, final int width,
			final int height, final File imageFile) {
		FileInputStream no_image = null;
		try {
			no_image = new FileInputStream(requestResponse.getServletContext().getRealPath(
					PageUtils.getResourcePath() + "/images/no_image.jpg"));
			ImageUtils.thumbnail(no_image, width, height, new FileOutputStream(imageFile));
		} catch (final Exception ex) {
			logger.warn(ex);
		} finally {
			if (no_image != null) {
				try {
					no_image.close();
				} catch (final IOException ex) {
				}
			}
		}
	}

	public abstract static class AbstractPathInputStreamAware implements IInputStreamAware {
		private final String path;

		public AbstractPathInputStreamAware(final String path) {
			this.path = path;
		}

		@Override
		public String getFilename(final PageRequestResponse requestResponse, final int width,
				final int height) {
			final StringBuilder sb = new StringBuilder();
			sb.append(StringUtils.hash(path));
			if (width == 0 && height == 0) {
				sb.append(".").append(
						StringUtils.text(StringUtils.getFilenameExtension(path), "png").toLowerCase());
			} else {
				sb.append("_").append(width).append("_").append(height).append(".png");
			}
			return sb.toString();
		}
	}

	public static interface IInputStreamAware {
		String getFilename(PageRequestResponse requestResponse, int width, int height);

		InputStream getInputStream() throws IOException;
	}
}
