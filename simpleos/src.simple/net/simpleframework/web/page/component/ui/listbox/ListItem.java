package net.simpleframework.web.page.component.ui.listbox;

import net.simpleframework.util.ConvertUtils;
import net.simpleframework.web.page.component.AbstractNodeUIBean;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ListItem extends AbstractNodeUIBean {

	private final ListboxBean listboxBean;

	private String tip;

	private String run;

	private boolean sub;

	private final Object data;

	public ListItem(final Element element, final ListboxBean listboxBean, final Object data) {
		super(element);
		this.listboxBean = listboxBean;
		this.data = data;
		setText(ConvertUtils.toString(data));
	}

	public ListItem(final ListboxBean listboxBean, final Object data) {
		this(null, listboxBean, data);
	}

	public String getTip() {
		return tip;
	}

	public void setRun(String run) {
		this.run = run;
	}

	public String getRun() {
		return run;
	}

	public void setTip(final String tip) {
		this.tip = tip;
	}

	public Object getDataObject() {
		return data;
	}

	public ListboxBean getListboxBean() {
		return listboxBean;
	}

	public void setSub(boolean sub) {
		this.sub = sub;
	}

	public boolean isSub() {
		return sub;
	}
}
