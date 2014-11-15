package net.simpleframework.content.blog;

import java.util.Collection;
import java.util.Map;

import net.simpleframework.content.ContentUtils;
import net.simpleframework.content.component.catalog.AbstractAccountCatalogHandle;
import net.simpleframework.core.bean.IIdBeanAware;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeBean;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeNode;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class BlogCatalogHandle extends AbstractAccountCatalogHandle {

	@Override
	public Class<? extends IIdBeanAware> getEntityBeanClass() {
		return BlogCatalog.class;
	}

	@Override
	public IBlogApplicationModule getApplicationModule() {
		return BlogUtils.applicationModule;
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, OrgUtils.um().getUserIdParameterName());
		return parameters;
	}

	@Override
	protected IAccount getAccount(final ComponentParameter compParameter) {
		return ContentUtils.getAccountAware().getAccount(compParameter);
	}

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("rootText".equals(beanProperty)) {
			return LocaleI18n.getMessage("my_blog.1");
		} else if ("draggable".equals(beanProperty) || "showContextMenu".equals(beanProperty)) {
			return ContentUtils.getAccountAware().isMyAccount(compParameter);
		} else if (beanProperty.startsWith("job")) {
			if (ContentUtils.getAccountAware().isMyAccount(compParameter)) {
				return IJob.sj_account_normal;
			}
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public Collection<? extends AbstractTreeNode> getCatalogTreenodes(
			final ComponentParameter compParameter, final AbstractTreeBean treeBean,
			final AbstractTreeNode treeNode, final boolean dictionary) {
		if (treeNode != null) {
			final String imgBase = BlogUtils.getCssPath(compParameter) + "/images/";
			treeNode.setOpened(true);
			final BlogCatalog catalog = (BlogCatalog) treeNode.getDataObject();
			if (catalog != null) {
				treeNode.setPostfixText(WebUtils.enclose(catalog.getBlogs()));
				treeNode.setImage(imgBase + "folder.png");
			} else {
				treeNode.setImage(imgBase + "tree_root.png");
			}
			if (!dictionary) {
				String jsCallback = "$Actions['__my_blog_pager']('"
						+ BlogUtils.applicationModule.getCatalogIdName(compParameter) + "=";
				if (catalog != null) {
					jsCallback += catalog.getId();
				}
				jsCallback += "')";
				treeNode.setJsClickCallback(jsCallback);
			}
		}
		return super.getCatalogTreenodes(compParameter, treeBean, treeNode, dictionary);
	}

	static BlogCatalog getBlogCatalogByRequest(final PageRequestResponse requestResponse) {
		final String id = requestResponse.getRequestParameter(BlogUtils.applicationModule
				.getCatalogIdName(requestResponse));
		return BlogUtils.getTableEntityManager(BlogCatalog.class).queryForObjectById(id,
				BlogCatalog.class);
	}
}
