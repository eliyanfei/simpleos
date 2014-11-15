package net.simpleframework.content.blog;

import java.util.Date;
import java.util.Map;

import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.content.component.newspager.INewsPagerHandle;
import net.simpleframework.content.component.newspager.NewsPagerRemarkHandle;
import net.simpleframework.content.component.remark.RemarkItem;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.core.bean.IIdBeanAware;
import net.simpleframework.core.id.ID;
import net.simpleframework.my.space.MySpaceUtils;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountContext;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class BlogRemarkHandle extends NewsPagerRemarkHandle {

	@Override
	public Class<? extends IIdBeanAware> getEntityBeanClass() {
		return BlogRemark.class;
	}

	@Override
	public IBlogApplicationModule getApplicationModule() {
		return BlogUtils.applicationModule;
	}

	@Override
	protected String getTopic(final ComponentParameter compParameter, ID documentId) {
		final ComponentParameter nComponentParameter = nComponentParameter(compParameter);
		final INewsPagerHandle nHandle = (INewsPagerHandle) nComponentParameter.getComponentHandle();
		final Blog blog = BlogUtils.getTableEntityManager().queryForObjectById(documentId, Blog.class);
		if (blog != null) {
			compParameter.setRequestAttribute("userId", blog.getUserId());
			return "-博客《" + nHandle.wrapOpenLink(compParameter, blog) + "》";
		}
		return null;
	}

	protected ComponentParameter nComponentParameter(final ComponentParameter compParameter) {
		return ComponentParameter.get(compParameter, getApplicationModule().getComponentBean(compParameter));
	}

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("jobEdit".equals(beanProperty) || "jobDelete".equals(beanProperty)) {
			final IAccount login = AccountSession.getLogin(compParameter.getSession());
			if (login != null) {
				final ComponentParameter nComponentParameter = nComponentParameter(compParameter);
				final INewsPagerHandle nHandle = (INewsPagerHandle) nComponentParameter.getComponentHandle();
				final Blog blog = nHandle.getEntityBeanByRequest(nComponentParameter);
				if (blog != null && login.getId().equals2(blog.getUserId())) {
					return IJob.sj_account_normal;
				}
			}
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public <T extends IDataObjectBean> void doAddCallback(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		super.doAddCallback(compParameter, temgr, t, data, beanClazz);

		final RemarkItem item = (RemarkItem) t;

		final IAccount account = OrgUtils.am().queryForObjectById(item.getUserId());
		AccountContext.update(account, "blog_remark", item.getDocumentId());

		MySpaceUtils.addSapceLog(compParameter, null, EFunctionModule.blog_remark, item.getId());

		final ComponentParameter nComponentParameter = nComponentParameter(compParameter);
		final INewsPagerHandle nHandle = (INewsPagerHandle) nComponentParameter.getComponentHandle();
		final Blog blog = nHandle.getEntityBeanByRequest(nComponentParameter);
		if (blog != null) {
			blog.setLastUpdate(new Date());
			BlogUtils.getTableEntityManager(Blog.class).update(new Object[] { "lastUpdate" }, blog);
		}
		nHandle.doAttentionSent(nComponentParameter, item);
	}
}
