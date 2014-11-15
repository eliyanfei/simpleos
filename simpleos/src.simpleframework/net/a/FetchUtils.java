package net.a;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import net.itsite.utils.DateUtils;
import net.itsite.utils.IOUtils;
import net.itsite.utils.StringsUtils;

/**
 * 文件提取
 * @author 李岩飞
 *
 */
public class FetchUtils {
	public static void main(String[] args) {
		final String filePath1 = "d:\\prj\\";
		final String filePath2 = "d:\\works\\prj\\prj\\";
		IOUtils.listAllFilesNoRs(new File(filePath2), new FileFilter() {

			@Override
			public boolean accept(File file) {
				if (file.getName().equals(".svn") || file.getName().equals("desktop_index_c.xml")) {
					return false;
				}
				final long modify = file.lastModified();
				final long time = DateUtils.toDate("2014-01-04 14:00", "yyyy-MM-dd HH:mm").getTime();
				if (modify >= time) {
					if (file.isFile()) {
						File f = new File(StringsUtils.replace(file.getAbsolutePath(), filePath2, filePath1));
						f.getParentFile().mkdirs();
						try {
							IOUtils.copyFile(file, f);
						} catch (IOException e) {
							e.printStackTrace();
						}
						System.out.println(f.getName());
					}
				}
				return true;
			}
		});
	}
}
