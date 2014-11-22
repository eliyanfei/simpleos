package net.simpleframework.content.component.newspager;

import java.util.ArrayList;
import java.util.Map;

import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.content.component.vote.DefaultVoteHandle;
import net.simpleframework.my.space.MySpaceUtils;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleos.SimpleosUtil;

public class NewsPagerVoteHandle extends DefaultVoteHandle {
	@Override
	public String getDocumentIdParameterName(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = NewsPagerUtils.getComponentParameter(compParameter);
		return ((INewsPagerHandle) nComponentParameter.getComponentHandle()).getIdParameterName(nComponentParameter);
	}

	@Override
	public Object getDocumentId(ComponentParameter compParameter) {
		return super.getDocumentId(compParameter);
	}

	@Override
	public ITableEntityManager getTableEntityManager(final ComponentParameter compParameter, final Class<?> beanClazz) {
		return MySpaceUtils.getTableEntityManager(beanClazz);
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		final ComponentParameter nComponentParameter = NewsPagerUtils.getComponentParameter(compParameter);
		parameters.putAll(nComponentParameter.getComponentHandle().getFormParameters(nComponentParameter));
		return parameters;
	}

	@Override
	public String getManagerToolbar(final ComponentParameter compParameter) {
		final ArrayList<String> al = new ArrayList<String>();
		final IAccount account = AccountSession.getLogin(compParameter.getSession());
		if (account != null) {
			final ComponentParameter nComponentParameter = NewsPagerUtils.getComponentParameter(compParameter);
			final NewsBean newBean = ((INewsPagerHandle) nComponentParameter.getComponentHandle()).getEntityBeanByRequest(nComponentParameter);
			if (newBean != null && SimpleosUtil.isManageOrSelf(compParameter, SimpleosUtil.applicationModule, newBean.getUserId())) {
				al.add(TB_VOTE_REFRESH);
				al.add(TB_VOTE_EDIT);
			}
		}
		return StringUtils.join(al, HTMLBuilder.SEP);
	}
}
