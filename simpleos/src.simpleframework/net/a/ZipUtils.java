package net.a;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import net.itsite.utils.IOUtils;

public class ZipUtils {
	public static void main(String[] args) {
		File file = new File("D:\\works\\projectlist\\src.simpleframework\\net\\simpleframework");
		list(file);
	}

	public static void list(File file) {
		file.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				if (pathname.isAbsolute()) {
					list(pathname);
				}
				if (pathname.isFile()) {
					if (pathname.getName().startsWith("resource")) {
						try {
							IOUtils.delete(pathname);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

				return false;
			}
		});
	}
}
