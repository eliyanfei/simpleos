package net.simpleframework.web.page;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import net.simpleframework.util.CollectionUtils;
import net.simpleframework.util.LangUtils;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MultipartPageRequest extends HttpServletRequestWrapper implements
		IMultipartPageRequest {
	private static final ServletFileUpload fileUpload;

	static {
		final FileItemFactory fileItemFactory = new DiskFileItemFactory();
		fileUpload = new ServletFileUpload(fileItemFactory);
	}

	private final Map<String, FileItem> multipartFiles = new HashMap<String, FileItem>();

	private final Map<String, String[]> multipartParameters = new HashMap<String, String[]>();

	public MultipartPageRequest(final HttpServletRequest request) {
		super(request);
		parseRequest(request);
	}

	private void parseRequest(final HttpServletRequest request) {
		List<?> l;
		try {
			l = fileUpload.parseRequest(request);
		} catch (final FileUploadException e) {
			throw PageException.wrapException(e);
		}
		for (final Object o : l) {
			final FileItem item = (FileItem) o;
			final String fieldName = item.getFieldName();
			if (item.isFormField()) {
				final String val = item.getString();
				String[] params = multipartParameters.get(fieldName);
				if (params == null) {
					multipartParameters.put(fieldName, new String[] { val });
				} else {
					multipartParameters.put(fieldName, params = (String[]) LangUtils.add(params, val));
				}
			} else {
				multipartFiles.put(fieldName, item);
			}
		}
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return multipartParameters;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return CollectionUtils.enumeration(multipartParameters.keySet());
	}

	@Override
	public String[] getParameterValues(final String name) {
		return multipartParameters.get(name);
	}

	@Override
	public String getParameter(final String name) {
		final String[] params = getParameterValues(name);
		return params != null && params.length > 0 ? params[0] : null;
	}

	@Override
	public IMultipartFile getFile(final String name) {
		final FileItem item = multipartFiles.get(name);
		return new IMultipartFile() {
			@Override
			public String getOriginalFilename() {
				final String filename = item.getName();
				if (filename == null) {
					return "";
				}
				int pos = filename.lastIndexOf("/");
				if (pos == -1) {
					pos = filename.lastIndexOf("\\");
				}
				if (pos != -1) {
					return filename.substring(pos + 1);
				} else {
					return filename;
				}
			}

			@Override
			public long getSize() {
				return item.getSize();
			}

			@Override
			public InputStream getInputStream() throws IOException {
				return item.getInputStream();
			}

			@Override
			public byte[] getBytes() throws IOException {
				final byte[] bytes = item.get();
				return bytes != null ? bytes : new byte[0];
			}

			@Override
			public void transferTo(final File file) throws IOException {
				if (file.exists() && !file.delete()) {
					throw new IOException("Destination file [" + file.getAbsolutePath()
							+ "] already exists and could not be deleted");
				}
				try {
					item.write(file);
				} catch (final IOException ex) {
					throw ex;
				} catch (final Exception ex) {
					throw new IOException("Could not transfer to file: " + ex.getMessage());
				}
			}
		};
	}
}
