package net.a;

import java.io.File;
import java.io.FileFilter;

import net.itsite.utils.IOUtils;

public class SVNUtils {
	public static void main(String[] args) {
		File file = new File("D:\\developer");
		findSvn(file);
	}

	public static void findSvn(File file) {
		file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				//				if (file.getName().equals("green") || file.getName().equals("red")) {
				deleteSvn(file);
				//				}
				if (file.isDirectory()) {
					findSvn(file);
				}
				return false;
			}
		});
	}

	public static void deleteSvn(File file) {
		file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				if (file.getName().equals(".svn")) {
					System.out.println(file.getAbsolutePath());
					IOUtils.delete(file);
				}
				if (file.isDirectory()) {
					deleteSvn(file);
				}
				return false;
			}
		});
	}
}
