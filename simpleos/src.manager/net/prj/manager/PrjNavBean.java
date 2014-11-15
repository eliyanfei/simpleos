package net.prj.manager;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.id.ID;

/**
 * 导航，幻灯片
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2014-1-20下午05:08:55
 */
public class PrjNavBean extends AbstractIdDataObjectBean {
	private String title;
	private String url;
	private String image;
	private long oorder;

	public void setOorder(long oorder) {
		this.oorder = oorder;
	}

	public long getOorder() {
		return oorder;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
