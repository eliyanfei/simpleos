package net.simpleframework.workflow;

import java.io.InputStream;
import java.io.Reader;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ProcessModelLobBean extends AbstractIdDataObjectBean {

	private Reader processSchema;

	private InputStream processImage;

	public Reader getProcessSchema() {
		return processSchema;
	}

	public void setProcessSchema(final Reader processSchema) {
		this.processSchema = processSchema;
	}

	public InputStream getProcessImage() {
		return processImage;
	}

	public void setProcessImage(final InputStream processImage) {
		this.processImage = processImage;
	}

	private static final long serialVersionUID = -8281273400041652815L;
}
