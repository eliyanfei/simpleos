package net.prj.manager.datatemp;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.id.ID;

/**
 * @author 模板对象
 * 2013-3-28上午09:48:16
 */
public class PrjDataTempValueBean extends AbstractIdDataObjectBean {
	private ID dataTempId;//模板ID
	private String title;//名称

	public PrjDataTempValueBean() {
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
