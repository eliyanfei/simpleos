package net.simpleframework.content.component.filepager;

import java.io.InputStream;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class FileLobBean extends AbstractIdDataObjectBean {

	private static final long serialVersionUID = -5884063720733822083L;

	private InputStream lob;

	public InputStream getLob() {
		return lob;
	}

	public void setLob(final InputStream lob) {
		this.lob = lob;
	}
}
