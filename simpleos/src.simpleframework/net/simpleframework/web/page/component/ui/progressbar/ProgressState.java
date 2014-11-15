package net.simpleframework.web.page.component.ui.progressbar;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ProgressState implements Serializable {
	private static final long serialVersionUID = -5194114792906218671L;

	public int maxProgressValue;

	public int step;

	public ArrayList<Object> messages = new ArrayList<Object>();

	public boolean abort;

	public void step(final String message) {
		step++;
		messages.add(message);
	}
}
