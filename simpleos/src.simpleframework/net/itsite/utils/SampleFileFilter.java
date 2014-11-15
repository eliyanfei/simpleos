package net.itsite.utils;

import java.io.File;
import java.io.FileFilter;

public class SampleFileFilter implements FileFilter {
	public final String[] fileNamesExt;
	public final boolean filterDirectory;

	/**
	 * 过滤文件，不过滤目录，
	 * 
	 * @param fileNamesExt
	 *            文件名后缀
	 */
	public SampleFileFilter(final String... fileNamesExt) {
		this(false, fileNamesExt);
	}

	private final boolean hasExts;

	/**
	 * 过滤文件和目录
	 * 
	 * @param filterDirectory
	 *            指定是否过滤目录
	 * @param fileNamesExt
	 *            文件名后缀
	 */
	public SampleFileFilter(final boolean filterDirectory, final String... fileNamesExt) {
		super();
		this.filterDirectory = filterDirectory;
		this.fileNamesExt = fileNamesExt;
		hasExts = this.fileNamesExt != null && this.fileNamesExt.length > 0;
	}

	@Override
	public boolean accept(final File path) {
		// 如果当前没有指定文件名后缀，将认为所有的数据都可用
		if (!hasExts)
			return true;

		// 如果没有指定过滤目录但当前传入的是目录，将返回false
		if (filterDirectory && path.isDirectory())
			return false;

		final String fileName = path.getName().toLowerCase();
		String tmpFileName;
		for (final String fileNameExt : this.fileNamesExt) {
			tmpFileName = fileNameExt.toLowerCase();
			if (fileName.endsWith(tmpFileName) || tmpFileName.equals(fileName))
				return true;
		}
		return false;
	}
}

