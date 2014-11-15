package net.simpleframework.web.page.component.ui.picshow;

import java.util.Collection;

import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.ComponentParameter;

public class PicShowUtils {
	public static final String BEAN_ID = "picshow_@bid";

	public static String genJavascript(final ComponentParameter compParameter) {
		final StringBuffer buf = new StringBuffer();
		try {
			buf.append("var slide_data = {");
			PicShowBean picShow = (PicShowBean) compParameter.componentBean;
			final IPicShowHandle handle = (IPicShowHandle) picShow.getComponentHandle(compParameter);
			if (handle != null) {
				picShow = handle.getPicShowBean(picShow, compParameter);
			}

			buf.append("slide:{");
			buf.append("width:").append(picShow.getWidth()).append(",");
			buf.append("height:").append(picShow.getHeight()).append(",");
			buf.append("showCenter:").append(picShow.isShowCenter()).append(",");
			buf.append("path:\"" + compParameter.getServletContext().getContextPath() + picShow.getPath() + "\"");
			buf.append("},");
			buf.append("images:[");
			int i = 0;
			Collection<PicItem> items = picShow.getPicItems();
			for (final PicItem picItem : items) {
				buf.append("{");
				buf.append("title:").append("\"").append(picItem.getTitle()).append("\",");
				buf.append("intro:").append("\"").append(picItem.getDesc()).append("\",");
				buf.append("thumb_50:").append("\"");
				if(StringUtils.hasText(picShow.getArg())){
					buf.append(picShow.getArg()+"/");
				}
				buf.append(picItem.getImg_x50()).append("\",");
				buf.append("thumb_160:").append("\"");
				if(StringUtils.hasText(picShow.getArg())){
					buf.append(picShow.getArg()+"/");
				}
				buf.append( picItem.getImg_x160()).append("\",");
				buf.append("image_url:").append("\"");
				if(StringUtils.hasText(picShow.getArg())){
					buf.append(picShow.getArg()+"/");
				}
				buf.append(picItem.getImg_x()).append("\",");
				buf.append("createtime:").append("\"").append(picItem.getDate()).append("\",");
				buf.append("callback:").append("function (item){").append(picItem.getJsClickCallBack()).append("},");
				buf.append("id:").append("\"").append(picItem.getId()).append("\"");
				buf.append("}");
				if (i != items.size() - 1) {
					buf.append(",");
				}
			}
			buf.append("],");
			final PicPrevious picPrevious = picShow.getPicPrevious();
			buf.append("prev_album:");
			buf.append("{");
			buf.append("title:\"" + picPrevious.getTitle() + "\",");
			buf.append("arg:\"" + picPrevious.getArg() + "\", ");
			buf.append("thumb_50:\"" + picPrevious.getImg_x50() + "\"");
			buf.append("},");
			final PicNext picNext = picShow.getPicNext();
			buf.append("next_album:");
			buf.append("{");
			buf.append("title:\"" + picNext.getTitle() + "\",");
			buf.append("arg:\"" + picNext.getArg() + "\", ");
			buf.append("thumb_50:\"" + picNext.getImg_x50() + "\"");
			buf.append("}");
			buf.append("};");
			buf.append("_initData();");
			if (!picShow.isShow()) {
				buf.append("_checkShowOther();");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buf.toString();
	}

}
