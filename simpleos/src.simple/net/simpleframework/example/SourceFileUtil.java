package net.simpleframework.example;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;

public class SourceFileUtil {
	private static StringBuffer srcBuffer = new StringBuffer();
	private static List<String> srcList;

	/**
	 * @param realPath
	 *           : ServletContext of calling page
	 * @param p
	 *           : request of calling page
	 * @param srcFileName
	 *           : Source file Name
	 * @return
	 */
	public static String getSrcContent(final ServletContext context, final HttpServletRequest request, final String srcFileName) {
		final String srcFileRelativePath = !srcFileName.toLowerCase().endsWith(".java") ? "app/demo/comps/" + request.getParameter("p")
				: "example.src";
		return getSrcContent(request, srcFileRelativePath, srcFileName);
	}

	public static String getSrcContent(final HttpServletRequest request, String path, final String srcFileName) {
		final File demoFile = new File(request.getRealPath("/") + path + File.separator + srcFileName);
		srcList = null;
		try {
			if (demoFile.exists()) {
				srcList = FileUtils.readLines(demoFile, "UTF-8");
			}
		} catch (final IOException e) {
			new Throwable(e.getMessage());
		}
		if (srcList != null) {
			srcBuffer.setLength(0);
			for (final String srcLine : srcList) {
				srcBuffer.append(srcLine).append("\r\n");
			}
		}
		return null == srcBuffer ? "" : srcBuffer.toString();
	}
}