package net.simpleframework.my.space;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.id.ID;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class SapceResourceBean extends AbstractIdDataObjectBean {

	private ID logId;

	private ELogResource logResource;

	private ID resourceId;

	private String resourceUrl;

	public ID getLogId() {
		return logId;
	}

	public void setLogId(final ID logId) {
		this.logId = logId;
	}

	public ELogResource getLogResource() {
		return logResource;
	}

	public void setLogResource(final ELogResource logResource) {
		this.logResource = logResource;
	}

	public ID getResourceId() {
		return resourceId;
	}

	public void setResourceId(final ID resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceUrl() {
		return resourceUrl;
	}

	public void setResourceUrl(final String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}

	private static final long serialVersionUID = -3957792343244222722L;
}
