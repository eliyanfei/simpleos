package net.simpleframework.content.component.catalog;

import java.util.Collection;
import java.util.Map;

import net.simpleframework.content.IContentHandle;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.menu.MenuBean;
import net.simpleframework.web.page.component.ui.menu.MenuItem;
import net.simpleframework.web.page.component.ui.propeditor.PropEditorBean;
import net.simpleframework.web.page.component.ui.propeditor.PropField;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeBean;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeNode;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface ICatalogHandle extends IContentHandle {

	boolean showHtml();
	
	IDataObjectQuery<? extends Catalog> catalogs(ComponentParameter compParameter, Object parentId);

	IDataObjectQuery<? extends Catalog> catalogs(ComponentParameter compParameter);

	IDataObjectQuery<CatalogOwner> catalogOwners(ComponentParameter compParameter, Object catalogId);

	Collection<? extends AbstractTreeNode> getCatalogTreenodes(ComponentParameter compParameter,
			AbstractTreeBean treeBean, AbstractTreeNode treeNode, boolean dictionary);

	Collection<MenuItem> getRootContextMenu(ComponentParameter compParameter, MenuBean menuBean);

	/********************************** PropEditor *****************************/
	Collection<PropField> getPropFields(ComponentParameter compParameter, PropEditorBean formEditor);

	Map<String, Object> doSavePropEditor(ComponentParameter compParameter);

	void doLoadPropEditor(PageParameter pageParameter, Catalog catalog,
			Map<String, Object> dataBinding);
}
