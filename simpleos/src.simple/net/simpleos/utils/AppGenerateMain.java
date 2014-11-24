package net.simpleos.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.zip.ZipOutputStream;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月24日 下午1:27:05 
 *
 */
public class AppGenerateMain {
	public static List<String> filter = new ArrayList<String>();
	static {
		filter.add("db.properties");
		filter.add("lib");
		filter.add("web.xml");
		filter.add("classes");
		filter.add(".gitignore");
	}

	public static void main(String[] args) {
		try {
			String path = "E:\\git\\simpleos\\simpleos\\WebRoot";
			String resourceFile = "E:\\git\\simpleos\\simpleos\\src.simple\\org\\tuckey\\web\\filters\\urlrewrite\\simpleos.zip";
			final File file = new File(resourceFile);
			file.delete();
			final ZipOutputStream out = new ZipOutputStream(file);
			new File(path).listFiles(new FileFilter() {

				@Override
				public boolean accept(File f) {
					try {
						if (filter.contains(f.getName())) {
							return false;
						}
						zipFilter(out, f, f.getName());
					} catch (Exception e) {
					}
					return false;
				}
			});
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void zipFilter(final ZipOutputStream out, final File f, String base) throws Exception {
		if (filter.contains(f.getName())) {
			return;
		}
		if (f.isDirectory()) {
			final File[] fl = f.listFiles();
			base = base.length() == 0 ? "" : base + File.separator;
			for (final File element : fl) {
				zipFilter(out, element, base + element.getName());
			}
		} else {
			out.putNextEntry(new org.apache.tools.zip.ZipEntry(base));
			final FileInputStream in = new FileInputStream(f);
			IOUtils.copyStream(in, out);
			in.close();
		}
	}
}
