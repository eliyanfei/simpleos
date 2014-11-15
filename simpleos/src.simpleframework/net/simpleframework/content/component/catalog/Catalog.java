package net.simpleframework.content.component.catalog;

import net.simpleframework.ado.DataObjectManagerUtils;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.content.AbstractContent;
import net.simpleframework.core.bean.IDescriptionBeanAware;
import net.simpleframework.core.bean.ITextBeanAware;
import net.simpleframework.core.bean.ITreeBeanAware;
import net.simpleframework.core.id.ID;
import net.simpleframework.web.page.PageUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class Catalog extends AbstractContent implements ITreeBeanAware, ITextBeanAware,
		IDescriptionBeanAware {
	private static final long serialVersionUID = -1646978317768619027L;

	private ID documentId;

	private ID parentId;

	private String name, text;

	private String description;

	public ID getDocumentId() {
		return documentId;
	}

	public void setDocumentId(final ID documentId) {
		this.documentId = documentId;
	}

	@Override
	public ID getParentId() {
		return parentId;
	}

	@Override
	public void setParentId(final ID parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public void setText(final String text) {
		this.text = text;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(final String description) {
		this.description = description;
	}

	@Override
	public ITreeBeanAware parent() {
		final ITableEntityManager temgr = DataObjectManagerUtils.getTableEntityManager(
				PageUtils.pageContext.getApplication(), CatalogUtils.catalog_table);
		return temgr.queryForObjectById(parentId, Catalog.class);
	}

	@Override
	public String toString() {
		return getText();
	}
}
