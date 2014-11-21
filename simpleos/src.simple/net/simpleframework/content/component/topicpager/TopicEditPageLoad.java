package net.simpleframework.content.component.topicpager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.DefaultPageHandle;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TopicEditPageLoad extends DefaultPageHandle {
	@Override
	public Object getBeanProperty(final PageParameter pageParameter, final String beanProperty) {
		final ComponentParameter nComponentParameter = TopicPagerUtils
				.getComponentParameter(pageParameter);
		if (nComponentParameter.componentBean != null) {
			final ITopicPagerHandle tHandle = (ITopicPagerHandle) nComponentParameter
					.getComponentHandle();
			if ("importPage".equals(beanProperty)) {
				final ArrayList<String> al = new ArrayList<String>();
				final String homePath = nComponentParameter.componentBean.getResourceHomePath();
				al.add(homePath + "/jsp/topic_edit_editor.xml");
				final String tb = tHandle.getHtmlEditorToolbar(nComponentParameter);
				if (StringUtils.hasText(tb)) {
					al.add(homePath + "/jsp/topic_edit_toolbar.xml");
				}
				return al.toArray(new String[al.size()]);
			}
		}
		return super.getBeanProperty(pageParameter, beanProperty);
	}

	public void topicEdit(final PageParameter pageParameter, final Map<String, Object> dataBinding,
			final List<String> visibleToggleSelector, final List<String> readonlySelector,
			final List<String> disabledSelector) {
		dataBinding.put("tp_attention", true);
		dataBinding.put("tp_att1", true);
		dataBinding.put("tp_att2", true);
		final ComponentParameter nComponentParameter = TopicPagerUtils
				.getComponentParameter(pageParameter);
		final ITopicPagerHandle tHandle = (ITopicPagerHandle) nComponentParameter
				.getComponentHandle();
		final TopicBean topicBean = tHandle.getEntityBeanByRequest(nComponentParameter);
		if (topicBean != null) {
			dataBinding.put(tHandle.getIdParameterName(nComponentParameter), topicBean.getId());
			dataBinding.put("tp_topic", topicBean.getTopic());
			dataBinding.put("tp_keywords", topicBean.getKeywords());
			final PostsBean postsBean = tHandle.getPostsBean(nComponentParameter, topicBean);
			if (postsBean != null) {
				final PostsTextBean postText = tHandle.getPostsText(nComponentParameter, postsBean);
				if (postText != null) {
					dataBinding.put("tp_content", postText.getContent());
				}
			}
		}
	}

	public void topicReply(final PageParameter pageParameter, final Map<String, Object> dataBinding,
			final List<String> visibleToggleSelector, final List<String> readonlySelector,
			final List<String> disabledSelector) {
		dataBinding.put("tp_att1", true);
		dataBinding.put("tp_att2", true);
		final ComponentParameter nComponentParameter = TopicPagerUtils
				.getComponentParameter(pageParameter);
		final ITopicPagerHandle tHandle = (ITopicPagerHandle) nComponentParameter
				.getComponentHandle();
		final TopicBean topicBean = tHandle.getEntityBeanByRequest(nComponentParameter);
		if (topicBean != null) {
			final String postId = nComponentParameter.getRequestParameter("postId");
			if (StringUtils.hasText(postId)) {
				final PostsTextBean postText = tHandle.getEntityBeanById(nComponentParameter, postId,
						PostsTextBean.class);
				if (postText != null) {
					dataBinding.put("tp_topic",
							StringUtils.text(postText.getSubject(), topicBean.getTopic()));
					dataBinding.put("tp_content", postText.getContent());
				}
			} else {
				dataBinding.put("tp_topic",
						LocaleI18n.getMessage("TopicEditPageLoad.0") + topicBean.getTopic());
			}
		}
	}

	public void topicEdit2(final PageParameter pageParameter, final Map<String, Object> dataBinding,
			final List<String> visibleToggleSelector, final List<String> readonlySelector,
			final List<String> disabledSelector) {
		final ComponentParameter nComponentParameter = TopicPagerUtils
				.getComponentParameter(pageParameter);
		final ITopicPagerHandle tHandle = (ITopicPagerHandle) nComponentParameter
				.getComponentHandle();
		final TopicBean topicBean = tHandle.getEntityBeanByRequest(nComponentParameter);
		if (topicBean != null) {
			dataBinding.put("tp_star", topicBean.getStar());
			dataBinding.put("tp_type", topicBean.getTtype());
			dataBinding.put("tp_status", topicBean.getStatus());
		}
	}
}
