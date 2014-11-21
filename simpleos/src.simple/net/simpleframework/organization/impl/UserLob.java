package net.simpleframework.organization.impl;

import java.io.InputStream;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class UserLob extends AbstractIdDataObjectBean {
	private InputStream photo;

	public InputStream getPhoto() {
		return photo;
	}

	public void setPhoto(final InputStream photo) {
		this.photo = photo;
	}

	private static final long serialVersionUID = -3981205005752155025L;
}
