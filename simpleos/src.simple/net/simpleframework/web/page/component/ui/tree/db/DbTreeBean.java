package net.simpleframework.web.page.component.ui.tree.db;

import net.simpleframework.core.IApplication;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.IComponentHandle;
import net.simpleframework.web.page.component.IComponentRegistry;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeBean;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DbTreeBean extends AbstractTreeBean {

	private String dataSource;

	private String tableName;

	private String idName, parentIdName;

	private String textName;

	public DbTreeBean(final IComponentRegistry componentRegistry, final PageDocument pageDocument,
			final Element element) {
		super(componentRegistry, pageDocument, element);
		setDynamicLoading(true);
	}

	@Override
	protected Class<? extends IComponentHandle> getDefaultHandleClass() {
		return DbTreeHandle.class;
	}

	public String getDataSource() {
		return StringUtils.hasText(dataSource) ? dataSource : IApplication.defaultDataSourceName;
	}

	public void setDataSource(final String dataSource) {
		this.dataSource = dataSource;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(final String tableName) {
		this.tableName = tableName;
	}

	public String getIdName() {
		return StringUtils.blank(idName).toUpperCase();
	}

	public void setIdName(final String idName) {
		this.idName = idName;
	}

	public String getParentIdName() {
		return StringUtils.blank(parentIdName).toUpperCase();
	}

	public void setParentIdName(final String parentIdName) {
		this.parentIdName = parentIdName;
	}

	public String getTextName() {
		return StringUtils.blank(textName).toUpperCase();
	}

	public void setTextName(final String textName) {
		this.textName = textName;
	}
}
