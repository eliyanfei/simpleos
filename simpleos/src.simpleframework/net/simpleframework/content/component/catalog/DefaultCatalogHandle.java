package net.simpleframework.content.component.catalog;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.content.AbstractContentHandle;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.core.bean.IIdBeanAware;
import net.simpleframework.core.bean.ITreeBeanAware;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.EMemberType;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.GenId;
import net.simpleframework.util.LangUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.HandleException;
import net.simpleframework.web.page.component.ui.menu.MenuBean;
import net.simpleframework.web.page.component.ui.menu.MenuItem;
import net.simpleframework.web.page.component.ui.menu.MenuUtils;
import net.simpleframework.web.page.component.ui.propeditor.PropEditorBean;
import net.simpleframework.web.page.component.ui.propeditor.PropField;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeBean;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeNode;
import net.simpleframework.web.page.component.ui.tree.TreeException;
import net.simpleframework.web.page.component.ui.tree.TreeNode;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultCatalogHandle extends AbstractContentHandle implements ICatalogHandle {
	@Override
	public void handleCreated(final PageRequestResponse requestResponse, final AbstractComponentBean componentBean) {
		if (Catalog.class.equals(getEntityBeanClass())) {
			PageUtils.doDatabase(Catalog.class, componentBean);
		}
	}

	@Override
	public boolean showHtml() {
		return false;
	}

	@Override
	public Class<? extends IIdBeanAware> getEntityBeanClass() {
		return Catalog.class;
	}

	@Override
	public void putTables(final Map<Class<?>, Table> tables) {
		tables.put(Catalog.class, CatalogUtils.catalog_table);
		tables.put(CatalogOwner.class, CatalogUtils.catalog_owner_table);
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		parameters.put(CatalogUtils.BEAN_ID, compParameter.componentBean.hashId());
		final Object documentId = getDocumentId(compParameter);
		if (documentId != null) {
			parameters.put(getDocumentIdParameterName(compParameter), documentId);
		}
		return parameters;
	}

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("rootText".equals(beanProperty)) {
			final StringBuilder sb = new StringBuilder();
			sb.append(((CatalogBean) compParameter.componentBean).getRootText());
			sb.append("<br /><a class=\"addbtn a2\" onclick=\"__catalog_action(this).add(this);Event.stop(event);\">");
			sb.append(LocaleI18n.getMessage("Add")).append("</a>");
			return sb.toString();
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public IDataObjectQuery<? extends Catalog> catalogs(final ComponentParameter compParameter) {
		return catalogs(compParameter, null);
	}

	@Override
	public IDataObjectQuery<? extends Catalog> catalogs(final ComponentParameter compParameter, final Object parentId) {
		return beans(compParameter, parentId, "order by oorder desc");
	}

	@Override
	public IDataObjectQuery<CatalogOwner> catalogOwners(final ComponentParameter compParameter, final Object catalogId) {
		return getTableEntityManager(compParameter, CatalogOwner.class).query(new ExpressionValue("catalogId=?", new Object[] { catalogId }),
				CatalogOwner.class);
	}

	private void initCatalog(final ComponentParameter compParameter, final Catalog catalog, final Map<String, Object> data) {
		for (final Map.Entry<String, Object> entry : data.entrySet()) {
			final String key = entry.getKey();
			if ("parentId".equals(key)) {
				catalog.setParentId(getTableEntityManager(compParameter).getTable().newID(data.get("parentId")));
			} else if ("userId".equals(key)) {
				final IUser user = OrgUtils.um().queryForObjectById(data.get("userId"));
				if (user != null) {
					catalog.setUserId(user.getId());
				}
			} else {
				if (BeanUtils.isWriteable(catalog, key)) {
					BeanUtils.setProperty(catalog, key, entry.getValue());
				}
			}
		}
	}

	/**
	 * 是否允许编辑
	 * @return
	 */
	protected boolean isAllowEdit() {
		return false;
	}

	@Override
	public <T extends IDataObjectBean> void doBeforeEdit(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		super.doBeforeEdit(compParameter, temgr, t, data, beanClazz);
		final Catalog catalog = (Catalog) t;
		if (catalog.isBuildIn() && !isAllowEdit())
			throw HandleException.wrapException(LocaleI18n.getMessage("buildin.1"));
		initCatalog(compParameter, catalog, data);
		ITreeBeanAware.Utils.assertParentId(catalog);
	}

	@Override
	public <T extends IDataObjectBean> void doBeforeAdd(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		if (t instanceof Catalog) {
			final Catalog catalog = (Catalog) t;
			catalog.initThis(compParameter);
			final Object documentId = getDocumentId(compParameter);
			if (documentId != null) {
				catalog.setDocumentId(temgr.getTable().newID(documentId));
			}
			initCatalog(compParameter, catalog, data);
		} else if (t instanceof CatalogOwner) {
			final CatalogOwner co = (CatalogOwner) t;
			co.setCatalogId((ID) data.get("catalogId"));
			co.setOwnerType((EMemberType) data.get("ownerType"));
			co.setOwnerId((ID) data.get("ownerId"));
		}
	}

	@Override
	public <T extends IDataObjectBean> void doAddCallback(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		if (t instanceof Catalog) {
			final Catalog catalog = (Catalog) t;
			final ID userId = catalog.getUserId();
			if (userId != null) {
				final IUser user = OrgUtils.um().queryForObjectById(userId.getValue());
				if (user != null && !IUser.admin.equals(user.getName())) {
					final ITableEntityManager temgr2 = getTableEntityManager(compParameter, CatalogOwner.class);
					if (temgr2 != null) {
						final CatalogOwner catalogOwner = new CatalogOwner();
						catalogOwner.setCatalogId(catalog.getId());
						catalogOwner.setOwnerType(EMemberType.user);
						catalogOwner.setOwnerId(catalog.getUserId());
						temgr2.insert(catalogOwner);
					}
				}
			}
		}
	}

	@Override
	public <T extends IDataObjectBean> void doDelete(final ComponentParameter compParameter, final IDataObjectValue ev, final Class<T> beanClazz) {
		if (Catalog.class.isAssignableFrom(beanClazz)) {
			final Object catalogId = ev.getValues()[0];
			final Catalog catalog = getEntityBeanById(compParameter, catalogId);
			if (catalog != null) {
				if (catalog.isBuildIn())
					throw HandleException.wrapException(LocaleI18n.getMessage("buildin.0"));
				final IDataObjectQuery<? extends Catalog> qs = catalogs(compParameter, catalogId);
				if (qs.getCount() > 0) {
					throw TreeException.wrapDeleteException();
				} else {
					super.doDelete(compParameter, ev, beanClazz);
				}
			}
		} else {
			super.doDelete(compParameter, ev, beanClazz);
		}
	}

	@Override
	public <T extends IDataObjectBean> void doDeleteCallback(final ComponentParameter compParameter, final IDataObjectValue dataObjectValue,
			final Class<T> beanClazz) {
		if (Catalog.class.isAssignableFrom(beanClazz)) {
			final ITableEntityManager temgr = getTableEntityManager(compParameter, CatalogOwner.class);
			if (temgr != null) {
				temgr.delete(new ExpressionValue("catalogId=?", new Object[] { dataObjectValue.getValues()[0] }));
			}
		}
	}

	protected String getRootID(final AbstractTreeBean treeBean) {
		return treeBean.hashId();
	}

	@Override
	public Collection<? extends AbstractTreeNode> getCatalogTreenodes(final ComponentParameter compParameter, final AbstractTreeBean treeBean,
			final AbstractTreeNode treeNode, final boolean dictionary) {
		final Collection<AbstractTreeNode> coll = new ArrayList<AbstractTreeNode>();
		final boolean showContextMenu = (Boolean) compParameter.getBeanProperty("showContextMenu");
		if (treeNode == null && !dictionary) {
			final AbstractTreeNode root = new TreeNode(treeBean, null, null);
			coll.add(root);
			root.setId(getRootID(treeBean));
			root.setOpened(true);
			root.setText((String) compParameter.getBeanProperty("rootText"));
			if (showContextMenu) {
				root.setContextMenu("__catalogTreeMenu2");
			}
			final Boolean draggable = (Boolean) compParameter.getBeanProperty("draggable");
			root.setDraggable(draggable);
			root.setAcceptdrop(draggable);
		} else {
			final Object object = treeNode != null ? treeNode.getDataObject() : null;
			if (object instanceof Catalog || treeNode == null || treeNode.getId().equals(getRootID(treeBean))) {
				final Object catalogId = object instanceof Catalog ? ((Catalog) object).getId() : null;
				final IDataObjectQuery<? extends Catalog> catalogs = catalogs(compParameter, catalogId);
				Catalog catalog;
				while ((catalog = catalogs.next()) != null) {
					final AbstractTreeNode treeNode2 = new TreeNode(treeBean, treeNode, catalog);
					coll.add(treeNode2);
					if (!dictionary) {
						if (showContextMenu) {
							treeNode2.setContextMenu("__catalogTreeMenu");
						}
						final Boolean draggable = (Boolean) compParameter.getBeanProperty("draggable");
						treeNode2.setDraggable(draggable);
						treeNode2.setAcceptdrop(draggable);
					}
				}
			}
		}
		return coll;
	}

	@Override
	public String getIdParameterName(final ComponentParameter compParameter) {
		return CatalogUtils.CATALOG_ID;
	}

	@Override
	public Collection<MenuItem> getRootContextMenu(final ComponentParameter compParameter, final MenuBean menuBean) {
		return createXmlMenu("menu2.xml", menuBean);
	}

	@Override
	public String getJavascriptCallback(final ComponentParameter compParameter, final String jsAction, final Object bean) {
		String jsCallback = StringUtils.blank(super.getJavascriptCallback(compParameter, jsAction, bean));
		if (LangUtils.contains(new String[] { "add", "edit", "delete", "exchange" }, jsAction)) {
			jsCallback += "$Actions[\"" + compParameter.componentBean.getName() + "\"].refresh();";
		}
		return jsCallback;
	}

	private ArrayList<MenuItem> contextMenuItems;

	@Override
	public synchronized List<MenuItem> getContextMenu(final ComponentParameter compParameter, final MenuBean menuBean) {
		if (contextMenuItems == null) {
			contextMenuItems = new ArrayList<MenuItem>();
			contextMenuItems.addAll(super.getContextMenu(compParameter, menuBean));
			final ArrayList<MenuItem> removes = new ArrayList<MenuItem>();
			for (final MenuItem menuItem : contextMenuItems) {
				if (!doContextMenu(menuItem)) {
					removes.add(menuItem);
				}
			}
			if (removes.size() > 0) {
				contextMenuItems.removeAll(removes);
			}
			MenuUtils.removeDblSeparator(contextMenuItems);
		}
		return contextMenuItems;
	}

	protected boolean hideOwnerMgrMenu() {
		return false;
	}

	protected boolean doContextMenu(final MenuItem menuItem) {
		if (hideOwnerMgrMenu()) {
			final String jsSelectCallback = menuItem.getJsSelectCallback();
			if (jsSelectCallback != null && jsSelectCallback.trim().indexOf("owner_mgr") != -1) {
				return false;
			}
		}
		return true;
	}

	/********************************** PropEditor *****************************/

	@Override
	public Collection<PropField> getPropFields(final ComponentParameter compParameter, final PropEditorBean formEditor) {
		return null;
	}

	@Override
	public Map<String, Object> doSavePropEditor(final ComponentParameter compParameter) {
		final Map<String, Object> data = new HashMap<String, Object>();
		final Enumeration<?> e = compParameter.request.getParameterNames();
		while (e.hasMoreElements()) {
			final String key = (String) e.nextElement();
			if (key != null && key.startsWith("catalog_")) {
				data.put(key.substring(8), compParameter.getRequestParameter(key));
			}
		}
		if (!StringUtils.hasText(compParameter.getRequestParameter("catalog_name"))) {
			data.put("name", GenId.genUID());
		}
		return data;
	}

	@Override
	public void doLoadPropEditor(final PageParameter pageParameter, final Catalog catalog, final Map<String, Object> dataBinding) {
		for (final PropertyDescriptor descriptor : BeanUtils.getPropertyDescriptors(catalog.getClass())) {
			final Method readMethod = BeanUtils.getReadMethod(descriptor);
			if (readMethod != null) {
				final Method writeMethod = BeanUtils.getWriteMethod(descriptor);
				if (writeMethod != null) {
					try {
						dataBinding.put("catalog_" + descriptor.getName(), readMethod.invoke(catalog));
					} catch (final Exception e) {
						throw HandleException.wrapException(e);
					}
				}
			}
		}
	}
}
