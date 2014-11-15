package net.itsite.utils;

import java.io.File;

public class UnFileUtils {

	/**
	 * 解压ZIP
	 * @param targetPath
	 * @param absolutePath
	 */
	public static boolean unZip(final String targetPath, final String absolutePath) {
		final File targetFile = new File(targetPath);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		try {
			ZipUtils.unZip(absolutePath, targetPath);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
