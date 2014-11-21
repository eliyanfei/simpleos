package net.simpleframework.content;

import net.simpleframework.ado.DataObjectManagerUtils;
import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.lucene.LuceneQuery;
import net.simpleframework.content.component.newspager.INewsPagerHandle;
import net.simpleframework.content.component.newspager.NewsBean;
import net.simpleframework.content.component.topicpager.ITopicPagerHandle;
import net.simpleframework.content.component.topicpager.PostsTextBean;
import net.simpleframework.content.component.topicpager.TopicBean;
import net.simpleframework.core.IApplicationModule;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.DateUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class ContentLayoutUtils {
	public static IDataObjectQuery<?> getQueryByRequest(final PageRequestResponse requestResponse) {
		final IDataObjectQuery<?> qs = (IDataObjectQuery<?>) requestResponse
				.getRequestAttribute("__qs");
		if (qs != null) {
			final int rows = ConvertUtils.toInt(requestResponse.getRequestParameter("rows"), 6);
			if (rows > 0) {
				qs.setCount(rows);
			}
		}
		return qs;
	}

	public static <T> IDataObjectQuery<T> getQueryByExpression(
			final PageRequestResponse requestResponse, final IApplicationModule module,
			final Class<T> beanClass, final IDataObjectValue ev) {
		final IDataObjectQuery<T> qs = DataObjectManagerUtils
				.getTableEntityManager(module, beanClass).query(ev, beanClass);
		if (qs != null) {
			final int rows = ConvertUtils.toInt(requestResponse.getRequestParameter("rows"), 6);
			if (rows > 0) {
				qs.setCount(rows);
			}
		}
		return qs;
	}

	public static String layoutTime(final ComponentParameter compParameter,
			final AbstractContent bean, final boolean showUser) {
		final StringBuilder sb = new StringBuilder();
		final String dateFormat = compParameter.getRequestParameter("dateFormat");
		if (StringUtils.hasText(dateFormat)) {
			try {
				sb.append(ConvertUtils.toDateString(bean.getCreateDate(), dateFormat));
			} catch (final Exception ex) {
				sb.append(ex.getMessage());
			}
		} else {
			sb.append(DateUtils.getRelativeDate(bean.getCreateDate()));
		}
		if (showUser) {
			sb.append(", ");
			sb.append(ContentUtils.getAccountAware().wrapAccountHref(compParameter, bean.getUserId(),
					bean.getUserText()));
		}
		final IContentPagerHandle nHandle = (IContentPagerHandle) compParameter.getComponentHandle();
		if (nHandle != null && nHandle.isNew(compParameter, bean)) {
			sb.append("<span class=\"new_gif_image\" style=\"margin-left: 10px;\"></span>");
		}
		return sb.toString();
	}

	public static String layoutDesc(final int index, final ComponentParameter compParameter,
			final AbstractContent bean) {
		final StringBuilder sb = new StringBuilder();
		final int descNums = ConvertUtils.toInt(compParameter.getRequestParameter("descNums"), 0);
		if (descNums > 0 && index >= descNums) {
			return sb.toString();
		}
		final int descLength = ConvertUtils.toInt(compParameter.getRequestParameter("descLength"), 0);
		if (descLength > 0) {
			if (bean instanceof NewsBean) {
				sb.append(ContentUtils.getShortContent((IContentBeanAware) bean, descLength, false));
			} else if (bean instanceof TopicBean) {
				final ITopicPagerHandle tHadle = (ITopicPagerHandle) compParameter.getComponentHandle();
				final PostsTextBean postText = tHadle.getPostsText(compParameter, bean);
				if (postText != null) {
					sb.append(ContentUtils.getShortContent(postText, descLength, false));
				}
			}
		}
		if (sb.length() > 0) {
			sb.insert(0, "<div class=\"gray-color wrap_text\" style=\"padding-top: 2px;\">");
			sb.append("</div>");
		}
		return sb.toString();
	}

	public static boolean isRequestNews(final ComponentParameter compParameter,
			final IDataObjectQuery<?> qs, final NewsBean news) {
		if (qs instanceof LuceneQuery) {
			final INewsPagerHandle nHandle = (INewsPagerHandle) compParameter.getComponentHandle();
			final NewsBean news2 = nHandle.getEntityBeanByRequest(compParameter);
			if (news.equals2(news2)) {
				qs.setCount(qs.getCount() + 1);
				return true;
			}
		}
		return false;
	}

	public static String dotImagePath(final PageRequestResponse requestResponse,
			final String imagesPath) {
		return requestResponse.wrapContextPath(imagesPath + "images/"
				+ StringUtils.text(requestResponse.getRequestParameter("dot"), "dot") + ".png");
	}
}
