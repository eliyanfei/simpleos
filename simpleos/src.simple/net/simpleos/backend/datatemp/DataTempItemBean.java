package net.simpleos.backend.datatemp;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.id.ID;

/**
 * @author 模板对象
 * 2013-3-28上午09:48:16
 */
public class DataTempItemBean extends AbstractIdDataObjectBean {
	private ID dataTempId;//模板ID
	private String title;//名称

	public DataTempItemBean() {
	}

	public ID getDataTempId() {
		return dataTempId;
	}

	public void setDataTempId(ID dataTempId) {
		this.dataTempId = dataTempId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
