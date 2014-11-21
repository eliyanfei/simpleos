package net.simpleframework.content.news;

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
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountContext;
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
public class NewsRemarkHandle extends NewsPagerRemarkHandle {
	@Override
	public Class<? extends IIdBeanAware> getEntityBeanClass() {
		return NewsRemark.class;
	}

	@Override
	protected String getTopic(final ComponentParameter compParameter, ID documentId) {
		final ComponentParameter nComponentParameter = ComponentParameter.get(compParameter,
				NewsUtils.applicationModule.getComponentBean(compParameter));
		final INewsPagerHandle nHandle = (INewsPagerHandle) nComponentParameter.getComponentHandle();
		final News news = nHandle.getEntityBeanByRequest(nComponentParameter);
		if (news != null) {
			compParameter.setRequestAttribute("userId", news.getUserId());
			return "-资讯《" + nHandle.wrapOpenLink(compParameter, news) + "》";
		}
		return null;
	}

	@Override
	public <T extends IDataObjectBean> void doAddCallback(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		super.doAddCallback(compParameter, temgr, t, data, beanClazz);

		final RemarkItem item = (RemarkItem) t;

		final IAccount account = OrgUtils.am().queryForObjectById(item.getUserId());
		AccountContext.update(account, "news_remark", item.getDocumentId());

		MySpaceUtils.addSapceLog(compParameter, null, EFunctionModule.news_remark, item.getId());

		final ComponentParameter nComponentParameter = ComponentParameter.get(compParameter,
				NewsUtils.applicationModule.getComponentBean(compParameter));
		final INewsPagerHandle nHandle = (INewsPagerHandle) nComponentParameter.getComponentHandle();
		final News news = nHandle.getEntityBeanByRequest(nComponentParameter);
		if (news != null) {
			news.setLastUpdate(new Date());
			NewsUtils.getTableEntityManager(News.class).update(new Object[] { "lastUpdate" }, news);
		}
		nHandle.doAttentionSent(nComponentParameter, item);
	}
}
