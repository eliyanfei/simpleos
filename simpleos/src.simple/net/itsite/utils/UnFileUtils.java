package net.itsite.utils;

import java.io.File;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月20日 下午12:00:44 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
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
