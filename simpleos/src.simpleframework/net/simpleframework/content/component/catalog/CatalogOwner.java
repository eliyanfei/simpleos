package net.simpleframework.content.component.catalog;

import net.simpleframework.core.bean.AbstractDataObjectBean;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.EMemberType;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class CatalogOwner extends AbstractDataObjectBean {
	private ID catalogId;

	private EMemberType ownerType;

	private ID ownerId;

	public ID getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(final ID catalogId) {
		this.catalogId = catalogId;
	}

	public EMemberType getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(final EMemberType ownerType) {
		this.ownerType = ownerType;
	}

	public ID getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(final ID ownerId) {
		this.ownerId = ownerId;
	}

	private static final long serialVersionUID = 3723622885317072183L;
}
