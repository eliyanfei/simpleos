package net.simpleframework.web.page.component.ui.tree;

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
public class FolderTreeBean extends AbstractTreeBean {

	private String rootFolderPath;

	private boolean showRoot = true;

	private boolean showFile = false;

	public FolderTreeBean(final IComponentRegistry componentRegistry,
			final PageDocument pageDocument, final Element element) {
		super(componentRegistry, pageDocument, element);
		setDynamicLoading(true);
	}

	@Override
	protected Class<? extends IComponentHandle> getDefaultHandleClass() {
		return FolderTreeHandle.class;
	}

	public String getRootFolderPath() {
		return rootFolderPath;
	}

	public void setRootFolderPath(final String rootFolderPath) {
		this.rootFolderPath = rootFolderPath;
	}

	public boolean isShowRoot() {
		return showRoot;
	}

	public void setShowRoot(final boolean showRoot) {
		this.showRoot = showRoot;
	}

	public boolean isShowFile() {
		return showFile;
	}

	public void setShowFile(final boolean showFile) {
		this.showFile = showFile;
	}
}
