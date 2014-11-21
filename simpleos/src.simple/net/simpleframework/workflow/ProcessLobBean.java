package net.simpleframework.workflow;

import java.io.Reader;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ProcessLobBean extends AbstractIdDataObjectBean {
	private Reader processModel;

	public Reader getProcessModel() {
		return processModel;
	}

	public void setProcessModel(final Reader processModel) {
		this.processModel = processModel;
	}

	private static final long serialVersionUID = 219135045162448573L;
}
