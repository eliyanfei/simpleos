package net.simpleframework.content.component.topicpager;

import java.util.Map;

import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractPagerHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PostsViewHandle extends AbstractPagerHandle {

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("title".equalsIgnoreCase(beanProperty) || "jsLoadedCallback".equals(beanProperty)) {
			final ComponentParameter nComponentParameter = TopicPagerUtils
					.getComponentParameter(compParameter);
			final ITopicPagerHandle tHandle = (ITopicPagerHandle) nComponentParameter
					.getComponentHandle();
			if ("title".equalsIgnoreCase(beanProperty)) {
				return tHandle.getNavigateHTML(nComponentParameter);
			} else {
				return tHandle.getJavascriptCallback(nComponentParameter, "load", null);
			}
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		final ComponentParameter nComponentParameter = TopicPagerUtils
				.getComponentParameter(compParameter);
		final ITopicPagerHandle tHandle = (ITopicPagerHandle) nComponentParameter
				.getComponentHandle();
		putParameter(compParameter, parameters, tHandle.getIdParameterName(nComponentParameter));
		putParameter(compParameter, parameters, TopicPagerUtils.BEAN_ID);
		putParameter(compParameter, parameters, tHandle.getCatalogIdName(nComponentParameter));
		putParameter(compParameter, parameters, OrgUtils.um().getUserIdParameterName());
		putParameter(compParameter, parameters, "dsort");
		return parameters;
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = TopicPagerUtils
				.getComponentParameter(compParameter);
		final ITopicPagerHandle tHandle = (ITopicPagerHandle) nComponentParameter
				.getComponentHandle();
		return tHandle.getPostsList(nComponentParameter,
				(TopicBean) tHandle.getEntityBeanByRequest(nComponentParameter));
	}

	@Override
	protected void doResult(final ComponentParameter compParameter,
			final IDataObjectQuery<?> dataQuery, final int start) {
		super.doResult(compParameter, dataQuery, start);
		final int c = dataQuery.getCount();
		if (c > 1) {
			dataQuery.move(0);
			compParameter.setRequestAttribute("firstPost", dataQuery.next());
			dataQuery.move(c - 2);
			compParameter.setRequestAttribute("lastPost", dataQuery.next());
		}
	}
}
