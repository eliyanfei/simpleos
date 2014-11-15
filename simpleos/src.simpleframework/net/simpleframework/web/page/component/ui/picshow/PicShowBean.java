package net.simpleframework.web.page.component.ui.picshow;

import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.AbstractContainerBean;
import net.simpleframework.web.page.component.IComponentHandle;
import net.simpleframework.web.page.component.IComponentRegistry;

import org.dom4j.Element;

public class PicShowBean extends AbstractContainerBean {
	private Collection<PicItem> picItems;
	private PicNext picNext;
	private PicPrevious picPrevious;
	private String arg;
	private String path;
	private String currArg;
	private boolean show = false;
	private boolean showTop = true;
	private boolean showCenter = true;

	public boolean isShowTop() {
		return showTop;
	}

	public void setShowTop(boolean showTop) {
		this.showTop = showTop;
	}

	public boolean isShowCenter() {
		return showCenter;
	}

	public void setShowCenter(boolean showCenter) {
		this.showCenter = showCenter;
	}

	public boolean isShowbottom() {
		return showbottom;
	}

	public void setShowbottom(boolean showbottom) {
		this.showbottom = showbottom;
	}

	private boolean showbottom = true;
	private boolean override = false;

	public PicShowBean(final IComponentRegistry componentRegistry, final PageDocument pageDocument, final Element element) {
		super(componentRegistry, pageDocument, element);
	}

	public boolean isOverride() {
		return override;
	}

	public void setOverride(boolean override) {
		this.override = override;
	}

	public Collection<PicItem> getPicItems() {
		if (picItems == null) {
			picItems = new ArrayList<PicItem>();
		}
		return picItems;
	}

	public String getArg() {
		return arg;
	}

	public void setArg(String arg) {
		if (!StringUtils.hasText(currArg)) {
			this.currArg = arg;
		}
		this.arg = arg;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public PicNext getPicNext() {
		return picNext == null ? picNext = new PicNext() : picNext;
	}

	public void setPicNext(PicNext picNext) {
		this.picNext = picNext;
	}

	public void setPicItems(Collection<PicItem> picItems) {
		this.picItems = picItems;
	}

	public PicPrevious getPicPrevious() {
		return picPrevious == null ? picPrevious = new PicPrevious() : picPrevious;
	}

	public void setPicPrevious(PicPrevious picPrevious) {
		this.picPrevious = picPrevious;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	@Override
	public IComponentHandle getComponentHandle(final PageRequestResponse requestResponse) {
		IComponentHandle handle = super.getComponentHandle(requestResponse);
		return handle == null ? new DefaultPicShowHandle() : handle;
	}

	public String getCurrArg() {
		return currArg;
	}
}
