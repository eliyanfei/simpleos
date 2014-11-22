package net.simpleos.mvc.myfavorite;

import java.util.Date;

import net.simpleframework.core.bean.AbstractIdUserDataObjectBean;
import net.simpleframework.core.id.ID;
import net.simpleframework.web.EFunctionModule;

/**
 * 
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-16上午11:16:15
 */
public class MyFavoriteBean extends AbstractIdUserDataObjectBean {
	String title;
	String url;
	ID refId;
	Date createdate;
	EFunctionModule type;

	public MyFavoriteBean() {
		this.createdate = new Date();
	}

	public void setRefId(ID refId) {
		this.refId = refId;
	}

	public ID getRefId() {
		return refId;
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

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public EFunctionModule getType() {
		return type;
	}

	public void setType(EFunctionModule type) {
		this.type = type;
	}

}
