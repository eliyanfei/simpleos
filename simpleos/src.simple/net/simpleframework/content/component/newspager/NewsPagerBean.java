package net.simpleframework.content.component.newspager;

import net.simpleframework.content.AbstractContentPagerBean;
import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.IComponentHandle;
import net.simpleframework.web.page.component.IComponentRegistry;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class NewsPagerBean extends AbstractContentPagerBean {
	private boolean showImage;

	public NewsPagerBean(final IComponentRegistry componentRegistry,
			final PageDocument pageDocument, final Element element) {
		super(componentRegistry, pageDocument, element);
		setExportAction("false");
	}

	@Override
	public String getDataPath() {
		return getResourceHomePath() + "/jsp/newspager.jsp";
	}

	@Override
	protected Class<? extends IComponentHandle> getDefaultHandleClass() {
		return DefaultNewsPagerHandle.class;
	}

	public boolean isShowImage() {
		return showImage;
	}

	public void setShowImage(final boolean showImage) {
		this.showImage = showImage;
	}
}
