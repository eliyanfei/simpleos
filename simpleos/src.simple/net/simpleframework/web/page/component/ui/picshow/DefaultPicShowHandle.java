package net.simpleframework.web.page.component.ui.picshow;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.AbstractComponentHandle;
import net.simpleframework.web.page.component.ComponentParameter;

public class DefaultPicShowHandle extends AbstractComponentHandle implements IPicShowHandle {
	FileFilterImg filterFilter = new FileFilterImg();

	@Override
	public PicShowBean getPicShowBean(final PicShowBean picShow, final ComponentParameter compParameter) throws Exception {
		final String filePath = compParameter.getServletContext().getRealPath("") + picShow.getPath();
		final String arg = compParameter.getRequestParameter("arg");
		if (StringUtils.hasText(arg)) {
			picShow.setArg(arg);
		} else {
			picShow.setArg((picShow.getCurrArg()==null?"":picShow.getCurrArg()));
		}
		if (picShow.isShow()) {
			final File file = new File(filePath);
			if (file.isDirectory() && file.exists()) {
				File pFile = file, nFile = file;
				final File[] files = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					pFile = nFile = files[i];
					if (files[i].getName().equalsIgnoreCase(picShow.getArg())) {
						if (i > 0) {
							pFile = files[i - 1];
						}
						if (i < files.length - 1) {
							nFile = files[i + 1];
						}
						break;
					}
				}
				picPrevious(picShow.getPicPrevious(), pFile);
				picNext(picShow.getPicNext(), nFile);
			}
		}
		getPicItems(picShow.getPicItems(), filePath + picShow.getArg(), picShow.isOverride());
		return picShow;
	}

	class FileFilterImg implements FileFilter {
		final String regStr = "jpg,jpeg,gif,png,bmp,";

		@Override
		public boolean accept(File pathname) {
			return regStr.indexOf(StringUtils.getFilenameExtension(pathname.getName()).toLowerCase()) != -1;
		}
	}

	/**
	 * �û��Զ���
	 */
	protected void picPrevious(final PicPrevious picPrevious, final File file) {
		if (file.isDirectory()) {
			final File[] files = file.listFiles(filterFilter);
			if (files.length > 0) {
				picPrevious.setImg_x50(files[0].getName());
				picPrevious.setTitle(file.getName());
				picPrevious.setArg(file.getName());
			}
		}
	}

	/**
	 * �û��Զ���
	 */
	protected void picNext(final PicNext picNext, final File file) {
		if (file.isDirectory()) {
			final File[] files = file.listFiles(filterFilter);
			if (files.length > 0) {
				picNext.setImg_x50(files[0].getName());
				picNext.setTitle(file.getName());
				picNext.setArg(file.getName());
			}
		}
	}

	/**
	 * �û��Զ���
	 */
	protected void getPicItems(final Collection<PicItem> picItems, final String realPath, final boolean override) {
		final File file = new File(realPath);
		if (file.isDirectory() && file.exists() && !override) {
			picItems.clear();
			final File[] files = file.listFiles(filterFilter);
			int i = 0;
			for (final File f : files) {
				final String fn = getFileName(f);
				final PicItem picItem = new PicItem();
				picItem.setId(String.valueOf(i++));
				picItem.setDate(formatDate(f.lastModified()));
				picItem.setTitle(fn);
				picItem.setDesc(fn);
				picItem.setImg_x(f.getName());
				picItem.setImg_x160(f.getName());
				picItem.setImg_x50(f.getName());
				picItem.setJsClickCallBack("alert(item)");
				picItems.add(picItem);
			}
		}
	}

	protected String getFileName(final File file) {
		final String fn = file.getName();
		return fn.substring(0, fn.lastIndexOf('.'));
	}

	protected String formatDate(final long value) {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
		return sdf.format(new Date(value));
	}
}
