package net.simpleframework.content.component.topicpager;

import java.util.ArrayList;
import java.util.Map;

import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.content.component.vote.DefaultVoteHandle;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ado.IDbComponentHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TopicVoteHandle extends DefaultVoteHandle {
	@Override
	public String getDocumentIdParameterName(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = TopicPagerUtils
				.getComponentParameter(compParameter);
		return ((ITopicPagerHandle) nComponentParameter.getComponentHandle())
				.getIdParameterName(nComponentParameter);
	}

	@Override
	public ITableEntityManager getTableEntityManager(final ComponentParameter compParameter,
			final Class<?> beanClazz) {
		final ComponentParameter nComponentParameter = TopicPagerUtils
				.getComponentParameter(compParameter);
		return ((IDbComponentHandle) nComponentParameter.getComponentHandle()).getTableEntityManager(
				nComponentParameter, beanClazz);
	}

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("selector".equals(beanProperty)) {
			return "#__pager_postsId form";
		} else if ("jobEdit".equals(beanProperty)) {
			final ComponentParameter nComponentParameter = TopicPagerUtils
					.getComponentParameter(compParameter);
			return nComponentParameter.getBeanProperty("jobEdit");
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		final ComponentParameter nComponentParameter = TopicPagerUtils
				.getComponentParameter(compParameter);
		parameters.putAll(nComponentParameter.getComponentHandle().getFormParameters(
				nComponentParameter));
		return parameters;
	}

	@Override
	public String getManagerToolbar(final ComponentParameter compParameter) {
		final ArrayList<String> al = new ArrayList<String>();
		final IAccount account = AccountSession.getLogin(compParameter.getSession());
		if (account != null) {
			final ComponentParameter nComponentParameter = TopicPagerUtils
					.getComponentParameter(compParameter);
			final TopicBean topic = ((ITopicPagerHandle) nComponentParameter.getComponentHandle())
					.getEntityBeanByRequest(nComponentParameter);
			if ((topic != null && AccountSession.isAccount(compParameter.getSession(),
					topic.getUserId()))
					|| OrgUtils.isMember((String) compParameter.getBeanProperty("jobEdit"),
							account.user())) {
				al.add(TB_VOTE_REFRESH);
				al.add(TB_VOTE_EDIT);
			}
		}
		return StringUtils.join(al, HTMLBuilder.SEP);
	}
}
