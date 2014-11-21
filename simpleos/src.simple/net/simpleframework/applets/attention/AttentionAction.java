package net.simpleframework.applets.attention;

import java.util.Map;

import net.simpleframework.content.IContentPagerHandle;
import net.simpleframework.content.bbs.BbsUtils;
import net.simpleframework.content.blog.BlogUtils;
import net.simpleframework.content.component.newspager.INewsPagerHandle;
import net.simpleframework.content.component.newspager.NewsBean;
import net.simpleframework.content.component.topicpager.ITopicPagerHandle;
import net.simpleframework.content.component.topicpager.TopicBean;
import net.simpleframework.content.news.NewsUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class AttentionAction extends AbstractAjaxRequestHandle {

	public IForward bbsAttention(final ComponentParameter compParameter) {
		final AbstractComponentBean componentBean = BbsUtils.applicationModule
				.getComponentBean(compParameter);
		if (componentBean == null) {
			return null;
		}
		final ComponentParameter nComponentParameter = ComponentParameter.get(compParameter,
				componentBean);
		final ITopicPagerHandle tHandle = (ITopicPagerHandle) nComponentParameter
				.getComponentHandle();
		final TopicBean topic = tHandle.getEntityBeanByRequest(nComponentParameter);
		if (topic == null) {
			return null;
		}
		tHandle.contentAttention(nComponentParameter, topic, true);
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				json.put("ok", Boolean.TRUE);
			}
		});
	}

	public IForward newsAttention(final ComponentParameter compParameter) {
		final EFunctionModule vtype = ConvertUtils.toEnum(EFunctionModule.class,
				compParameter.getRequestParameter(IContentPagerHandle._VTYPE));
		AbstractComponentBean componentBean = null;
		if (vtype == EFunctionModule.blog) {
			componentBean = BlogUtils.applicationModule.getComponentBean(compParameter);
		} else if (vtype == EFunctionModule.news) {
			componentBean = NewsUtils.applicationModule.getComponentBean(compParameter);
		}
		if (componentBean == null) {
			return null;
		}
		final ComponentParameter nComponentParameter = ComponentParameter.get(compParameter,
				componentBean);
		final INewsPagerHandle nHandle = (INewsPagerHandle) nComponentParameter.getComponentHandle();
		final NewsBean news = nHandle.getEntityBeanByRequest(nComponentParameter);
		if (news == null) {
			return null;
		}
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				nHandle.contentAttention(nComponentParameter, news, true);
				json.put("ok", Boolean.TRUE);
			}
		});
	}
}
